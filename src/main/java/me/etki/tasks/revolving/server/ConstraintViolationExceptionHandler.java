package me.etki.tasks.revolving.server;

import com.zandero.rest.exception.ExceptionHandler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import me.etki.tasks.revolving.api.http.JsonProblem;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstraintViolationExceptionHandler implements ExceptionHandler<ConstraintViolationException> {
    @Override
    public void write(ConstraintViolationException result, HttpServerRequest request, HttpServerResponse response) {
        Map<String, List<String>> violations = new HashMap<>();
        for (ConstraintViolation violation : result.getConstraintViolations()) {
            String key = violation.getPropertyPath().toString();
            violations.putIfAbsent(key, new ArrayList<>(2));
            violations.get(key).add(violation.getMessage());
        }
        JsonProblem problem = new JsonProblem()
                .setStatus(400)
                .setTitle("Invalid input")
                .setExtra(violations);
        response.setChunked(true).setStatusCode(400).end(Json.encode(problem));
    }
}
