package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TabelaImpostoRendaRepository extends JpaRepository<TabelaImpostoRenda, Long> {

    Optional<TabelaImpostoRenda> findTopByOrderById();
}