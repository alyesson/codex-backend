package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.OrdemCompra;
import br.com.codex.v1.domain.compras.OrdemItensCompra;
import br.com.codex.v1.domain.dto.OrdemCompraDto;
import br.com.codex.v1.domain.dto.OrdemItensCompraDto;
import br.com.codex.v1.service.OrdemCompraService;
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
@RequestMapping(value = "v1/api/ordem_compra")
public class OrdemCompraResource {

    @Autowired
    private OrdemCompraService ordemCompraService;

    @PostMapping
    public ResponseEntity<OrdemCompraDto> create(@Valid @RequestBody OrdemCompraDto ordemCompraDto) {
        OrdemCompra obj = ordemCompraService.create(ordemCompraDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'GERENTE_ADMINISTRATIVO', 'GERENTE_CONTABILIDADE', 'GERENTE_FINANCEIRO', 'GERENTE_COMPRAS', 'GERENTE_VENDAS')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<OrdemCompraDto> update(@PathVariable Long id, @RequestParam String situacao){
        ordemCompraService.update(id, situacao);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<OrdemItensCompraDto>> findAllItens(@PathVariable Long id) {
        List<OrdemItensCompra> itens = ordemCompraService.findAllItensByOrdemId(id);
        List<OrdemItensCompraDto> listDto = itens.stream().map(OrdemItensCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/solicitacoes_usuario_ano")
    public ResponseEntity <List<OrdemCompraDto>> findAllByYearUsuario(@RequestParam(value = "ano") Integer ano, @RequestParam(value = "centroCustoUsuario") String centroCustoUsuario, @RequestParam(value = "solicitante") String solicitante){
        List<OrdemCompra> objVenda = ordemCompraService.findAllByYearUsuario(ano, centroCustoUsuario, solicitante);
        List<OrdemCompraDto> listDto = objVenda.stream().map(OrdemCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO', 'GERENTE_COMPRAS', 'GERENTE_VENDAS', 'GERENTE_ESTOQUE', 'GERENTE_ADMINISTRATIVO', 'GERENTE_CONTABILIDADE')")
    @GetMapping("/solicitacoes_compra_ano")
    public ResponseEntity <List<OrdemCompraDto>> findAllByYear(@RequestParam(value = "ano") Integer ano, @RequestParam(value = "centroCustoUsuario") String centroCustoUsuario){
        List<OrdemCompra> objVenda = ordemCompraService.findAllByYear(ano, centroCustoUsuario);
        List<OrdemCompraDto> listDto = objVenda.stream().map(OrdemCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/situacao")
    public ResponseEntity <List<OrdemCompraDto>> findAllBySituacao(){
        List<OrdemCompra> objOrdem = ordemCompraService.findAllBySituacao();
        List<OrdemCompraDto> listDto = objOrdem.stream().map(OrdemCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemCompra> findById(@PathVariable Long id){
        OrdemCompra obj = ordemCompraService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    //ASolicitações de Compra Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_TI', 'TI', 'GERENTE_ADMINISTRATIVO', 'ADMINISTRATIVO')")
    @GetMapping(value = "/solicitacoes_periodo")
    public ResponseEntity<List<OrdemCompraDto>> findAllOrdemPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<OrdemCompra> list = ordemCompraService.findAllOrdemPeriodo(dataInicial, dataFinal);
        List<OrdemCompraDto> listDto = list.stream().map(OrdemCompraDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
