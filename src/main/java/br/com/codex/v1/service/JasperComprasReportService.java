package br.com.codex.v1.service;

import br.com.codex.v1.configuration.StartupInitializerDev;
import br.com.codex.v1.domain.compras.*;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.CotacaoItensCompraRepository;
import br.com.codex.v1.domain.repository.PedidoItensCompraRepository;
import br.com.codex.v1.domain.repository.SolicitacaoItensCompraRepository;
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
                throw new RuntimeException("Solicitacao de compra não encontrada com ID: " + solicitacaoCompraId);
            }
            // Busca os Itens pelo SolicitacaoCompraItemRepository
            List<SolicitacaoItensCompra> itens = solicitacaoItensCompraRepository.findBySolicitacaoCompraId(solicitacaoCompraId);

            Map<String, Object> parameters = new HashMap<>();

            // PARÂMETRO PARA O RELATÓRIO PRINCIPAL
            parameters.put("P_CODIGO", solicitacaoCompraId);
            parameters.put("P_CODIGOID", solicitacaoCompraId);

            parameters.put("id", solicitacaoCompra.getId());
            parameters.put("solicitante", solicitacaoCompra.getSolicitante());
            parameters.put("data_solicitacao", solicitacaoCompra.getDataSolicitacao());
            parameters.put("departamento", solicitacaoCompra.getDepartamento());
            parameters.put("destino_material", solicitacaoCompra.getDestinoMaterial());
            parameters.put("item_estoque", solicitacaoCompra.getItemEstoque());
            parameters.put("situacao", solicitacaoCompra.getSituacao());
            parameters.put("motivo_compra", solicitacaoCompra.getMotivoCompra());
            parameters.put("opcao_marca", solicitacaoCompra.getOpcaoMarca());
            parameters.put("urgente", solicitacaoCompra.getUrgente());
            parameters.put("REPORT_CONNECTION", connection);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/main/resources/reports/solicitacao_compra_template.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar Pdf: " + e.getMessage(), e);
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
            parameters.put("numero_solicitacao", cotacaoCompra.getNumeroSolicitacao());
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

            // PARÂMETRO PARA O RELATÓRIO PRINCIPAL
            parameters.put("P_CODIGO", pedidoCompraId);
            parameters.put("P_CODIGOID", pedidoCompraId);

            parameters.put("id", pedidoCompra.getId());
            parameters.put("aprovador", pedidoCompra.getAprovador());
            parameters.put("centro_custo", pedidoCompra.getCentroCusto());
            parameters.put("cnpj", pedidoCompra.getCnpj());
            parameters.put("comprador", pedidoCompra.getComprador());
            parameters.put("condicoes_pagamento", pedidoCompra.getCondicoesPagamento());
            parameters.put("contato", pedidoCompra.getContato());
            parameters.put("data_aprovacao", pedidoCompra.getDataAprovacao());
            parameters.put("data_entrega_prevista", pedidoCompra.getDataEntregaPrevista());
            parameters.put("data_entrega_real", pedidoCompra.getDataEntregaReal());
            parameters.put("data_pedido", pedidoCompra.getDataPedido());
            parameters.put("departamento", pedidoCompra.getDepartamento());
            parameters.put("forma_pagamento", pedidoCompra.getFormaPagamento());
            parameters.put("endereco", pedidoCompra.getEndereco());
            parameters.put("fornecedor", pedidoCompra.getFornecedor());
            parameters.put("ie", pedidoCompra.getIe());
            parameters.put("justificativa", pedidoCompra.getJustificativa());
            parameters.put("link_compra", pedidoCompra.getLinkCompra());
            parameters.put("numero_cotacao", pedidoCompra.getNumeroCotacao());
            parameters.put("numero_parcelas", pedidoCompra.getNumeroParcelas());
            parameters.put("numero_requisicao", pedidoCompra.getNumeroRequisicao());
            parameters.put("observacao", pedidoCompra.getObservacao());
            parameters.put("situacao", converterSituacaoParaTexto(pedidoCompra.getSituacao()));
            parameters.put("solicitante", pedidoCompra.getSolicitante());
            parameters.put("validade", pedidoCompra.getValidade());
            parameters.put("valor_pedido", pedidoCompra.getValorPedido());
            parameters.put("valor_frete", pedidoCompra.getValorFrete());
            parameters.put("valor_desconto", pedidoCompra.getValorDesconto());
            parameters.put("valor_total", pedidoCompra.getValorTotal());
            parameters.put("REPORT_CONNECTION", connection);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/main/resources/reports/pedido_compra_template.jasper"));
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
}