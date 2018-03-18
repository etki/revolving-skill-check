package me.etki.tasks.revolving.server;

import io.vertx.core.http.HttpMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Binding {
    @Getter
    private final String path;
    @Getter
    private final HttpMethod method;
}
