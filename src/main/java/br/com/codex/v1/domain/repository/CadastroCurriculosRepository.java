package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroCurriculos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CadastroCurriculosRepository extends JpaRepository<CadastroCurriculos, Integer> {

    Optional<CadastroCurriculos> findByContato(String contato);
}
