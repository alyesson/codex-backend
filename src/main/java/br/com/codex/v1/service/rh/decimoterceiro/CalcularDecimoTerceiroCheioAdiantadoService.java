package br.com.codex.v1.service.rh.decimoterceiro;

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
public class CalcularDecimoTerceiroCheioAdiantadoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularDecimoTerceiroCheioAdiantadoService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularDecimoTerceiroCheioAdiantado() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioBase = folha.getSalarioBase();

            // ✅ Validação do salário base
            if (salarioBase == null || salarioBase.compareTo(BigDecimal.ZERO) <= 0) {
                logger.warn("Salário base inválido para 13º adiantado - matrícula {}", numeroMatricula);
                return resultado;
            }

            resultado.put("vencimentos", salarioBase);

            logger.info("13º cheio adiantado calculado para {}: R$ {}", numeroMatricula, salarioBase);

        } catch (Exception e) {
            logger.error("Erro ao calcular 13º cheio adiantado para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular décimo terceiro cheio adiantado: " + e.getMessage());
        }

        return resultado;
    }
}
