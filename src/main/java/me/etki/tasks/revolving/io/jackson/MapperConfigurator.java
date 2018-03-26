package me.etki.tasks.revolving.io.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MapperConfigurator {
    private MapperConfigurator() {
        // static helper class
    }

    public static ObjectMapper configure(ObjectMapper mapper) {
        SimpleModule module = new SimpleModule()
                .addSerializer(new BigDecimalSerializer());
        return mapper
                .registerModules(new JavaTimeModule(), module)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
