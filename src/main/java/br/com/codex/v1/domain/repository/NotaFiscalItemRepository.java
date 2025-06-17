package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.NotaFiscalItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaFiscalItemRepository extends JpaRepository<NotaFiscalItem, Long> {
}
