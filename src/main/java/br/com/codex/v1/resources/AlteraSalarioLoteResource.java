package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.AlteraSalarioLoteDto;
import br.com.codex.v1.domain.rh.AlteraSalarioLote;
import br.com.codex.v1.service.AlteraSalarioLoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/api/alteracao_salario_lote")
public class AlteraSalarioLoteResource {

    @Autowired
    private AlteraSalarioLoteService alteraSalarioLoteService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<AlteraSalarioLoteDto> create(@Valid @RequestBody AlteraSalarioLoteDto orcamentoDto) {
        AlteraSalarioLote obj = alteraSalarioLoteService.create(orcamentoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping(value = "/alteracao_periodo")
    public ResponseEntity<List<AlteraSalarioLoteDto>> findAllByDataAlteracao(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<AlteraSalarioLote> list = alteraSalarioLoteService.findAllByDataAlteracao(dataInicial, dataFinal);
        List<AlteraSalarioLoteDto> listDto = list.stream().map(AlteraSalarioLoteDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_RH', 'RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity <AlteraSalarioLoteDto> deleteById(@PathVariable Long id){
        alteraSalarioLoteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @GetMapping("/{id}")
    public ResponseEntity<AlteraSalarioLoteDto> findById(@PathVariable Long id) {
        AlteraSalarioLote obj = alteraSalarioLoteService.findById(id);
        return ResponseEntity.ok().body(new AlteraSalarioLoteDto(obj));
    }
}
