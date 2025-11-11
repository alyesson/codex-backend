package br.com.codex.v1.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DataSourceConfig {

    private final Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();
    private DataSource defaultDataSource;

    @Value("${spring.datasource.url}")
    private String defaultDbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void init() {
        this.defaultDataSource = createDataSource(defaultDbUrl);
        targetDataSources.put("default", defaultDataSource);
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicDataSource routingDataSource = new DynamicDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    public synchronized void addDataSource(String tenantId, DataSource dataSource) {
        targetDataSources.put(tenantId, dataSource);
        DynamicDataSource routingDataSource = (DynamicDataSource) dataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet();
    }

    public boolean containsDataSource(String tenantId) {
        return targetDataSources.containsKey(tenantId);
    }

    public DataSource createDataSource(String url) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}