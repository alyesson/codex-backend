package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.AtivoImobilizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtivoImobilizadoRepository extends JpaRepository<AtivoImobilizado, Long> {

    Optional<AtivoImobilizado> findByCodigoBem(String codigo);

    Optional<AtivoImobilizado> findByNumeroSerie(String numeroSerie);
}
