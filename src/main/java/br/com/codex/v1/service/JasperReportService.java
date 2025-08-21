package br.com.codex.v1.service;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.vendas.Venda;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service
public class JasperReportService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private VendaService vendaService;

    public byte[] generateVendaReport(Long vendaId) throws Exception {
        Connection connection = null;
        try {
            // Busca a venda completa pelo ID
            Venda venda = vendaService.findById(vendaId);
            if (venda == null) {
                throw new RuntimeException("Venda não encontrada com ID: " + vendaId);
            }

            connection = dataSource.getConnection();

            // Prepara os parâmetros conforme esperado pelo relatório
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("codigo", venda.getCodigo());
            parameters.put("data_emissao", venda.getDataEmissao());
            parameters.put("vendedor", venda.getVendedor());
            parameters.put("cliente", venda.getConsumidor());
            parameters.put("documento", venda.getDocumentoConsumidor());
            parameters.put("valor_total", venda.getValorFinal());
            parameters.put("situacao", Situacao.valueOf(String.valueOf(venda.getSituacao())));
            parameters.put("observacao", venda.getObservacoes());
            parameters.put("tipo_venda", venda.getTipoVenda());
            parameters.put("validade", venda.getDataValidade());
            parameters.put("desconto", venda.getDescontoTotal());
            parameters.put("frete", venda.getValorFrete());

            // DataSource para os itens da venda
            JRBeanCollectionDataSource itensDataSource = new JRBeanCollectionDataSource(venda.getItens());
            parameters.put("ITENS_DATA_SOURCE", itensDataSource);

            // Caminho para o subrelatório
            parameters.put("SUBREPORT_DIR", "src/main/resources/reports/");

            // Compila e preenche o relatório
            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/reports/venda_template.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Exporta para PDF
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { /* log error */ }
            }
        }
    }

    private Integer converterSituacaoParaNumero(Situacao situacao) {
        if (situacao == null) return 0;
        switch (situacao) {
            case ABERTO: return 1;
            case FECHADO: return 2;
            case APROVADO: return 3;
            case REJEITADO: return 4;
            case PENDENTE: return 5;
            case DEVOLUCAO: return 6;
            default: return 0;
        }
    }
}