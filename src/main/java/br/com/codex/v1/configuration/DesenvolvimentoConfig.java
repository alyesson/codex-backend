package br.com.codex.v1.configuration;

import br.com.codex.v1.service.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.*;

@Configuration
@Profile("desenvolvimento")
public class DesenvolvimentoConfig implements DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DesenvolvimentoConfig.class);

    @Autowired
    private DBService dbService;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Override
    public boolean criaBaseDadosClienteFilial(String nomeBase) {
        String dbName = nomeBase.toLowerCase(); // PostgreSQL geralmente usa lowercase
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Conecta ao database padrão (geralmente 'postgres')
            String baseUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/") + 1);
            String jdbcUrl = baseUrl + dbName;

            connection = DriverManager.getConnection(baseUrl, dbUsername, dbPassword);
            statement = connection.createStatement();

            // Verifica se o database já existe - Sintaxe PostgreSQL
            resultSet = statement.executeQuery(
                    "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'"
            );

            if (!resultSet.next()) {
                logger.info("Criando banco de dados: {}", dbName);

                // Cria o database - Sintaxe PostgreSQL
                // O PostgreSQL usa UTF-8 por padrão, então não precisa especificar
                statement.executeUpdate("CREATE DATABASE " + dbName);

                // Opcional: se quiser especificar encoding e template
                // statement.executeUpdate("CREATE DATABASE " + dbName + " ENCODING 'UTF8' TEMPLATE template0");

                // Cria o DataSource para a nova base
                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setUrl(jdbcUrl);
                dataSource.setUsername(dbUsername);
                dataSource.setPassword(dbPassword);
                dataSource.setDriverClassName(driverClassName);

                // Inicializa tabelas e dados
                dbService.criaOutrasBases(dataSource);
                logger.info("Banco de dados {} criado e populado com sucesso!", dbName);
                return true;
            } else {
                logger.warn("Banco {} já existe. Ignorando criação.", dbName);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Erro ao criar base de dados: {}", e.getMessage());
            throw new RuntimeException("Erro ao criar base de dados. Por favor, contate o suporte técnico.", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                logger.warn("Erro ao fechar conexão JDBC: {}", e.getMessage());
            }
        }
    }
}