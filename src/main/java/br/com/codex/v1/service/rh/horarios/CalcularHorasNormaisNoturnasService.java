package br.com.codex.v1.service.rh.horarios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
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
public class CalcularHorasNormaisNoturnasService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal>calcularHorasNormaisNoturnas(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            LocalTime horaFim = folha.getHoraSaida();

            LocalTime hora22 = LocalTime.parse("22:00");
            LocalTime hora05 = LocalTime.parse("05:00"); // Fim do período noturno

            // ✅ 1. CALCULAR HORAS NOTURNAS POR DIA
            BigDecimal horasNoturnasPorDia = BigDecimal.ZERO;

            if (horaFim.isAfter(hora22) || horaFim.isBefore(hora05)) {
                // Trabalha no período noturno
                Duration horasNoturnas;

                if (horaFim.isBefore(hora05)) {
                    // Trabalha da madrugada até antes das 5h
                    horasNoturnas = Duration.between(LocalTime.MIDNIGHT, horaFim)
                            .plus(Duration.between(hora22, LocalTime.MAX)); // 22h até 23:59
                } else {
                    // Trabalha das 22h até hora de saída
                    horasNoturnas = Duration.between(hora22, horaFim);
                }

                // Converter para horas decimais
                double horasDecimais = horasNoturnas.toHours() +
                        horasNoturnas.toMinutesPart() / 60.0;

                // Aplicar fator de redução (52,5 minutos por hora noturna)
                horasNoturnasPorDia = BigDecimal.valueOf(horasDecimais * 1.142857).setScale(2, RoundingMode.HALF_UP);
            }

            // ✅ 2. CALCULAR DIAS ÚTEIS NO MÊS - UMA ÚNICA VEZ
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();
            int diasUteis = calculoBaseService.calcularDiasUteisNoMes(year, month);

            // ✅ 3. CÁLCULO FINAL DAS HORAS NOTURNAS
            BigDecimal totalHorasNoturnasMes = horasNoturnasPorDia.multiply(new BigDecimal(diasUteis)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal valorTotal = totalHorasNoturnasMes.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", totalHorasNoturnasMes);
            resultado.put("vencimentos", valorTotal);
            resultado.put("descontos", BigDecimal.ZERO);

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular horas noturnas: " + e.getMessage());
        }
    }
}
