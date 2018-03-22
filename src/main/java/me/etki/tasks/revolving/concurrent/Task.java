package me.etki.tasks.revolving.concurrent;

@FunctionalInterface
public interface Task {
    @SuppressWarnings("squid:S00112")
    void execute() throws Exception;
}
