package br.com.codexloja.v1.resources;

import br.com.codexloja.v1.domain.contabilidade.Contas;
import br.com.codexloja.v1.domain.dto.ContasDto;
import br.com.codexloja.v1.service.ContasService;
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
@RequestMapping(value = "v1/api/conta_contabil")
public class ContasResource {

    @Autowired
    ContasService contasService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @PostMapping
    public ResponseEntity<ContasDto> create(@Valid @RequestBody ContasDto contasDto){
        Contas obj = contasService.create(contasDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    /*@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ContasDto> update(@PathVariable Integer id, @Valid @RequestBody ContasDto contasDto){
        Contas obj = contasService.update(id, contasDto);
        return ResponseEntity.ok().body(new ContasDto((obj)));
    }*/

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ContasDto> delete(@PathVariable Integer id){
        contasService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ContasDto> findById(@PathVariable Integer id){
        Contas objGrupo = contasService.findById(id);
        return ResponseEntity.ok().body(new ContasDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping
    public ResponseEntity<List<ContasDto>> findAll(){
        List<Contas> list = contasService.findAll();
        List<ContasDto> listDto = list.stream().map(ContasDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
