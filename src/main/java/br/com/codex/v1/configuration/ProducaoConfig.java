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
public class ProducaoConfig{

    private static final Logger logger = LoggerFactory.getLogger(ProducaoConfig.class);

    @Autowired
    private DBService dbService;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    /*public boolean criaBaseDadosClienteFilial(String nomeBase) {

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
    }*/
}
