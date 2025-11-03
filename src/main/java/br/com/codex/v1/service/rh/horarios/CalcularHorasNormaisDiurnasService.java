package br.com.codex.v1.service.rh.horarios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.utilitario.Calendario;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CalcularHorasNormaisDiurnasService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    Calendario calendario = new Calendario();

    @Setter
    String numeroMatricula;
    BigDecimal valorReferenteHoraDiurna;

    public Map<String, BigDecimal>  calculaHorasNormaisDiurnas(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);

            assert folha != null;
            LocalTime horaIni = folha.getHoraEntrada();
            LocalTime horaFim = folha.getHoraSaida();
            BigDecimal salarioPorHora = folha.getSalarioHora();

            LocalTime hora22 = LocalTime.parse("22:00");
            LocalTime hora13 = LocalTime.parse("13:00");

            // ✅ 1. CALCULAR HORAS DIURNAS TRABALHADAS POR DIA
            Duration horasTrabalhadas;

            if (horaFim.isBefore(hora13)) {
                // Turno matutino (termina antes das 13h)
                horasTrabalhadas = Duration.between(horaIni, horaFim);
            } else if (horaFim.isBefore(hora22)) {
                // Turno diurno normal (termina antes das 22h)
                horasTrabalhadas = Duration.between(horaIni, horaFim);
            } else {
                // Turno que ultrapassa às 22h - considera apenas até 22h para horas diurnas
                horasTrabalhadas = Duration.between(horaIni, hora22);
            }

            // ✅ 2. SUBTRAIR HORÁRIO DE ALMOÇO (1 hora) e converter para decimal
            horasTrabalhadas = horasTrabalhadas.minusHours(1);

            double horasDecimais = horasTrabalhadas.toHours() + horasTrabalhadas.toMinutesPart() / 60.0;

            BigDecimal horasPorDia = BigDecimal.valueOf(horasDecimais).setScale(2, RoundingMode.HALF_UP);

            // ✅ 3. CALCULAR DIAS ÚTEIS NO MÊS - UMA ÚNICA VEZ
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();
            int diasUteis = 0;

            YearMonth anoMes = YearMonth.of(year, month);
            Set<LocalDate> feriados = new HashSet<>();
            feriados.addAll(calendario.getFeriadosFixos(year));
            feriados.addAll(calendario.getFeriadosMoveis(year));

            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                LocalDate data = anoMes.atDay(dia);
                if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                    diasUteis++;
                }
            }

            // ✅ 4. CÁLCULO FINAL DAS HORAS DIURNAS
            BigDecimal totalHorasMes = horasPorDia.multiply(new BigDecimal(diasUteis)).setScale(2, RoundingMode.HALF_UP);

            valorReferenteHoraDiurna = totalHorasMes.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", totalHorasMes);     // Total de horas no mês
            resultado.put("vencimentos", valorReferenteHoraDiurna);       // Valor total em R$
            resultado.put("descontos", BigDecimal.ZERO);

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular horas diurnas: " + e.getMessage());
        }
    }
}
