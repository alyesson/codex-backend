package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.RelatorioHorasExtras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatorioHorasExtrasRepository extends JpaRepository<RelatorioHorasExtras, Long> {
}
