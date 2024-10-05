package br.com.codex.v1.service;

import br.com.codex.v1.domain.estoque.EntradaMaterial;
import br.com.codex.v1.domain.estoque.Produto;
import br.com.codex.v1.domain.dto.EntradaMaterialDto;
import br.com.codex.v1.domain.repository.EntradaMaterialRepository;
import br.com.codex.v1.domain.repository.ProdutoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class EntradaMaterialService {

    @Autowired
    private EntradaMaterialRepository entradaMaterialRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public EntradaMaterial create(EntradaMaterialDto entradaMaterialDto) {
        Optional<Produto> objProduto = produtoRepository.findByCodigo(entradaMaterialDto.getCodigoProduto());

        Produto produto = objProduto.get();
        if (entradaMaterialDto.getQuantidade() > produto.getMaximo()) {
            throw new DataIntegrityViolationException("Quantidade máxima no estoque para este produto é de " + produto.getMaximo() + " unidades");
        }

        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataFormatada = dataAtual.format(formatter);
        entradaMaterialDto.setDataEntrada(java.sql.Date.valueOf(dataFormatada));

        entradaMaterialDto.setId(null);
        validarEntradaMaterial(entradaMaterialDto);
        EntradaMaterial objEntra = new EntradaMaterial(entradaMaterialDto);
        return entradaMaterialRepository.save(objEntra);
    }

    public EntradaMaterial findById(Integer id) {
        Optional<EntradaMaterial> objEntrada = entradaMaterialRepository.findById(id);
        return objEntrada.orElseThrow(() -> new ObjectNotFoundException("Material não encontrado"));
    }

    public void delete(Integer id) {
        entradaMaterialRepository.deleteById(id);
    }

    public List<EntradaMaterial> findAll() {
        return entradaMaterialRepository.findAll();
    }

    public List<EntradaMaterial> findAllByProdutoEstoque() {
        return entradaMaterialRepository.findAllByProdutoEstoque();
    }

    public void validarEntradaMaterial(EntradaMaterialDto entradaMaterialDto) {
        boolean entradaExistente = entradaMaterialRepository.existsByNotaFiscalAndCodigoProduto(
                entradaMaterialDto.getNotaFiscal(), entradaMaterialDto.getCodigoProduto());
        if (entradaExistente) {
            throw new DataIntegrityViolationException("Já foi realizada uma entrada deste produto para esta nota fiscal.");
        }
    }

    public EntradaMaterial ajustaSaldo(String codigoProduto, String lote, int quantidade) {
        Optional<EntradaMaterial> objEntra = entradaMaterialRepository.findByCodigoProdutoAndLote(codigoProduto, lote);
        Optional<Produto> objProduto = produtoRepository.findByCodigo(codigoProduto);

        if (objEntra.isPresent() && objProduto.isPresent()) {
            EntradaMaterial entradaMaterial = objEntra.get();
            Produto produto = objProduto.get();

            if (quantidade > produto.getMaximo()) {
                throw new DataIntegrityViolationException("Quantidade a ser ajustada não pode ser maior do que " + produto.getMaximo());
            }

            int novoSaldo = objEntra.get().getQuantidade() + quantidade;
            entradaMaterial.setQuantidade(novoSaldo);
            return entradaMaterialRepository.save(entradaMaterial);
        } else {
            throw new DataIntegrityViolationException("Produto com lote '" + lote + "' não encontrado");
        }
    }

    private Produto findByCodigoProduto(String codigoProduto) {
        Optional<Produto> codigo = produtoRepository.findByCodigo(codigoProduto);
        return codigo.orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado com esse código"));
    }

    public List<EntradaMaterial> findAllEntradaPeriodo(Date dataInicial, Date dataFinal) {
        return entradaMaterialRepository.findAllEntradaPeriodo(dataInicial, dataFinal);
    }
}
