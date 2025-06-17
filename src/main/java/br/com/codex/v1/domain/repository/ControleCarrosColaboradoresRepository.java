package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.ControleCarrosColaboradores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ControleCarrosColaboradoresRepository extends JpaRepository<ControleCarrosColaboradores, Long> {

    Optional<ControleCarrosColaboradores>findByNomeColaborador(String nomeColaborador);

    Optional<ControleCarrosColaboradores>findByPlacaVeiculo(String placaVeiculo);
}
