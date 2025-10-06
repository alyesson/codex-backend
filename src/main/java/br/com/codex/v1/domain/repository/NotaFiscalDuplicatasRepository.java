package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.NotaFiscalDuplicatas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaFiscalDuplicatasRepository extends JpaRepository<NotaFiscalDuplicatas, Long> {
}
