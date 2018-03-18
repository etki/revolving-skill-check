package me.etki.tasks.revolving.api.http;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Accessors(chain = true)
public class JsonProblem {
    public static final JsonProblem NOT_FOUND = new JsonProblem()
            .setStatus(404)
            .setTitle("Not Found")
            .setDetail("Requested resource does not exist");
    public static final JsonProblem MISSING_BODY = new JsonProblem()
            .setStatus(400)
            .setTitle("Missing request body")
            .setDetail("This route requires payload");

    @Getter
    @Setter
    @NonNull
    private String type = "about:blank";
    @Getter
    @Setter
    private int status;
    @Getter
    @Setter
    @NonNull
    private String title;
    @Getter
    @Setter
    private String detail;
    @Getter
    @Setter
    private String instance;
    @Getter
    @Setter
    @JsonUnwrapped
    private Map<String, ?> extra = new HashMap<>();
}
