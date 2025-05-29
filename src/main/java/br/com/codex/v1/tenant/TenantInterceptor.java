package br.com.codex.v1.tenant;

import br.com.codex.v1.configuration.DatabaseContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantHeader = request.getHeader("X-Empresa-JDBCURL");

        if (tenantHeader != null && !tenantHeader.isEmpty()) {
            String dbName = extrairNomeBanco(tenantHeader);
            DatabaseContextHolder.setCurrentDb(dbName);
        }

        return true;
    }

    private String extrairNomeBanco(String jdbcUrl) {
        String[] parts = jdbcUrl.split("/");
        return parts[parts.length - 1].split("\\?")[0];
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        DatabaseContextHolder.clear();
    }
}
