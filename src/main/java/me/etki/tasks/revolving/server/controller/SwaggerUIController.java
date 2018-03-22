package me.etki.tasks.revolving.server.controller;

import io.vertx.core.http.HttpServerResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("/v1/_openapi")
public class SwaggerUIController {
    @GET
    public void redirectAction(@Context HttpServerResponse response) {
        response
                .setChunked(true)
                .setStatusCode(302)
                .putHeader("Location", "/v1/_openapi/index.html?url=/v1/_openapi/schema.yml");
    }
}
