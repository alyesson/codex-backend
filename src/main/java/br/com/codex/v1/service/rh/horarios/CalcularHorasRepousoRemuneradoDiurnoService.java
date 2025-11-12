package br.com.codex.v1.service.rh.horarios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.service.rh.CalculosAuxiliaresFolha;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularHorasRepousoRemuneradoDiurnoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private CalculosAuxiliaresFolha calculosAuxiliaresFolha;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularHorasRepousoRemuneradoDiurno() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            LocalTime horaIni = folha.getHoraEntrada();
            LocalTime horaFim = folha.getHoraSaida();

            // ✅ 1. CALCULAR HORAS DIURNAS TRABALHADAS POR DIA
            BigDecimal horasDiurnasPorDia = calcularHorasDiurnasPorDia(horaIni, horaFim);

            // ✅ 2. CALCULAR DIAS DE REPOUSO (domingos + feriados)
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();
            int diasRepouso = calculosAuxiliaresFolha.calcularDiasRepousoNoMes(year, month);

            // ✅ 3. CÁLCULO FINAL DO DSR DIURNO
            BigDecimal totalHorasDSRDiurno = horasDiurnasPorDia.multiply(new BigDecimal(diasRepouso))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal valorDSRDiurno = totalHorasDSRDiurno.multiply(salarioPorHora)
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", totalHorasDSRDiurno);     // Total de horas no DSR Diurno
            resultado.put("vencimentos", valorDSRDiurno);         // Valor em R$
            resultado.put("descontos", BigDecimal.ZERO);

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular DSR Diurno: " + e.getMessage());
        }
    }

    /**
     * ✅ CALCULA HORAS DIURNAS POR DIA (período das 05h às 22h)
     */
    private BigDecimal calcularHorasDiurnasPorDia(LocalTime horaIni, LocalTime horaFim) {
        LocalTime hora05 = LocalTime.of(5, 0);   // Início do período diurno
        LocalTime hora22 = LocalTime.of(22, 0);  // Fim do período diurno

        long minutosDiurnos = 0;

        // ✅ CASO 1: Jornada normal (não cruza meia-noite)
        if (!horaFim.isBefore(horaIni)) {
            minutosDiurnos = calcularMinutosDiurnosJornadaNormal(horaIni, horaFim, hora05, hora22);
        }
        // ✅ CASO 2: Jornada que cruza meia-noite (ex: 20h às 04h)
        else {
            minutosDiurnos = calcularMinutosDiurnosJornadaMadrugada(horaIni, horaFim, hora05, hora22);
        }

        // ✅ SUBTRAIR HORÁRIO DE ALMOÇO (1 hora = 60 minutos)
        minutosDiurnos = Math.max(0, minutosDiurnos - 60);

        // Converter para horas decimais
        return converterMinutosParaHoras(minutosDiurnos);
    }

    /**
     * ✅ CALCULA MINUTOS DIURNOS - JORNADA NORMAL
     */
    private long calcularMinutosDiurnosJornadaNormal(LocalTime horaIni, LocalTime horaFim,
                                                     LocalTime hora05, LocalTime hora22) {
        long minutosDiurnos = 0;

        // Período diurno da manhã (05h-12h)
        if (horaIni.isBefore(hora05) && horaFim.isAfter(hora05)) {
            LocalTime inicioDiurno = hora05;
            LocalTime fimDiurno = horaFim.isBefore(hora22) ? horaFim : hora22;
            minutosDiurnos += Duration.between(inicioDiurno, fimDiurno).toMinutes();
        }
        // Período diurno normal
        else if ((horaIni.isAfter(hora05) || horaIni.equals(hora05)) &&
                horaIni.isBefore(hora22) &&
                horaFim.isAfter(hora05)) {

            LocalTime inicioDiurno = horaIni;
            LocalTime fimDiurno = horaFim.isBefore(hora22) ? horaFim : hora22;

            if (fimDiurno.isAfter(inicioDiurno)) {
                minutosDiurnos += Duration.between(inicioDiurno, fimDiurno).toMinutes();
            }
        }

        return minutosDiurnos;
    }

    /**
     * ✅ CALCULA MINUTOS DIURNOS - JORNADA MADRUGADA
     */
    private long calcularMinutosDiurnosJornadaMadrugada(LocalTime horaIni, LocalTime horaFim,
                                                        LocalTime hora05, LocalTime hora22) {
        long minutosDiurnos = 0;

        // Período diurno antes da meia-noite (se houver)
        if (horaIni.isBefore(hora22)) {
            LocalTime inicioDiurno = horaIni.isAfter(hora05) ? horaIni : hora05;
            LocalTime fimDiurno = hora22;

            if (fimDiurno.isAfter(inicioDiurno)) {
                minutosDiurnos += Duration.between(inicioDiurno, fimDiurno).toMinutes();
            }
        }

        // Período diurno após meia-noite (05h-...)
        if (horaFim.isAfter(hora05)) {
            LocalTime inicioDiurno = hora05;
            LocalTime fimDiurno = horaFim.isBefore(hora22) ? horaFim : hora22;

            if (fimDiurno.isAfter(inicioDiurno)) {
                minutosDiurnos += Duration.between(inicioDiurno, fimDiurno).toMinutes();
            }
        }

        return minutosDiurnos;
    }

    /**
     * ✅ CONVERTE MINUTOS PARA HORAS DECIMAIS
     */
    private BigDecimal converterMinutosParaHoras(long minutos) {
        if (minutos <= 0) {
            return BigDecimal.ZERO;
        }

        double horas = minutos / 60.0;
        return BigDecimal.valueOf(horas).setScale(2, RoundingMode.HALF_UP);
    }
}