package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.TabelaImpostoRendaDto;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import br.com.codex.v1.service.TabelaImpostoRendaService;
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
@RequestMapping(value = "v1/api/tabela_imposto_renda")
public class TabelaImpostoRendaResource {

    @Autowired
    private TabelaImpostoRendaService tabelaImpostoRendaService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<TabelaImpostoRendaDto> create(@Valid @RequestBody TabelaImpostoRendaDto tabelaImpostoRendaDto){
        TabelaImpostoRenda obj = tabelaImpostoRendaService.create(tabelaImpostoRendaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<TabelaImpostoRendaDto> update(@PathVariable Long id, @Valid @RequestBody TabelaImpostoRendaDto tabelaImpostoRendaDto){
        TabelaImpostoRenda obj = tabelaImpostoRendaService.update(id, tabelaImpostoRendaDto);
        return ResponseEntity.ok().body(new TabelaImpostoRendaDto((obj)));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TabelaImpostoRendaDto> findById(@PathVariable Long id){
        TabelaImpostoRenda objGrupo = tabelaImpostoRendaService.findById(id);
        return ResponseEntity.ok().body(new TabelaImpostoRendaDto(objGrupo));
    }

    @GetMapping
    public ResponseEntity<List<TabelaImpostoRendaDto>> findAll(){
        List<TabelaImpostoRenda> list = tabelaImpostoRendaService.findAll();
        List<TabelaImpostoRendaDto> listDto = list.stream().map(TabelaImpostoRendaDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
