package br.com.codex.v1.service;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.VendaItensRepository;
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
            parameters.put("situacao", venda.getSituacao());
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
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { /* log error */ }
            }
        }
    }
}