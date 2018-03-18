package me.etki.tasks.revolving.cli.command;

import com.github.rvesse.airline.annotations.Command;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import me.etki.tasks.revolving.cli.CliCommand;
import me.etki.tasks.revolving.cli.options.DatabaseOptions;
import me.etki.tasks.revolving.cli.options.ServerOptions;
import me.etki.tasks.revolving.di.configuration.ConfigurationModule;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Command(name = "serve", description = "Spins up internal server")
public class ServeCommand implements CliCommand {
    @Inject
    private DatabaseOptions database = new DatabaseOptions();

    @Inject
    private ServerOptions server = new ServerOptions();

    @Override
    public CompletableFuture<Void> run(Injector container) {
        CompletableFuture<Void> promise = new CompletableFuture<>();
        //noinspection CodeBlock2Expr
        container
                .getInstance(HttpServer.class)
                .requestHandler(container.getInstance(Router.class)::accept)
                .listen()
                .requestStream()
                .endHandler(promise::complete);
        return promise;
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
