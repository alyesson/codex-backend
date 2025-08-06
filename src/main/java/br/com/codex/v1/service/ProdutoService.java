package br.com.codex.v1.service;

import br.com.codex.v1.domain.estoque.Produto;
import br.com.codex.v1.domain.dto.ProdutoDto;
import br.com.codex.v1.domain.repository.ProdutoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    public Produto create(ProdutoDto produtoDto) {
        produtoDto.setId(null);
        validaProduto(produtoDto);
        Produto grupo = new Produto(produtoDto);
        return produtoRepository.save(grupo);
    }

    public Produto update(Long id, ProdutoDto produtoDto) {
        produtoDto.setId(id);
        Produto obj = findById(id);
        obj = new Produto(produtoDto);
        return produtoRepository.save(obj);
    }

    public Produto findById(Long id) {
        Optional<Produto> obj = produtoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado"));
    }

    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }

    public List<Produto> findAll(){
        return produtoRepository.findAll();
    }

    private void validaProduto(ProdutoDto produtoDto){
        Optional<Produto> obj = produtoRepository.findByCodigo(produtoDto.getCodigo());
        if(obj.isPresent() && obj.get().getCodigo().equals(produtoDto.getCodigo())){
            throw new DataIntegrityViolationException("Este produto já existe");
        }
    }

    public Produto findByDescricao(String produto) {
        Optional<Produto> objProd = produtoRepository.findByDescricao(produto);
        return objProd.orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado"));
    }

    public Produto findByCodigo(String codigoProduto){
        Optional<Produto> obj = produtoRepository.findByCodigo(codigoProduto);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado com o código "+codigoProduto));
    }

    public List<String> findByUnidadeComercial(){
        return produtoRepository.findByUnidadeComercial();
    }
}
