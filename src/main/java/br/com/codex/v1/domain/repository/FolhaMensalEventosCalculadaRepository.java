package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaMensalEventosCalculada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface FolhaMensalEventosCalculadaRepository extends JpaRepository<FolhaMensalEventosCalculada, Long> {

    /**
     * Calcula a média dos vencimentos das Horas Extras 50,70 e 100% sobre o salário
     * dos últimos 6 meses para uma determinada matrícula e evento.
     */
    @Query("SELECT COALESCE(AVG(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :numeroEvento " +
            "AND f.folhaMensalCalculada.dataProcessamento >= :dataLimite")
    BigDecimal findMediaHorasExtrasUltimosSeisMeses(@Param("matricula") String matricula, @Param("numeroEvento") Integer numeroEvento, @Param("dataLimite") LocalDate dataLimite);

    @Query("SELECT COALESCE(SUM(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f " +
                "WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano " +
                "AND MONTH(f.folhaMensalCalculada.dataProcessamento) = :mes")
    BigDecimal findSomaHorasExtrasPorMesEAno(@Param("matricula") String matricula,@Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT COALESCE(AVG(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f " +
                "WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano " +
                "AND MONTH(f.folhaMensalCalculada.dataProcessamento) BETWEEN 1 AND :mes")
    BigDecimal findMediaHorasExtrasAteMes(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT COALESCE(SUM(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula " +
            "AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(f.folhaMensalCalculada.dataProcessamento) = :mes")
    BigDecimal findSomaEventoPorMesEAno(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT f.vencimentos FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula " +
            "AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano ORDER BY f.folhaMensalCalculada.dataProcessamento DESC")
    BigDecimal findUltimoValorInsalubridade(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano);

    @Query("SELECT f.vencimentos FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula " +
            "AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano ORDER BY f.folhaMensalCalculada.dataProcessamento DESC")
    BigDecimal findUltimoValorPericulosidade(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano);

    @Query("SELECT COALESCE(SUM(f.referencia), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula " +
            "AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano")
    BigDecimal findSomaQuantidadeHorasExtrasAno(@Param("matricula") String matricula,  @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano);
}
