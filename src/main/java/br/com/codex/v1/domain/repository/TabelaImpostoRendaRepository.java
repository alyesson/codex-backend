package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TabelaImpostoRendaRepository extends JpaRepository<TabelaImpostoRenda, Long> {

    Optional<TabelaImpostoRenda> findTopByOrderById();
}