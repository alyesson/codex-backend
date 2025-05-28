package br.com.codex.v1.configuration;

import org.reflections.Reflections;

import javax.persistence.Entity;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class PersistenceUnitInfoAdapter implements PersistenceUnitInfo {

    private final String persistenceUnitName;
    private final DataSource dataSource;

    public PersistenceUnitInfoAdapter(String persistenceUnitName, DataSource dataSource) {
        this.persistenceUnitName = persistenceUnitName;
        this.dataSource = dataSource;
    }

    @Override
    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    @Override
    public String getPersistenceProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
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
        return dataSource;
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
        return null;
    }

    @Override
    public List<String> getManagedClassNames() {
        // Liste as suas entidades aqui com FQCN (package completo)
        Reflections reflections = new Reflections("br.com.codex.v1.domain");
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
        return entities.stream().map(Class::getName).collect(Collectors.toList());
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return true; // só as listadas em getManagedClassNames serão usadas
    }

    @Override
    public Properties getProperties() {
        return new Properties();
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return "2.1";
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
        // não utilizado
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public javax.persistence.SharedCacheMode getSharedCacheMode() {
        return javax.persistence.SharedCacheMode.UNSPECIFIED;
    }

    @Override
    public javax.persistence.ValidationMode getValidationMode() {
        return javax.persistence.ValidationMode.AUTO;
    }
}
