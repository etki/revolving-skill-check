package me.etki.tasks.revolving.database;

import com.google.inject.Inject;
import io.vertx.core.Vertx;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.concurrent.CompletableFuture;

public class AsyncExecutor {
    private final EntityManagerFactory factory;
    private final Vertx vertx;

    @Inject
    public AsyncExecutor(EntityManagerFactory factory, Vertx vertx) {
        this.factory = factory;
        this.vertx = vertx;
    }

    public <T> CompletableFuture<T> execute(Unit<T> unit) {
        CompletableFuture<T> synchronizer = new CompletableFuture<>();
        vertx.<T>executeBlocking(future -> {
            EntityManager manager = factory.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            try {
                T result = unit.apply(manager);
                manager.flush();
                transaction.commit();
                future.complete(result);
            } catch (Exception e) {
                transaction.rollback();
                future.fail(e);
            }
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
