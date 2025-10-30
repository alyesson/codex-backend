package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaFerias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FolhaFeriasRepository extends JpaRepository<FolhaFerias, Long> {

    Optional<FolhaFerias> findByNumeroMatricula(String matricula);
}
