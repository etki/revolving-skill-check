package me.etki.tasks.revolving.di.database;

import com.google.inject.AbstractModule;
import com.netflix.governator.guice.lazy.LazySingletonScope;
import me.etki.tasks.revolving.database.AsyncExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

public class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataSource.class)
                .toProvider(DataSourceProvider.class)
                .in(LazySingletonScope.get());
        bind(AsyncExecutor.class)
                .toProvider(AsyncExecutorProvider.class)
                .in(LazySingletonScope.get());
        bind(EntityManagerFactory.class)
                .toProvider(HibernateProvider.class)
                .in(LazySingletonScope.get());
    }
}
