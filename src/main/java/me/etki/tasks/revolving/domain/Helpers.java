package me.etki.tasks.revolving.domain;

import me.etki.tasks.revolving.api.Page;
import me.etki.tasks.revolving.api.exception.ResourceNotFoundException;
import me.etki.tasks.revolving.concurrent.CompletableFutures;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("WeakerAccess")
public class Helpers {
    private Helpers() {
        // static helpers class
    }

    public static <T> CompletableFuture<Page<T>> requirePage(CompletableFuture<Page<T>> future) {
        return requirePage(future, "Could not find requested page");
    }

    public static <T> CompletableFuture<Page<T>> requirePage(CompletableFuture<Page<T>> future, String message) {
        return future.thenCompose(page -> {
            if (page != null && !page.getContent().isEmpty()) {
                return CompletableFutures.completed(page);
            }
            ResourceNotFoundException exception = new ResourceNotFoundException(message);
            return CompletableFutures.exceptional(exception);
        });
    }

    @SuppressWarnings("squid:S1602")
    public static <T> CompletableFuture<T> requireEntity(CompletableFuture<Optional<T>> future, String message) {
        return future.thenCompose(entity -> {
            //noinspection CodeBlock2Expr
            return entity
                    .map(CompletableFutures::completed)
                    .orElseGet(() -> {
                        ResourceNotFoundException exception = new ResourceNotFoundException(message);
                        return CompletableFutures.exceptional(exception);
                    });
        });
    }

    public static <T> CompletableFuture<T> requireEntity(CompletableFuture<Optional<T>> future) {
        return requireEntity(future, "Couldn't find requested entity");
    }
}
