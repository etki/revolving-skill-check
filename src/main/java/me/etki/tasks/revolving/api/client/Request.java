package me.etki.tasks.revolving.api.client;

import io.vertx.core.http.HttpMethod;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
public class Request {

    @Getter
    @Setter
    private String route;
    @Getter
    @Setter
    private HttpMethod method = HttpMethod.GET;

    @Getter
    @Setter
    private Map<String, List<String>> parameters = Collections.emptyMap();

    public static Request of(String route) {
        return new Request().setRoute(route);
    }

    public static Request of(HttpMethod method, String route) {
        return new Request().setMethod(method).setRoute(route);
    }
}
