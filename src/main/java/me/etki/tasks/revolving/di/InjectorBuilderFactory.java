package me.etki.tasks.revolving.di;

import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.guice.LifecycleInjectorBuilder;
import me.etki.tasks.revolving.di.misc.MiscModule;
import me.etki.tasks.revolving.di.vertx.VertXModule;

public class InjectorBuilderFactory {

    private InjectorBuilderFactory() {
        // static access only
    }

    public static LifecycleInjectorBuilder createBuilder() {
        return LifecycleInjector
                .builder()
                .withAdditionalModules(new VertXModule(), new MiscModule());
    }
}
