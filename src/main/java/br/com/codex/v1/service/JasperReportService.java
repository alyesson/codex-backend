package br.com.codex.v1.service;

import br.com.codex.v1.domain.vendas.Venda;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JasperReportService {

    @Value("${jasper.reports.path}")
    private String jasperReportsPath;

    public byte[] generateVendaReport(Venda venda) throws Exception {
        // Carregar o template JRXML
        String reportPath = jasperReportsPath + "/venda_template.jrxml";
        File template = new File(reportPath);

        if (!template.exists()) {
            throw new FileNotFoundException("Template Jasper não encontrado: " + reportPath);
        }

        // Compilar o relatório
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

        // Preparar parâmetros
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CODIGO_VENDA", venda.getCodigo());
        parameters.put("DATA_EMISSAO", venda.getDataEmissao());
        parameters.put("VENDEDOR", venda.getVendedor());
        parameters.put("CLIENTE", venda.getConsumidor());
        parameters.put("DOCUMENTO", venda.getDocumentoConsumidor());
        parameters.put("VALOR_TOTAL", venda.getValorFinal());
        parameters.put("SITUACAO", venda.getSituacao().toString());
        parameters.put("DATA_EMISSAO_RELATORIO", new Date());

        // Criar datasource com os itens
        JRBeanCollectionDataSource itemsDataSource = new JRBeanCollectionDataSource(
                venda.getItens().stream().map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("codigo", item.getCodigo());
                    itemMap.put("descricao", item.getDescricao());
                    itemMap.put("quantidade", item.getQuantidade());
                    itemMap.put("valorUnitario", item.getValorUnitario());
                    itemMap.put("valorTotal", item.getValorTotal());
                    itemMap.put("unidade", "UN");
                    return itemMap;
                }).collect(Collectors.toList())
        );

        parameters.put("ITENS_DATA_SOURCE", itemsDataSource);

        // Gerar relatório
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, parameters, new JREmptyDataSource()
        );

        // Exportar para PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}