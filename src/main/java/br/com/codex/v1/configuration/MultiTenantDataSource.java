package br.com.codex.v1.configuration;

import br.com.codex.v1.service.DynamicDataSourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class MultiTenantDataSource extends AbstractRoutingDataSource {

    @Autowired
    private DynamicDataSourceProvider dataSourceProvider;

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant(); // só retorna o CNPJ
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String tenant = (String) determineCurrentLookupKey();
        if (tenant == null) throw new RuntimeException("Tenant (CNPJ) não informado");

        return dataSourceProvider.getDataSource(tenant); // monta sob demanda
    }
}
