package me.etki.tasks.revolving.di.api;

import com.google.inject.Inject;
import com.google.inject.Provider;
import me.etki.tasks.revolving.api.client.ApiClient;
import me.etki.tasks.revolving.api.client.Transport;

public class ApiClientProvider implements Provider<ApiClient> {

    private final Transport transport;

    @Inject
    public ApiClientProvider(Transport transport) {
        this.transport = transport;
    }

    @Override
    public ApiClient get() {
        return new ApiClient(transport);
    }
}
