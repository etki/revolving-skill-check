package me.etki.tasks.revolving.server.controller;

import io.vertx.core.Future;
import me.etki.tasks.revolving.api.Acknowledgement;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/_shutdown")
@Produces(MediaType.APPLICATION_JSON)
public class ShutdownController {

    @POST
    public Future<Acknowledgement> executionAction() {
        return Future.succeededFuture(new Acknowledgement(true));
    }
}
