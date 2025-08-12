package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.vendas.OrcamentoItens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrcamentoItensRepository extends JpaRepository<OrcamentoItens, Long> {

    List<OrcamentoItens> findByOrcamentoId(Long orcamentoId);
}
