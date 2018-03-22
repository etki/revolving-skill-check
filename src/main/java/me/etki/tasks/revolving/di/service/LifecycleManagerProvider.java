package me.etki.tasks.revolving.di.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.core.Vertx;
import me.etki.tasks.revolving.service.LifecycleManager;

public class LifecycleManagerProvider implements Provider<LifecycleManager> {

    private final Vertx vertx;

    @Inject
    public LifecycleManagerProvider(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public LifecycleManager get() {
        return new LifecycleManager(vertx);
    }
}
