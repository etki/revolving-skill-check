package me.etki.tasks.revolving.di.database;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;
import me.etki.tasks.revolving.cli.options.DatabaseOptions;

import javax.sql.DataSource;

public class DataSourceProvider implements Provider<DataSource> {
    private final DatabaseOptions configuration;

    @Inject
    public DataSourceProvider(DatabaseOptions configuration) {
        this.configuration = configuration;
    }

    @Override
    public DataSource get() {
        HikariDataSource source = new HikariDataSource();
        String jdbcUrl = configuration.getJdbcUrl();
        if (!jdbcUrl.startsWith("jdbc:")) {
            jdbcUrl = "jdbc:" + jdbcUrl;
        }
        source.setJdbcUrl(jdbcUrl);
        return source;
    }
}
