package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.fiscal.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {

    @Query("SELECT c FROM NotaFiscal c WHERE YEAR(c.emissao) = :ano and MONTH(c.emissao) = :mes AND c.documentoEmitente=:documentoEmitente")
    List<NotaFiscal> consultarNotasMesCorrente(@Param("ano") Integer ano, @Param("mes") Integer mes, @Param("documentoEmitente") String documentoEmitente);

    @Query("SELECT c FROM NotaFiscal c WHERE YEAR(c.emissao) = :ano")
    List<NotaFiscal> consultarNotasAnoCorrente(@Param("ano") Integer ano);

    @Query("SELECT a FROM NotaFiscal a WHERE a.emissao BETWEEN :dataInicial AND :dataFinal AND a.documentoEmitente=:documentoEmitente")
    List<NotaFiscal> consultarNotasPorPeriodo(@Param("dataInicial") LocalDate dataInicial, @Param("dataFinal") LocalDate dataFinal, @Param("documentoEmitente") String documentoEmitente);

    @Query("SELECT DISTINCT n.cfop FROM NotaFiscalItem n JOIN n.notaFiscal nf WHERE nf.emissao BETWEEN :dataInicio AND :dataFim")
    Set<Integer> findDistinctCfopSaidaByPeriodo(@Param("dataInicio") LocalDate inicio, @Param("dataFim") LocalDate fim);
}
