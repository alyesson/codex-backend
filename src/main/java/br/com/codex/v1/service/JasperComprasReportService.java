package br.com.codex.v1.service;

import br.com.codex.v1.configuration.StartupInitializerDev;
import br.com.codex.v1.domain.compras.CotacaoCompra;
import br.com.codex.v1.domain.compras.CotacaoItensCompra;
import br.com.codex.v1.domain.compras.OrdemCompra;
import br.com.codex.v1.domain.compras.OrdemItensCompra;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.CotacaoItensCompraRepository;
import br.com.codex.v1.domain.repository.OrdemItensCompraRepository;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
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
    private OrdemCompraService ordemCompraService;

    @Autowired
    private OrdemItensCompraRepository ordemItensCompraRepository;

    @Autowired
    private CotacaoCompraService cotacaoCompraService;

    @Autowired
    private CotacaoItensCompraRepository cotacaoItensCompraRepository;

    public byte[] generateOrdemCompraReport(Long ordemCompraId) throws Exception {
        Connection connection = null;
        try {

            connection = dataSource.getConnection();

            OrdemCompra ordemCompra = ordemCompraService.findById(ordemCompraId);
            if (ordemCompra == null) {
                throw new RuntimeException("Ordem de compra não encontrada com ID: " + ordemCompraId);
            }
            // Busca os Itens pelo OrdemCompraItemRepository
            List<OrdemItensCompra> itens = ordemItensCompraRepository.findByOrdemCompraId(ordemCompraId);

            Map<String, Object> parameters = new HashMap<>();

            // PARÂMETRO PARA O RELATÓRIO PRINCIPAL
            parameters.put("P_CODIGO", ordemCompraId);
            parameters.put("P_CODIGOID", ordemCompraId);

            parameters.put("id", ordemCompra.getId());
            parameters.put("solicitante", ordemCompra.getSolicitante());
            parameters.put("data_solicitacao", ordemCompra.getDataSolicitacao());
            parameters.put("departamento", ordemCompra.getDepartamento());
            parameters.put("destino_material", ordemCompra.getDestinoMaterial());
            parameters.put("item_estoque", ordemCompra.getItemEstoque());
            parameters.put("situacao", ordemCompra.getSituacao());
            parameters.put("motivo_compra", ordemCompra.getMotivoCompra());
            parameters.put("opcao_marca", ordemCompra.getOpcaoMarca());
            parameters.put("urgente", ordemCompra.getUrgente());
            parameters.put("REPORT_CONNECTION", connection);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/main/resources/reports/ordem_compra_template.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar Pdf: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close();
                } catch (SQLException e) {
                    logger.error("Erro ao gerar PDF de ordem de compra: " +e);
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
            // Busca os Itens pelo OrdemCompraItemRepository
            List<CotacaoItensCompra> itens = cotacaoItensCompraRepository.findByCotacaoCompraId(cotacaoCompraId);

            Map<String, Object> parameters = new HashMap<>();

            // PARÂMETRO PARA O RELATÓRIO PRINCIPAL
            parameters.put("P_CODIGO", cotacaoCompraId);
            parameters.put("P_CODIGOID", cotacaoCompraId);

            parameters.put("id", cotacaoCompra.getId());
            parameters.put("cnpj", cotacaoCompra.getCnpj());
            parameters.put("comprador", cotacaoCompra.getComprador());
            parameters.put("condicoes_pagamento", cotacaoCompra.getCondicoesPagamento());
            parameters.put("contato", cotacaoCompra.getContato());
            parameters.put("data_abertura", cotacaoCompra.getDataAbertura());
            parameters.put("endereco", cotacaoCompra.getEndereco());
            parameters.put("fornecedor", cotacaoCompra.getFornecedor());
            parameters.put("ie", cotacaoCompra.getIe());
            parameters.put("link_compra", cotacaoCompra.getLinkCompra());
            parameters.put("numero_ordem", cotacaoCompra.getNumeroOrdem());
            parameters.put("observacao", cotacaoCompra.getObservacao());
            parameters.put("prazo_entrega", cotacaoCompra.getPrazoEntrega());
            parameters.put("situacao", cotacaoCompra.getSituacao());
            parameters.put("solicitante", cotacaoCompra.getSolicitante());
            parameters.put("validade", cotacaoCompra.getValidade());
            parameters.put("valor_cotado", cotacaoCompra.getValorCotado());
            parameters.put("REPORT_CONNECTION", connection);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/main/resources/reports/cotacao_compra_template.jasper"));
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

    private String converterSituacaoParaTexto(Situacao situacao) {
        if (situacao == null) return "DESCONHECIDA";
        return situacao.getDescricao();
    }
}