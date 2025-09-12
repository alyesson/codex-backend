package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.*;
import br.com.codex.v1.domain.repository.EmpresaRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperContabilidadeReportService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public byte[] gerarPdfDRE(DREDto dre) {
        try {
            Document document = new Document(PageSize.A4.rotate()); // PDF horizontal
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Adicionar título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("DEMONSTRAÇÃO DO RESULTADO DO EXERCÍCIO (DRE)", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Adicionar receitas
            adicionarSecao(document, "RECEITAS", dre.getReceitas());

            // Adicionar custos
            adicionarSecao(document, "CUSTOS", dre.getCustos());

            // Adicionar despesas
            adicionarSecao(document, "DESPESAS", dre.getDespesas());

            // Adicionar totais
            adicionarTotais(document, dre);

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do DRE", e);
        }
    }

    public byte[] gerarPdfDFC(DFCDto dfc) throws Exception {
        // Carregar o relatório Jasper
        InputStream jasperStream = this.getClass().getResourceAsStream("/reports/dfc.jasper");

        if (jasperStream == null) {
            throw new RuntimeException("Relatório DFC não encontrado");
        }

        // Preparar parâmetros
        Map<String, Object> parametros = new HashMap<>();

        // Adicionar dados do DFC como parâmetros
        parametros.put("FLUXO_OPERACIONAL", new JRBeanCollectionDataSource(dfc.getFluxoOperacional()));
        parametros.put("FLUXO_INVESTIMENTO", new JRBeanCollectionDataSource(dfc.getFluxoInvestimento()));
        parametros.put("FLUXO_FINANCIAMENTO", new JRBeanCollectionDataSource(dfc.getFluxoFinanciamento()));

        parametros.put("TOTAL_OPERACIONAL", dfc.getTotalOperacional());
        parametros.put("TOTAL_INVESTIMENTO", dfc.getTotalInvestimento());
        parametros.put("TOTAL_FINANCIAMENTO", dfc.getTotalFinanciamento());

        parametros.put("SALDO_INICIAL", dfc.getSaldoInicial());
        parametros.put("VARIACAO_PERIODO", dfc.getVariacaoPeriodo());
        parametros.put("SALDO_FINAL", dfc.getSaldoFinal());

        // Data atual para o relatório
        parametros.put("DATA_EMISSAO", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        // Compilar e preencher o relatório
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());

        // Exportar para PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] gerarPdfBalancete(BalanceteDto balancete) throws Exception {
        InputStream jasperStream = this.getClass().getResourceAsStream("/reports/balancete.jasper");

        if (jasperStream == null) {
            throw new RuntimeException("Relatório Balancete não encontrado");
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("CONTAS", new JRBeanCollectionDataSource(balancete.getContas()));
        parametros.put("TOTAL_DEBITO", balancete.getResumo().getTotalDebito());
        parametros.put("TOTAL_CREDITO", balancete.getResumo().getTotalCredito());
        parametros.put("SALDO_FINAL", balancete.getResumo().getSaldoFinal());
        parametros.put("DATA_INICIAL", balancete.getDataInicial().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        parametros.put("DATA_FINAL", balancete.getDataFinal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        parametros.put("NIVEL_DETALHE", "1".equals(balancete.getNivelDetalhe()) ? "Sintético" : "Analítico");
        parametros.put("DATA_EMISSAO", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(balancete.getContas());
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        // AQUI VOCÊ USA O DATA SOURCE EM VEZ DO JREmptyDataSource ↓
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        // Exportar para PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private void adicionarSecao(Document document, String titulo, List<GrupoContabilDto> grupos) throws DocumentException {

        if (grupos == null || grupos.isEmpty()) return;

        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph sectionTitle = new Paragraph(titulo, sectionFont);
        sectionTitle.setSpacingBefore(15);
        sectionTitle.setSpacingAfter(10);
        document.add(sectionTitle);

        for (GrupoContabilDto grupo : grupos) {
            // Tabela para o grupo
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(5);
            table.setSpacingAfter(5);

            // Cabeçalho do grupo
            PdfPCell groupHeaderCell = new PdfPCell(new Phrase(grupo.getNome(),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            groupHeaderCell.setColspan(2);
            groupHeaderCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            groupHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(groupHeaderCell);

            // Itens do grupo
            for (ItemContabilDto item : grupo.getItens()) {
                table.addCell(new Phrase(item.getNome(),
                        FontFactory.getFont(FontFactory.HELVETICA, 10)));
                table.addCell(new Phrase(formatarValor(item.getValor()),
                        FontFactory.getFont(FontFactory.HELVETICA, 10)));
            }

            // Total do grupo
            PdfPCell totalLabelCell = new PdfPCell(new Phrase("Total " + grupo.getNome(),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalLabelCell);

            PdfPCell totalValueCell = new PdfPCell(new Phrase(formatarValor(grupo.getTotal()),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalValueCell);

            document.add(table);
        }
    }

    private void adicionarTotais(Document document, DREDto dre) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);

        // Total Receitas
        adicionarLinhaTotal(table, "TOTAL RECEITAS", dre.getTotalReceitas());

        // Total Custos
        adicionarLinhaTotal(table, "TOTAL CUSTOS", dre.getTotalCustos());

        // Total Despesas
        adicionarLinhaTotal(table, "TOTAL DESPESAS", dre.getTotalDespesas());

        // Resultado Operacional
        adicionarLinhaTotal(table, "RESULTADO OPERACIONAL", dre.getResultadoOperacional());

        // Resultado Líquido
        adicionarLinhaTotal(table, "RESULTADO LÍQUIDO", dre.getResultadoLiquido());

        document.add(table);
    }

    private void adicionarLinhaTotal(PdfPTable table, String label, BigDecimal valor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(formatarValor(valor),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }

    private String formatarValor(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return String.format("R$ %,.2f", valor);
    }
}
