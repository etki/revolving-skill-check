package me.etki.tasks.revolving.database;

import com.google.inject.Inject;
import io.vertx.core.Vertx;
import me.etki.tasks.revolving.api.exception.ContentionException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import java.util.concurrent.CompletableFuture;

public class AsyncExecutor {
    private final EntityManagerFactory factory;
    private final Vertx vertx;

    @Inject
    public AsyncExecutor(EntityManagerFactory factory, Vertx vertx) {
        this.factory = factory;
        this.vertx = vertx;
    }

    public <T> CompletableFuture<T> execute(ExecutionUnit<T> unit) {
        return execute(unit, 10);
    }

    public <T> CompletableFuture<T> execute(ExecutionUnit<T> unit, int attempts) {
        CompletableFuture<T> synchronizer = new CompletableFuture<>();
        vertx.<T>executeBlocking(future -> {
            EntityManager manager = factory.createEntityManager();
            for (int i = 0; i < attempts; i++) {
                EntityTransaction transaction = manager.getTransaction();
                transaction.begin();
                try {
                    T result = unit.execute(manager);
                    manager.flush();
                    transaction.commit();
                    future.complete(result);
                    break;
                } catch (RollbackException e) {
                    transaction.rollback();
                    // ignore the exception itself and just start next cycle
                } catch (Exception e) {
                    transaction.rollback();
                    future.fail(e);
                    break;
                }
            }
            if (!future.isComplete()) {
                String message = "Could not perform operation due to high contention";
                future.fail(new ContentionException(message));
            }
            manager.close();
        }, result -> {
            if (result.succeeded()) {
                synchronizer.complete(result.result());
            } else {
                synchronizer.completeExceptionally(result.cause());
            }
        });
        return synchronizer;
    }
}
