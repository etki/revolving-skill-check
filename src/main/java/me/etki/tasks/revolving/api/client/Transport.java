package me.etki.tasks.revolving.api.client;

import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import me.etki.tasks.revolving.cli.options.ServerOptions;

import java.util.concurrent.CompletableFuture;

public class Transport {

    private final WebClient client;

    @Inject
    public Transport(Vertx vertx, ServerOptions options) {
        WebClientOptions configuration = new WebClientOptions()
                .setDefaultHost(options.getHost())
                .setDefaultPort(options.getPort());
        this.client = WebClient.create(vertx, configuration);
    }

    // todo: this is quite a messy method, isn't it?
    @SuppressWarnings("squid:S1602")
    public <T> CompletableFuture<T> execute(Request request, Class<T> responseType) {
        HttpRequest<T> resource = client
                .request(request.getMethod(), request.getRoute())
                .timeout(1000)
                .as(BodyCodec.create((Buffer buffer) -> {
                    if (buffer.length() == 0) {
                        return null;
                    }
                    return Json.decodeValue(buffer, responseType);
                }));
        //noinspection CodeBlock2Expr
        request.getParameters().forEach((key, values) -> {
            values.forEach(value -> resource.addQueryParam(key, value));
        });
        CompletableFuture<T> promise = new CompletableFuture<>();
        Handler<AsyncResult<HttpResponse<T>>> handler = response -> {
            HttpResponse<T> result = response.result();
            if (response.cause() != null && (result == null || result.statusCode() != 404)) {
                promise.completeExceptionally(response.cause());
                return;
            }
            if (result.statusCode() == 404) {
                promise.complete(null);
                return;
            }
            if (result.statusCode() >= 500) {
                promise.completeExceptionally(new ServerException(result.statusMessage()));
                return;
            }
            if (result.statusCode() >= 400) {
                promise.completeExceptionally(new ClientException(result.statusMessage()));
                return;
            }
            promise.complete(response.result().body());
        };
        if (request instanceof PayloadRequest) {
            resource.sendJson(((PayloadRequest) request).getPayload(), handler);
        } else {
            resource.send(handler);
        }
        return promise;
    }
}
