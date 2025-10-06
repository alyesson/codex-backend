package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.InformacaoesComplementares;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformacaoesComplementaresRepository extends JpaRepository<InformacaoesComplementares, Long> {

    Optional<InformacaoesComplementares> findByCodigo(String codigo);
}
