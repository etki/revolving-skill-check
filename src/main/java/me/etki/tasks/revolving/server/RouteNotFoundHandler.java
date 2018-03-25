package me.etki.tasks.revolving.server;

import com.zandero.rest.writer.NotFoundResponseWriter;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import me.etki.tasks.revolving.api.http.JsonProblem;

public class RouteNotFoundHandler extends NotFoundResponseWriter {
    @Override
    public void write(HttpServerRequest request, HttpServerResponse response) {
        JsonProblem problem = new JsonProblem()
                .setStatus(404)
                .setTitle("Route Not Found")
                .setDetail(String.format("No route is registered for path `%s`", request.path()));
        response.setChunked(true).setStatusCode(404).end(Json.encodePrettily(problem));
    }
}
