package br.com.codex.v1.resources;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.estoque.SolicitacaoMaterial;
import br.com.codex.v1.domain.estoque.SolicitacaoMaterialItens;
import br.com.codex.v1.domain.dto.SolicitacaoMaterialDto;
import br.com.codex.v1.domain.dto.SolicitacaoMaterialItensDto;
import br.com.codex.v1.service.SolicitacaoMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/solicitacao_material")
public class SolicitacaoMaterialResource {

    @Autowired
    private SolicitacaoMaterialService solicitacaoMaterialService;

    @PostMapping
    public ResponseEntity<SolicitacaoMaterialDto> create(@Valid @RequestBody SolicitacaoMaterialDto solicitacaoMaterialDto) {
        SolicitacaoMaterial obj = solicitacaoMaterialService.create(solicitacaoMaterialDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping("/solicitacao_material_ano")
    public ResponseEntity<List<SolicitacaoMaterialDto>> findAllByYear() {
        List<SolicitacaoMaterial> list = solicitacaoMaterialService.findAllByYear();
        List<SolicitacaoMaterialDto> listDto = list.stream().map(SolicitacaoMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/solicitacao_usuario_ano")
    public ResponseEntity<List<SolicitacaoMaterialDto>> findAllByYearUsuario(@RequestParam(value = "email") String email) {
        List<SolicitacaoMaterial> list = solicitacaoMaterialService.findAllByYearUsuario(email);
        List<SolicitacaoMaterialDto> listDto = list.stream().map(SolicitacaoMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping("/situacao")
    public ResponseEntity<List<SolicitacaoMaterialDto>> findAllBySituacao(@RequestParam(value = "situacao") Situacao situacao) {
        List<SolicitacaoMaterial> list = solicitacaoMaterialService.findAllBySituacao(situacao);
        List<SolicitacaoMaterialDto> listDto = list.stream().map(SolicitacaoMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<SolicitacaoMaterialDto> update(@PathVariable Long id, @RequestParam Situacao situacao) {
        solicitacaoMaterialService.update(id, situacao);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @PutMapping(value = "/situacao/{id}")
    public ResponseEntity<SolicitacaoMaterialItensDto> updateItens(@PathVariable Long id, @RequestParam String situacao) {
        solicitacaoMaterialService.updateItens(id, situacao);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<SolicitacaoMaterialItensDto>> findAllItens(@PathVariable Long id) {
        List<SolicitacaoMaterialItens> itens = solicitacaoMaterialService.findAllItensBySolicitacaoId(id);
        List<SolicitacaoMaterialItensDto> listDto = itens.stream().map(SolicitacaoMaterialItensDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoMaterialDto> findById(@PathVariable Long id) {
        SolicitacaoMaterial obj = solicitacaoMaterialService.findById(id);
        return ResponseEntity.ok().body(new SolicitacaoMaterialDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping("/solicitacao_periodo")
    public ResponseEntity<List<SolicitacaoMaterialDto>> findAllSolicitacaoPeriodo(
            @RequestParam("dataInicial") LocalDate dataInicial,
            @RequestParam("dataFinal") LocalDate dataFinal) {
        List<SolicitacaoMaterial> list = solicitacaoMaterialService.findAllSolicitacaoPeriodo(dataInicial, dataFinal);
        List<SolicitacaoMaterialDto> listDto = list.stream().map(SolicitacaoMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}