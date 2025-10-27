package br.com.codex.v1.service.rh.horarios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
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
public class CalcularHorasRepousoRemuneradoDiurnoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    Calendario calendario = new Calendario();

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal>calcularHorasRepousoRemuneradoDiurno(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            LocalTime horaIniHRDiurno = folha.getHoraEntrada();
            LocalTime horaFimHRDiurno = folha.getHoraSaida();

            LocalTime hora22HRDiurno = LocalTime.parse("22:00");
            LocalTime hora13HRDiurno = LocalTime.parse("13:00");

            // ✅ 1. CALCULAR HORAS DIURNAS (lógica simplificada)
            Duration horasTrabalhadasDiurnas;

            if (horaFimHRDiurno.isBefore(hora13HRDiurno)) {
                // Turno totalmente diurno (termina antes das 13h)
                horasTrabalhadasDiurnas = Duration.between(horaIniHRDiurno, horaFimHRDiurno);
            } else if (horaFimHRDiurno.isBefore(hora22HRDiurno)) {
                // Turno diurno (termina antes das 22h)
                horasTrabalhadasDiurnas = Duration.between(horaIniHRDiurno, horaFimHRDiurno);
            } else {
                // Turno que ultrapassa as 22h - considera apenas até 22h
                horasTrabalhadasDiurnas = Duration.between(horaIniHRDiurno, hora22HRDiurno);
            }

            // ✅ 2. SUBTRAIR HORÁRIO DE ALMOÇO (1 hora)
            horasTrabalhadasDiurnas = horasTrabalhadasDiurnas.minusHours(1);

            // Converter para horas decimais (ex: 7.5 horas)
            double horasDiurnasDecimais = horasTrabalhadasDiurnas.toHours() +
                    horasTrabalhadasDiurnas.toMinutesPart() / 60.0;

            BigDecimal horasDiurnasPorDia = BigDecimal.valueOf(horasDiurnasDecimais)
                    .setScale(2, RoundingMode.HALF_UP);

            // ✅ 3. CALCULAR DIAS DE REPOUSO (domingos + feriados) - UMA ÚNICA VEZ
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();
            int diasRepouso = 0;

            YearMonth anoMes = YearMonth.of(year, month);
            Set<LocalDate> feriados = new HashSet<>();
            feriados.addAll(calendario.getFeriadosFixos(year));
            feriados.addAll(calendario.getFeriadosMoveis(year));

            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                LocalDate data = anoMes.atDay(dia);
                if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                    diasRepouso++;
                }
            }

            // ✅ 4. CÁLCULO FINAL DO DSR
            BigDecimal totalHorasDSR = horasDiurnasPorDia.multiply(new BigDecimal(diasRepouso))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal valorDSR = totalHorasDSR.multiply(salarioPorHora)
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", totalHorasDSR);     // Total de horas no DSR
            resultado.put("vencimentos", valorDSR);         // Valor em R$
            resultado.put("descontos", BigDecimal.ZERO);

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular DSR Diurno: " + e.getMessage());
        }
    }
}
