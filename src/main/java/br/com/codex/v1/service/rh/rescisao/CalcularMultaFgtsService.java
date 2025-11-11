package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularMultaFGTS(String tipoDemissao) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal fgts = rescisao.getValorFgts();

            // Multa de 40% para demissão sem justa causa
            if ("Sem Justa Causa".equals(tipoDemissao)) {
                BigDecimal multaFGTS = fgts.multiply(new BigDecimal("0.40")).setScale(2, RoundingMode.HALF_UP);

                resultado.put("referencia", new BigDecimal("40")); // 40%
                resultado.put("vencimentos", multaFGTS);

                logger.info("Multa FGTS calculada para {}, valor = R$ {}", numeroMatricula, multaFGTS);
            } else {
                resultado.put("vencimentos", BigDecimal.ZERO);
                logger.info("Sem multa FGTS para demissão com justa causa - matrícula {}", numeroMatricula);
            }
        } catch (Exception e) {
            logger.error("Erro ao calcular multa FGTS para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular multa FGTS: " + e.getMessage());
        }
        return resultado;
    }
}