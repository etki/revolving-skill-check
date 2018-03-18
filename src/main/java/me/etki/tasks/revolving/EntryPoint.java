package me.etki.tasks.revolving;

import me.etki.tasks.revolving.cli.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) {
        Runner runner = new Runner();
        try {
            runner.run(args).get();
        } catch (Exception e) {
            LOGGER.error("Application has thrown an unexpected exception:", e);
            System.exit(1);
        }
    }
}
