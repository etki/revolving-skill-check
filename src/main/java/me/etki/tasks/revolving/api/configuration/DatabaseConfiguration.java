package me.etki.tasks.revolving.api.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class DatabaseConfiguration {
    @Getter
    @Setter
    private String jdbcUrl = "h2:mem";
}
