package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.*;
import br.com.codex.v1.domain.fiscal.NotaFiscalServico;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JasperNotaServicoReportService {
    private static final Logger logger = LoggerFactory.getLogger(JasperNotaServicoReportService.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private NotaFiscalServicoService notaFiscalServicoService;

    public byte[] generateNotaFiscalServicoReport(Long notaServicoId) throws Exception {
        Connection connection = null;
        try {

            NotaFiscalServico notaServico = notaFiscalServicoService.findById(notaServicoId);
            if (notaServico == null) {
                throw new ObjectNotFoundException("Nota fiscal não encontrada: " + notaServicoId);
            }

            InputStream jasperStream = resourceLoader.getResource("classpath:reports/nota_fiscal_servico_template.jasper").getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_CODIGO", notaServicoId.toString());
            connection = dataSource.getConnection();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) { /* log error */ }
            }
        }
    }
}