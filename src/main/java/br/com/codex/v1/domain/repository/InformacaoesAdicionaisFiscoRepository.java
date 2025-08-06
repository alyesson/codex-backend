package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.InformacaoesAdicionaisFisco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InformacaoesAdicionaisFiscoRepository extends JpaRepository<InformacaoesAdicionaisFisco, Long> {

    Optional<InformacaoesAdicionaisFisco> findByCodigo(String codigo);
}
