package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.ControleNsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Optional;

public interface ControleNsuRepository extends JpaRepository<ControleNsu, Long> {

    Optional<ControleNsu> findByCnpjAndAmbiente(String cnpj, String ambiente);

    @Query("SELECT MAX(c.ultimoNsu) FROM ControleNsu c WHERE c.cnpj = :cnpj")
    Optional<Long> findUltimoNsuByCnpj(@Param("cnpj") String cnpj);
}
