package br.com.codex.v1.resources;

import br.com.codex.v1.domain.estoque.EntradaMaterial;
import br.com.codex.v1.domain.dto.EntradaMaterialDto;
import br.com.codex.v1.service.EntradaMaterialService;
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
@RequestMapping(value = "v1/api/entrada_material")
public class EntradaMaterialResource {

    @Autowired
    EntradaMaterialService entradaMaterialService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA')")
    @PostMapping
    public ResponseEntity<EntradaMaterialDto> create(@Valid @RequestBody EntradaMaterialDto entradaMaterialDto){
        EntradaMaterial objEntrada = entradaMaterialService.create(entradaMaterialDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objEntrada.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    //Lista todos os produtos, incluindo os zerados
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA', 'GERENTE_VENDAS', 'GERENTE_COMPRAS', 'GERENTE_VENDAS', 'VENDEDOR', 'COMPRADOR')")
    @GetMapping
    public ResponseEntity<List<EntradaMaterialDto>> findAll(){
        List<EntradaMaterial> list = entradaMaterialService.findAll();
        List<EntradaMaterialDto> listObj = list.stream().map(EntradaMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    //Lista todos os produtos do estoque onde a quantidade é diferente de 0
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA', 'GERENTE_VENDAS', 'VENDEDOR')")
    @GetMapping(value = "/produtos_estoque")
    public ResponseEntity<List<EntradaMaterialDto>> findAllByProdutoEstoque(){
        List<EntradaMaterial> lista = entradaMaterialService.findAllByProdutoEstoque();
        List<EntradaMaterialDto> objLista = lista.stream().map(EntradaMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(objLista);
    }

    //Entradas de Material Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA')")
    @GetMapping(value = "/entrada_estoque_periodo")
    public ResponseEntity<List<EntradaMaterialDto>> findAllEntradaPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<EntradaMaterial> list = entradaMaterialService.findAllEntradaPeriodo(dataInicial, dataFinal);
        List<EntradaMaterialDto> listDto = list.stream().map(EntradaMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA')")
    @PutMapping(value = "/ajusta_estoque/{codigoProduto}/{lote}/{quantidade}")
    public ResponseEntity<EntradaMaterialDto> incluiSaldo(@PathVariable String codigoProduto, @PathVariable String lote, @PathVariable int quantidade) {
        EntradaMaterial materialObj = entradaMaterialService.ajustaSaldo(codigoProduto, lote, quantidade);
        return ResponseEntity.ok().body(new EntradaMaterialDto(materialObj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<EntradaMaterialDto> delete(@PathVariable Long id){
        entradaMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntradaMaterialDto> findById(@PathVariable Long id){
        EntradaMaterial objEntrada = entradaMaterialService.findById(id);
        return ResponseEntity.ok().body(new EntradaMaterialDto(objEntrada));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA', 'GERENTE_VENDAS', 'VENDEDOR')")
    @GetMapping(value = "/produto/{codigoProduto}")
    public ResponseEntity<EntradaMaterialDto> findByCodigoProduto(@PathVariable String codigoProduto){ // CORRIGI: mesmo nome
        EntradaMaterial entraObj = entradaMaterialService.findByCodigoProduto(codigoProduto);
        return ResponseEntity.ok().body(new EntradaMaterialDto(entraObj));
    }
}
