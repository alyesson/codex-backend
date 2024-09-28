package br.com.codexloja.v1.resources;

import br.com.codexloja.v1.domain.estoque.HistoricoMovimentacaoMaterial;
import br.com.codexloja.v1.domain.dto.HistoricoMovimentacaoMaterialDto;
import javax.validation.Valid;

import br.com.codexloja.v1.service.HistoricoMovimentacaoMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/historico_movimentacao_material")
public class HistoricoMovimentacaoMaterialResource {

    @Autowired
    HistoricoMovimentacaoMaterialService historicoMovimentacaoMaterialService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @PostMapping
    public ResponseEntity<HistoricoMovimentacaoMaterialDto> create(@Valid @RequestBody HistoricoMovimentacaoMaterialDto historicoMovimentacaoMaterialDto){
        HistoricoMovimentacaoMaterial objEntrada = historicoMovimentacaoMaterialService.create(historicoMovimentacaoMaterialDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objEntrada.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HistoricoMovimentacaoMaterialDto> delete(@PathVariable Integer id){
        historicoMovimentacaoMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<HistoricoMovimentacaoMaterialDto> findById(@PathVariable Integer id){
        HistoricoMovimentacaoMaterial objHistoricoEntrada = historicoMovimentacaoMaterialService.findById(id);
        return ResponseEntity.ok().body(new HistoricoMovimentacaoMaterialDto(objHistoricoEntrada));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping
    public ResponseEntity<List<HistoricoMovimentacaoMaterialDto>> findAll(){
        List<HistoricoMovimentacaoMaterial> list = historicoMovimentacaoMaterialService.findAll();
        List<HistoricoMovimentacaoMaterialDto> listObj = list.stream().map(HistoricoMovimentacaoMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping(value = "/movimentacao_nota_fiscal")
    public ResponseEntity<List<HistoricoMovimentacaoMaterialDto>> findAllByNotaFiscal(@RequestParam(value = "ano") Integer ano){
        List<HistoricoMovimentacaoMaterial> list = historicoMovimentacaoMaterialService.findAllByNota(ano);
        List<HistoricoMovimentacaoMaterialDto> listObj = list.stream().map(HistoricoMovimentacaoMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping(value = "/despesa_mensal")
    public ResponseEntity<List<HistoricoMovimentacaoMaterialDto>> findAllMonth(@RequestParam(value = "ano") Integer ano, @RequestParam(value = "mes") Integer mes){
        List<HistoricoMovimentacaoMaterial> list = historicoMovimentacaoMaterialService.findAllByYearAndMonth(ano, mes);
        List<HistoricoMovimentacaoMaterialDto> listDto = list.stream().map(HistoricoMovimentacaoMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    //Método para gerar gráfico de despesa com material no ano
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping(value = "/despesa_anual")
    public ResponseEntity<List<BigDecimal>> findAllYear(@RequestParam(value = "ano") Integer ano){
        List<BigDecimal> values = historicoMovimentacaoMaterialService.findAllByYear(ano);
        return ResponseEntity.ok().body(values);
    }
}
