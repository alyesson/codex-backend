package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.OrcamentoDto;
import br.com.codex.v1.domain.dto.OrcamentoItensDto;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.vendas.Orcamento;
import br.com.codex.v1.domain.vendas.OrcamentoItens;
import br.com.codex.v1.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/orcamento")
public class OrcamentoResource {

    @Autowired
    private OrcamentoService orcamentoService;

    @PostMapping
    public ResponseEntity<OrcamentoDto> create(@Valid @RequestBody OrcamentoDto orcamentoDto) {
        Orcamento obj = orcamentoService.create(orcamentoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @PutMapping(value = "/situacao/{id}")
    public ResponseEntity<OrcamentoDto> atualizarSituacao(@PathVariable Long id, @RequestParam Situacao situacao) {

        Orcamento orcamento = orcamentoService.atualizarSituacao(id, situacao);
        return ResponseEntity.ok(new OrcamentoDto(orcamento));
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<OrcamentoItensDto>> findAllItens(@PathVariable Long id) {
        List<OrcamentoItens> itens = orcamentoService.findAllItensByOrcamentoId(id);
        List<OrcamentoItensDto> listDto = itens.stream().map(OrcamentoItensDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/orcamento_vendedor_ano")
    public ResponseEntity <List<OrcamentoDto>> findAllByYearUsuario(@RequestParam(value = "ano") Integer ano, @RequestParam(value = "vendedor") String vendedor){
        List<Orcamento> objVenda = orcamentoService.findAllByYearUsuario(ano, vendedor);
        List<OrcamentoDto> listDto = objVenda.stream().map(OrcamentoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS', 'VENDAS')")
    @GetMapping("/orcamento_ano")
    public ResponseEntity <List<OrcamentoDto>> findAllByYear(@RequestParam(value = "ano") Integer ano){
        List<Orcamento> objVenda = orcamentoService.findAllByYear(ano);
        List<OrcamentoDto> listDto = objVenda.stream().map(OrcamentoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/situacao")
    public ResponseEntity <List<OrcamentoDto>> findAllBySituacao(){
        List<Orcamento> objSolicitacao = orcamentoService.findAllBySituacao();
        List<OrcamentoDto> listDto = objSolicitacao.stream().map(OrcamentoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoDto> findById(@PathVariable Long id){
        Orcamento obj = orcamentoService.findById(id);
        OrcamentoDto response = new OrcamentoDto(obj);
        return ResponseEntity.ok().body(response);
    }

    //ASolicitações de Compra Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_VENDAS')")
    @GetMapping(value = "/orcamento_periodo")
    public ResponseEntity<List<OrcamentoDto>> findAllOrcamentoPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<Orcamento> list = orcamentoService.findAllOrcamentoPeriodo(dataInicial, dataFinal);
        List<OrcamentoDto> listDto = list.stream().map(OrcamentoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
