package br.com.codex.v1.resources;

import br.com.codex.v1.domain.compras.Fornecedores;
import br.com.codex.v1.domain.dto.FornecedoresDto;
import br.com.codex.v1.service.FornecedoresService;
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
@RequestMapping(value = "v1/api/fornecedores")
public class FornecedoresResource {

    @Autowired
    private FornecedoresService fornecedoresService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PostMapping
    public ResponseEntity<FornecedoresDto> create(@Valid @RequestBody FornecedoresDto fornecedoresDto){
        Fornecedores objFornec = fornecedoresService.create(fornecedoresDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objFornec.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<FornecedoresDto> update(@PathVariable Long id, @Valid @RequestBody FornecedoresDto fornecedoresDto){
        Fornecedores objFornec = fornecedoresService.update(id, fornecedoresDto);
        return ResponseEntity.ok().body(new FornecedoresDto(objFornec));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<FornecedoresDto> delete(@PathVariable Long id){
        fornecedoresService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<FornecedoresDto> findById(@PathVariable Long id){
        Fornecedores objFornec = fornecedoresService.findById(id);
        return ResponseEntity.ok().body(new FornecedoresDto(objFornec));
    }

    @GetMapping
    public ResponseEntity<List<FornecedoresDto>> findAll(){
        List<Fornecedores> list = fornecedoresService.findAll();
        List<FornecedoresDto> listDto = list.stream().map(FornecedoresDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/cnpj/{cnpj}")
    public ResponseEntity<FornecedoresDto> findByCnpj(@PathVariable String cnpj){
        Fornecedores objFornec = fornecedoresService.findByCnpj(cnpj);
        return ResponseEntity.ok().body(new FornecedoresDto(objFornec));
    }
}
