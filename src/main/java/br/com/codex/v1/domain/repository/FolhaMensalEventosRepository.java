package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaMensalEventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolhaMensalEventosRepository extends JpaRepository<FolhaMensalEventos, Long> {

    void deleteByFolhaMensalId(Long id);

    List<FolhaMensalEventos> findAllEventosByFolhaMensalId(Long eventoId);
}
