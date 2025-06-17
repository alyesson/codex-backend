package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.ContaPagarDto;
import br.com.codex.v1.domain.financeiro.ContaPagar;
import br.com.codex.v1.service.ContasPagarService;
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
@RequestMapping(value ="v1/api/conta_pagar")
public class ContasPagarResource {

    @Autowired
    private ContasPagarService contasPagarService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @PostMapping
    public ResponseEntity<ContaPagarDto> create(@Valid @RequestBody ContaPagarDto contaPagarDto) {
        ContaPagar obj = contasPagarService.create(contaPagarDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ContaPagarDto> update(@PathVariable Long id, @RequestParam String situacao){
        contasPagarService.update(id, situacao);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @GetMapping
    public ResponseEntity <List<ContaPagarDto>> findAll(){
        List<ContaPagar> objVenda = contasPagarService.findAll();
        List<ContaPagarDto> listDto = objVenda.stream().map(ContaPagarDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContaPagarDto> findById(@PathVariable Long id){
        ContaPagar obj = contasPagarService.findById(id);
        return ResponseEntity.ok().body(new ContaPagarDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ContaPagarDto> delete (@PathVariable Long id){
        contasPagarService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
