package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.AvaliacaoFornecedores;
import br.com.codex.v1.domain.compras.AvaliacaoFornecedoresDetalhes;
import br.com.codex.v1.domain.dto.AvaliacaoFornecedoresDto;
import br.com.codex.v1.domain.dto.AvaliacaoFornecedoresDetalhesDto;
import br.com.codex.v1.service.AvaliacaoFornecedoresService;
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
@RequestMapping(value = "v1/api/avaliacao_fornecedores")
public class AvaliacaoFornecedoresResource {

    @Autowired
    private AvaliacaoFornecedoresService avaliacaoFornecedoresService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRAS')")
    @PostMapping
    public ResponseEntity<AvaliacaoFornecedoresDto> create(@Valid @RequestBody AvaliacaoFornecedoresDto avaliacaoFornecedoresDto) {
        AvaliacaoFornecedores obj = avaliacaoFornecedoresService.create(avaliacaoFornecedoresDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRAS')")
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<AvaliacaoFornecedoresDetalhesDto>> findAllItens(@PathVariable Long id) {
        List<AvaliacaoFornecedoresDetalhes> itens = avaliacaoFornecedoresService.findAllItensByAvaliacaoId(id);
        List<AvaliacaoFornecedoresDetalhesDto> listDto = itens.stream().map(AvaliacaoFornecedoresDetalhesDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_COMPRAS', 'COMPRAS')")
    @GetMapping
    public ResponseEntity <List<AvaliacaoFornecedoresDto>> findAll(){
        List<AvaliacaoFornecedores> objAvaliacao = avaliacaoFornecedoresService.findAll();
        List<AvaliacaoFornecedoresDto> listDto = objAvaliacao.stream().map(AvaliacaoFornecedoresDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_COMPRAS', 'COMPRAS')")
    @GetMapping("/{id}")
    public ResponseEntity <AvaliacaoFornecedoresDto> findById(@PathVariable Long id){
        AvaliacaoFornecedores objCotacao = avaliacaoFornecedoresService.findById(id);
        return ResponseEntity.ok().body(new AvaliacaoFornecedoresDto(objCotacao));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_COMPRAS', 'COMPRAS')")
    @DeleteMapping("/{id}")
    public ResponseEntity <AvaliacaoFornecedoresDto> deleteById(@PathVariable Long id){
        avaliacaoFornecedoresService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
