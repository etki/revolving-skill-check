package me.etki.tasks.revolving.di.vertx;

import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class VertXModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HttpServer.class).toProvider(HttpServerProvider.class);
        bind(Vertx.class).toProvider(VertXProvider.class);
    }
}
