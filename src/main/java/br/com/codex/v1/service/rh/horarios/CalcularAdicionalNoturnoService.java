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
public class CalcularAdicionalNoturnoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private CalculosAuxiliaresFolha calculosAuxiliaresFolha;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularAdicionalNoturno() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            BigDecimal percentualAdicionalNoturno = folha.getPercentualAdicionalNoturno();
            LocalTime horaIni = folha.getHoraEntrada();
            LocalTime horaFim = folha.getHoraSaida();

            // ✅ 1. CALCULAR HORAS NOTURNAS TRABALHADAS POR DIA (CORRIGIDO)
            BigDecimal horasNoturnasPorDia = calcularHorasNoturnasPorDia(horaIni, horaFim);

            // ✅ 2. CALCULAR DIAS ÚTEIS NO MÊS
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();
            int diasUteis = calculosAuxiliaresFolha.calcularDiasUteisNoMes(year, month);

            // ✅ 3. CALCULAR HORAS NOTURNAS MENSAL (dias úteis)
            BigDecimal totalHorasNoturnasTrabalhadas = horasNoturnasPorDia
                    .multiply(new BigDecimal(diasUteis))
                    .setScale(2, RoundingMode.HALF_UP);

            // ✅ 4. CALCULAR HORAS NOTURNAS NO DSR (CORRIGIDO)
            BigDecimal horasNoturnasDSR = calcularHorasNoturnasDSR(horasNoturnasPorDia, year, month);

            // ✅ 5. TOTAL GERAL DE HORAS NOTURNAS
            BigDecimal totalHorasNoturnas = totalHorasNoturnasTrabalhadas.add(horasNoturnasDSR);

            // ✅ 6. CÁLCULO DO ADICIONAL NOTURNO
            BigDecimal valorBaseHorasNoturnas = totalHorasNoturnas.multiply(salarioPorHora);
            BigDecimal valorAdicionalNoturno = valorBaseHorasNoturnas.multiply(percentualAdicionalNoturno).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            resultado.put("referencia", totalHorasNoturnas);
            resultado.put("vencimentos", valorAdicionalNoturno);
            resultado.put("descontos", BigDecimal.ZERO);

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular adicional noturno: " + e.getMessage());
        }
    }

    /**
     * ✅ CALCULA HORAS NOTURNAS POR DIA CONSIDERANDO REDUÇÃO (52,5min = 1h)
     */
    private BigDecimal calcularHorasNoturnasPorDia(LocalTime horaIni, LocalTime horaFim) {
        LocalTime hora22 = LocalTime.of(22, 0);
        LocalTime hora05 = LocalTime.of(5, 0);

        long minutosNoturnos = 0;

        // ✅ CASO 1: Jornada normal (não cruza meia-noite)
        if (!horaFim.isBefore(horaIni)) {
            // Verifica se a jornada tem período noturno
            if (horaIni.isBefore(hora05) || horaIni.isAfter(hora22) || horaIni.equals(hora22)) {
                LocalTime inicioNoturno = horaIni.isBefore(hora05) ? LocalTime.MIDNIGHT : hora22;
                LocalTime fimNoturno = horaFim.isAfter(hora05) ? hora05 : horaFim;

                if (fimNoturno.isAfter(inicioNoturno)) {
                    minutosNoturnos = Duration.between(inicioNoturno, fimNoturno).toMinutes();
                }
            }
        }
        // ✅ CASO 2: Jornada que cruza meia-noite (ex: 20h às 04h)
        else {
            // Período noturno antes da meia-noite (22h-23:59)
            if (horaIni.isBefore(hora05) || horaIni.isAfter(hora22) || horaIni.equals(hora22)) {
                minutosNoturnos += Duration.between(hora22, LocalTime.MAX).toMinutes();
            }

            // Período noturno após meia-noite (00h-05h)
            if (horaFim.isBefore(hora05)) {
                minutosNoturnos += Duration.between(LocalTime.MIDNIGHT, horaFim).toMinutes();
            }
        }

        // ✅ APLICAR REDUÇÃO HORÁRIA (52,5 minutos = 1 hora noturna)
        return converterMinutosParaHorasNoturnas(minutosNoturnos);
    }

    /**
     * ✅ CALCULA HORAS NOTURNAS NO DSR (CORRIGIDO)
     */
    private BigDecimal calcularHorasNoturnasDSR(BigDecimal horasNoturnasPorDia, int year, int month) {
        int diasRepouso = calculosAuxiliaresFolha.calcularDiasRepousoNoMes(year, month);

        // ✅ CORREÇÃO: Média diária × dias de repouso
        return horasNoturnasPorDia.multiply(new BigDecimal(diasRepouso)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * ✅ CONVERTE MINUTOS TRABALHADOS PARA HORAS NOTURNAS (com redução)
     */
    private BigDecimal converterMinutosParaHorasNoturnas(long minutosTrabalhados) {
        if (minutosTrabalhados <= 0) {
            return BigDecimal.ZERO;
        }

        // Fórmula: Minutos Trabalhados ÷ 52,5
        double horasNoturnas = minutosTrabalhados / 52.5;
        return BigDecimal.valueOf(horasNoturnas).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * ✅ MÉTUDO AUXILIAR PARA DEBUG
     */
    public void debugHorarios(LocalTime horaIni, LocalTime horaFim) {
        System.out.println("=== DEBUG ADICIONAL NOTURNO ===");
        System.out.println("Hora Entrada: " + horaIni);
        System.out.println("Hora Saída: " + horaFim);
        System.out.println("Horas Noturnas/Dia: " + calcularHorasNoturnasPorDia(horaIni, horaFim));
        System.out.println("==============================");
    }
}