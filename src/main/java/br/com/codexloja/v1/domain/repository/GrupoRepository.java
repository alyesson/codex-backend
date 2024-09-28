package br.com.codexloja.v1.domain.repository;

import br.com.codexloja.v1.domain.estoque.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoRepository extends JpaRepository<Grupo, Integer> {
    Optional<Grupo> findByCodigo(String codigo);
}
