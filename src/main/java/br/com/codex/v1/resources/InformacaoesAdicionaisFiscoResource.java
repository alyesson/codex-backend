package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.InformacaoesAdicionaisFiscoDto;
import br.com.codex.v1.domain.fiscal.InformacaoesAdicionaisFisco;
import br.com.codex.v1.service.InformacaoesAdicionaisFiscoService;
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
@RequestMapping(value = "v1/api/informacao_fisco_nfe")
public class InformacaoesAdicionaisFiscoResource {
    
    @Autowired
    private InformacaoesAdicionaisFiscoService informacaoesAdicionaisFiscoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PostMapping
    public ResponseEntity<InformacaoesAdicionaisFiscoDto> create(@Valid @RequestBody InformacaoesAdicionaisFiscoDto informacaoesAdicionaisFiscoDto){
        InformacaoesAdicionaisFisco obj = informacaoesAdicionaisFiscoService.create(informacaoesAdicionaisFiscoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<InformacaoesAdicionaisFiscoDto> update(@PathVariable Long id, @Valid @RequestBody InformacaoesAdicionaisFiscoDto informacaoesAdicionaisFiscoDto){
        InformacaoesAdicionaisFisco obj = informacaoesAdicionaisFiscoService.update(id, informacaoesAdicionaisFiscoDto);
        return ResponseEntity.ok().body(new InformacaoesAdicionaisFiscoDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<InformacaoesAdicionaisFiscoDto> delete(@PathVariable Long id){
        informacaoesAdicionaisFiscoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InformacaoesAdicionaisFiscoDto> findById(@PathVariable Long id){
        InformacaoesAdicionaisFisco objGrupo = informacaoesAdicionaisFiscoService.findById(id);
        return ResponseEntity.ok().body(new InformacaoesAdicionaisFiscoDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping
    public ResponseEntity<List<InformacaoesAdicionaisFiscoDto>> findAll(){
        List<InformacaoesAdicionaisFisco> list = informacaoesAdicionaisFiscoService.findAll();
        List<InformacaoesAdicionaisFiscoDto> listDto = list.stream().map(InformacaoesAdicionaisFiscoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
