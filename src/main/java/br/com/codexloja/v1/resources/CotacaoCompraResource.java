package br.com.codexloja.v1.resources;

import br.com.codexloja.v1.domain.compras.CotacaoCompra;
import br.com.codexloja.v1.domain.compras.CotacaoItensCompra;
import br.com.codexloja.v1.domain.dto.CotacaoCompraDto;
import br.com.codexloja.v1.domain.dto.CotacaoItensCompraDto;
import br.com.codexloja.v1.service.CotacaoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/cotacao_compra")
public class CotacaoCompraResource {

    @Autowired
    private CotacaoCompraService cotacaoCompraService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRAS')")
    @PostMapping
    public ResponseEntity<CotacaoCompraDto> create(@Valid @RequestBody CotacaoCompraDto cotacaoCompraDto) {
        CotacaoCompra obj = cotacaoCompraService.create(cotacaoCompraDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRAS')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CotacaoCompraDto> update(@PathVariable Integer id, @RequestParam String situacao){
        cotacaoCompraService.update(id, situacao);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRAS')")
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<CotacaoItensCompraDto>> findAllItens(@PathVariable Integer id) {
        List<CotacaoItensCompra> itens = cotacaoCompraService.findAllItensByCotacaoId(id);
        List<CotacaoItensCompraDto> listDto = itens.stream().map(CotacaoItensCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRAS')")
    @GetMapping
    public ResponseEntity <List<CotacaoCompraDto>> findAll(){
        List<CotacaoCompra> objCotacao = cotacaoCompraService.findAll();
        List<CotacaoCompraDto> listDto = objCotacao.stream().map(CotacaoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRAS')")
    @GetMapping("/{id}")
    public ResponseEntity <CotacaoCompraDto> findById(@PathVariable Integer id){
        CotacaoCompra objCotacao = cotacaoCompraService.findById(id);
        return ResponseEntity.ok().body(new CotacaoCompraDto(objCotacao));
    }
}
