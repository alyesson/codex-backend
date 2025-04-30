package br.com.codex.v1.resources;

import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import br.com.codex.v1.domain.dto.LancamentoContabilDto;
import br.com.codex.v1.service.LancamentoContabilService;
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
@RequestMapping(value = "v1/api/lancamento_contabil")
public class LancamentoContabilResource {

    @Autowired
    private LancamentoContabilService lancamentoContabilService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @PostMapping
    public ResponseEntity<LancamentoContabilDto> create(@Valid @RequestBody LancamentoContabilDto lancamentoContabilDto){
        LancamentoContabil obj = lancamentoContabilService.create(lancamentoContabilDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping("/periodo_ano_mes")
    public ResponseEntity<List<LancamentoContabilDto>> findAllByYearAndMonth(@RequestParam Integer ano, @RequestParam Integer mes){
        List<LancamentoContabil> list = lancamentoContabilService.findAllByYearAndMonth(ano, mes);
        List<LancamentoContabilDto> listDto = list.stream().map(LancamentoContabilDto::new).collect(Collectors.toList());

        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping("/periodo_ano")
    public ResponseEntity<List<LancamentoContabilDto>> findAllByYearRange(@RequestParam("dataInicial") Date dataInicial,
                                                                          @RequestParam("dataFinal") Date dataFinal){
        List<LancamentoContabil> list = lancamentoContabilService.findAllByYearRange(dataInicial, dataFinal);
        List<LancamentoContabilDto> listDto = list.stream().map(LancamentoContabilDto::new).collect(Collectors.toList());

        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<LancamentoContabilDto> findById(@PathVariable Integer id){
        LancamentoContabil obj = lancamentoContabilService.findById(id);
        return ResponseEntity.ok().body(new LancamentoContabilDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_CONTABILIDADE', 'CONTABILIDADE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<LancamentoContabilDto> delete(@PathVariable Integer id){
        lancamentoContabilService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
