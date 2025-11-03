package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularReembolsoCrecheService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularReembolsoCrecheService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularReembolsoCreche() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal reembolsoCreche = folha.getValeCreche();

            // ✅ Validação do valor
            if (reembolsoCreche == null || reembolsoCreche.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("Sem reembolso de creche para matrícula {}", numeroMatricula);
                return resultado;
            }

            resultado.put("referencia", reembolsoCreche);
            resultado.put("vencimentos", reembolsoCreche);

            logger.info("Reembolso creche calculado para {}: R$ {}", numeroMatricula, reembolsoCreche);

        } catch (Exception e) {
            logger.error("Erro ao calcular reembolso creche para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular reembolso creche: " + e.getMessage());
        }

        return resultado;
    }
}
