package me.etki.tasks.revolving.di.api;

import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import me.etki.tasks.revolving.api.client.ApiClient;
import me.etki.tasks.revolving.api.client.Transport;

public class ApiModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Transport.class)
                .toProvider(TransportProvider.class)
                .in(LazySingletonScope.get());
        bind(ApiClient.class)
                .toProvider(ApiClientProvider.class)
                .in(LazySingletonScope.get());
    }
}
