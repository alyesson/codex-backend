package br.com.codex.v1.configuration;

import br.com.codex.v1.service.DBService;

import br.com.codex.v1.tenant.TenantExecutor;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@Profile("desenvolvimento")
public class DesenvolvimentoConfig implements DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DesenvolvimentoConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    private final DBService dbService;
    private final JdbcTemplate jdbcTemplate;

    public DesenvolvimentoConfig(DBService dbService, JdbcTemplate jdbcTemplate) {
        this.dbService = dbService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean criaBaseDadosClienteFilial(String nomeBase) {
        try {
            // 1. Cria o banco, se ainda não existir
            if (!verificaSeBancoExiste(nomeBase)) {
                criarBanco(nomeBase);
            } else {
                logger.info("Banco {} já existe. Ignorando criação.", nomeBase);
                return false;
            }

            // 2. Cria o datasource para o novo banco
            DataSource novoDataSource = criarDataSource(nomeBase);

            // 3. Configura o Hibernate para criar o schema
            configurarHibernate(novoDataSource);

            // 4. Executa a geração das tabelas e dados iniciais no contexto do novo banco
            TenantExecutor.runWithDataSource(novoDataSource, () -> {
                try {
                    dbService.instanciaDB();
                } catch (Exception e) {
                    logger.error("Erro ao popular dados iniciais: {}", e.getMessage());
                    throw new RuntimeException("Erro ao popular dados iniciais", e);
                }
            });

            logger.info("Banco {} criado e inicializado com sucesso.", nomeBase);
            return true;

        } catch (Exception e) {
            logger.error("Erro ao criar banco de dados: {}", e.getMessage());
            throw new RuntimeException("Erro ao criar base de dados. Por favor, contate o suporte técnico.");
        }
    }

    private void configurarHibernate(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("br.com.codex.v1.domain");
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "create");
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");

        // Configurações para nomenclatura snake_case
        props.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        props.put("hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");

        // Configurações para evitar problemas com DDL
        props.put("hibernate.hbm2ddl.halt_on_error", "false");
        props.put("hibernate.hbm2ddl.create_namespaces", "true");

        // Desabilita validação durante a criação do schema
        props.put("javax.persistence.schema-generation.database.action", "create");
        props.put("javax.persistence.schema-generation.create-source", "metadata");
        props.put("javax.persistence.schema-generation.drop-source", "metadata");
        props.put("javax.persistence.schema-generation.scripts.action", "none");
        props.put("hibernate.connection.autocommit", "true");
        props.put("hibernate.transaction.flush_before_completion", "true");
        props.put("hibernate.connection.release_mode", "after_transaction");

        factory.setJpaProperties(props);
        factory.afterPropertiesSet();
    }

    private boolean verificaSeBancoExiste(String nomeBase) {
        String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        return jdbcTemplate.queryForList(sql, String.class, nomeBase).size() > 0;
    }

    private void criarBanco(String nomeBase) {
        String sql = "CREATE DATABASE " + nomeBase + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
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
}

