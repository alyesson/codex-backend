package br.com.codex.v1.domain.fiscal;

import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.dto.XmlNotaFiscalDto;
import br.com.codex.v1.service.XmlNotaFiscalService;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("v1/api/xml_nota_fiscal")
public class XmlNotaFiscalResource {

    @Autowired
    private XmlNotaFiscalService service;

    @GetMapping("/download/{chave}")
    public ResponseEntity<byte[]> downloadXml(@PathVariable String chave) {
        XmlNotaFiscal xmlNota = service.buscarPorChave(chave);

        if (xmlNota == null || xmlNota.getXmlContent() == null) {
            return ResponseEntity.notFound().build();
        }

        // Configura headers para download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/xml")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=NFe_" + chave + ".xml")
                .body(xmlNota.getXmlContent().getBytes(StandardCharsets.UTF_8));
    }
}
