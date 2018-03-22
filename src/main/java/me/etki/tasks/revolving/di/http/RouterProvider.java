package me.etki.tasks.revolving.di.http;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.zandero.rest.RestBuilder;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import me.etki.tasks.revolving.server.ConstraintViolationExceptionHandler;
import me.etki.tasks.revolving.server.controller.*;

import java.util.Arrays;
import java.util.List;

public class RouterProvider implements Provider<Router> {
    private final Injector injector;
    private final Vertx vertx;

    @Inject
    public RouterProvider(Injector injector, Vertx vertx) {
        this.injector = injector;
        this.vertx = vertx;
    }

    @Override
    public Router get() {
        List<Class> controllers = Arrays.asList(
                AccountController.class,
                HealthController.class,
                RateController.class,
                ShutdownController.class,
                SwaggerUIController.class,
                TransferController.class
        );
        Router router = new RestBuilder(vertx)
                .register(controllers.stream().map(injector::getInstance).toArray())
                .errorHandler(ConstraintViolationExceptionHandler.class)
                .build();
        router
                .route("/v1/_openapi/schema.yml")
                .handler(StaticHandler.create("openapi.yml"));
        router
                .route("/v1/_openapi/*")
                // ooh noes that's a hardcoded version
                .handler(StaticHandler.create("META-INF/resources/webjars/swagger-ui/3.12.1"));
        return router;
    }
}
