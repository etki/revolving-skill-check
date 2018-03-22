package me.etki.tasks.revolving.cli.command;

import com.github.rvesse.airline.annotations.Command;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import me.etki.tasks.revolving.cli.CliCommand;
import me.etki.tasks.revolving.cli.options.DatabaseOptions;
import me.etki.tasks.revolving.cli.options.ServerOptions;
import me.etki.tasks.revolving.concurrent.CompletableFutures;
import me.etki.tasks.revolving.database.MigrationService;
import me.etki.tasks.revolving.di.configuration.ConfigurationModule;
import me.etki.tasks.revolving.service.LifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Command(name = "serve", description = "Spins up internal server")
public class ServeCommand implements CliCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServeCommand.class);

    @Inject
    private DatabaseOptions database = new DatabaseOptions();

    @Inject
    private ServerOptions server = new ServerOptions();

    @Override
    public CompletableFuture<Void> run(Injector container) {
        MigrationService migrations = container.getInstance(MigrationService.class);
        return CompletableFutures
                .execute(migrations::migrate)
                .thenCompose(nothing -> {
                    container
                            .getInstance(HttpServer.class)
                            .requestHandler(container.getInstance(Router.class)::accept)
                            .listen();
                    LOGGER.info("Server is up and running, have a nice flight");
                    return container
                            .getInstance(LifecycleManager.class)
                            .getShutdownFuture();
                });
    }

    @Override
    public Collection<Module> createModules() {
        ConfigurationModule module = new ConfigurationModule(
                server.toConfiguration(),
                database.toConfiguration()
        );
        return Collections.singleton(module);
    }
}
