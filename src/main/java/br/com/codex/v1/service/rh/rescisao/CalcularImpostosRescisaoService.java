package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.service.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularImpostosRescisaoService {

    private static final Logger logger = LoggerFactory.getLogger(CalcularImpostosRescisaoService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularINSSRescisao(FolhaRescisao rescisao) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            BigDecimal valorTotalRescisao = calcularValorTotalRescisao(rescisao);
            BigDecimal inssRescisao = calculoBaseService.calcularINSS(valorTotalRescisao);

            resultado.put("referencia", inssRescisao);
            resultado.put("descontos", inssRescisao);

            logger.info("INSS sobre rescisão calculado para {}: R$ {}", numeroMatricula, inssRescisao);

        } catch (Exception e) {
            logger.error("Erro ao calcular INSS sobre rescisão para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular INSS sobre rescisão: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularIRRFRescisao(FolhaRescisao rescisao, Integer numeroDependentes) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            BigDecimal valorTotalRescisao = calcularValorTotalRescisao(rescisao);
            BigDecimal inssRescisao = calculoBaseService.calcularINSS(valorTotalRescisao);
            BigDecimal irrfRescisao = calcularIRRF(valorTotalRescisao, inssRescisao, numeroDependentes);

            resultado.put("referencia", irrfRescisao);
            resultado.put("descontos", irrfRescisao);

            logger.info("IRRF sobre rescisão calculado para {}: R$ {}",
                    numeroMatricula, irrfRescisao);

        } catch (Exception e) {
            logger.error("Erro ao calcular IRRF sobre rescisão para {}: {}",
                    numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular IRRF sobre rescisão: " + e.getMessage());
        }

        return resultado;
    }

    private BigDecimal calcularIRRF(BigDecimal valorTotal, BigDecimal inss, Integer dependentes) {
        BigDecimal baseCalculo = valorTotal.subtract(inss);

        if (dependentes > 0) {
            BigDecimal deducaoDependentes = new BigDecimal("189.59").multiply(new BigDecimal(dependentes));
            baseCalculo = baseCalculo.subtract(deducaoDependentes);
        }

        if (baseCalculo.compareTo(BigDecimal.ZERO) < 0) {
            baseCalculo = BigDecimal.ZERO;
        }

        return calculoBaseService.calcularIRRF(baseCalculo);
    }

    private BigDecimal calcularValorTotalRescisao(FolhaRescisao rescisao) {
        // Simulação do valor total da rescisão para cálculo de INSS/IRRF
        BigDecimal salarioBase = rescisao.getSalarioBase();
        return salarioBase.multiply(new BigDecimal("2")); // Exemplo: 2x o salário base
    }
}
