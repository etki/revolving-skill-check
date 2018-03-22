package me.etki.tasks.revolving.server.controller;

import com.google.inject.Inject;
import io.vertx.core.Future;
import me.etki.tasks.revolving.api.Acknowledgement;
import me.etki.tasks.revolving.service.LifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1/_shutdown")
@Produces(MediaType.APPLICATION_JSON)
public class ShutdownController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownController.class);

    private final LifecycleManager manager;

    @Inject
    public ShutdownController(LifecycleManager manager) {
        this.manager = manager;
    }

    @POST
    public Future<Acknowledgement> executionAction() {
        LOGGER.info("Received shutdown call");
        manager.requestShutdown();
        return Future.succeededFuture(new Acknowledgement(true));
    }
}
