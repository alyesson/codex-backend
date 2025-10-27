package br.com.codex.v1.service.rh.rescisao;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularFeriasRescisaoService {

    private static final Logger logger = LoggerFactory.getLogger(CalcularFeriasRescisaoService.class);

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularFeriasProporcionais(BigDecimal salarioBase, LocalDate dataAdmissao,
                                                               LocalDate dataDemissao, Integer faltas, String tipoSalario) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            Period periodo = Period.between(dataAdmissao, dataDemissao);
            int mesesTrabalhados = periodo.getMonths();
            int dias = periodo.getDays();

            // Se trabalhou mais de 15 dias no mês, conta o mês
            if (dias >= 15) {
                mesesTrabalhados++;
            }

            // Aplica redução por faltas
            int diasFerias = calcularDiasFeriasPorFaltas(faltas);

            BigDecimal valorFerias = salarioBase.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados)).multiply(new BigDecimal(diasFerias)).divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);

            resultado.put("referencia", BigDecimal.ONE);
            resultado.put("vencimentos", valorFerias);

            logger.info("Férias proporcionais calculadas para {}: {} meses = R$ {}", numeroMatricula, mesesTrabalhados, valorFerias);

        } catch (Exception e) {
            logger.error("Erro ao calcular férias proporcionais para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular férias proporcionais: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularFeriasVencidas(BigDecimal salarioBase, Integer faltas, String tipoSalario) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            int diasFerias = calcularDiasFeriasPorFaltas(faltas);

            BigDecimal valorFerias = salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(diasFerias));
            BigDecimal umTerco = valorFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);

            BigDecimal total = valorFerias.add(umTerco).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", BigDecimal.ONE);
            resultado.put("vencimentos", total);

            logger.info("Férias vencidas calculadas para {}: {} dias = R$ {}",
                    numeroMatricula, diasFerias, total);

        } catch (Exception e) {
            logger.error("Erro ao calcular férias vencidas para {}: {}",
                    numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular férias vencidas: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularUmTercoFerias(BigDecimal salarioBase) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            BigDecimal umTercoFerias = salarioBase.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);

            resultado.put("referencia", umTercoFerias);
            resultado.put("vencimentos", umTercoFerias);

            logger.info("1/3 de férias calculado para {}: R$ {}",
                    numeroMatricula, umTercoFerias);

        } catch (Exception e) {
            logger.error("Erro ao calcular 1/3 de férias para {}: {}",
                    numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular 1/3 de férias: " + e.getMessage());
        }

        return resultado;
    }

    private int calcularDiasFeriasPorFaltas(Integer faltas) {
        if (faltas == null || faltas <= 5) {
            return 30;
        } else if (faltas >= 6 && faltas <= 14) {
            return 24;
        } else if (faltas >= 15 && faltas <= 23) {
            return 18;
        } else if (faltas >= 24 && faltas <= 32) {
            return 12;
        } else {
            return 0;
        }
    }
}
