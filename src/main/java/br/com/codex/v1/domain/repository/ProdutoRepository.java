package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    Optional<Produto> findByCodigo(String codigo);

    Optional<Produto> findByDescricao(String produto);
}
