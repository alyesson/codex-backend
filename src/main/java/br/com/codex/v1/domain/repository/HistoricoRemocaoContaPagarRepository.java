package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.financeiro.HistoricoRemocaoContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRemocaoContaPagarRepository extends JpaRepository<HistoricoRemocaoContaPagar, Integer> {
}
