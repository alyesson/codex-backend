package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroCargos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CadastroCargosRepository extends JpaRepository<CadastroCargos, Long> {

    Optional<CadastroCargos> findByCodigoCargo(String codigoCargo);
}
