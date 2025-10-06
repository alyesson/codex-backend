package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.ValeAlimentacaoRefeicaoDto;
import br.com.codex.v1.domain.rh.ValeAlimentacaoRefeicao;
import br.com.codex.v1.service.ValeAlimentacaoRefeicaoService;
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
@RequestMapping("v1/api/vale_alimentacao_refeicao")
public class ValeAlimentacaoRefeicaoResource {

    @Autowired
    private ValeAlimentacaoRefeicaoService service;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PostMapping
    public ResponseEntity<ValeAlimentacaoRefeicaoDto> create(@Valid @RequestBody ValeAlimentacaoRefeicaoDto valeAlimentacaoRefeicaoDto) {
        ValeAlimentacaoRefeicao obj = service.create(valeAlimentacaoRefeicaoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PutMapping("/{id}")
    public ResponseEntity<ValeAlimentacaoRefeicaoDto> update(@PathVariable Long id, @Valid @RequestBody ValeAlimentacaoRefeicaoDto valeAlimentacaoRefeicaoDto) {
        ValeAlimentacaoRefeicao obj = service.update(id, valeAlimentacaoRefeicaoDto);
        return ResponseEntity.ok().body(new ValeAlimentacaoRefeicaoDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ValeAlimentacaoRefeicaoDto> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<ValeAlimentacaoRefeicaoDto> findById(@PathVariable Long id) {
        ValeAlimentacaoRefeicao obj = service.findById(id);
        return ResponseEntity.ok().body(new ValeAlimentacaoRefeicaoDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping
    public ResponseEntity<List<ValeAlimentacaoRefeicaoDto>> findAll() {
        List<ValeAlimentacaoRefeicao> list = service.findAll();
        List<ValeAlimentacaoRefeicaoDto> listDto = list.stream().map(ValeAlimentacaoRefeicaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/colaborador/{colaboradorId}")
    public ResponseEntity<List<ValeAlimentacaoRefeicaoDto>> findByColaboradorId(@PathVariable Long colaboradorId) {
        List<ValeAlimentacaoRefeicao> list = service.findByColaboradorId(colaboradorId);
        List<ValeAlimentacaoRefeicaoDto> listDto = list.stream().map(ValeAlimentacaoRefeicaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/ativos")
    public ResponseEntity<List<ValeAlimentacaoRefeicaoDto>> findAtivos() {
        List<ValeAlimentacaoRefeicao> list = service.findAtivos();
        List<ValeAlimentacaoRefeicaoDto> listDto = list.stream().map(ValeAlimentacaoRefeicaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/tipo/{tipoBeneficio}")
    public ResponseEntity<List<ValeAlimentacaoRefeicaoDto>> findByTipoBeneficio(@PathVariable String tipoBeneficio) {
        List<ValeAlimentacaoRefeicao> list = service.findByTipoBeneficio(tipoBeneficio);
        List<ValeAlimentacaoRefeicaoDto> listDto = list.stream().map(ValeAlimentacaoRefeicaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/colaborador/cpf/{cpf}")
    public ResponseEntity<ValeAlimentacaoRefeicaoDto> findAtivoByColaboradorCpf(@PathVariable String cpf) {
        ValeAlimentacaoRefeicao obj = service.findAtivoByColaboradorCpf(cpf);
        return ResponseEntity.ok().body(new ValeAlimentacaoRefeicaoDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/vencimentos-proximos")
    public ResponseEntity<List<ValeAlimentacaoRefeicaoDto>> findVencimentosProximos() {
        List<ValeAlimentacaoRefeicao> list = service.findVencimentosProximos();
        List<ValeAlimentacaoRefeicaoDto> listDto = list.stream().map(ValeAlimentacaoRefeicaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ValeAlimentacaoRefeicaoDto> desativar(@PathVariable Long id) {
        ValeAlimentacaoRefeicao obj = service.desativar(id);
        return ResponseEntity.ok().body(new ValeAlimentacaoRefeicaoDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH')")
    @PatchMapping("/{id}/reativar")
    public ResponseEntity<ValeAlimentacaoRefeicaoDto> reativar(@PathVariable Long id) {
        ValeAlimentacaoRefeicao obj = service.reativar(id);
        return ResponseEntity.ok().body(new ValeAlimentacaoRefeicaoDto(obj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/refeitorio-interno")
    public ResponseEntity<List<ValeAlimentacaoRefeicaoDto>> findByRefeitorioInterno() {
        List<ValeAlimentacaoRefeicao> list = service.findByRefeitorioInterno();
        List<ValeAlimentacaoRefeicaoDto> listDto = list.stream().map(ValeAlimentacaoRefeicaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_RH', 'RH', 'GERENTE')")
    @GetMapping("/restaurante-conveniado")
    public ResponseEntity<List<ValeAlimentacaoRefeicaoDto>> findByRestauranteConveniado() {
        List<ValeAlimentacaoRefeicao> list = service.findByRestauranteConveniado();
        List<ValeAlimentacaoRefeicaoDto> listDto = list.stream().map(ValeAlimentacaoRefeicaoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
