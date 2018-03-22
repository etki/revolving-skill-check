package me.etki.tasks.revolving.vertx;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.json.Json;
import me.etki.tasks.revolving.io.jackson.BigDecimalSerializer;

import java.util.Arrays;

public class VertXStaticConfigurator {
    private VertXStaticConfigurator() {
        // static access only
    }

    @SuppressWarnings("squid:S1602")
    public static void configure() {
        SimpleModule module = new SimpleModule()
                .addSerializer(new BigDecimalSerializer());
        //noinspection CodeBlock2Expr
        Arrays.asList(Json.mapper, Json.prettyMapper).forEach(mapper -> {
            mapper
                    .registerModules(new JavaTimeModule(), module)
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        });
    }
}
