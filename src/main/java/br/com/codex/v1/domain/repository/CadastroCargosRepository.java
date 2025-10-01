package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroCargos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CadastroCargosRepository extends JpaRepository<CadastroCargos, Long> {

    Optional<CadastroCargos> findByCodigoCargo(String codigoCargo);
}
