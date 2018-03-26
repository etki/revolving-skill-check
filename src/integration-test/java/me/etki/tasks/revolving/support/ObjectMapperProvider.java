package me.etki.tasks.revolving.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.etki.tasks.revolving.io.jackson.MapperConfigurator;

@SuppressWarnings("WeakerAccess")
public class ObjectMapperProvider {
    private static ObjectMapper objectMapper;

    private ObjectMapperProvider() {
        // static access only
    }

    public static ObjectMapper create() {
        return MapperConfigurator.configure(new ObjectMapper());
    }

    public static synchronized ObjectMapper getInstance() {
        if (objectMapper == null) {
            objectMapper = create();
        }
        return objectMapper;
    }
}
