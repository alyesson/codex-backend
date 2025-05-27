package br.com.codex.v1.configuration;

import br.com.codex.v1.service.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.util.Properties;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Component
@Profile("desenvolvimento")
public class DatabaseTenantInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseTenantInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    private final DBService dbService;

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    public DatabaseTenantInitializer(JdbcTemplate jdbcTemplate, DBService dbService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbService = dbService;
    }

    public boolean inicializarEmpresa(String nomeBase) {
        try {
            if (!verificaSeBancoExiste(nomeBase)) {
                criarBanco(nomeBase);
            }

            DataSource novoDataSource = criarDataSource(nomeBase);

            // 1. Configura Hibernate e cria schema
            configurarHibernate(novoDataSource);

            // 2. Adiciona ao DataSource dinâmico
            dataSourceConfig.addDataSource(nomeBase, novoDataSource);

            // 3. Define o tenant atual
            DatabaseContextHolder.setCurrentDb(nomeBase);

            // 4. Popula dados iniciais
            dbService.instanciaDB();

            return true;
        } catch (Exception e) {
            logger.error("Erro ao criar tenant {}: {}", nomeBase, e.getMessage());
            throw new RuntimeException("Falha ao criar tenant", e);
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    private boolean verificaSeBancoExiste(String nomeBase) {
        String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        return !jdbcTemplate.queryForList(sql, String.class, nomeBase).isEmpty();
    }

    private void criarBanco(String nomeBase) {
        String sql = "CREATE DATABASE IF NOT EXISTS " + nomeBase + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        jdbcTemplate.execute(sql);
    }

    private DataSource criarDataSource(String nomeBase) {
        String jdbcUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/") + 1) + nomeBase + "?serverTimezone=UTC";

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    private void configurarHibernate(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("br.com.codex.v1.domain");
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "create"); // SEMPRE recria o schema
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.physical_naming_strategy", "br.com.codex.v1.configuration.SnakeCaseNamingStrategy");
        props.put("hibernate.hbm2ddl.halt_on_error", "false");
        props.put("hibernate.jdbc.time_zone", "UTC");
        props.put("hibernate.show_sql", "true");

        // Desativa validação de schema para evitar erros
        props.put("javax.persistence.schema-generation.database.action", "create");
        props.put("javax.persistence.schema-generation.create-source", "metadata");
        props.put("javax.persistence.schema-generation.drop-source", "metadata");

        factory.setJpaProperties(props);
        factory.afterPropertiesSet();
    }
}

