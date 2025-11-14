package br.com.codex.v1.security;

import br.com.codex.v1.configuration.DataSourceConfig;
import br.com.codex.v1.configuration.DatabaseContextHolder;
import br.com.codex.v1.domain.dto.CredenciaisDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final DataSourceConfig dataSourceConfig;
    private String defaultDbUrl;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JWTUtil jwtUtil,
                                   DataSourceConfig dataSourceConfig,
                                   String defaultDbUrl) { // Modificado construtor
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.dataSourceConfig = dataSourceConfig;
        this.defaultDbUrl = defaultDbUrl;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            CredenciaisDto creds = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDto.class);

            // Monta a URL completa
            String baseUrl = defaultDbUrl.substring(0, defaultDbUrl.lastIndexOf("/") + 1);
            String jdbcUrl = baseUrl + creds.getJdbcUrl();

            System.out.println("URL completa: " + jdbcUrl); // Log para depuração
            String dbName = extractDatabaseName(creds.getJdbcUrl()); // Extrai apenas do nome do banco

            if (!dataSourceConfig.containsDataSource(dbName)) {
                DataSource newDataSource = createDataSource(jdbcUrl); // Usa a URL completa aqui
                dataSourceConfig.addDataSource(dbName, newDataSource);
            }

            DatabaseContextHolder.setCurrentDb(dbName);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    creds.getEmail(),
                    creds.getSenha(),
                    new ArrayList<>());

            return authenticationManager.authenticate(authToken);
        } catch (Exception e) {
            throw new RuntimeException("Falha na autenticação: " + e.getMessage(), e);
        }
    }

    private String extractDatabaseName(String jdbcUrl) {
        String[] parts = jdbcUrl.split("/");
        return parts[parts.length - 1].split("\\?")[0];
    }

    private DataSource createDataSource(String jdbcUrl) {
        return DataSourceBuilder.create()
                .url(jdbcUrl) // Usa a URL completa aqui
                .username("postgres")
                .password("QE5kcC43NDgw-2#9_12Jnma=1@#KT")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((UserSS) authResult.getPrincipal()).getUsername();
        List<String> perfis = ((UserSS) authResult.getPrincipal()).getPerfis();
        String token = jwtUtil.generateToken(username, perfis);
        response.setHeader("access-control-expose-headers", "Authorization");
        response.setHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().append(json());
    }

    private CharSequence json() {
        long date = new Date().getTime();
        return "{"
                + "\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não Autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\"}";
    }
}
