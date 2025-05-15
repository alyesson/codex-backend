package br.com.codex.v1.configuration;

import br.com.codex.v1.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.*;

@Configuration
@Profile("producao")
public class ProducaoConfig implements DatabaseConfig{
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
        try {
            // Extrai o nome do banco da URL (codex)
            String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?"));

            // Cria conexão sem especificar o banco de dados
            String baseUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/")) + "/";

            Connection connection = DriverManager.getConnection(baseUrl, dbUsername, dbPassword);
            Statement statement = connection.createStatement();

            // Verifica se o banco existe
            ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE '" + dbName + "'");

            if (!resultSet.next()) {
                // Banco não existe, cria o banco
                statement.executeUpdate("CREATE DATABASE " + dbName);

                // Agora executa a inicialização do banco
                this.dbService.instanciaDB();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean criaBaseDadosClienteFilial(String nomeBase) {
        try {
            // Extrai o nome do banco da URL (codex)
            String dbName = nomeBase;

            // Cria conexão sem especificar o banco de dados
            String baseUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/")) + "/";

            Connection connection = DriverManager.getConnection(baseUrl, dbUsername, dbPassword);
            Statement statement = connection.createStatement();

            // Verifica se o banco existe
            ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE '" + dbName + "'");

            if (!resultSet.next()) {
                // Banco não existe, cria o banco
                statement.executeUpdate("CREATE DATABASE " + dbName);

                // Agora executa a inicialização do banco
                this.dbService.instanciaDB();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
