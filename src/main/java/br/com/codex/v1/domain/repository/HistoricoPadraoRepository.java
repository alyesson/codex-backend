package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.HistoricoPadrao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoricoPadraoRepository extends JpaRepository<HistoricoPadrao, Integer> {

    Optional<HistoricoPadrao> findByCodigo(String codigo);

    HistoricoPadrao findByDescricaoContaining(String vendaDeMercadorias);
}
