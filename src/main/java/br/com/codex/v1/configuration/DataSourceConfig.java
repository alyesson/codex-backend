package br.com.codex.v1.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String defaultDbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private final Map<Object, Object> targetDataSources = new HashMap<>();

    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicDataSource routingDataSource = new DynamicDataSource();

        // DataSource padrão (codex)
        DataSource defaultDataSource = DataSourceBuilder.create()
                .url(defaultDbUrl)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();

        targetDataSources.put("default", defaultDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);

        return routingDataSource;
    }

    // Método para adicionar novos DataSources em tempo de execução
    public void addDataSource(String dbName, DataSource dataSource) {
        targetDataSources.put(dbName, dataSource);
        DynamicDataSource routingDataSource = (DynamicDataSource) dataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet(); // Atualiza o DataSource
    }
}
