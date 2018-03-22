package me.etki.tasks.revolving.server.controller;

import com.google.inject.Inject;
import io.vertx.core.Future;
import me.etki.tasks.revolving.api.DecimalValue;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.api.Rate;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/v1/rate")
@Produces(MediaType.APPLICATION_JSON)
public class RateController {

    private final AnxiousValidator validator;

    @Inject
    public RateController(AnxiousValidator validator) {
        this.validator = validator;
    }

    @GET
    public Future<Page<Rate>> indexAction() {
        return Future.succeededFuture(null);
    }

    @GET
    @Path("/:source/:target")
    public Future<Rate> readAction(
            @PathParam("source") String source,
            @PathParam("target") String target) {

        return Future.succeededFuture(null);
    }

    @POST
    @Path("/:source/:target")
    @Consumes(MediaType.APPLICATION_JSON)
    public Future<Rate> setAction(
            @PathParam("source") String source,
            @PathParam("target") String target,
            DecimalValue value) {

        validator.assertValid(value);
        return Future.succeededFuture(null);
    }
}
