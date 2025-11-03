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
public class CalcularAbonoSalarialService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularAbonoSalarialService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularAbonoSalarial() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal abonoSalarial = folha.getAbonoSalarial();

            // ✅ Validação do valor
            if (abonoSalarial == null || abonoSalarial.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("Sem abono salarial para matrícula {}", numeroMatricula);
                return resultado;
            }

            resultado.put("referencia", abonoSalarial);
            resultado.put("vencimentos", abonoSalarial);

            logger.info("Abono salarial calculado para {}: R$ {}", numeroMatricula, abonoSalarial);

        } catch (Exception e) {
            logger.error("Erro ao calcular abono salarial para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular abono salarial: " + e.getMessage());
        }

        return resultado;
    }
}
