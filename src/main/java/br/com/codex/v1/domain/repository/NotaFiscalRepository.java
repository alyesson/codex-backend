package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {
}
