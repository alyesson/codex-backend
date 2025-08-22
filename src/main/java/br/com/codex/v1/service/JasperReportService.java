package br.com.codex.v1.service;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.OrcamentoItensRepository;
import br.com.codex.v1.domain.repository.VendaItensRepository;
import br.com.codex.v1.domain.vendas.Orcamento;
import br.com.codex.v1.domain.vendas.OrcamentoItens;
import br.com.codex.v1.domain.vendas.Venda;
import br.com.codex.v1.domain.vendas.VendaItens;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service
public class JasperReportService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private VendaService vendaService;

    @Autowired
    private VendaItensRepository vendaItensRepository;

    @Autowired
    private OrcamentoService orcamentoService;

    @Autowired
    private OrcamentoItensRepository orcamentoItensRepository;

    public byte[] generateVendaReport(Long vendaId) throws Exception {
        Connection connection = null;
        try {

            connection = dataSource.getConnection();

            Venda venda = vendaService.findById(vendaId);
            if (venda == null) {
                throw new RuntimeException("Venda não encontrada com ID: " + vendaId);
            }
            // Busca os Itens pelo VendaItemRepository
            List<VendaItens> itens = vendaItensRepository.findByVendaId(vendaId);

            Map<String, Object> parameters = new HashMap<>();

            // PARÂMETRO PARA O RELATÓRIO PRINCIPAL
            parameters.put("P_CODIGO", vendaId);
            parameters.put("P_CODIGOID", vendaId);

            parameters.put("codigo", venda.getCodigo());
            parameters.put("data_emissao", venda.getDataEmissao());
            parameters.put("vendedor", venda.getVendedor());
            parameters.put("cliente", venda.getConsumidor());
            parameters.put("documento", venda.getDocumentoConsumidor());
            parameters.put("valor_total", venda.getValorFinal());
            parameters.put("situacao", venda.getSituacao().getDescricao());
            parameters.put("observacao", venda.getObservacoes());
            parameters.put("tipo_venda", venda.getTipoVenda());
            parameters.put("validade", venda.getDataValidade());
            parameters.put("desconto", venda.getDescontoTotal());
            parameters.put("frete", venda.getValorFrete());
            parameters.put("REPORT_CONNECTION", connection);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/main/resources/reports/venda_template.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar Pdf: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { /* log error */ }
            }
        }
    }

    public byte[] generateOrcamentoReport(Long orcamentoId) throws Exception {
        Connection connection = null;
        try {

            connection = dataSource.getConnection();

            Orcamento orcamento = orcamentoService.findById(orcamentoId);
            if (orcamento == null) {
                throw new RuntimeException("orçamento não encontrado com ID: " + orcamentoId);
            }
            // Busca os Itens pelo VendaItemRepository
            List<OrcamentoItens> itens = orcamentoItensRepository.findByOrcamentoId(orcamentoId);

            Map<String, Object> parameters = new HashMap<>();

            // PARÂMETRO PARA O RELATÓRIO PRINCIPAL
            parameters.put("P_CODIGO", orcamentoId);
            parameters.put("P_CODIGOID", orcamentoId);

            parameters.put("codigo", orcamento.getCodigo());
            parameters.put("data_emissao", orcamento.getDataEmissao());
            parameters.put("vendedor", orcamento.getVendedor());
            parameters.put("cliente", orcamento.getConsumidor());
            parameters.put("documento", orcamento.getDocumentoConsumidor());
            parameters.put("valor_total", orcamento.getValorFinal());
            parameters.put("situacao", converterSituacaoParaTexto(orcamento.getSituacao()));
            parameters.put("observacao", orcamento.getObservacoes());
            parameters.put("tipo_venda", orcamento.getTipoOrcamento());
            parameters.put("validade", orcamento.getDataValidade());
            parameters.put("desconto", orcamento.getDescontoTotal());
            parameters.put("frete", orcamento.getValorFrete());
            parameters.put("REPORT_CONNECTION", connection);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/main/resources/reports/orcamento_template.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar Pdf: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { /* log error */ }
            }
        }
    }

    private String converterSituacaoParaTexto(Situacao situacao) {
        if (situacao == null) return "DESCONHECIDA";
        return situacao.getDescricao();
    }
}