package me.etki.tasks.revolving.di.vertx;

import io.vertx.core.Vertx;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Since Vert.X creates bunch of heavy stuff in constructor (e.g. thread
 * pools) and provides no means of checking if it was instantiated,
 * additional proxy is used.
 */
public class VertXSingletonFactory {
    private final AtomicLong accesses = new AtomicLong();
    private volatile Vertx instance;

    public synchronized Vertx get() {
        accesses.incrementAndGet();
        if (instance == null) {
            instance = Vertx.vertx();
        }
        return instance;
    }

    public long getAccesses() {
        return accesses.get();
    }
}
