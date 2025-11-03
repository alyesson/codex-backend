package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.service.TabelaDeducaoInssService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularSalarioFamiliaService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularSalarioFamiliaService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaDeducaoInssService tabelaDeducaoInssService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularSalarioFamilia() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal valorCotaSalarioFamilia = tabelaDeducaoInssService.getSalarioFamilia();
            int numeroDependentes = folha.getDependentesIrrf();

            // ✅ Validação de dependentes
            if (numeroDependentes <= 0) {
                logger.info("Sem dependentes para salário família - matrícula {}", numeroMatricula);
                return resultado;
            }

            // ✅ Calcular salário família
            BigDecimal valorSalarioFamilia = calculoBaseService.calcularSalarioFamilia(valorCotaSalarioFamilia, numeroDependentes);

            resultado.put("referencia", BigDecimal.valueOf(numeroDependentes));
            resultado.put("vencimentos", valorSalarioFamilia);

            logger.info("Salário família calculado para {}: {} dependentes = R$ {}", numeroMatricula, numeroDependentes, valorSalarioFamilia);

        } catch (Exception e) {
            logger.error("Erro ao calcular salário família para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular salário família: " + e.getMessage());
        }

        return resultado;
    }
}
