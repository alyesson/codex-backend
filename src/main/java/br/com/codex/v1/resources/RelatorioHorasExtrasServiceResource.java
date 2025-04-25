package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.RelatorioHorasExtrasDto;
import br.com.codex.v1.domain.rh.RelatorioHorasExtras;
import br.com.codex.v1.service.RelatorioHorasExtrasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "v1/api/relatorio_horas_extras")
public class RelatorioHorasExtrasServiceResource {

    @Autowired
    private RelatorioHorasExtrasService relatorioHorasExtrasService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<RelatorioHorasExtrasDto> create (@Valid @RequestBody RelatorioHorasExtrasDto relatorioHorasExtrasDto){
        RelatorioHorasExtras objRelatorioHoras = relatorioHorasExtrasService.create(relatorioHorasExtrasDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objRelatorioHoras.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
