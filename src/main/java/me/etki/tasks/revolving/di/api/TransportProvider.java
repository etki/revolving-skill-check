package me.etki.tasks.revolving.di.api;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.core.Vertx;
import me.etki.tasks.revolving.api.client.Transport;
import me.etki.tasks.revolving.cli.options.ServerOptions;

public class TransportProvider implements Provider<Transport> {

    private final Vertx vertx;
    private final ServerOptions options;

    @Inject
    public TransportProvider(Vertx vertx, ServerOptions options) {
        this.vertx = vertx;
        this.options = options;
    }

    @Override
    public Transport get() {
        return new Transport(vertx, options);
    }
}
