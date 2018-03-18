package me.etki.tasks.revolving.di.configuration;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import me.etki.tasks.revolving.api.configuration.DatabaseConfiguration;
import me.etki.tasks.revolving.api.configuration.ServerConfiguration;

@RequiredArgsConstructor
public class ConfigurationModule extends AbstractModule {
    private final ServerConfiguration server;
    private final DatabaseConfiguration database;

    public ConfigurationModule(ServerConfiguration server) {
        this(server, null);
    }

    public ConfigurationModule() {
        this(null, null);
    }

    @Override
    protected void configure() {
        if (server != null) {
            bind(ServerConfiguration.class).toInstance(server);
        }
        if (database != null) {
            bind(DatabaseConfiguration.class).toInstance(database);
        }
    }
}
