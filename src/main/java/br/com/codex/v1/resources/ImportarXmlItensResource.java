package br.com.codex.v1.resources;

import br.com.codex.v1.domain.contabilidade.ImportarXmlItens;
import br.com.codex.v1.domain.dto.NotaFiscalItensDto;
import br.com.codex.v1.service.ImportarXmlItensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/nota_fiscal_itens")
public class ImportarXmlItensResource {

    @Autowired
    private ImportarXmlItensService importarXmlItensService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/id_nota")
    public ResponseEntity<List<NotaFiscalItensDto>> findByNumeroNotaFiscal(@RequestParam(value = "numeroNotaFiscal") String numeroNotaFiscal){
        List<ImportarXmlItens> list = importarXmlItensService.findByNumero(numeroNotaFiscal);
        List<NotaFiscalItensDto> objList = list.stream().map(NotaFiscalItensDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(objList);
    }
}
