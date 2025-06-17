package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.CriteriosAvaliacao;
import br.com.codex.v1.domain.dto.CriteriosAvaliacaoDto;
import br.com.codex.v1.service.CriteriosAvaliacaoService;
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
@RequestMapping(value = "v1/api/criterios_avaliacao")
public class CriteriosAvaliacaoResource {

    @Autowired
    private CriteriosAvaliacaoService criteriosAvaliacaoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS')")
    @PostMapping
    public ResponseEntity<CriteriosAvaliacaoDto> create(@Valid @RequestBody CriteriosAvaliacaoDto critoerioavaliacaoDto){
        CriteriosAvaliacao obj = criteriosAvaliacaoService.create(critoerioavaliacaoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CriteriosAvaliacaoDto> update(@PathVariable Long id, @Valid @RequestBody CriteriosAvaliacaoDto critoerioavaliacaoDto){
        CriteriosAvaliacao obj = criteriosAvaliacaoService.update(id, critoerioavaliacaoDto);
        return ResponseEntity.ok().body(new CriteriosAvaliacaoDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CriteriosAvaliacaoDto> delete(@PathVariable Long id){
        criteriosAvaliacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRAS')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CriteriosAvaliacaoDto> findById(@PathVariable Long id){
        CriteriosAvaliacao objCriterios = criteriosAvaliacaoService.findById(id);
        return ResponseEntity.ok().body(new CriteriosAvaliacaoDto(objCriterios));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRAS')")
    @GetMapping
    public ResponseEntity<List<CriteriosAvaliacaoDto>> findAll(){
        List<CriteriosAvaliacao> list = criteriosAvaliacaoService.findAll();
        List<CriteriosAvaliacaoDto> listDto = list.stream().map(CriteriosAvaliacaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
