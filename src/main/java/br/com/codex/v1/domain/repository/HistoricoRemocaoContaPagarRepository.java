package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.financeiro.HistoricoRemocaoContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoRemocaoContaPagarRepository extends JpaRepository<HistoricoRemocaoContaPagar, Long> {
}
