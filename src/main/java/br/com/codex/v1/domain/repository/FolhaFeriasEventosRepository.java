package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaFeriasEventos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolhaFeriasEventosRepository extends JpaRepository<FolhaFeriasEventos, Long> {

    void deleteByFolhaFeriasId(Long id);

    List<FolhaFeriasEventos> findAllEventosByFolhaFeriasId(Long eventoId);
}
