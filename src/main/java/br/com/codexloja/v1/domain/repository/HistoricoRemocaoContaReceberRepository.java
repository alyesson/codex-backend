package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.financeiro.HistoricoRemocaoContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRemocaoContaReceberRepository extends JpaRepository<HistoricoRemocaoContaReceber, Integer> {
}
