package me.etki.tasks.revolving.cli;

import com.github.rvesse.airline.Cli;
import com.google.inject.Injector;
import me.etki.tasks.revolving.cli.command.*;
import me.etki.tasks.revolving.concurrent.CompletableFutures;
import me.etki.tasks.revolving.di.InjectorBuilderFactory;
import me.etki.tasks.revolving.service.LifecycleManager;

import java.util.concurrent.CompletableFuture;

public class Runner {

    @SuppressWarnings("squid:S1602")
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
            return runner
                    .parse(args)
                    .run(injector)
                    .handle((nothing, error) -> {
                        //noinspection CodeBlock2Expr
                        return injector
                                .getInstance(LifecycleManager.class)
                                .shutdown()
                                .thenCompose(v -> {
                                    if (error != null) {
                                        return CompletableFutures.exceptional(error);
                                    }
                                    return CompletableFutures.VOID;
                                });
                    })
                    .thenCompose(future -> future);
        } catch (Exception e) {
            return CompletableFutures.exceptional(e);
        }
    }
}
