package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.SubGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubGrupoRepository extends JpaRepository<SubGrupo, Integer> {
    Optional<SubGrupo> findByCodigoSubGrupo(String codigoSubGrupo);
}
