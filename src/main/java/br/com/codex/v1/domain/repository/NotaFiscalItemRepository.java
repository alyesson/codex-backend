package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.NotaFiscalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaFiscalItemRepository extends JpaRepository<NotaFiscalItem, Long> {
}
