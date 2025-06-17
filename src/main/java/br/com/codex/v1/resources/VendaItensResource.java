package br.com.codex.v1.resources;

import br.com.codex.v1.domain.vendas.VendaItens;
import br.com.codex.v1.domain.dto.VendaItensDto;
import br.com.codex.v1.service.VendaItensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/venda_itens")
public class VendaItensResource {

    @Autowired
    private VendaItensService vendaItensService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @PostMapping("/lista_itens")
    public ResponseEntity<List<VendaItensDto>> createList(@RequestBody List<VendaItensDto> vendaItensDtoList) {
        List<VendaItens> vendaItensList = vendaItensService.create(vendaItensDtoList);
        List<VendaItensDto> vendaItensDtoSavedList = vendaItensList.stream()
                .map(VendaItensDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(vendaItensDtoSavedList);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<VendaItensDto> findById(@PathVariable Long id){
        VendaItens objItens = vendaItensService.findById(id);
        return ResponseEntity.ok().body(new VendaItensDto(objItens));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @GetMapping("/all_itens")
    public ResponseEntity<List<VendaItensDto>> findAllVendaItens() {
        List<VendaItens> vendaItens = vendaItensService.findAll();
        List<VendaItensDto> vendaItensDtoList = vendaItens.stream()
                .map(VendaItensDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(vendaItensDtoList);
    }
}
