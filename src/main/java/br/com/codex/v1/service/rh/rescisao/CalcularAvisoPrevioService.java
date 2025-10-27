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
public class CalcularAvisoPrevioService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularAvisoPrevioService.class);

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularAvisoPrevioTrabalhado(Integer diasTrabalhados) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        // Aviso prévio trabalhado já está incluso no saldo de salário
        resultado.put("referencia", new BigDecimal(diasTrabalhados));

        logger.info("Aviso prévio trabalhado calculado para {}: {} dias",
                numeroMatricula, diasTrabalhados);

        return resultado;
    }

    public Map<String, BigDecimal> calcularAvisoPrevioIndenizado(BigDecimal salarioBase, LocalDate dataAdmissao,LocalDate dataDemissao, String tipoSalario,BigDecimal salarioPorHora) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            Period periodo = Period.between(dataAdmissao, dataDemissao);
            int anosTrabalhados = periodo.getYears();

            // Base: 30 dias + 3 dias por ano trabalhado
            int diasAvisoPrevio = 30 + (3 * anosTrabalhados);
            BigDecimal valorAvisoPrevio;

            if ("Mensal".equals(tipoSalario)) {
                valorAvisoPrevio = salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(diasAvisoPrevio)).setScale(2, RoundingMode.HALF_UP);
            } else {
                BigDecimal horasPorDia = new BigDecimal("8");
                BigDecimal valorDia = salarioPorHora.multiply(horasPorDia);
                valorAvisoPrevio = valorDia.multiply(new BigDecimal(diasAvisoPrevio)) .setScale(2, RoundingMode.HALF_UP);
            }

            resultado.put("referencia", BigDecimal.ONE);
            resultado.put("vencimentos", valorAvisoPrevio);

            logger.info("Aviso prévio indenizado calculado para {}: {} dias = R$ {}", numeroMatricula, diasAvisoPrevio, valorAvisoPrevio);

        } catch (Exception e) {
            logger.error("Erro ao calcular aviso prévio indenizado para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular aviso prévio indenizado: " + e.getMessage());
        }

        return resultado;
    }
}
