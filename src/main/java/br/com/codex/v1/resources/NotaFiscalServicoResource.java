package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.NotaFiscalServicoDto;
import br.com.codex.v1.domain.fiscal.NotaFiscalServico;
import br.com.codex.v1.service.NotaFiscalServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/notas_fiscais_servico")
public class NotaFiscalServicoResource {

    @Autowired
    private NotaFiscalServicoService notaFiscalServicoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FISCAL', 'FISCAL')")
    @PostMapping
    public ResponseEntity<NotaFiscalServicoDto> create(@Valid @RequestBody NotaFiscalServicoDto notaFiscalServicoDto) {
        NotaFiscalServico obj = notaFiscalServicoService.create(notaFiscalServicoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FISCAL', 'FISCAL')")
    @GetMapping(value = "/periodo")
    public ResponseEntity<List<NotaFiscalServicoDto>> findByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        List<NotaFiscalServico> list = notaFiscalServicoService.findByPeriodo(dataInicial, dataFinal);
        List<NotaFiscalServicoDto> listDto = list.stream().map(NotaFiscalServicoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FISCAL')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<NotaFiscalServicoDto> delete(@PathVariable Long id) {
        notaFiscalServicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FISCAL', 'FISCAL')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<NotaFiscalServicoDto> findById(@PathVariable Long id) {
        NotaFiscalServico obj = notaFiscalServicoService.findById(id);
        return ResponseEntity.ok().body(new NotaFiscalServicoDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FISCAL', 'FISCAL')")
    @GetMapping(value = "/ano/{year}")
    public ResponseEntity<List<NotaFiscalServicoDto>> findByYear(@PathVariable Integer year) {
        List<NotaFiscalServico> list = notaFiscalServicoService.findByYear(year);
        List<NotaFiscalServicoDto> listDto = list.stream().map(NotaFiscalServicoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    // No controlador NotaFiscalServicoResource
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FISCAL', 'FISCAL')")
    @PatchMapping(value = "/{id}/cancelar")
    public ResponseEntity<NotaFiscalServicoDto> cancelar(@PathVariable Long id,  @RequestParam String justificativa) {
        NotaFiscalServico obj = notaFiscalServicoService.cancelar(id, justificativa);
        return ResponseEntity.ok().body(new NotaFiscalServicoDto(obj));
    }
}
