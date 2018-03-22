package me.etki.tasks.revolving.api.client;

import com.google.inject.Inject;
import io.vertx.core.http.HttpMethod;
import me.etki.tasks.revolving.api.Acknowledgement;
import me.etki.tasks.revolving.api.ServiceStatus;
import me.etki.tasks.revolving.server.Routes;

import java.util.concurrent.CompletableFuture;

public class ApiClient {

    private final Transport transport;

    @Inject
    public ApiClient(Transport transport) {
        this.transport = transport;
    }

    public CompletableFuture<ServiceStatus> getHealth() {
        return transport.execute(Request.of(Routes.HEALTH), ServiceStatus.class);
    }

    public CompletableFuture<Acknowledgement> shutdown() {
        return transport.execute(
                Request.of(HttpMethod.POST, Routes.SHUTDOWN),
                Acknowledgement.class
        );
    }
}
