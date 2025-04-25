package br.com.codex.v1.resources;

import br.com.codex.v1.domain.estoque.EntradaMaterial;
import br.com.codex.v1.domain.estoque.Produto;
import br.com.codex.v1.domain.estoque.SaidaMaterial;
import br.com.codex.v1.domain.dto.EntradaMaterialDto;
import br.com.codex.v1.domain.dto.ProdutoDto;
import br.com.codex.v1.domain.dto.SaidaMaterialDto;
import br.com.codex.v1.service.SaidaMaterialService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/saida_material")
public class SaidaMaterialResource {

    @Autowired
    private SaidaMaterialService saidaMaterialService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA')")
    @PostMapping
    public ResponseEntity<SaidaMaterialDto> create(@Valid @RequestBody SaidaMaterialDto saidaMaterialDto){
        SaidaMaterial saiObj = saidaMaterialService.create(saidaMaterialDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saiObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'CAIXA', 'GERENTE_ESTOQUE', 'ESTOQUISTA')")
    @PutMapping(value = "/debitar_saldo/{codigoProduto}/{lote}/{quantidade}")
    public ResponseEntity<EntradaMaterialDto> debitarSaldo(@PathVariable String codigoProduto, @PathVariable String lote, @PathVariable int quantidade){
        EntradaMaterial entradaMaterial = saidaMaterialService.removeSaldo(codigoProduto, lote, quantidade);
        return ResponseEntity.ok().body(new EntradaMaterialDto(entradaMaterial));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA', 'CAIXA')")
    @GetMapping(value = "/codigo/{codigoProduto}")
    public ResponseEntity<ProdutoDto> findByCodigoProduto(@PathVariable String codigoProduto){
        Produto produtoObj = saidaMaterialService.findByCodigoProduto(codigoProduto);
        return ResponseEntity.ok().body(new ProdutoDto(produtoObj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @GetMapping(value = "/ano_corrente")
    public ResponseEntity<List<SaidaMaterialDto>> findAllByYear(@RequestParam (value = "dataSaida") Integer anoAtual){
        List<SaidaMaterial> list = saidaMaterialService.findAllByYear(anoAtual);
        List<SaidaMaterialDto> objList = list.stream().map(SaidaMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(objList);
    }

    //Saída de Material Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @GetMapping(value = "/saida_estoque_periodo")
    public ResponseEntity<List<SaidaMaterialDto>> findAllSaidaPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<SaidaMaterial> list = saidaMaterialService.findAllSaidaPeriodo(dataInicial, dataFinal);
        List<SaidaMaterialDto> listDto = list.stream().map(SaidaMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
