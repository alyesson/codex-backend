package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.MotivoAcerto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotivoAcertoRepository extends JpaRepository<MotivoAcerto, Integer> {

    Optional<MotivoAcerto> findByCodigo(String codigo);
}
