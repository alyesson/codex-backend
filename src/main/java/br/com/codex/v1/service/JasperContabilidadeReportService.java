package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.DREDto;
import br.com.codex.v1.domain.dto.GrupoContabilDto;
import br.com.codex.v1.domain.dto.ItemContabilDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

@Service
public class JasperContabilidadeReportService {

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

    private void adicionarSecao(Document document, String titulo, List<GrupoContabilDto> grupos)
            throws DocumentException {

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
