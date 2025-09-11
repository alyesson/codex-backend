package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.*;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.CotacaoItensCompraRepository;
import br.com.codex.v1.domain.repository.PedidoItensCompraRepository;
import br.com.codex.v1.domain.repository.SolicitacaoItensCompraRepository;
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
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperComprasReportService {
    private static final Logger logger = LoggerFactory.getLogger(JasperComprasReportService.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SolicitacaoCompraService solicitacaoCompraService;

    @Autowired
    private SolicitacaoItensCompraRepository solicitacaoItensCompraRepository;

    @Autowired
    private CotacaoCompraService cotacaoCompraService;

    @Autowired
    private CotacaoItensCompraRepository cotacaoItensCompraRepository;

    @Autowired
    private PedidoCompraService pedidoCompraService;

    @Autowired
    private PedidoItensCompraRepository pedidoItensCompraRepository;

    public byte[] generateSolicitacaoCompraReport(Long solicitacaoCompraId) throws Exception {
        Connection connection = null;
        try {

            connection = dataSource.getConnection();

            SolicitacaoCompra solicitacaoCompra = solicitacaoCompraService.findById(solicitacaoCompraId);
            if (solicitacaoCompra == null) {
                throw new ObjectNotFoundException("Solicitacao de compra não encontrada com ID: " + solicitacaoCompraId);
            }
            // Busca os Itens pelo SolicitacaoCompraItemRepository
            List<SolicitacaoItensCompra> itens = solicitacaoItensCompraRepository.findBySolicitacaoCompraId(solicitacaoCompraId);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_CODIGO", solicitacaoCompraId);
            parameters.put("P_CODIGOID", solicitacaoCompraId);
            parameters.put("SUBREPORT_DIR", "reports/");

            JasperReport jasperReport = loadReport("solicitacao_compra_template.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new ObjectNotFoundException("Erro ao gerar Pdf: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close();
                } catch (SQLException e) {
                    logger.error("Erro ao gerar PDF de solicitacao de compra: " +e);
                }
            }
        }
    }

    public byte[] generateCotacaoCompraReport(Long cotacaoCompraId) throws Exception {
        Connection connection = null;
        try {

            connection = dataSource.getConnection();

            CotacaoCompra cotacaoCompra = cotacaoCompraService.findById(cotacaoCompraId);
            if (cotacaoCompra == null) {
                throw new RuntimeException("Cotação não encontrada com ID: " + cotacaoCompraId);
            }
            // Busca os Itens pelo SolicitacaoCompraItemRepository
            List<CotacaoItensCompra> itens = cotacaoItensCompraRepository.findByCotacaoCompraId(cotacaoCompraId);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_CODIGO", cotacaoCompraId);
            parameters.put("P_CODIGOID", cotacaoCompraId);
            parameters.put("SUBREPORT_DIR", "reports/");

            JasperReport jasperReport = loadReport("cotacao_compra_template.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar Pdf: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close();
                } catch (SQLException e) {
                    logger.error("Erro ao gerar PDF de cotação de compra: " +e);
                }
            }
        }
    }

    public byte[] generatePedidoCompraReport(Long pedidoCompraId) throws Exception {
        Connection connection = null;
        try {

            connection = dataSource.getConnection();

            PedidoCompra pedidoCompra = pedidoCompraService.findById(pedidoCompraId);
            if (pedidoCompra == null) {
                throw new RuntimeException("Pedido não encontrada com ID: " + pedidoCompraId);
            }
            // Busca os Itens pelo SolicitacaoCompraItemRepository
            List<PedidoItensCompra> itens = pedidoItensCompraRepository.findByPedidoCompraId(pedidoCompraId);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_CODIGO", pedidoCompraId);
            parameters.put("P_CODIGOID", pedidoCompraId);

            parameters.put("SUBREPORT_DIR", "reports/");

            JasperReport jasperReport = loadReport("pedido_compra_template.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar Pdf: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close();
                } catch (SQLException e) {
                    logger.error("Erro ao gerar PDF de pedido de compra: " +e);
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