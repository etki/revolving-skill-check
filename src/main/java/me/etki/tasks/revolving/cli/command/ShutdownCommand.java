package me.etki.tasks.revolving.cli.command;

import com.github.rvesse.airline.annotations.Command;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import me.etki.tasks.revolving.api.client.ApiClient;
import me.etki.tasks.revolving.cli.CliCommand;
import me.etki.tasks.revolving.cli.options.ServerOptions;
import me.etki.tasks.revolving.di.configuration.ConfigurationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Command(name = "shutdown", description = "Shuts down remote application, CI-purposes only")
public class ShutdownCommand implements CliCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownCommand.class);

    @Inject
    private ServerOptions server = new ServerOptions();

    @Override
    public CompletableFuture<Void> run(Injector container) {
        LOGGER.info("Performing remote shutdown");
        return container
                .getInstance(ApiClient.class)
                .shutdown()
                .thenRun(() -> LOGGER.info("Successfully shut down remote application"));
    }

    @Override
    public Collection<Module> createModules() {
        return Collections.singleton(new ConfigurationModule(server.toConfiguration()));
    }
}
