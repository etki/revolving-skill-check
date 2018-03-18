package me.etki.tasks.revolving.cli;

import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public interface CliCommand {
    CompletableFuture<Void> run(Injector container);
    default Collection<Module> createModules() {
        return Collections.emptyList();
    }
}
