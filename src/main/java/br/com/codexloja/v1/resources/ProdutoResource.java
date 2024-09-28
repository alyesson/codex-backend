package br.com.codexloja.v1.resources;

import br.com.codexloja.v1.domain.estoque.Produto;
import br.com.codexloja.v1.domain.dto.ProdutoDto;
import br.com.codexloja.v1.service.ProdutoService;
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

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'GERENTE_COMPRAS')")
    @PostMapping
    public ResponseEntity<ProdutoDto> create(@Valid @RequestBody ProdutoDto produtoDto){
        Produto objProd = produtoService.create(produtoDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(objProd.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'GERENTE_COMPRAS')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoDto> update(@PathVariable Integer id, @Valid @RequestBody ProdutoDto produtoDto){
        Produto objProd = produtoService.update(id, produtoDto);
        return ResponseEntity.ok().body(new ProdutoDto(objProd));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProdutoDto> delete(@PathVariable Integer id){
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE', 'GERENTE_COMPRAS', 'COMPRADOR')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoDto> findById(@PathVariable Integer id){
        Produto objProd = produtoService.findById(id);
        return ResponseEntity.ok().body(new ProdutoDto(objProd));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDto>> findAll(){
        List<Produto> list = produtoService.findAll();
        List<ProdutoDto> listDto = list.stream().map(ProdutoDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'SOCIO', 'GERENTE_ESTOQUE', 'ESTOQUE', 'GERENTE_COMPRAS', 'COMPRADOR', 'GERENTE_VENDAS', 'VENDEDOR', 'CAIXA')")
    @GetMapping(value = "/descricao/{produto}")
    public ResponseEntity<ProdutoDto> findProduto(@PathVariable String produto){
        Produto objProd = produtoService.findByDescricao(produto);
        return ResponseEntity.ok().body(new ProdutoDto(objProd));
    }
}
