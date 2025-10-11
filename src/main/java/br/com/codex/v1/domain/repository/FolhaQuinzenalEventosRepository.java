package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaQuinzenalEventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolhaQuinzenalEventosRepository extends JpaRepository<FolhaQuinzenalEventos, Long> {

    List<FolhaQuinzenalEventos> findAllEventosByFolhaQuinzenalId(Long eventoId);

    void deleteByFolhaQuinzenalId(Long id);
}
