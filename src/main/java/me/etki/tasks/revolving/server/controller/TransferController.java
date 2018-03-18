package me.etki.tasks.revolving.server.controller;

import com.google.inject.Inject;
import io.vertx.core.Future;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.api.Transfer;
import me.etki.tasks.revolving.api.TransferInput;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/v1/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransferController {

    private final AnxiousValidator validator;

    @Inject
    public TransferController(AnxiousValidator validator) {
        this.validator = validator;
    }

    @GET
    public Future<Page<Transfer>> indexAction() {
        return Future.succeededFuture(null);
    }

    @GET
    @Path("/:id")
    public Future<Transfer> readAction(@PathParam("id") String id) {
        return Future.succeededFuture(null);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Future<Transfer> createAction(TransferInput input) {
        validator.assertValid(input);
        return Future.succeededFuture(null);
    }
}
