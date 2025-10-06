package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {

    List<RegistroPonto> findByPisAndDataBetween(String pis, LocalDate dataInicio, LocalDate dataFim);

    List<RegistroPonto> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

    List<RegistroPonto> findByLoteImportacao(String loteImportacao);

    @Query("SELECT r FROM RegistroPonto r WHERE r.colaborador.id = :colaboradorId AND r.data BETWEEN :dataInicio AND :dataFim ORDER BY r.data, r.hora")
    List<RegistroPonto> findByColaboradorAndPeriodo(@Param("colaboradorId") Long colaboradorId, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT COUNT(r) FROM RegistroPonto r WHERE r.loteImportacao = :loteImportacao")
    Long countByLoteImportacao(@Param("loteImportacao") String loteImportacao);

    @Modifying
    @Query("DELETE FROM RegistroPonto r WHERE r.loteImportacao = :loteImportacao")
    void deleteByLoteImportacao(@Param("loteImportacao") String loteImportacao);
}
