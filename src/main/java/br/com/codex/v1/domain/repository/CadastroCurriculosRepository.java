package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.CadastroCurriculos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CadastroCurriculosRepository extends JpaRepository<CadastroCurriculos, Long> {

    Optional<CadastroCurriculos> findByContato(String contato);
}
