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
public class CalcularHorasRepousoRemuneradoNoturnoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private CalculosAuxiliaresFolha calculosAuxiliaresFolha;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularHorasRepousoRemuneradoNoturno() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            String tipoDeJornada = folha.getJornada();

            if("12 x 36".equalsIgnoreCase(tipoDeJornada) || "12x36".equalsIgnoreCase(tipoDeJornada)){
                return calcularDSRNoturno12x36();
            }else {
                return calcularDSRNoturnoNormal();
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular DSR Noturno: " + e.getMessage());
        }
    }

    private Map<String, BigDecimal> calcularDSRNoturnoNormal() {
        Map<String, BigDecimal> resultado = new HashMap<>();

        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
        BigDecimal salarioPorHora = folha.getSalarioHora();
        LocalTime horaIni = folha.getHoraEntrada();
        LocalTime horaFim = folha.getHoraSaida();

        // ✅ 1. CALCULAR HORAS NOTURNAS POR DIA (COM REDUÇÃO)
        BigDecimal horasNoturnasPorDia = calcularHorasNoturnasPorDia(horaIni, horaFim);

        // ✅ 2. CALCULAR DIAS DE REPOUSO NO MÊS
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int diasRepouso = calculosAuxiliaresFolha.calcularDiasRepousoNoMes(year, month);

        // ✅ 3. CÁLCULO FINAL DO DSR NOTURNO
        BigDecimal totalHorasDSRNoturno = horasNoturnasPorDia.multiply(new BigDecimal(diasRepouso)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorDSRNoturno = totalHorasDSRNoturno.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);

        resultado.put("referencia", totalHorasDSRNoturno);
        resultado.put("vencimentos", valorDSRNoturno);
        resultado.put("descontos", BigDecimal.ZERO);

        return resultado;
    }

    /**
     * ✅ CALCULA HORAS NOTURNAS POR DIA COM REDUÇÃO (52,5min = 1h)
     */
    private BigDecimal calcularHorasNoturnasPorDia(LocalTime horaIni, LocalTime horaFim) {
        LocalTime hora22 = LocalTime.of(22, 0);
        LocalTime hora05 = LocalTime.of(5, 0);

        long minutosNoturnos = 0;

        // ✅ CASO 1: Jornada normal (não cruza meia-noite)
        if (!horaFim.isBefore(horaIni)) {
            minutosNoturnos = calcularMinutosNoturnosJornadaNormal(horaIni, horaFim, hora22, hora05);
        }
        // ✅ CASO 2: Jornada que cruza meia-noite
        else {
            minutosNoturnos = calcularMinutosNoturnosJornadaMadrugada(horaIni, horaFim, hora22, hora05);
        }

        // ✅ APLICAR REDUÇÃO HORÁRIA (52,5 minutos = 1 hora noturna)
        return converterMinutosParaHorasNoturnas(minutosNoturnos);
    }

    /**
     * ✅ CALCULA MINUTOS NOTURNOS - JORNADA NORMAL
     */
    private long calcularMinutosNoturnosJornadaNormal(LocalTime horaIni, LocalTime horaFim, LocalTime hora22, LocalTime hora05) {
        long minutosNoturnos = 0;

        // Verifica se trabalhou no período noturno (22h-05h)
        boolean trabalhouNoturno = (horaIni.isBefore(hora05) || horaIni.isAfter(hora22) ||
                horaIni.equals(hora22)) && (horaFim.isBefore(hora05) || horaFim.isAfter(hora22));

        if (trabalhouNoturno) {
            // Início do período noturno
            LocalTime inicioNoturno = horaIni.isBefore(hora05) ? LocalTime.MIDNIGHT : (horaIni.isAfter(hora22) ? horaIni : hora22);

            // Fim do período noturno
            LocalTime fimNoturno = horaFim.isAfter(hora05) ? hora05 : (horaFim.isAfter(hora22) ? horaFim : horaFim);

            if (fimNoturno.isAfter(inicioNoturno)) {
                minutosNoturnos = Duration.between(inicioNoturno, fimNoturno).toMinutes();
            }
        }

        return minutosNoturnos;
    }

    /**
     * ✅ CALCULA MINUTOS NOTURNOS - JORNADA MADRUGADA
     */
    private long calcularMinutosNoturnosJornadaMadrugada(LocalTime horaIni, LocalTime horaFim, LocalTime hora22, LocalTime hora05) {
        long minutosNoturnos = 0;

        // Período noturno antes da meia-noite (22h-23:59)
        if (horaIni.isBefore(hora05) || horaIni.isAfter(hora22) || horaIni.equals(hora22)) {
            LocalTime inicioNoturno = horaIni.isAfter(hora22) ? horaIni : hora22;
            minutosNoturnos += Duration.between(inicioNoturno, LocalTime.MAX).toMinutes();
            // Adiciona 1 minuto para incluir o último minuto do dia
            minutosNoturnos += 1;
        }

        // Período noturno após meia-noite (00h-05h)
        if (horaFim.isBefore(hora05)) {
            LocalTime fimNoturno = horaFim;
            minutosNoturnos += Duration.between(LocalTime.MIDNIGHT, fimNoturno).toMinutes();
        }

        return minutosNoturnos;
    }

    /**
     * ✅ CONVERTE MINUTOS TRABALHADOS PARA HORAS NOTURNAS (com redução)
     */
    private BigDecimal converterMinutosParaHorasNoturnas(long minutosTrabalhados) {
        if (minutosTrabalhados <= 0) {
            return BigDecimal.ZERO;
        }

        // ✅ FÓRMULA CORRETA: Minutos Trabalhados ÷ 52,5
        double horasNoturnas = minutosTrabalhados / 52.5;
        return BigDecimal.valueOf(horasNoturnas).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * ✅ MÉTUDO ESPECÍFICO PARA JORNADA 12x36
     */
    public Map<String, BigDecimal> calcularDSRNoturno12x36() {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            LocalTime horaIni = folha.getHoraEntrada();
            LocalTime horaFim = folha.getHoraSaida();

            // ✅ Na jornada 12x36, considera 12 horas de trabalho por dia
            // Das quais calculamos quantas são noturnas
            BigDecimal horasNoturnasPorDia = calcularHorasNoturnasPorDia(horaIni, horaFim);

            // ✅ Na jornada 12x36, geralmente tem cerca de 15 dias de trabalho no mês
            // Logo, tem cerca de 15 dias de repouso
            int diasRepouso = 15;

            BigDecimal totalHorasDSRNoturno = horasNoturnasPorDia.multiply(new BigDecimal(diasRepouso))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal valorDSRNoturno = totalHorasDSRNoturno.multiply(salarioPorHora)
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", totalHorasDSRNoturno);
            resultado.put("vencimentos", valorDSRNoturno);
            resultado.put("descontos", BigDecimal.ZERO);

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular DSR Noturno 12x36: " + e.getMessage());
        }
    }
}