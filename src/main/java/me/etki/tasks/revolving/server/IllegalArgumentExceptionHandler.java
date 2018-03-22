package me.etki.tasks.revolving.server;

import com.zandero.rest.exception.ExceptionHandler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import me.etki.tasks.revolving.api.http.JsonProblem;

public class IllegalArgumentExceptionHandler implements ExceptionHandler<IllegalArgumentException> {
    @Override
    public void write(IllegalArgumentException result, HttpServerRequest request, HttpServerResponse response) {
        JsonProblem problem = new JsonProblem()
                .setTitle("Invalid Request")
                .setDetail("Failed to read request: " + result.getMessage())
                .setStatus(400);
        response.setChunked(true).setStatusCode(400).end(Json.encode(problem));
    }
}
