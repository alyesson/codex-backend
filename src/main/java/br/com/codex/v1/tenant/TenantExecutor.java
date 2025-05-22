package br.com.codex.v1.tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;

public class TenantExecutor {

    private static final ThreadLocal<DataSource> currentDataSource = new ThreadLocal<>();

    /**
     * Fornece o DataSource atual para o contexto.
     */
    public static DataSource getCurrentDataSource() {
        return currentDataSource.get();
    }

    /**
     * Executa uma tarefa com um DataSource temporário.
     */
    public static void runWithDataSource(DataSource dataSource, Runnable task) {
        try {
            currentDataSource.set(dataSource);
            task.run();
        } finally {
            currentDataSource.remove();
        }
    }

    /**
     * Para usar com AbstractRoutingDataSource.
     */
    public static class RoutingDataSource extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            // Não estamos usando uma chave (como "tenantId"), só o DataSource diretamente
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

