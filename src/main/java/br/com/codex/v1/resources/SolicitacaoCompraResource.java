package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.SolicitacaoCompra;
import br.com.codex.v1.domain.compras.SolicitacaoItensCompra;
import br.com.codex.v1.domain.dto.SolicitacaoCompraDto;
import br.com.codex.v1.domain.dto.SolicitacaoItensCompraDto;
import br.com.codex.v1.service.SolicitacaoCompraService;
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
@RequestMapping(value = "v1/api/solicitacao_compra")
public class SolicitacaoCompraResource {

    @Autowired
    private SolicitacaoCompraService solicitacaoCompraService;

    @PostMapping
    public ResponseEntity<SolicitacaoCompraDto> create(@Valid @RequestBody SolicitacaoCompraDto solicitacaoCompraDto) {
        SolicitacaoCompra obj = solicitacaoCompraService.create(solicitacaoCompraDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'COMPRADOR', 'GERENTE_COMPRAS')")
    @GetMapping("/solicitacao_compra_ano")
    public ResponseEntity <List<SolicitacaoCompraDto>> findAllByYear(){
        List<SolicitacaoCompra> objVenda = solicitacaoCompraService.findAllByYear();
        List<SolicitacaoCompraDto> listDto = objVenda.stream().map(SolicitacaoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/solicitacao_usuario_ano")
    public ResponseEntity <List<SolicitacaoCompraDto>> findAllByYearUsuario(@RequestParam(value = "centroCustoUsuario") String centroCustoUsuario, @RequestParam(value = "solicitante") String solicitante){
        List<SolicitacaoCompra> objVenda = solicitacaoCompraService.findAllByYearUsuario(centroCustoUsuario, solicitante);
        List<SolicitacaoCompraDto> listDto = objVenda.stream().map(SolicitacaoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/situacao")
    public ResponseEntity <List<SolicitacaoCompraDto>> findAllBySituacao(){
        List<SolicitacaoCompra> objSolicitacao = solicitacaoCompraService.findAllBySituacao();
        List<SolicitacaoCompraDto> listDto = objSolicitacao.stream().map(SolicitacaoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<SolicitacaoCompraDto> update(@PathVariable Long id, @RequestParam String situacao){
        solicitacaoCompraService.update(id, situacao);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<SolicitacaoItensCompraDto>> findAllItens(@PathVariable Long id) {
        List<SolicitacaoItensCompra> itens = solicitacaoCompraService.findAllItensBySolicitacaoId(id);
        List<SolicitacaoItensCompraDto> listDto = itens.stream().map(SolicitacaoItensCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoCompra> findById(@PathVariable Long id){
        SolicitacaoCompra obj = solicitacaoCompraService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    //ASolicitações de Compra Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI', 'GERENTE_ADMINISTRATIVO', 'ADMINISTRATIVO')")
    @GetMapping(value = "/solicitacao_periodo")
    public ResponseEntity<List<SolicitacaoCompraDto>> findAllSolicitacaoPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<SolicitacaoCompra> list = solicitacaoCompraService.findAllSolicitacaoPeriodo(dataInicial, dataFinal);
        List<SolicitacaoCompraDto> listDto = list.stream().map(SolicitacaoCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
