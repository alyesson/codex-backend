package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.compras.AvaliacaoFornecedoresDetalhes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoFornecedoresItensRepository extends JpaRepository<AvaliacaoFornecedoresDetalhes, Long> {

    List<AvaliacaoFornecedoresDetalhes> findByAvaliacaoFornecedoresId(Long fornecedorId);
}
