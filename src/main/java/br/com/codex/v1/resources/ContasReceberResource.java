package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.ContaReceberDto;
import br.com.codex.v1.domain.financeiro.ContaReceber;
import br.com.codex.v1.service.ContasReceberService;
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
@RequestMapping(value ="v1/api/conta_receber")
public class ContasReceberResource {

    @Autowired
    private ContasReceberService contasReceberService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @PostMapping
    public ResponseEntity<ContaReceberDto> create(@Valid @RequestBody ContaReceberDto contaReceberDto) {
        ContaReceber obj = contasReceberService.create(contaReceberDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ContaReceberDto> update(@PathVariable Integer id, @RequestParam String situacao){
        contasReceberService.update(id, situacao);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @GetMapping
    public ResponseEntity <List<ContaReceberDto>> findAll(){
        List<ContaReceber> objVenda = contasReceberService.findAll();
        List<ContaReceberDto> listDto = objVenda.stream().map(ContaReceberDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContaReceberDto> findById(@PathVariable Integer id){
        ContaReceber obj = contasReceberService.findById(id);
        return ResponseEntity.ok().body(new ContaReceberDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ContaReceberDto> delete (@PathVariable Integer id){
        contasReceberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
