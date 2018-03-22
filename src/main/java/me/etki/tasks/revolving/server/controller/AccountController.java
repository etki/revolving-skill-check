package me.etki.tasks.revolving.server.controller;

import com.google.inject.Inject;
import io.vertx.core.Future;
import me.etki.tasks.revolving.api.Account;
import me.etki.tasks.revolving.api.AccountInput;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.concurrent.CompletableFutures;
import me.etki.tasks.revolving.domain.AccountManager;
import me.etki.tasks.revolving.server.Routes;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path(Routes.ACCOUNT)
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    private final AnxiousValidator validator;
    private final AccountManager manager;

    @Inject
    public AccountController(AnxiousValidator validator, AccountManager manager) {
        this.validator = validator;
        this.manager = manager;
    }

    @GET
    public Future<Page<Account>> indexAction(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("32") int size) {
        return CompletableFutures.toVertXFuture(manager.getPage(page, size));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Future<Account> createAction(AccountInput input) {
        validator.assertValid(input);
        return CompletableFutures.toVertXFuture(manager.create(input));
    }

    @GET
    @Path("/{id}")
    public Future<Account> getAction(@PathParam("id") UUID id) {
        return CompletableFutures.toVertXFuture(manager.get(id));
    }
}
