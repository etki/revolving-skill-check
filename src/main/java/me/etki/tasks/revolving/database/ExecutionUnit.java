package me.etki.tasks.revolving.database;

import javax.persistence.EntityManager;

@FunctionalInterface
public interface ExecutionUnit<T> {
    @SuppressWarnings("squid:S00112")
    T execute(EntityManager manager) throws Exception;
}
