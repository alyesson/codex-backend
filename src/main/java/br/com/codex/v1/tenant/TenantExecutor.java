package br.com.codex.v1.tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;

public class TenantExecutor {

    private static final ThreadLocal<DataSource> currentDataSource = new ThreadLocal<>();

    public static DataSource getCurrentDataSource() {
        return currentDataSource.get();
    }

    public static void runWithDataSource(DataSource dataSource, Runnable task) {
        try {
            currentDataSource.set(dataSource);
            task.run();
        } finally {
            currentDataSource.remove();
        }
    }

    public static class RoutingDataSource extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            return null;
        }

        @Override
        protected DataSource determineTargetDataSource() {
            DataSource ds = TenantExecutor.getCurrentDataSource();
            if (ds == null) {
                throw new IllegalStateException("Nenhum DataSource definido no contexto atual.");
            }
            return ds;
        }
    }
}

