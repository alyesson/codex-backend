package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.SerieNfe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerieNfeRepository extends JpaRepository<SerieNfe, Long> {

    Optional<SerieNfe> findByNumeroSerieAndCnpjAndAmbiente(String numeroSerie, String cnpj, String ambiente);
}
