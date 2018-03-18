package me.etki.tasks.revolving.api.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class ServerConfiguration {
    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private int port = 8080;
}
