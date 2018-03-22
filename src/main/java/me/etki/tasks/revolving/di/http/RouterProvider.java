package me.etki.tasks.revolving.di.http;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.zandero.rest.RestBuilder;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import me.etki.tasks.revolving.server.ConstraintViolationExceptionHandler;
import me.etki.tasks.revolving.server.ContentionExceptionHandler;
import me.etki.tasks.revolving.server.IllegalArgumentExceptionHandler;
import me.etki.tasks.revolving.server.ResourceNotFoundExceptionHandler;
import me.etki.tasks.revolving.server.RouteNotFoundHandler;
import me.etki.tasks.revolving.server.controller.AccountBalanceController;
import me.etki.tasks.revolving.server.controller.AccountController;
import me.etki.tasks.revolving.server.controller.HealthController;
import me.etki.tasks.revolving.server.controller.RateController;
import me.etki.tasks.revolving.server.controller.ShutdownController;
import me.etki.tasks.revolving.server.controller.SwaggerUIController;
import me.etki.tasks.revolving.server.controller.TransferController;

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
        List<Class<?>> controllers = Arrays.asList(
                AccountController.class,
                AccountBalanceController.class,
                HealthController.class,
                RateController.class,
                ShutdownController.class,
                SwaggerUIController.class,
                TransferController.class
        );
        Router router = new RestBuilder(vertx)
                .register(controllers.stream().map(injector::getInstance).toArray())
                .errorHandler(ConstraintViolationExceptionHandler.class)
                .errorHandler(IllegalArgumentExceptionHandler.class)
                .errorHandler(ResourceNotFoundExceptionHandler.class)
                .errorHandler(ContentionExceptionHandler.class)
                .notFound(RouteNotFoundHandler.class)
                .build();
        router
                .route("/v1/_openapi/schema.yml")
                .handler(StaticHandler.create("openapi.yml"));
        router
                .route("/v1/_openapi/*")
                // ooh noes that's a hardcoded version
                .handler(StaticHandler.create("META-INF/resources/webjars/swagger-ui/3.13.0"));
        return router;
    }
}
