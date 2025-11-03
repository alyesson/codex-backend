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
public class CalcularPrimeiraParcelaAbonoSalarialService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularPrimeiraParcelaAbonoSalarialService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularPrimeiraParcelaAbonoSalarial() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal primeiraParcelaAbonoSalarial = folha.getAbonoSalarial();

            // ✅ Validação do valor
            if (primeiraParcelaAbonoSalarial == null || primeiraParcelaAbonoSalarial.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("Sem 1ª parcela abono salarial para matrícula {}", numeroMatricula);
                return resultado;
            }

            resultado.put("referencia", primeiraParcelaAbonoSalarial);
            resultado.put("vencimentos", primeiraParcelaAbonoSalarial);

            logger.info("1ª parcela abono salarial calculada para {}: R$ {}", numeroMatricula, primeiraParcelaAbonoSalarial);

        } catch (Exception e) {
            logger.error("Erro ao calcular 1ª parcela abono salarial para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular 1ª parcela abono salarial: " + e.getMessage());
        }

        return resultado;
    }
}
