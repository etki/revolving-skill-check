package me.etki.tasks.revolving.server;

import io.vertx.ext.web.RoutingContext;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface RestController<R, B> {
    Collection<Binding> getBindings();
    default Class<B> getRequestBodyType() {
        return null;
    }
    CompletableFuture<R> handle(RoutingContext context, B payload);
}
