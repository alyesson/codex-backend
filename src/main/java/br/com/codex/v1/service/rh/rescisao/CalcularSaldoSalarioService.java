package br.com.codex.v1.service.rh.rescisao;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularSaldoSalarioService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularSaldoSalarioService.class);

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularSaldoSalario(BigDecimal salarioBase, Integer diasTrabalhados, String tipoSalario, BigDecimal salarioPorHora) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            BigDecimal saldoSalario;

            if ("Mensal".equals(tipoSalario)) {
                saldoSalario = salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(diasTrabalhados)).setScale(2, RoundingMode.HALF_UP);
            } else {
                // Para horistas
                BigDecimal horasPorDia = new BigDecimal("8"); // Padrão 8h/dia
                BigDecimal valorDia = salarioPorHora.multiply(horasPorDia);
                saldoSalario = valorDia.multiply(new BigDecimal(diasTrabalhados)).setScale(2, RoundingMode.HALF_UP);
            }

            resultado.put("referencia", new BigDecimal(diasTrabalhados));
            resultado.put("vencimentos", saldoSalario);

            logger.info("Saldo de salário calculado para {}: {} dias = R$ {}", numeroMatricula, diasTrabalhados, saldoSalario);

        } catch (Exception e) {
            logger.error("Erro ao calcular saldo de salário para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular saldo de salário: " + e.getMessage());
        }

        return resultado;
    }
}
