package br.com.codex.v1.service.rh.especiais;

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
public class CalcularSalarioMaternidadeService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularSalarioMaternidadeService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularSalarioMaternidade() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioMaternidade = folha.getSalarioBase();

            if (salarioMaternidade == null || salarioMaternidade.compareTo(BigDecimal.ZERO) <= 0) {
                logger.warn("Salário maternidade inválido ou zero para matrícula {}", numeroMatricula);
                return resultado;
            }

            resultado.put("referencia", salarioMaternidade);
            resultado.put("vencimentos", salarioMaternidade);

            logger.info("Salário maternidade calculado para {}: R$ {}", numeroMatricula, salarioMaternidade);
        } catch (Exception e) {
            logger.error("Erro ao calcular salário maternidade para matrícula {}: {}",  numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular salário maternidade: " + e.getMessage());
        }

        return resultado;
    }
}
