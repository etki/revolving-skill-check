package me.etki.tasks.revolving.di.vertx;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.core.Vertx;

public class VertXProvider implements Provider<Vertx> {

    private final VertXSingletonFactory container;

    @Inject
    public VertXProvider(VertXSingletonFactory container) {
        this.container = container;
    }

    @Override
    public Vertx get() {
        return container.get();
    }
}
