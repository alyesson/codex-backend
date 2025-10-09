package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.TabelaDeducaoInssDto;
import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import br.com.codex.v1.service.TabelaDeducaoInssService;
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
@RequestMapping(value = "v1/api/tabela_deducao_inss")
public class TabelaDeducaoInssResource {

    @Autowired
    private TabelaDeducaoInssService tabelaDeducaoInssService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<TabelaDeducaoInssDto> create(@Valid @RequestBody TabelaDeducaoInssDto tabelaDeducaoInssDto){
        TabelaDeducaoInss obj = tabelaDeducaoInssService.create(tabelaDeducaoInssDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<TabelaDeducaoInssDto> update(@PathVariable Long id, @Valid @RequestBody TabelaDeducaoInssDto tabelaDeducaoInssDto){
        TabelaDeducaoInss obj = tabelaDeducaoInssService.update(id, tabelaDeducaoInssDto);
        return ResponseEntity.ok().body(new TabelaDeducaoInssDto((obj)));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TabelaDeducaoInssDto> findById(@PathVariable Long id){
        TabelaDeducaoInss objGrupo = tabelaDeducaoInssService.findById(id);
        return ResponseEntity.ok().body(new TabelaDeducaoInssDto(objGrupo));
    }

    @GetMapping
    public ResponseEntity<List<TabelaDeducaoInssDto>> findAll(){
        List<TabelaDeducaoInss> list = tabelaDeducaoInssService.findAll();
        List<TabelaDeducaoInssDto> listDto = list.stream().map(TabelaDeducaoInssDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
