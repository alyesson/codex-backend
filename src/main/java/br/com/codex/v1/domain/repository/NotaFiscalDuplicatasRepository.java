package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.NotaFiscalDuplicatas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaFiscalDuplicatasRepository extends JpaRepository<NotaFiscalDuplicatas, Long> {
}
