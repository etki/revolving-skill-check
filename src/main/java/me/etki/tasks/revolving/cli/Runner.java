package me.etki.tasks.revolving.cli;

import com.github.rvesse.airline.Cli;
import com.google.inject.Injector;
import me.etki.tasks.revolving.cli.command.*;
import me.etki.tasks.revolving.concurrent.CompletableFutures;
import me.etki.tasks.revolving.di.InjectorBuilderFactory;

import java.util.concurrent.CompletableFuture;

public class Runner {
    public CompletableFuture<Void> run(String[] args) {
        Cli<CliCommand> runner = Cli
                .<CliCommand>builder("bin/console")
                .withDefaultCommand(HelpCommand.class)
                .withCommand(HelpCommand.class)
                .withCommand(ServeCommand.class)
                .withCommand(AwaitCommand.class)
                .withCommand(ShutdownCommand.class)
                .withCommand(TestCommand.class)
                .build();
        try {
            CliCommand command = runner.parse(args);
            Injector injector = InjectorBuilderFactory
                    .createBuilder()
                    .withAdditionalModules(command.createModules())
                    .build()
                    .createInjector();
            return runner.parse(args).run(injector);
        } catch (Exception e) {
            return CompletableFutures.exceptional(e);
        }
    }
}
