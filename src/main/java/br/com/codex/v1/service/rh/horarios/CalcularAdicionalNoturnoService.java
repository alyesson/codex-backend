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

    public Map<String, BigDecimal>calcularAdicionalNoturno(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            BigDecimal percentualAdicionalNoturno = folha.getPercentualAdicionalNoturno();
            LocalTime horaFim = folha.getHoraSaida();

            LocalTime hora22 = LocalTime.parse("22:00");
            LocalTime hora05 = LocalTime.parse("05:00");

            // ✅ 1. CALCULAR HORAS NOTURNAS TRABALHADAS (mesma lógica do caso 12)
            BigDecimal horasNoturnasTrabalhadas = BigDecimal.ZERO;

            if (horaFim.isAfter(hora22) || horaFim.isBefore(hora05)) {
                Duration horasNoturnas;

                if (horaFim.isBefore(hora05)) {
                    // Trabalha da madrugada até antes das 5h
                    horasNoturnas = Duration.between(LocalTime.MIDNIGHT, horaFim)
                            .plus(Duration.between(hora22, LocalTime.MAX));
                } else {
                    // Trabalha das 22h até hora de saída
                    horasNoturnas = Duration.between(hora22, horaFim);
                }

                double horasDecimais = horasNoturnas.toHours() + horasNoturnas.toMinutesPart() / 60.0;
                horasNoturnasTrabalhadas = BigDecimal.valueOf(horasDecimais * 1.142857)
                        .setScale(2, RoundingMode.HALF_UP);
            }

            // ✅ 2. CALCULAR HORAS NOTURNAS NO DSR
            BigDecimal horasNoturnasDSR = BigDecimal.ZERO;
            if (horasNoturnasTrabalhadas.compareTo(BigDecimal.ZERO) > 0) {
                int year = LocalDate.now().getYear();
                int month = LocalDate.now().getMonthValue();
                int diasRepouso = calculosAuxiliaresFolha.calcularDiasRepousoNoMes(year, month);

                horasNoturnasDSR = horasNoturnasTrabalhadas
                        .multiply(new BigDecimal(diasRepouso))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            // ✅ 3. CÁLCULO FINAL DO ADICIONAL NOTURNO
            BigDecimal totalHorasNoturnas = horasNoturnasTrabalhadas.add(horasNoturnasDSR);

            BigDecimal valorAdicionalNoturno = totalHorasNoturnas.multiply(salarioPorHora).multiply(percentualAdicionalNoturno)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            resultado.put("referencia", totalHorasNoturnas);
            resultado.put("vencimentos", valorAdicionalNoturno);
            resultado.put("descontos", BigDecimal.ZERO);

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular adicional noturno: " + e.getMessage());
        }
    }
}
