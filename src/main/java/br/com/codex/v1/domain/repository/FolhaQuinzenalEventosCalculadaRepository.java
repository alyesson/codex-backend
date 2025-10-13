package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaQuinzenalEventos;
import br.com.codex.v1.domain.rh.FolhaQuinzenalEventosCalculada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolhaQuinzenalEventosCalculadaRepository extends JpaRepository<FolhaQuinzenalEventosCalculada, Long> {

    List<FolhaQuinzenalEventosCalculada> findAllEventosByFolhaQuinzenalCalculadaId(Long eventoId);

    void deleteByFolhaQuinzenalCalculadaId(Long id);
}
