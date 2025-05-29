package br.com.codex.v1.tenant;

import br.com.codex.v1.configuration.DataSourceConfig;
import br.com.codex.v1.configuration.DatabaseContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    @Autowired // Adicione esta injecão
    private DataSourceConfig dataSourceConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String jdbcUrl = httpRequest.getHeader("X-Empresa-JDBCURL");

        try {
            if (jdbcUrl != null && jdbcUrl.startsWith("jdbc:mysql://")) {
                String dbName = extractDatabaseName(jdbcUrl);

                if (!dataSourceConfig.containsDataSource(dbName)) {
                    DataSource newDataSource = dataSourceConfig.createDataSource(jdbcUrl);
                    dataSourceConfig.addDataSource(dbName, newDataSource);
                }

                DatabaseContextHolder.setCurrentDb(dbName);
            }
            chain.doFilter(request, response);
        } finally {
            DatabaseContextHolder.clear();
        }
    }

    private String extractDatabaseName(String jdbcUrl) {
        String[] parts = jdbcUrl.split("/");
        return parts[parts.length - 1].split("\\?")[0];
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
