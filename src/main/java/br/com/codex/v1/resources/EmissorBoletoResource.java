package br.com.codex.v1.resources;

import br.com.codex.v1.domain.dto.EmissorBoletoDto;
import br.com.codex.v1.domain.financeiro.EmissorBoleto;
import br.com.codex.v1.service.EmissorBoletoService;
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
@RequestMapping(value = "v1/api/emissao_boleto")
public class EmissorBoletoResource {
    
    @Autowired
    private EmissorBoletoService emissorBoletoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @PostMapping
    public ResponseEntity< EmissorBoletoDto> create(@Valid @RequestBody  EmissorBoletoDto emissorBoletoDto){
        EmissorBoleto obj = emissorBoletoService.create(emissorBoletoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<EmissorBoletoDto> findById(@PathVariable Long id){
         EmissorBoleto objGrupo = emissorBoletoService.findById(id);
        return ResponseEntity.ok().body(new  EmissorBoletoDto(objGrupo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_FINANCEIRO', 'FINANCEIRO')")
    @GetMapping(value = "/boletos_periodo")
    public ResponseEntity<List< EmissorBoletoDto>>findAllAtendimentosPeriodo(@RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                                          @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List< EmissorBoleto> list = emissorBoletoService.findAllBoletosPeriodo(dataInicial, dataFinal);
        List< EmissorBoletoDto> listDto = list.stream().map(EmissorBoletoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
