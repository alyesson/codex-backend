package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.dto.TotaisFolhaPontoDto;
import br.com.codex.v1.domain.rh.EspelhoPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EspelhoPontoRepository extends JpaRepository<EspelhoPonto, Long> {

    // Buscar por colaborador e período
    @Query("SELECT e FROM EspelhoPonto e WHERE e.colaborador.id = :colaboradorId AND e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.data")
    List<EspelhoPonto> findByColaboradorAndPeriodo(@Param("colaboradorId") Long colaboradorId, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    // Buscar por PIS e período
    @Query("SELECT e FROM EspelhoPonto e WHERE e.colaborador.numeroPis = :pis AND e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.data")
    List<EspelhoPonto> findByPisAndPeriodo(@Param("pis") String pis, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    // Buscar por período (todos os colaboradores)
    @Query("SELECT e FROM EspelhoPonto e WHERE e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.colaborador.nomeColaborador, e.data")
    List<EspelhoPonto> findByPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    // Buscar por departamento e período
    @Query("SELECT e FROM EspelhoPonto e WHERE e.colaborador.nomeDepartamento = :departamento AND e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.colaborador.nomeColaborador, e.data")
    List<EspelhoPonto> findByDepartamentoAndPeriodo(@Param("departamento") String departamento, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    // Verificar se já existe espelho para um colaborador na data
    @Query("SELECT COUNT(e) > 0 FROM EspelhoPonto e WHERE e.colaborador.id = :colaboradorId AND e.data = :data")
    boolean existsByColaboradorAndData(@Param("colaboradorId") Long colaboradorId, @Param("data") LocalDate data);

    // Buscar totais por colaborador no período (para folha ponto)
    @Query("SELECT new br.com.codex.v1.domain.dto.TotaisFolhaPontoDto(" +
            "e.colaborador.id, e.colaborador.nomeColaborador, e.colaborador.numeroPis, " +
            "SUM(e.horasTrabalhadasMinutos), SUM(e.horasExtrasMinutos), SUM(e.horasFaltantesMinutos), " +
            "SUM(e.custoHorasExtras)) " +
            "FROM EspelhoPonto e " +
            "WHERE e.data BETWEEN :dataInicio AND :dataFim " +
            "GROUP BY e.colaborador.id, e.colaborador.nomeColaborador, e.colaborador.numeroPis")
    List<TotaisFolhaPontoDto> findTotaisPorColaboradorNoPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
