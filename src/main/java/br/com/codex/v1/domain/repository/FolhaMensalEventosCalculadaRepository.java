package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaMensalEventosCalculada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FolhaMensalEventosCalculadaRepository extends JpaRepository<FolhaMensalEventosCalculada, Long> {

    @Query("SELECT COALESCE(AVG(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :numeroEvento AND f.folhaMensalCalculada.dataProcessamento >= :dataLimite")
    BigDecimal findMediaHorasExtrasUltimosSeisMeses(@Param("matricula") String matricula, @Param("numeroEvento") Integer numeroEvento, @Param("dataLimite") LocalDate dataLimite);

    @Query("SELECT COALESCE(SUM(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(f.folhaMensalCalculada.dataProcessamento) = :mes")
    BigDecimal findSomaHorasExtrasPorMesEAno(@Param("matricula") String matricula,@Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT COALESCE(AVG(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(f.folhaMensalCalculada.dataProcessamento) BETWEEN 1 AND :mes")
    BigDecimal findMediaHorasExtrasAteMes(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT COALESCE(SUM(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(f.folhaMensalCalculada.dataProcessamento) = :mes")
    BigDecimal findSomaEventoPorMesEAno(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano, @Param("mes") Integer mes);

    @Query("SELECT f.vencimentos FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano ORDER BY f.folhaMensalCalculada.dataProcessamento DESC")
    BigDecimal findUltimoValorInsalubridade(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano);

    @Query("SELECT f.vencimentos FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano ORDER BY f.folhaMensalCalculada.dataProcessamento DESC")
    BigDecimal findUltimoValorPericulosidade(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano);

    @Query("SELECT COALESCE(SUM(f.referencia), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano")
    BigDecimal findSomaQuantidadeHorasExtrasAno(@Param("matricula") String matricula,  @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano);

    @Query("SELECT COALESCE(SUM(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND YEAR(f.folhaMensalCalculada.dataProcessamento) = :ano")
    BigDecimal findSomaValorHorasExtrasAno(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("ano") Integer ano);

    @Query("SELECT COALESCE(SUM(fmec.vencimentos), 0) FROM FolhaMensalEventosCalculada fmec WHERE fmec.folhaMensalCalculada.matriculaColaborador = :matricula AND YEAR(fmec.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(fmec.folhaMensalCalculada.dataProcessamento) = :mes AND fmec.codigoEvento IN ('51')")
    BigDecimal findSomaComissoesPorMesEAno(@Param("matricula") String matricula, @Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(fmec.referencia), 0) FROM FolhaMensalEventosCalculada fmec WHERE fmec.folhaMensalCalculada.matriculaColaborador = :matricula AND YEAR(fmec.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(fmec.folhaMensalCalculada.dataProcessamento) = :mes AND fmec.codigoEvento = '98'") // Código HE 50%
    BigDecimal findQuantidadeHorasExtras50PorMesEAno(@Param("matricula") String matricula, @Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(fmec.referencia), 0) FROM FolhaMensalEventosCalculada fmec WHERE fmec.folhaMensalCalculada.matriculaColaborador = :matricula AND YEAR(fmec.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(fmec.folhaMensalCalculada.dataProcessamento) = :mes AND fmec.codigoEvento = '99'") // Código HE 70%
    BigDecimal findQuantidadeHorasExtras70PorMesEAno(@Param("matricula") String matricula, @Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(fmec.referencia), 0) FROM FolhaMensalEventosCalculada fmec WHERE fmec.folhaMensalCalculada.matriculaColaborador = :matricula AND YEAR(fmec.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(fmec.folhaMensalCalculada.dataProcessamento) = :mes AND fmec.codigoEvento = '100'") // Código HE 70%
    BigDecimal findQuantidadeHorasExtras100PorMesEAno(@Param("matricula") String matricula, @Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(fmec.vencimentos), 0) FROM FolhaMensalEventosCalculada fmec WHERE fmec.folhaMensalCalculada.matriculaColaborador = :matricula AND YEAR(fmec.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(fmec.folhaMensalCalculada.dataProcessamento) = :mes AND fmec.codigoEvento = '5'") // Código DSR Diurno
    BigDecimal findDSRDiurnoPorMesEAno(@Param("matricula") String matricula, @Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(fmec.vencimentos), 0) FROM FolhaMensalEventosCalculada fmec WHERE fmec.folhaMensalCalculada.matriculaColaborador = :matricula AND YEAR(fmec.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(fmec.folhaMensalCalculada.dataProcessamento) = :mes AND fmec.codigoEvento = '25'") // Código DSR Noturno
    BigDecimal findDSRNoturnoPorMesEAno(@Param("matricula") String matricula, @Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(fmec.vencimentos), 0) FROM FolhaMensalEventosCalculada fmec WHERE fmec.folhaMensalCalculada.matriculaColaborador = :matricula AND YEAR(fmec.folhaMensalCalculada.dataProcessamento) = :ano AND MONTH(fmec.folhaMensalCalculada.dataProcessamento) = :mes AND fmec.codigoEvento = '14'") // Código Adicional Noturno
    BigDecimal findAdicionalNoturnoPorMesEAno(@Param("matricula") String matricula,  @Param("ano") int ano, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(f.referencia), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento =:codigoEvento AND f.folhaMensalCalculada.dataProcessamento BETWEEN :dataInicio AND :dataFim")
    BigDecimal findTotalHorasExtras50Periodo(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(f.referencia), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento =:codigoEvento AND f.folhaMensalCalculada.dataProcessamento BETWEEN :dataInicio AND :dataFim")
    BigDecimal findTotalHorasExtras70Periodo(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(f.referencia), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento =:codigoEvento AND f.folhaMensalCalculada.dataProcessamento BETWEEN :dataInicio AND :dataFim")
    BigDecimal findTotalHorasExtras100Periodo(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND f.folhaMensalCalculada.dataProcessamento BETWEEN :dataInicio AND :dataFim")
    BigDecimal findSomaInsalubridadePeriodo(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
    void deleteByFolhaMensalCalculadaId(Long id);

    @Query("SELECT COALESCE(SUM(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND f.folhaMensalCalculada.dataProcessamento BETWEEN :dataInicio AND :dataFim")
    BigDecimal findSomaPericulosidadePeriodo(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    @Query("SELECT COALESCE(SUM(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :codigoEvento AND f.folhaMensalCalculada.dataProcessamento BETWEEN :dataInicio AND :dataFim")
    BigDecimal findSomaAdicionalNoturnoPeriodo(@Param("matricula") String matricula, @Param("codigoEvento") Integer codigoEvento, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    List<FolhaMensalEventosCalculada> findAllEventosByFolhaMensalCalculadaId(Long eventoId);
}
