package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.ControleNsu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ControleNsuRepository extends JpaRepository<ControleNsu, Long> {

    Optional<ControleNsu> findByCnpjAndAmbiente(String cnpj, String ambiente);
}
