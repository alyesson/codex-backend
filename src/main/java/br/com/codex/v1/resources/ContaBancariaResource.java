package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.ContaBancariaDto;
import br.com.codex.v1.domain.financeiro.ContaBancaria;
import br.com.codex.v1.service.ContaBancariaService;
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
@RequestMapping(value = "v1/api/conta_bancaria")
public class ContaBancariaResource {

    @Autowired
    private ContaBancariaService contaBancariaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO')")
    @PostMapping
    public ResponseEntity<ContaBancariaDto> create(@Valid @RequestBody ContaBancariaDto contaBancariaDto){
        ContaBancaria obj = contaBancariaService.create(contaBancariaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ContaBancariaDto> delete(@PathVariable Long id){
        contaBancariaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ContaBancariaDto> findById(@PathVariable Long id){
        ContaBancaria objGrupo = contaBancariaService.findById(id);
        return ResponseEntity.ok().body(new ContaBancariaDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @GetMapping
    public ResponseEntity<List<ContaBancariaDto>> findAll(){
        List<ContaBancaria> list = contaBancariaService.findAll();
        List<ContaBancariaDto> listDto = list.stream().map(ContaBancariaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
