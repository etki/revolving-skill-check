package me.etki.tasks.revolving.database;

import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class PersistenceUnit implements PersistenceUnitInfo {

    public static final String DEFAULT_NAME = "default";

    private final String name;
    private final DataSource source;
    private final List<Class> entities;

    public PersistenceUnit(String name, DataSource source, List<Class> entities) {
        this.name = name;
        this.source = source;
        this.entities = entities;
    }

    public PersistenceUnit(DataSource source, List<Class> entities) {
        this(DEFAULT_NAME, source, entities);
    }

    public PersistenceUnit(DataSource source, Class ... entities) {
        this(source, Arrays.asList(entities));
    }

    @Override
    public String getPersistenceUnitName() {
        return name;
    }

    @Override
    public String getPersistenceProviderClassName() {
        return HibernatePersistenceProvider.class.getName();
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public DataSource getJtaDataSource() {
        return null;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return source;
    }

    @Override
    public List<String> getMappingFileNames() {
        return Collections.emptyList();
    }

    @Override
    public List<URL> getJarFileUrls() {
        return Collections.emptyList();
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return PersistenceUnit.class.getProtectionDomain().getCodeSource().getLocation();
    }

    @Override
    public List<String> getManagedClassNames() {
        return entities
                .stream()
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return SharedCacheMode.NONE;
    }

    @Override
    public ValidationMode getValidationMode() {
        return ValidationMode.AUTO;
    }

    @Override
    public Properties getProperties() {
        return new Properties();
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return "2.0";
    }

    @Override
    public ClassLoader getClassLoader() {
        return PersistenceUnit.class.getClassLoader();
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
        // this is a dummy implementation, ignoring this stuff
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return PersistenceUnit.class.getClassLoader();
    }
}
