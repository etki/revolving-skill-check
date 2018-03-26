package me.etki.tasks.revolving.vertx;

import io.vertx.core.json.Json;
import me.etki.tasks.revolving.io.jackson.MapperConfigurator;

import java.util.Arrays;

public class VertXStaticConfigurator {
    private VertXStaticConfigurator() {
        // static access only
    }

    public static void configure() {
        Arrays
                .asList(Json.mapper, Json.prettyMapper)
                .forEach(MapperConfigurator::configure);
    }
}
