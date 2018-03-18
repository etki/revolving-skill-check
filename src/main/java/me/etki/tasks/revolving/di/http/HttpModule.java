package me.etki.tasks.revolving.di.http;

import com.google.inject.AbstractModule;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class HttpModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Router.class).toProvider(RouterProvider.class);
        bind(HttpServer.class).toProvider(HttpServerProvider.class);
    }
}
