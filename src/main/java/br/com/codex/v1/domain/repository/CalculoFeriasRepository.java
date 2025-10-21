package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CalculoFerias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalculoFeriasRepository extends JpaRepository<CalculoFerias, Long> {

    Optional<CalculoFerias> findByNumeroMatricula(String matricula);
}
