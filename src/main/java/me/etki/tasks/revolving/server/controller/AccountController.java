package me.etki.tasks.revolving.server.controller;

import com.google.inject.Inject;
import io.vertx.core.Future;
import me.etki.tasks.revolving.api.Account;
import me.etki.tasks.revolving.api.AccountInput;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.UUID;

@Path("/v1/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    private final AnxiousValidator validator;

    @Inject
    public AccountController(AnxiousValidator validator) {
        this.validator = validator;
    }

    @GET
    public Future<Page<Account>> indexAction() {
        return Future.succeededFuture(new Page<>(Collections.emptyList()));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Future<AccountInput> createAction(AccountInput input) {
        validator.assertValid(input);
        return Future.succeededFuture(input);
    }

    @GET
    @Path("/:id")
    public Future<Account> getAction(@PathParam("id") UUID id) {
        return Future.succeededFuture(null);
    }
}
