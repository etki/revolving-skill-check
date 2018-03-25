package me.etki.tasks.revolving.database;

import javax.persistence.EntityManager;
import java.util.function.Consumer;

@FunctionalInterface
public interface Unit<T> {
    @SuppressWarnings("squid:S00112")
    T execute(EntityManager manager) throws Exception;

    static Unit<Void> from(Consumer<EntityManager> consumer) {
        return manager -> {
            consumer.accept(manager);
            return null;
        };
    }
}
