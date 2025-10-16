package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaMensal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolhaMensalRepository extends JpaRepository<FolhaMensal, Long> {

    Optional<FolhaMensal>findByMatriculaColaborador(String numeroMatricula);
}
