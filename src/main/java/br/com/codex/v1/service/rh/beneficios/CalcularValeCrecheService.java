package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularValeCrecheService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularValeCrecheService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularValeCreche() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal valorValeCreche = folha.getValeCreche();

            // ✅ Validação do valor
            if (valorValeCreche == null || valorValeCreche.compareTo(BigDecimal.ZERO) < 0) {
                valorValeCreche = BigDecimal.ZERO;
            }

            // ✅ Limite mensal por dependente (ajuste conforme sua política)
            BigDecimal limiteMensal = new BigDecimal("500.00");
            if (valorValeCreche.compareTo(limiteMensal) > 0) {
                valorValeCreche = limiteMensal;
            }

            resultado.put("referencia", BigDecimal.ONE);
            resultado.put("vencimentos", valorValeCreche);

            logger.info("Vale creche calculado para {}: R$ {}", numeroMatricula, valorValeCreche);

        } catch (NumberFormatException e) {
            logger.error("Valor do Vale Creche inválido para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new DataIntegrityViolationException("Erro ao calcular vale-creche: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao calcular Vale Creche para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new DataIntegrityViolationException("Erro ao calcular vale-creche: " + e.getMessage());
        }

        return resultado;
    }
}
