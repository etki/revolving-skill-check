package me.etki.tasks.revolving;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class EntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) {
        LOGGER.info("Application has been successfully invoked!");
        LOGGER.info("Invocation arguments: {}", Arrays.asList(args));
    }
}
