package br.com.codexloja.v1.resources;

import br.com.codexloja.v1.domain.contabilidade.Lancamentos;
import br.com.codexloja.v1.domain.contabilidade.LancamentosContas;
import br.com.codexloja.v1.domain.dto.LancamentosContasDto;
import br.com.codexloja.v1.domain.dto.LancamentosDto;
import br.com.codexloja.v1.service.LancamentosService;
import br.com.codexloja.v1.utilitario.ConverteStringEmDate;
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
@RequestMapping(value = "v1/api/lancamentos_contabeis")
public class LancamentosResource {

    @Autowired
    private LancamentosService lancamentosService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PostMapping
    public ResponseEntity<LancamentosDto> create(@Valid @RequestBody LancamentosDto lancamentosDto){
        Lancamentos objLancamento = lancamentosService.create(lancamentosDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objLancamento.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<LancamentosDto> delete(@PathVariable Integer id){
        lancamentosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<LancamentosDto> findById(@PathVariable Integer id){
        Lancamentos objGrupo = lancamentosService.findById(id);
        return ResponseEntity.ok().body(new LancamentosDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/periodo")
    public ResponseEntity<List<LancamentosContasDto>> findAllLancamentos(@RequestParam(value = "dataInicio") String dataInicio, @RequestParam(value = "dataFim") String dataFim){
        ConverteStringEmDate conversor = new ConverteStringEmDate();
        List<LancamentosContas> list = lancamentosService.findAllLancamentos(conversor.converteStringEmDate(dataInicio), conversor.converteStringEmDate(dataFim));
        List<LancamentosContasDto> listDto = list.stream().map(LancamentosContasDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
