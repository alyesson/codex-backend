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
public class CalcularDecimoTerceiroRescisaoService {

    private static final Logger logger = LoggerFactory.getLogger(CalcularDecimoTerceiroRescisaoService.class);

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularDecimoTerceiroProporcional(BigDecimal salarioBase, LocalDate dataAdmissao,
                                                                      LocalDate dataDemissao, Integer faltas) {
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

            BigDecimal decimoTerceiro = salarioBase.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(mesesTrabalhados)).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", BigDecimal.ONE);
            resultado.put("vencimentos", decimoTerceiro);

            logger.info("13º proporcional calculado para {}: {} meses = R$ {}", numeroMatricula, mesesTrabalhados, decimoTerceiro);

        } catch (Exception e) {
            logger.error("Erro ao calcular 13º proporcional para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular 13º proporcional: " + e.getMessage());
        }

        return resultado;
    }
}
