package me.etki.tasks.revolving.server;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import me.etki.tasks.revolving.api.http.JsonProblem;
import me.etki.tasks.revolving.concurrent.CompletableFutures;

public class RestControllers {

    public static final String CONTENT_TYPE = "application/json";

    private RestControllers() {
        // static access only
    }

    public static <R, B> Handler<RoutingContext> toHandler(RestController<R, B> controller) {
        return context -> {
            HttpServerResponse response = context
                    .response()
                    .setChunked(true)
                    .putHeader("Content-Type", CONTENT_TYPE);
            B body = null;
            if (controller.getRequestBodyType() != null) {
                if (context.getBody() == null) {
                    response.setStatusCode(400).end(Json.encode(JsonProblem.MISSING_BODY));
                    return;
                }
                body = Json.decodeValue(context.getBody(), controller.getRequestBodyType());
            }
            // a little bit dirty but working way to catch synchronous errors
            CompletableFutures
                    .completed(body)
                    .thenCompose(value -> controller.handle(context, value))
                    .thenAccept(result -> response.end(Json.encode(result)))
                    .exceptionally(error -> {
                        JsonProblem problem = new JsonProblem()
                                .setStatus(500)
                                .setTitle("Internal server error")
                                .setDetail(error.getMessage());
                        response.setStatusCode(500).end(Json.encode(problem));
                        return null;
                    });
        };
    }

    public static void install(Router router, RestController<?, ?> controller) {
        //noinspection CodeBlock2Expr
        controller.getBindings().forEach(binding -> {
            router
                    .route(binding.getMethod(), binding.getPath())
                    .consumes(CONTENT_TYPE)
                    .produces(CONTENT_TYPE)
                    .handler(toHandler(controller));
        });
    }
}
