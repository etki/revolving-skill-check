package me.etki.tasks.revolving.di.vertx;

import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import io.vertx.core.Vertx;

public class VertXModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(VertXSingletonFactory.class)
                .toInstance(new VertXSingletonFactory());
        bind(Vertx.class)
                .toProvider(VertXProvider.class)
                .in(LazySingletonScope.get());
    }
}
