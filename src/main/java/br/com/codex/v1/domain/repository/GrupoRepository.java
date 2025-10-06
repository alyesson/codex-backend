package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    Optional<Grupo> findByCodigo(String codigo);
}
