package me.etki.tasks.revolving.di.vertx;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import me.etki.tasks.revolving.api.configuration.ServerConfiguration;

public class HttpServerProvider implements Provider<HttpServer> {
    private final Vertx vertx;
    private final ServerConfiguration options;

    @Inject
    public HttpServerProvider(Vertx vertx, ServerConfiguration options) {
        this.vertx = vertx;
        this.options = options;
    }

    @Override
    public HttpServer get() {
        HttpServerOptions configuration = new HttpServerOptions()
                .setPort(options.getPort());
        if (options.getHost() != null) {
            configuration.setHost(options.getHost());
        }
        return vertx.createHttpServer(configuration);
    }
}
