package me.etki.tasks.revolving.server.controller;

import io.vertx.core.Future;
import me.etki.tasks.revolving.api.HealthColor;
import me.etki.tasks.revolving.api.ServiceStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/_health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthController {

    private static final Future<ServiceStatus> RESPONSE
            = Future.succeededFuture(new ServiceStatus(HealthColor.GREEN));

    @GET
    public Future<ServiceStatus> indexAction() {
        return RESPONSE;
    }
}
