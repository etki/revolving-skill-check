package me.etki.tasks.revolving.cli.command;

import com.github.rvesse.airline.annotations.Command;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import me.etki.tasks.revolving.cli.CliCommand;
import me.etki.tasks.revolving.cli.options.ServerOptions;
import me.etki.tasks.revolving.di.configuration.ConfigurationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Command(name = "test", description = "Runs internal tests")
public class TestCommand implements CliCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestCommand.class);

    @Inject
    private ServerOptions server = new ServerOptions();

    @Override
    public CompletableFuture<Void> run(Injector container) {
        LOGGER.warn("Not implemented yet");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public Collection<Module> createModules() {
        return Collections.singleton(new ConfigurationModule(server.toConfiguration()));
    }
}
