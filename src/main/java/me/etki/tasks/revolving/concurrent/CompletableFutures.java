package me.etki.tasks.revolving.concurrent;

import io.vertx.core.Future;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("WeakerAccess")
public class CompletableFutures {
    public static final CompletableFuture<Void> VOID = completed(null);

    private CompletableFutures() {
        // static access only
    }

    public static <T> CompletableFuture<T> exceptional(Throwable throwable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(throwable);
        return future;
    }

    public static <T> CompletableFuture<T> completed(T value) {
        return CompletableFuture.completedFuture(value);
    }

    public static <T> CompletableFuture<T> execute(Callable<T> producer) {
        try {
            return completed(producer.call());
        } catch (Exception e) {
            return exceptional(e);
        }
    }

    public static CompletableFuture<Void> execute(Task task) {
        return execute(() -> {
            task.execute();
            return null;
        });
    }

    public static <T> Future<T> toVertXFuture(CompletableFuture<T> future) {
        Future<T> result = Future.future();
        future
                .thenAccept(result::complete)
                .exceptionally(error -> {
                    while (error instanceof CompletionException || error instanceof ExecutionException) {
                        error = error.getCause();
                    }
                    result.fail(error);
                    return null;
                });
        return result;
    }
}
