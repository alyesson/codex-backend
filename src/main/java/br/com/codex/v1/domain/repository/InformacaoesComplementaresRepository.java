package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.InformacaoesComplementares;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InformacaoesComplementaresRepository extends JpaRepository<InformacaoesComplementares, Long> {

    Optional<InformacaoesComplementares> findByCodigo(String codigo);
}
