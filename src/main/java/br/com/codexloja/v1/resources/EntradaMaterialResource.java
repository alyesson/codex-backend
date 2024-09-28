package br.com.codexloja.v1.resources;

import br.com.codexloja.v1.domain.estoque.EntradaMaterial;
import br.com.codexloja.v1.domain.dto.EntradaMaterialDto;
import br.com.codexloja.v1.service.EntradaMaterialService;
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

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @PostMapping
    public ResponseEntity<EntradaMaterialDto> create(@Valid @RequestBody EntradaMaterialDto entradaMaterialDto){
        EntradaMaterial objEntrada = entradaMaterialService.create(entradaMaterialDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objEntrada.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @PutMapping(value = "/ajusta_estoque/{codigoProduto}/{lote}/{quantidade}")
    public ResponseEntity<EntradaMaterialDto> incluiSaldo(@PathVariable String codigoProduto, @PathVariable String lote, @PathVariable int quantidade) {
        EntradaMaterial materialObj = entradaMaterialService.ajustaSaldo(codigoProduto, lote, quantidade);
        return ResponseEntity.ok().body(new EntradaMaterialDto(materialObj));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<EntradaMaterialDto> delete(@PathVariable Integer id){
        entradaMaterialService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntradaMaterialDto> findById(@PathVariable Integer id){
        EntradaMaterial objEntrada = entradaMaterialService.findById(id);
        return ResponseEntity.ok().body(new EntradaMaterialDto(objEntrada));
    }

    //Lista todos os produtos, incluindo os zerados
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE', 'GERENTE_VENDAS', 'GERENTE_COMPRAS', 'GERENTE_VENDAS', 'VENDEDOR', 'CAIXA', 'COMPRADOR')")
    @GetMapping
    public ResponseEntity<List<EntradaMaterialDto>> findAll(){
        List<EntradaMaterial> list = entradaMaterialService.findAll();
        List<EntradaMaterialDto> listObj = list.stream().map(EntradaMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listObj);
    }

    //Lista todos os produtos do estoque onde a quantidade é diferente de 0
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE', 'GERENTE_VENDAS', 'VENDEDOR', 'CAIXA')")
    @GetMapping(value = "/produtos_estoque")
    public ResponseEntity<List<EntradaMaterialDto>> findAllByProdutoEstoque(){
        List<EntradaMaterial> lista = entradaMaterialService.findAllByProdutoEstoque();
        List<EntradaMaterialDto> objLista = lista.stream().map(EntradaMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(objLista);
    }

    //Entradas de Material Por Período
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE')")
    @GetMapping(value = "/entrada_estoque_periodo")
    public ResponseEntity<List<EntradaMaterialDto>> findAllEntradaPeriodo(@RequestParam("dataInicial") Date dataInicial, @RequestParam("dataFinal") Date dataFinal){
        List<EntradaMaterial> list = entradaMaterialService.findAllEntradaPeriodo(dataInicial, dataFinal);
        List<EntradaMaterialDto> listDto = list.stream().map(EntradaMaterialDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }
}
