package me.etki.tasks.revolving.server;

import com.zandero.rest.exception.ExceptionHandler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import me.etki.tasks.revolving.api.exception.ContentionException;
import me.etki.tasks.revolving.api.http.JsonProblem;

public class ContentionExceptionHandler implements ExceptionHandler<ContentionException> {
    @Override
    public void write(ContentionException result, HttpServerRequest request, HttpServerResponse response) {
        JsonProblem problem = new JsonProblem()
                .setStatus(503)
                .setTitle("Service Temporarily Unavailable")
                .setDetail(result.getMessage());
        response.setChunked(true).setStatusCode(503).end(Json.encodePrettily(problem));
    }
}
