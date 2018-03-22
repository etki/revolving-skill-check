package me.etki.tasks.revolving.di.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import me.etki.tasks.revolving.di.vertx.VertXSingletonFactory;
import me.etki.tasks.revolving.service.LifecycleManager;

public class LifecycleManagerProvider implements Provider<LifecycleManager> {

    private final VertXSingletonFactory factory;

    @Inject
    public LifecycleManagerProvider(VertXSingletonFactory factory) {
        this.factory = factory;
    }

    @Override
    public LifecycleManager get() {
        return new LifecycleManager(factory);
    }
}
