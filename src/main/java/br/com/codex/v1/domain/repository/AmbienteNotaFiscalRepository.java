package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbienteNotaFiscalRepository extends JpaRepository<AmbienteNotaFiscal, Long> {
}
