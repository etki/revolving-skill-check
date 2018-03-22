package me.etki.tasks.revolving.server.controller;

import io.vertx.core.http.HttpServerResponse;
import me.etki.tasks.revolving.server.Routes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path(Routes.OPENAPI)
public class SwaggerUIController {
    @GET
    @SuppressWarnings("VoidMethodAnnotatedWithGET")
    public void redirectAction(@Context HttpServerResponse response) {
        response
                .setChunked(true)
                .setStatusCode(302)
                .putHeader("Location", String.format("%s/index.html?url=%s", Routes.OPENAPI, Routes.OPENAPI_SCHEMA));
    }
}
