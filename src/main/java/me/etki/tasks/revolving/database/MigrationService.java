package me.etki.tasks.revolving.database;

import com.google.inject.Inject;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import javax.sql.DataSource;
import java.sql.SQLException;

public class MigrationService {
    private static final String CHANGELOG_FILE = "migrations.yml";

    private final DataSource dataSource;

    @Inject
    public MigrationService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void migrate() throws LiquibaseException, SQLException {
        ResourceAccessor accessor = new ClassLoaderResourceAccessor(MigrationService.class.getClassLoader());
        DatabaseConnection connection = new JdbcConnection(dataSource.getConnection());
        Liquibase liquibase = new Liquibase(CHANGELOG_FILE, accessor, connection);
        liquibase.update((Contexts) null);
    }
}
