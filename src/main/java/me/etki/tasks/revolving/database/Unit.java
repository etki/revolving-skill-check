package me.etki.tasks.revolving.database;

import javax.persistence.EntityManager;
import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface Unit<T> extends Function<EntityManager, T> {
    static Unit<Void> from(Consumer<EntityManager> consumer) {
        return manager -> {
            consumer.accept(manager);
            return null;
        };
    }
}
