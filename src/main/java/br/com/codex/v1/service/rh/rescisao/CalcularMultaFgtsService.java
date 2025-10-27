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
public class CalcularMultaFgtsService {

    private static final Logger logger = LoggerFactory.getLogger(CalcularMultaFgtsService.class);

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularMultaFGTS(BigDecimal salarioBase, LocalDate dataAdmissao,
                                                     LocalDate dataDemissao, String tipoDemissao) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            Period periodo = Period.between(dataAdmissao, dataDemissao);
            int mesesTrabalhados = periodo.getYears() * 12 + periodo.getMonths();

            // FGTS mensal (8%)
            BigDecimal fgtsMensal = salarioBase.multiply(new BigDecimal("0.08"));
            BigDecimal totalFGTSDepositado = fgtsMensal.multiply(new BigDecimal(mesesTrabalhados));

            // Multa de 40% para demissão sem justa causa
            if ("SEM_JUSTA_CAUSA".equals(tipoDemissao)) {
                BigDecimal multaFGTS = totalFGTSDepositado.multiply(new BigDecimal("0.40")).setScale(2, RoundingMode.HALF_UP);

                resultado.put("referencia", new BigDecimal("40")); // 40%
                resultado.put("vencimentos", multaFGTS);

                logger.info("Multa FGTS calculada para {}: {} meses = R$ {}", numeroMatricula, mesesTrabalhados, multaFGTS);
            } else {
                logger.info("Sem multa FGTS para demissão com justa causa - matrícula {}", numeroMatricula);
            }

        } catch (Exception e) {
            logger.error("Erro ao calcular multa FGTS para {}: {}",
                    numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular multa FGTS: " + e.getMessage());
        }

        return resultado;
    }
}