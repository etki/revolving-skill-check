package me.etki.tasks.revolving.server;

import com.zandero.rest.exception.ExceptionHandler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import me.etki.tasks.revolving.api.exception.ResourceNotFoundException;
import me.etki.tasks.revolving.api.http.JsonProblem;

public class ResourceNotFoundExceptionHandler implements ExceptionHandler<ResourceNotFoundException> {
    @Override
    public void write(ResourceNotFoundException result, HttpServerRequest request, HttpServerResponse response) {
        JsonProblem problem = new JsonProblem()
                .setStatus(404)
                .setTitle("Resource Not Found")
                .setDetail(result.getMessage());
        response.setChunked(true).setStatusCode(404).end(Json.encode(problem));
    }
}
