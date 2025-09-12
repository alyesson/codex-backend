package br.com.codex.v1.resources;

import br.com.codex.v1.configuration.StartupInitializerDev;
import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.dto.BalanceteDto;
import br.com.codex.v1.domain.dto.BalancoPatrimonialDto;
import br.com.codex.v1.domain.dto.DFCDto;
import br.com.codex.v1.domain.dto.DREDto;
import br.com.codex.v1.domain.repository.EmpresaRepository;
import br.com.codex.v1.resources.exceptions.ResourceExceptionHandler;
import br.com.codex.v1.resources.exceptions.StandardError;
import br.com.codex.v1.resources.exceptions.ValidationError;
import br.com.codex.v1.service.JasperComprasReportService;
import br.com.codex.v1.service.JasperContabilidadeReportService;
import br.com.codex.v1.service.JasperVendasReportService;
import br.com.codex.v1.service.LancamentoContabilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@RequestMapping("v1/api/relatorios")
public class RelatorioResource {
    private static final Logger logger = LoggerFactory.getLogger(RelatorioResource.class);

    @Autowired
    private JasperVendasReportService jasperVendasReportService;

    @Autowired
    JasperComprasReportService jasperComprasReportService;

    @Autowired
    JasperContabilidadeReportService jasperContabilidadeReportService;

    @Autowired
    private LancamentoContabilService lancamentoContabilService;

    @Autowired
    private EmpresaRepository empresaRepository;

    @GetMapping("/balanco-patrimonial")
    public ResponseEntity<BalancoPatrimonialDto> getBalancoPatrimonial(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        BalancoPatrimonialDto balanco = lancamentoContabilService.gerarBalancoPatrimonial(dataInicial, dataFinal);
        return ResponseEntity.ok().body(balanco);
    }

    @GetMapping("/dre")
    public ResponseEntity<DREDto> getDRE(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam("empresaId") Long empresaId) {

        DREDto dre = lancamentoContabilService.gerarDRE(dataInicial, dataFinal, empresaId);
        return ResponseEntity.ok().body(dre);
    }

    @GetMapping(value = "/dre/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getDREPdf(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam("empresaId") Long empresaId) {

        try {
            // 1. Gerar os dados da DRE
            DREDto dre = lancamentoContabilService.gerarDRE(dataInicial, dataFinal, empresaId);

            // 2. Buscar o nome da empresa
            String nomeEmpresa = empresaRepository.findById(empresaId)
                    .map(Empresa::getRazaoSocial)
                    .orElse("Empresa não encontrada");

            // 3. Setar o nome da empresa no DTO
            dre.setEmpresaNome(nomeEmpresa);

            // 4. Gerar o PDF
            byte[] pdf = jasperContabilidadeReportService.gerarPdfDRE(dre);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dre.pdf")
                    .body(pdf);

        } catch (Exception e) {
            logger.error("Erro ao gerar PDF da DRE", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dfc")
    public ResponseEntity<DFCDto> getDFC(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam("empresaId") Long empresaId) {

        DFCDto dfc = lancamentoContabilService.gerarDFC(dataInicial, dataFinal, empresaId);
        return ResponseEntity.ok().body(dfc);
    }

    @GetMapping(value = "/dfc/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getDFCPdf(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam("empresaId") Long empresaId) throws Exception {

        DFCDto dfc = lancamentoContabilService.gerarDFC(dataInicial, dataFinal, empresaId);
        byte[] pdf = jasperContabilidadeReportService.gerarPdfDFC(dfc);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dfc.pdf")
                .body(pdf);
    }

    @GetMapping("/balancete")
    public ResponseEntity<BalanceteDto> getBalancete(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam("nivelDetalhe") String nivelDetalhe,
            @RequestParam(value = "empresaId", required = false) Long empresaId) {

        BalanceteDto balancete = lancamentoContabilService.gerarBalancete(dataInicial, dataFinal, nivelDetalhe, empresaId);
        return ResponseEntity.ok().body(balancete);
    }

    @GetMapping(value = "/balancete/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getBalancetePdf(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            @RequestParam("nivelDetalhe") String nivelDetalhe,
            @RequestParam(value = "empresaId", required = false) Long empresaId) throws Exception {

        BalanceteDto balancete = lancamentoContabilService.gerarBalancete(dataInicial, dataFinal, nivelDetalhe, empresaId);
        byte[] pdf = jasperContabilidadeReportService.gerarPdfBalancete(balancete);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=balancete.pdf")
                .body(pdf);
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
            headers.setContentDispositionFormData("filename", "cotacao_compra_" + id + ".pdf");
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
