package me.etki.tasks.revolving.di.database;

import com.google.inject.Inject;
import com.google.inject.Provider;
import me.etki.tasks.revolving.database.PersistenceUnit;
import me.etki.tasks.revolving.database.entity.AccountEntity;
import me.etki.tasks.revolving.database.entity.RateEntity;
import me.etki.tasks.revolving.database.entity.TransferEntity;
import org.h2.Driver;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HibernateProvider implements Provider<EntityManagerFactory> {
    private final DataSource source;

    @Inject
    public HibernateProvider(DataSource source) {
        this.source = source;
    }

    @Override
    public EntityManagerFactory get() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", H2Dialect.class);
        properties.put("hibernate.driver_class", Driver.class);
        properties.put("hibernate.connection.isolation", Connection.TRANSACTION_REPEATABLE_READ);
        properties.put("hibernate.connection.datasource", source);
        List<Class> entities = Arrays.asList(AccountEntity.class, RateEntity.class, TransferEntity.class);
        PersistenceUnitInfo unit = new PersistenceUnit(source, entities);
        return new HibernatePersistenceProvider().createContainerEntityManagerFactory(unit, properties);
    }
}
