package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaMensalCalculada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolhaMensalCalculadaRepository extends JpaRepository<FolhaMensalCalculada, Long> {

    Optional<FolhaMensalCalculada>findByMatriculaColaborador(String numeroMatricula);
}
