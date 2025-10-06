package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.ti.TiposCategorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiposCategoriasRepository extends JpaRepository<TiposCategorias, Long> {
}
