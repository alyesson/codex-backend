package br.com.codex.v1.service.rh.impostos;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularFgtsSalarioService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularFgtsSalarioService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularFgtsSalario() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            String descricaoDoCargo = folha.getCargoFuncionario();
            BigDecimal salarioBase = folha.getSalarioBase();

            // ✅ Validação do salário base
            if (salarioBase == null || salarioBase.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("Salário base inválido para cálculo de FGTS - matrícula {}", numeroMatricula);
                return resultado;
            }

            BigDecimal taxaFGTS;

            // ✅ Definir a taxa conforme o tipo de contrato
            if (descricaoDoCargo != null && descricaoDoCargo.toLowerCase().contains("aprendiz")) {
                taxaFGTS = new BigDecimal("0.02"); // 2% para aprendiz
            } else {
                taxaFGTS = new BigDecimal("0.08"); // 8% para demais empregados
            }

            // ✅ Calcular FGTS
            BigDecimal valorFGTS = salarioBase.multiply(taxaFGTS).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", taxaFGTS.multiply(new BigDecimal("100"))); // Percentual
            resultado.put("vencimentos", valorFGTS);

            logger.info("FGTS sobre salário calculado para {}: {}% = R$ {}", numeroMatricula, taxaFGTS.multiply(new BigDecimal("100")), valorFGTS);

        } catch (Exception e) {
            logger.error("Erro ao calcular FGTS sobre salário para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular o FGTS: " + e);
        }

        return resultado;
    }
}
