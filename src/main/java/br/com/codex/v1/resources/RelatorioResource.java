package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.BalancoPatrimonialDto;
import br.com.codex.v1.service.JasperComprasReportService;
import br.com.codex.v1.service.JasperVendasReportService;
import br.com.codex.v1.service.LancamentoContabilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.sql.Date;

@RestController
@RequestMapping("v1/api/relatorios")
public class RelatorioResource {

    @Autowired
    private JasperVendasReportService jasperVendasReportService;

    @Autowired
    JasperComprasReportService jasperComprasReportService;

    @Autowired
    private LancamentoContabilService lancamentoContabilService;

    @GetMapping("/balanco-patrimonial")
    public ResponseEntity<BalancoPatrimonialDto> getBalancoPatrimonial(
            @RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal) {

        BalancoPatrimonialDto balanco = lancamentoContabilService.gerarBalancoPatrimonial(dataInicial, dataFinal);
        return ResponseEntity.ok().body(balanco);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @GetMapping("/venda/{id}")
    public ResponseEntity<byte[]> gerarRelatorioVenda(@PathVariable Long id) {
        try {
            // Agora passa apenas o ID
            byte[] pdfBytes = jasperVendasReportService.generateVendaReport(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "venda_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar Pdf da venda: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @GetMapping("/orcamento/{id}")
    public ResponseEntity<byte[]> gerarRelatorioOrcamento(@PathVariable Long id) {
        try {
            // Agora passa apenas o ID
            byte[] pdfBytes = jasperVendasReportService.generateOrcamentoReport(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "venda_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar Pdf do orçamento: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping("/solicitacao_compra/{id}")
    public ResponseEntity<byte[]> gerarRelatorioSolicitacaoCompra(@PathVariable Long id) {
        try {
            // Agora passa apenas o ID
            byte[] pdfBytes = jasperComprasReportService.generateSolicitacaoCompraReport(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "solicitacao_compra_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar Pdf do solicitacao de compra: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping("/cotacao_compra/{id}")
    public ResponseEntity<byte[]> gerarRelatorioCotacaoCompra(@PathVariable Long id) {
        try {
            // Agora passa apenas o ID
            byte[] pdfBytes = jasperComprasReportService.generateCotacaoCompraReport(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "cotação_compra_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar Pdf da cotação de compra: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping("/pedido_compra/{id}")
    public ResponseEntity<byte[]> gerarRelatorioPedidoCompra(@PathVariable Long id) {
        try {
            // Agora passa apenas o ID
            byte[] pdfBytes = jasperComprasReportService.generatePedidoCompraReport(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "pedido_compra_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar Pdf do pedido de compra: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }
}
