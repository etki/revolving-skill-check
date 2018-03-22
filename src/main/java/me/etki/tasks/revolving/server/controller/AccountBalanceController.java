package me.etki.tasks.revolving.server.controller;

import com.google.inject.Inject;
import io.vertx.core.Future;
import me.etki.tasks.revolving.api.DecimalValue;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/v1/account/:id/balance")
@Produces(MediaType.APPLICATION_JSON)
public class AccountBalanceController {

    private final AnxiousValidator validator;

    @Inject
    public AccountBalanceController(AnxiousValidator validator) {
        this.validator = validator;
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Future<DecimalValue> modifyAction(@PathParam("id") String id, DecimalValue value) {
        validator.assertValid(value);
        return Future.succeededFuture(value);
    }
}
