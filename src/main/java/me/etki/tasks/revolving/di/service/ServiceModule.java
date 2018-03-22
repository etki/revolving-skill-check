package me.etki.tasks.revolving.di.service;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import me.etki.tasks.revolving.service.LifecycleManager;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LifecycleManager.class).toProvider(LifecycleManagerProvider.class).in(Singleton.class);
    }
}
