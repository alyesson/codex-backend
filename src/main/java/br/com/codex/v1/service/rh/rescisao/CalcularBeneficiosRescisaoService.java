package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.repository.TabelaDeducaoInssRepository;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
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
public class CalcularBeneficiosRescisaoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularBeneficiosRescisaoService.class);

    @Autowired
    private TabelaDeducaoInssRepository tabelaDeducaoInssRepository;

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularSalarioFamiliaRescisao(BigDecimal salarioBase, Integer dependentes, Integer diasTrabalhados) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            BigDecimal valorCota = tabelaDeducaoInssRepository.findTopByOrderById().map(TabelaDeducaoInss::getSalarioFamilia).orElse(new BigDecimal("50.00")); // Valor padrão
            BigDecimal valorSalarioMinimo = tabelaImpostoRendaRepository.findTopByOrderById().map(TabelaImpostoRenda::getSalarioMinimo).orElse(BigDecimal.ZERO);

            if (dependentes > 0 && salarioBase.compareTo(valorSalarioMinimo) <= 0) {
                BigDecimal salarioFamilia = valorCota.multiply(new BigDecimal(dependentes)).multiply(new BigDecimal(diasTrabalhados)).divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);

                resultado.put("referencia", new BigDecimal(dependentes));
                resultado.put("vencimentos", salarioFamilia);

                logger.info("Salário família rescisão calculado para {}: {} dependentes = R$ {}", numeroMatricula, dependentes, salarioFamilia);
            } else {
                logger.info("Sem direito a salário família na rescisão - matrícula {}", numeroMatricula);
                throw new RuntimeException("Erro ao calcular salário família rescisão");
            }

        } catch (Exception e) {
            logger.error("Erro ao calcular salário família rescisão para {}: {}", numeroMatricula, e.getMessage());
        }

        return resultado;
    }
}
