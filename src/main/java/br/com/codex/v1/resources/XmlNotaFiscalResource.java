package br.com.codex.v1.resources;

import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import br.com.codex.v1.service.XmlNotaFiscalService;
import br.com.swconsultoria.impressao.model.Impressao;
import br.com.swconsultoria.impressao.service.ImpressaoService;
import br.com.swconsultoria.impressao.util.ImpressaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("v1/api/xml_nota_fiscal")
public class XmlNotaFiscalResource {
    private static final Logger logger = LoggerFactory.getLogger(XmlNotaFiscalResource.class);

    @Autowired
    private XmlNotaFiscalService xmlNotaFiscalService;

    @GetMapping("/download/{chave}")
    public ResponseEntity<byte[]> downloadXml(@PathVariable String chave) {
        XmlNotaFiscal xmlNota = xmlNotaFiscalService.findByChaveAcesso(chave);

        if (xmlNota == null || xmlNota.getXmlContent() == null) {
            return ResponseEntity.notFound().build();
        }

        // Configura headers para download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=NFe_" + chave + ".xml")
                .body(xmlNota.getXmlContent().getBytes(StandardCharsets.UTF_8));
    }

    @PostMapping("/gerar-danfe")
    public ResponseEntity<byte[]> gerarDanfe(@RequestBody String chave) {
        try {
            // 1. Busca o XML no banco de dados pela chave
            XmlNotaFiscal xmlNota = xmlNotaFiscalService.findByChaveAcesso(chave);

            // 2. Verifica se o XML é válido
            if (xmlNota.getXmlContent() == null || xmlNota.getXmlContent().isEmpty()) {
                return ResponseEntity.badRequest().body("XML da nota fiscal está vazio".getBytes());
            }

            // 3. Remove possíveis BOM (Byte Order Mark) ou espaços no início
            String xmlSanitizado = xmlNota.getXmlContent().replaceAll("^\\s*", "");

            // 4. Valida se começa com <?xml
            if (!xmlSanitizado.startsWith("<?xml")) {
                throw new RuntimeException("Conteúdo XML inválido");
            }

            // 5. Cria e gera a DANFE
            Impressao impressao = ImpressaoUtil.impressaoPadraoNFe(xmlSanitizado);
            byte[] pdf = ImpressaoService.impressaoPdfByte(impressao);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename("danfe-"+chave+".pdf")
                    .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Erro ao gerar DANFE para chave: " + chave, e);
            return ResponseEntity.internalServerError()
                    .body(("Erro ao gerar DANFE: " + e.getMessage()).getBytes());
        }
    }
}
