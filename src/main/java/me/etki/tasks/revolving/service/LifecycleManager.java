package me.etki.tasks.revolving.service;

import com.google.inject.Inject;
import lombok.Getter;
import me.etki.tasks.revolving.concurrent.CompletableFutures;
import me.etki.tasks.revolving.di.vertx.VertXSingletonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class LifecycleManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleManager.class);

    private final VertXSingletonFactory vertx;

    @Getter
    private final CompletableFuture<Void> shutdownFuture = new CompletableFuture<>();

    @Inject
    public LifecycleManager(VertXSingletonFactory vertx) {
        this.vertx = vertx;
    }

    public CompletableFuture<Void> shutdown() {
        LOGGER.info("Shutting down application");
        return closeVertX()
                .thenRun(() -> LOGGER.info("Successfully shut down application"))
                .thenAccept(shutdownFuture::complete);
    }
    private CompletableFuture<Void> closeVertX() {
        if (vertx.getAccesses() == 0) {
            LOGGER.info("Vert.X hasn't been started, skipping");
            return CompletableFutures.VOID;
        }
        CompletableFuture<Void> synchronizer = new CompletableFuture<>();
        vertx.get().close(nothing -> {
            LOGGER.info("Successfully shut down Vert.X");
            synchronizer.complete(null);
        });
        return synchronizer;
    }
}
