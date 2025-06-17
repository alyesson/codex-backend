package br.com.codex.v1.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DatabaseContextHolder.getCurrentDb();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        Object lookupKey = determineCurrentLookupKey();
        if (lookupKey == null) {
            return super.determineTargetDataSource();
        }
        DataSource dataSource = (DataSource) this.getResolvedDataSources().get(lookupKey);
        if (dataSource == null) {
            return super.determineTargetDataSource();
        }
        return dataSource;
    }
}
