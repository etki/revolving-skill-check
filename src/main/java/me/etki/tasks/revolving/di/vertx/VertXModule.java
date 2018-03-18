package me.etki.tasks.revolving.di.vertx;

import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;

public class VertXModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Vertx.class).toProvider(VertXProvider.class);
    }
}
