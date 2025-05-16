package br.com.codex.v1.configuration;

import br.com.codex.v1.service.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.*;

@Configuration
@Profile("producao")
public class ProducaoConfig implements DatabaseConfig{

    private static final Logger logger = LoggerFactory.getLogger(ProducaoConfig.class);

    @Autowired
    private DBService dbService;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean
    public boolean instanciaDB() {
        String dbName = "codex";
        String baseUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/")) + "/";

        // 1. Verifica e cria o banco se não existir
        try (Connection conn = DriverManager.getConnection(baseUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement()) {

            // Verifica existência do banco
            ResultSet dbResult = stmt.executeQuery("SHOW DATABASES LIKE '" + dbName + "'");
            if (!dbResult.next()) {
                stmt.executeUpdate("CREATE DATABASE " + dbName);
                logger.info("Banco de dados criado: " + dbName);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao verificar/criar banco de dados", e);
        }

        // 2. Verifica se as tabelas estão vazias (após garantir que o banco existe)
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement()) {

            // Verifica se a tabela pessoa existe e está vazia
            ResultSet tableResult = stmt.executeQuery(
                    "SELECT COUNT(*) AS count FROM information_schema.tables " +
                            "WHERE table_schema = '" + dbName + "' AND table_name = 'pessoa'");

            if (tableResult.next() && tableResult.getInt("count") > 0) {
                // Tabela existe, verifica se está vazia
                ResultSet dataResult = stmt.executeQuery("SELECT 1 FROM pessoa LIMIT 1");
                if (!dataResult.next()) {
                    logger.info("Tabela pessoa vazia - inserindo dados iniciais");
                    dbService.instanciaDB();
                }
            } else {
                // Tabela não existe (o Hibernate vai criar)
                logger.info("Tabela pessoa não existe - será criada pelo Hibernate");
            }

            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao verificar tabelas", e);
        }
    }

    public boolean criaBaseDadosClienteFilial(String nomeBase) {

        String dbName = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            dbName = nomeBase;

            // Cria conexão sem especificar o banco de dados
            String baseUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/")) + "/";

            connection = DriverManager.getConnection(baseUrl, dbUsername, dbPassword);
            statement = connection.createStatement();

            // Verifica se o banco existe
            resultSet = statement.executeQuery("SHOW DATABASES LIKE '" + dbName + "'");

            if (!resultSet.next()) {
                // Banco não existe, cria o banco
                statement.executeUpdate("CREATE DATABASE " + dbName);

                // Agora executa a inicialização do banco
                this.dbService.instanciaDB();
            }

            connection.close();
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new RuntimeException("Erro ao criar base de dados. Por favor, contate o suporte técnico.");
        }
        return true;
    }
}
