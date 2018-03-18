package me.etki.tasks.revolving.di.vertx;

import com.google.inject.Provider;
import io.vertx.core.Vertx;

public class VertXProvider implements Provider<Vertx> {

    @Override
    public Vertx get() {
        return Vertx.vertx();
    }
}
