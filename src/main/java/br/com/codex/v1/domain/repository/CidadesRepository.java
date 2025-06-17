package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.cadastros.Cidades;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CidadesRepository extends JpaRepository<Cidades, Long> {

    Optional<Cidades>findByCodigoUf(Integer codigo);
}
