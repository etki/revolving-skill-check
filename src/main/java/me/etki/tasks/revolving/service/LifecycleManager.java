package me.etki.tasks.revolving.service;

import com.google.inject.Inject;
import io.vertx.core.Vertx;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class LifecycleManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleManager.class);

    private final Vertx vertx;

    @Getter
    private final CompletableFuture<Void> shutdownFuture = new CompletableFuture<>();

    @Inject
    public LifecycleManager(Vertx vertx) {
        this.vertx = vertx;
    }

    public CompletableFuture<Void> shutdown() {
        LOGGER.info("Shutting down application");
        return closeVertX()
                .thenRun(() -> LOGGER.info("Successfully shut down application"))
                .thenAccept(shutdownFuture::complete);
    }
    private CompletableFuture<Void> closeVertX() {
        CompletableFuture<Void> synchronizer = new CompletableFuture<>();
        vertx.close(nothing -> {
            LOGGER.info("Successfully shut down Vert.X");
            synchronizer.complete(null);
        });
        return synchronizer;
    }
}
