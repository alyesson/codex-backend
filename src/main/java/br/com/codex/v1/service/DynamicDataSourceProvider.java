package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.repository.EmpresaRepository;
import br.com.codex.v1.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DynamicDataSourceProvider {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UserSS userSS;

    private final Map<String, DataSource> cache = new ConcurrentHashMap<>();

    public DataSource getDataSource(String cnpj) {
        if (cache.containsKey(cnpj)) {
            return cache.get(cnpj);
        }

        Empresa empresa = empresaRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada para o CNPJ: " + cnpj));

        DataSource dataSource = DataSourceBuilder.create()
                .url("jdbc:mysql://servidor:3306/" + empresa.getJdbcUrl())
                .username(userSS.getUsername())
                .password(userSS.getPassword())
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();

        cache.put(cnpj, dataSource);
        return dataSource;
    }
}
