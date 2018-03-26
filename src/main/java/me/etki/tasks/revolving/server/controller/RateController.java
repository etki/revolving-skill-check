package me.etki.tasks.revolving.server.controller;

import com.google.inject.Inject;
import io.vertx.core.Future;
import me.etki.tasks.revolving.api.DecimalValue;
import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.api.Rate;
import me.etki.tasks.revolving.concurrent.CompletableFutures;
import me.etki.tasks.revolving.domain.RateManager;
import me.etki.tasks.revolving.server.Routes;
import me.etki.tasks.revolving.validation.AnxiousValidator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path(Routes.RATE)
@Produces(MediaType.APPLICATION_JSON)
public class RateController {

    private final RateManager manager;
    private final AnxiousValidator validator;

    @Inject
    public RateController(RateManager manager, AnxiousValidator validator) {
        this.manager = manager;
        this.validator = validator;
    }

    @GET
    public Future<Page<Rate>> indexAction(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("32") int size) {

        return CompletableFutures.toVertXFuture(manager.getPage(page, size));
    }

    @GET
    @Path("/{source}/{target}")
    public Future<Rate> readAction(
            @PathParam("source") String source,
            @PathParam("target") String target) {

        return CompletableFutures.toVertXFuture(manager.get(source, target));
    }

    @PUT
    @Path("/{source}/{target}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Future<Rate> setAction(
            @PathParam("source") String source,
            @PathParam("target") String target,
            DecimalValue value) {

        validator.assertValid(value, DecimalValue.Positive.class);
        return CompletableFutures.toVertXFuture(
                manager.set(source, target, value.normalize().getValue())
        );
    }
}
