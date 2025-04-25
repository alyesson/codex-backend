package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.CotacaoCompra;
import br.com.codex.v1.domain.compras.CotacaoItensCompra;
import br.com.codex.v1.domain.dto.CotacaoCompraDto;
import br.com.codex.v1.domain.dto.CotacaoItensCompraDto;
import br.com.codex.v1.service.CotacaoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/cotacao_compra")
public class CotacaoCompraResource {

    @Autowired
    private CotacaoCompraService cotacaoCompraService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_ADMINISTRATIVO', 'ADMINISTRATIVO', 'GERENTE_TI', 'TI')")
    @PostMapping
    public ResponseEntity<CotacaoCompraDto> create(@Valid @RequestBody CotacaoCompraDto cotacaoCompraDto) {
        CotacaoCompra obj = cotacaoCompraService.create(cotacaoCompraDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CotacaoCompraDto> update(@PathVariable Integer id, @RequestParam String situacao){
        cotacaoCompraService.update(id, situacao);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<CotacaoItensCompraDto>> findAllItens(@PathVariable Integer id) {
        List<CotacaoItensCompra> itens = cotacaoCompraService.findAllItensByCotacaoId(id);
        List<CotacaoItensCompraDto> listDto = itens.stream().map(CotacaoItensCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @GetMapping
    public ResponseEntity <List<CotacaoCompraDto>> findAll(){
        List<CotacaoCompra> objCotacao = cotacaoCompraService.findAll();
        List<CotacaoCompraDto> listDto = objCotacao.stream().map(CotacaoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @GetMapping("/{id}")
    public ResponseEntity <CotacaoCompraDto> findById(@PathVariable Integer id){
        CotacaoCompra objCotacao = cotacaoCompraService.findById(id);
        return ResponseEntity.ok().body(new CotacaoCompraDto(objCotacao));
    }

    //ASolicitações de Compra Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI', 'GERENTE_ADMINISTRATIVO', 'ADMINISTRATIVO')")
    @GetMapping(value = "/cotacoes_periodo")
    public ResponseEntity<List<CotacaoCompraDto>> findAllCotacaoPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<CotacaoCompra> list = cotacaoCompraService.findAllCotacoesPeriodo(dataInicial, dataFinal);
        List<CotacaoCompraDto> listDto = list.stream().map(CotacaoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
