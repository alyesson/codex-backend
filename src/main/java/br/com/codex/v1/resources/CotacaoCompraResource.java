package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.CotacaoCompra;
import br.com.codex.v1.domain.compras.CotacaoItensCompra;
import br.com.codex.v1.domain.dto.CotacaoCompraDto;
import br.com.codex.v1.domain.dto.CotacaoItensCompraDto;
import br.com.codex.v1.domain.dto.OrcamentoDto;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.vendas.Orcamento;
import br.com.codex.v1.service.CotacaoCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/cotacao_compra")
public class CotacaoCompraResource {

    @Autowired
    private CotacaoCompraService cotacaoCompraService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PostMapping
    public ResponseEntity<CotacaoCompraDto> create(@Valid @RequestBody CotacaoCompraDto cotacaoCompraDto) {
        CotacaoCompra obj = cotacaoCompraService.create(cotacaoCompraDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping(value = "/cotacoes_ano")
    public ResponseEntity <List<CotacaoCompraDto>> findAllByYear(){
        List<CotacaoCompra> objCotacao = cotacaoCompraService.findAllByYear();
        List<CotacaoCompraDto> listDto = objCotacao.stream().map(CotacaoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //ASolicitações de Compra Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping(value = "/cotacoes_periodo")
    public ResponseEntity<List<CotacaoCompraDto>> findAllCotacaoPeriodo(@RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                        @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {

        List<CotacaoCompra> list = cotacaoCompraService.findAllCotacoesPeriodo(dataInicial, dataFinal);
        List<CotacaoCompraDto> listDto = list.stream().map(CotacaoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CotacaoCompraDto> atualizarSituacao(@PathVariable Long id, @RequestParam String situacao){
        cotacaoCompraService.update(id, situacao);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<CotacaoItensCompraDto>> findAllItens(@PathVariable Long id) {
        List<CotacaoItensCompra> itens = cotacaoCompraService.findAllItensByCotacaoId(id);
        List<CotacaoItensCompraDto> listDto = itens.stream().map(CotacaoItensCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity <CotacaoCompraDto> findById(@PathVariable Long id){
        CotacaoCompra objCotacao = cotacaoCompraService.findById(id);
        return ResponseEntity.ok().body(new CotacaoCompraDto(objCotacao));
    }
}
