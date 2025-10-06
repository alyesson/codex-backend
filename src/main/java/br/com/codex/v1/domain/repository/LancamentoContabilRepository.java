package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import br.com.codex.v1.domain.fiscal.ImportarXml;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancamentoContabilRepository extends JpaRepository<LancamentoContabil, Long> {

    @Query("SELECT l FROM LancamentoContabil l WHERE YEAR(l.dataLancamento) = :ano AND MONTH(l.dataLancamento) = :mes")
    List<LancamentoContabil> findAllByYearAndMonth(@Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT l FROM LancamentoContabil l WHERE l.dataLancamento BETWEEN :dataInicio AND :dataFim")
    List<LancamentoContabil> findAllByYearRange(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    List<LancamentoContabil> findByNotaFiscalOrigem(ImportarXml nota);
}
