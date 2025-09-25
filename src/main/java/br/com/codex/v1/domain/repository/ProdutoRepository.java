package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByCodigo(String codigo);

    Optional<Produto> findByDescricao(String produto);

    @Query("SELECT DISTINCT p.unidadeComercial FROM Produto p WHERE p.unidadeComercial IS NOT NULL")
    List<String> findByUnidadeComercial();

    @Query("SELECT p.contaContabil FROM Produto p WHERE p.codigo IS NOT NULL AND p.codigo <> ''")
    List<Produto> findByContaContabil();
}
