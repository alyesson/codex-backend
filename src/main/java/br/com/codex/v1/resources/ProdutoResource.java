package br.com.codex.v1.resources;

import br.com.codex.v1.domain.estoque.Produto;
import br.com.codex.v1.domain.dto.ProdutoDto;
import br.com.codex.v1.service.ProdutoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "v1/api/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService produtoService;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ESTOQUE', 'GERENTE_COMPRAS', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @PostMapping
    public ResponseEntity<ProdutoDto> create(@Valid @RequestBody ProdutoDto produtoDto){
        Produto objProd = produtoService.create(produtoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objProd.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ESTOQUE', 'GERENTE_COMPRAS', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoDto> update(@PathVariable Long id, @Valid @RequestBody ProdutoDto produtoDto){
        Produto objProd = produtoService.update(id, produtoDto);
        return ResponseEntity.ok().body(new ProdutoDto(objProd));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ESTOQUE', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProdutoDto> delete(@PathVariable Long id){
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoDto> findById(@PathVariable Long id){
        Produto objProd = produtoService.findById(id);
        return ResponseEntity.ok().body(new ProdutoDto(objProd));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDto>> findAll(){
        List<Produto> list = produtoService.findAll();
        List<ProdutoDto> listDto = list.stream().map(ProdutoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SISTEMA', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUISTA', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_VENDAS', 'VENDEDOR', 'GERENTE_ADMINISTRATIVO', 'GERENTE_TI')")
    @GetMapping(value = "/descricao/{produto}")
    public ResponseEntity<ProdutoDto> findProduto(@PathVariable String produto){
        Produto objProd = produtoService.findByDescricao(produto);
        return ResponseEntity.ok().body(new ProdutoDto(objProd));
    }

    @GetMapping(value = "/codigo/{codigo}")
    public ResponseEntity<ProdutoDto> findCodigo(@PathVariable String codigo){
        Produto objProd = produtoService.findByCodigo(codigo);
        return ResponseEntity.ok().body(new ProdutoDto(objProd));
    }
}
