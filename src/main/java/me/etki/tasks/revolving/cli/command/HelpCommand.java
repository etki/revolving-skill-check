package me.etki.tasks.revolving.cli.command;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.help.Help;
import com.google.inject.Injector;
import me.etki.tasks.revolving.cli.CliCommand;
import me.etki.tasks.revolving.concurrent.CompletableFutures;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Command(name = "help", description = "Standard help window")
public class HelpCommand implements CliCommand {
    @Inject
    private Help<?> delegate;

    @Override
    public CompletableFuture<Void> run(Injector container) {
        return CompletableFutures.execute(delegate);
    }
}
