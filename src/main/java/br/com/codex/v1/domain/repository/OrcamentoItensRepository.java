package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.vendas.OrcamentoItens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrcamentoItensRepository extends JpaRepository<OrcamentoItens, Long> {

    List<OrcamentoItens> findByOrcamentoId(Long orcamentoId);
}
