package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.*;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.estoque.SolicitacaoMaterial;
import br.com.codex.v1.domain.estoque.SolicitacaoMaterialItens;
import br.com.codex.v1.domain.repository.SolicitacaoMaterialItensRepository;
import br.com.codex.v1.domain.repository.SolicitacaoMaterialRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperEstoqueReportService {
    private static final Logger logger = LoggerFactory.getLogger(JasperEstoqueReportService.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SolicitacaoMaterialService solicitacaoMaterialService;

    @Autowired
    private SolicitacaoMaterialItensRepository solicitacaoMaterialItensRepository;
    
    public byte[] generateSolicitacaoMaterialReport(Long solicitacaoMaterialId) throws Exception {
        Connection connection = null;
        try {

            connection = dataSource.getConnection();

            SolicitacaoMaterial solicitacaoMaterial = solicitacaoMaterialService.findById(solicitacaoMaterialId);
            if (solicitacaoMaterial == null) {
                throw new ObjectNotFoundException("Requisição de material não encontrada com ID: " + solicitacaoMaterialId);
            }
            // Busca os Itens pelo SolicitacaoMaterialItemRepository
            List<SolicitacaoMaterialItens> itens = solicitacaoMaterialItensRepository.findBySolicitacaoMaterialId(solicitacaoMaterialId);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_CODIGO", solicitacaoMaterialId);
            parameters.put("P_CODIGOID", solicitacaoMaterialId);
            parameters.put("SUBREPORT_DIR", "reports/");

            JasperReport jasperReport = loadReport("requisicao_material_template.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new ObjectNotFoundException("Erro ao gerar Pdf: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close();
                } catch (SQLException e) {
                    logger.error("Erro ao gerar PDF de requisição de material: " +e);
                }
            }
        }
    }

    private String converterSituacaoParaTexto(Situacao situacao) {
        if (situacao == null) return "DESCONHECIDA";
        return situacao.getDescricao();
    }

    private JasperReport loadReport(String reportName) {
        try {
            Resource resource = new ClassPathResource("reports/" + reportName);

            if (!resource.exists()) {
                throw new RuntimeException("Relatório não encontrado: reports/" + reportName);
            }

            try (InputStream stream = resource.getInputStream()) {
                return (JasperReport) JRLoader.loadObject(stream);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar relatório: " + reportName, e);
        }
    }
}