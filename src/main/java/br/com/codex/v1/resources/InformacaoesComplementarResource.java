package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.InformacaoesComplementaresDto;
import br.com.codex.v1.domain.fiscal.InformacaoesComplementares;
import br.com.codex.v1.service.InformacaoesComplementaresService;
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
public class InformacaoesComplementarResource {
    
    @Autowired
    private InformacaoesComplementaresService informacaoesComplementaresService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PostMapping
    public ResponseEntity<InformacaoesComplementaresDto> create(@Valid @RequestBody InformacaoesComplementaresDto informacaoesComplementaresDto){
        InformacaoesComplementares obj = informacaoesComplementaresService.create(informacaoesComplementaresDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<InformacaoesComplementaresDto> update(@PathVariable Long id, @Valid @RequestBody InformacaoesComplementaresDto informacaoesComplementaresDto){
        InformacaoesComplementares obj = informacaoesComplementaresService.update(id, informacaoesComplementaresDto);
        return ResponseEntity.ok().body(new InformacaoesComplementaresDto((obj)));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<InformacaoesComplementaresDto> delete(@PathVariable Long id){
        informacaoesComplementaresService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InformacaoesComplementaresDto> findById(@PathVariable Long id){
        InformacaoesComplementares objGrupo = informacaoesComplementaresService.findById(id);
        return ResponseEntity.ok().body(new InformacaoesComplementaresDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO','GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping
    public ResponseEntity<List<InformacaoesComplementaresDto>> findAll(){
        List<InformacaoesComplementares> list = informacaoesComplementaresService.findAll();
        List<InformacaoesComplementaresDto> listDto = list.stream().map(InformacaoesComplementaresDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
