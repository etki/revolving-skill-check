package me.etki.tasks.revolving.server.controller;

import com.google.inject.Inject;
import io.vertx.core.Future;
import me.etki.tasks.revolving.api.Account;
import me.etki.tasks.revolving.api.DecimalValue;
import me.etki.tasks.revolving.concurrent.CompletableFutures;
import me.etki.tasks.revolving.domain.AccountManager;
import me.etki.tasks.revolving.server.Routes;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path(Routes.ACCOUNT_BALANCE)
@Produces(MediaType.APPLICATION_JSON)
public class AccountBalanceController {

    private final AccountManager manager;
    private final AnxiousValidator validator;

    @Inject
    public AccountBalanceController(AccountManager manager, AnxiousValidator validator) {
        this.manager = manager;
        this.validator = validator;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Future<DecimalValue> modifyAction(@PathParam("id") UUID id, DecimalValue value) {
        validator.assertValid(value);
        return CompletableFutures
                .toVertXFuture(manager.setBalance(id, value.getValue()))
                .map(Account::getBalance)
                .map(DecimalValue::new);
    }
}
