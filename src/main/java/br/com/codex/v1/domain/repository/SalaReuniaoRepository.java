package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.SalaReuniao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaReuniaoRepository extends JpaRepository<SalaReuniao, Long> {
}
