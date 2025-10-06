package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.AvaliacaoFornecedoresDetalhes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoFornecedoresItensRepository extends JpaRepository<AvaliacaoFornecedoresDetalhes, Long> {

    List<AvaliacaoFornecedoresDetalhes> findByAvaliacaoFornecedoresId(Long fornecedorId);
}
