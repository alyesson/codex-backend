package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaRescisaoEventos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolhaRescisaoEventosRepository extends JpaRepository<FolhaRescisaoEventos, Long> {

    void deleteByFolhaRescisaoId(Long id);

    List<FolhaRescisaoEventos> findAllEventosByFolhaRescisaoId(Long eventoId);
}
