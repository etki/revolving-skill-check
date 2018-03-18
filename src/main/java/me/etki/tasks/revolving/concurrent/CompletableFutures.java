package me.etki.tasks.revolving.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

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
}
