package br.com.codex.v1.resources;

import br.com.codex.v1.domain.contabilidade.HistoricoPadrao;
import br.com.codex.v1.domain.dto.HistoricoPadraoDto;
import br.com.codex.v1.service.HistoricoPadraoService;
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
@RequestMapping(value = "v1/api/historico_padrao")
public class HistoricoPadraoResource {
    
    @Autowired
    private HistoricoPadraoService historicoPadraoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @PostMapping
    public ResponseEntity<HistoricoPadraoDto> create(@Valid @RequestBody HistoricoPadraoDto historicopadraoDto){
        HistoricoPadrao obj = historicoPadraoService.create(historicopadraoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<HistoricoPadraoDto> update(@PathVariable Integer id, @Valid @RequestBody HistoricoPadraoDto historicopadraoDto){
        HistoricoPadrao obj = historicoPadraoService.update(id, historicopadraoDto);
        return ResponseEntity.ok().body(new HistoricoPadraoDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HistoricoPadraoDto> delete(@PathVariable Integer id){
        historicoPadraoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<HistoricoPadraoDto> findById(@PathVariable Integer id){
        HistoricoPadrao objGrupo = historicoPadraoService.findById(id);
        return ResponseEntity.ok().body(new HistoricoPadraoDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping
    public ResponseEntity<List<HistoricoPadraoDto>> findAll(){
        List<HistoricoPadrao> list = historicoPadraoService.findAll();
        List<HistoricoPadraoDto> listDto = list.stream().map(HistoricoPadraoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
