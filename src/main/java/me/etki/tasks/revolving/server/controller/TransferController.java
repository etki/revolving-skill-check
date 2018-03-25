package me.etki.tasks.revolving.server.controller;

import com.google.inject.Inject;
import io.vertx.core.Future;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.api.Transfer;
import me.etki.tasks.revolving.api.TransferInput;
import me.etki.tasks.revolving.concurrent.CompletableFutures;
import me.etki.tasks.revolving.domain.TransferManager;
import me.etki.tasks.revolving.server.Routes;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path(Routes.TRANSFER)
@Produces(MediaType.APPLICATION_JSON)
public class TransferController {

    private final TransferManager manager;
    private final AnxiousValidator validator;

    @Inject
    public TransferController(TransferManager manager, AnxiousValidator validator) {
        this.manager = manager;
        this.validator = validator;
    }

    @GET
    public Future<Page<Transfer>> indexAction(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("32") int size) {

        return CompletableFutures.toVertXFuture(manager.getPage(page, size));
    }

    @GET
    @Path("/{id}")
    public Future<Transfer> readAction(@PathParam("id") UUID id) {
        return CompletableFutures.toVertXFuture(manager.get(id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Future<Transfer> createAction(TransferInput input) {
        validator.assertValid(input);
        return CompletableFutures.toVertXFuture(manager.create(input));
    }
}
