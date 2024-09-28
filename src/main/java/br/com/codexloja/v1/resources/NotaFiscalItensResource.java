package br.com.codexloja.v1.resources;

import br.com.codexloja.v1.domain.estoque.NotaFiscalItens;
import br.com.codexloja.v1.domain.dto.NotaFiscalItensDto;
import br.com.codexloja.v1.service.NotaFiscalItensService;
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
public class NotaFiscalItensResource {

    @Autowired
    private NotaFiscalItensService notaFiscalItensService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/id_nota")
    public ResponseEntity<List<NotaFiscalItensDto>> findByNumeroNotaFiscal(@RequestParam(value = "numeroNotaFiscal") String numeroNotaFiscal){
        List<NotaFiscalItens> list = notaFiscalItensService.findByNumero(numeroNotaFiscal);
        List<NotaFiscalItensDto> objList = list.stream().map(NotaFiscalItensDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(objList);
    }
}
