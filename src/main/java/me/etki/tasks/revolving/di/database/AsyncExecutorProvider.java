package me.etki.tasks.revolving.di.database;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.core.Vertx;
import me.etki.tasks.revolving.database.AsyncExecutor;

import javax.persistence.EntityManagerFactory;

public class AsyncExecutorProvider implements Provider<AsyncExecutor> {

    private final Vertx vertx;
    private final EntityManagerFactory factory;

    @Inject
    public AsyncExecutorProvider(Vertx vertx, EntityManagerFactory factory) {
        this.vertx = vertx;
        this.factory = factory;
    }

    @Override
    public AsyncExecutor get() {
        return new AsyncExecutor(factory, vertx);
    }
}
