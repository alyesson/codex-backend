package br.com.codex.v1.domain.repository;

import br.com.codex.v1.domain.rh.FolhaMensalEventos;
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
     * Calcula a média do valorProvento das Horas Extras 50,70 e 100% sobre o salário
     * dos últimos 6 meses para uma determinada matrícula e evento.
     */
    @Query("SELECT COALESCE(AVG(f.vencimentos), 0) FROM FolhaMensalEventosCalculada f WHERE f.folhaMensalCalculada.matriculaColaborador = :matricula AND f.codigoEvento = :numeroEvento AND f.folhaMensalCalculada.dataProcessamento >= :dataLimite")
    BigDecimal findMediaHorasExtrasUltimosSeisMeses(@Param("matricula") String matricula, @Param("numeroEvento") Integer numeroEvento, @Param("dataLimite") LocalDate dataLimite);

}
