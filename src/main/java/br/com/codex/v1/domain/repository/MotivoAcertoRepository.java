package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.estoque.MotivoAcerto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MotivoAcertoRepository extends JpaRepository<MotivoAcerto, Long> {

    Optional<MotivoAcerto> findByCodigo(String codigo);
}
