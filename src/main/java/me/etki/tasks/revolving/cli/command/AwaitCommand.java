package me.etki.tasks.revolving.cli.command;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.vertx.core.Vertx;
import me.etki.tasks.revolving.api.HealthColor;
import me.etki.tasks.revolving.api.ServiceStatus;
import me.etki.tasks.revolving.api.client.ApiClient;
import me.etki.tasks.revolving.cli.CliCommand;
import me.etki.tasks.revolving.cli.options.ServerOptions;
import me.etki.tasks.revolving.di.configuration.ConfigurationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@Command(name = "await", description = "Blocks until service is up")
public class AwaitCommand implements CliCommand {

    @Inject
    private ServerOptions server = new ServerOptions();

    @SuppressWarnings("FieldCanBeLocal")
    @Option(name = "--timeout", description = "timeout in seconds")
    private long timeout = 15;

    @Override
    public CompletableFuture<Void> run(Injector container) {
        ApiClient client = container.getInstance(ApiClient.class);
        Vertx vertx = container.getInstance(Vertx.class);
        return new RequestLoop(client, vertx).run(timeout);
    }

    @Override
    public Collection<Module> createModules() {
        return Collections.singleton(new ConfigurationModule(server.toConfiguration()));
    }

    @SuppressWarnings("WeakerAccess")
    private static class RequestLoop {
        private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoop.class);

        private final CompletableFuture<Void> timeout = new CompletableFuture<>();
        private final CompletableFuture<Void> success = new CompletableFuture<>();

        private final ApiClient client;
        private final Vertx vertx;

        public RequestLoop(ApiClient client, Vertx vertx) {
            this.client = client;
            this.vertx = vertx;
        }

        private <T> CompletableFuture<T> throttle(CompletableFuture<T> subject, long milliseconds) {
            CompletableFuture<Void> synchronizer = new CompletableFuture<>();
            vertx.setTimer(milliseconds, any -> synchronizer.complete(null));
            return synchronizer.thenCompose(none -> subject);
        }

        private ServiceStatus handle(ServiceStatus status, Throwable error) {
            if (status != null && HealthColor.GREEN.equals(status.getColor())) {
                LOGGER.info("Successfully accessed service");
                success.complete(null);
                return status;
            }
            if (error != null) {
                LOGGER.warn("Request failed with following error: {}", error.getMessage());
            }
            if (!timeout.isDone()) {
                LOGGER.info("Trying to perform next HTTP request");
                throttle(client.getHealth(), 1000).handle(this::handle);
            }
            return null;
        }

        public CompletableFuture<Void> run(long timeoutSeconds) {
            LOGGER.info("Running request loop");
            vertx.setTimer(timeoutSeconds * 1000, any -> {
                String message = "Could not ping application in requested time";
                TimeoutException exception = new TimeoutException(message);
                timeout.completeExceptionally(exception);
            });
            handle(null, null);
            return CompletableFuture.anyOf(timeout, success).thenAccept(none -> {});
        }
    }
}
