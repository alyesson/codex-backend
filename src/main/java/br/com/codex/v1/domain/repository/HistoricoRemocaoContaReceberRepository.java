package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.financeiro.HistoricoRemocaoContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoRemocaoContaReceberRepository extends JpaRepository<HistoricoRemocaoContaReceber, Long> {
}
