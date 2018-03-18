package me.etki.tasks.revolving.di;

import com.google.inject.Injector;
import com.netflix.governator.guice.LifecycleInjector;
import me.etki.tasks.revolving.api.configuration.DatabaseConfiguration;
import me.etki.tasks.revolving.api.configuration.ServerConfiguration;
import me.etki.tasks.revolving.di.misc.MiscModule;
import me.etki.tasks.revolving.di.vertx.VertXModule;

public class InjectorFactory {

    private InjectorFactory() {
        // static access only
    }

    public static Injector createInjector(
            ServerConfiguration server,
            DatabaseConfiguration database) {

        return LifecycleInjector
                .builder()
                .withModules(binder -> {
                    binder.bind(ServerConfiguration.class).toInstance(server);
                    binder.bind(DatabaseConfiguration.class).toInstance(database);
                })
                .withAdditionalModules(new VertXModule(), new MiscModule())
                .build()
                .createInjector();
    }
}
