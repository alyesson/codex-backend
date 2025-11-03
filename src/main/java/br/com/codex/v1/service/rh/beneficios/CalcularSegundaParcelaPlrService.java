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
public class CalcularSegundaParcelaPlrService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularSegundaParcelaPlrService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularSegundaParcelaPlr() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal plr2 = folha.getParticipacaoLucrosResultado();

            // ✅ Validação do valor
            if (plr2 == null || plr2.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("Sem 2ª parcela PLR para matrícula {}", numeroMatricula);
                return resultado;
            }

            resultado.put("referencia", plr2);
            resultado.put("vencimentos", plr2);

            logger.info("2ª parcela PLR calculada para {}: R$ {}", numeroMatricula, plr2);

        } catch (Exception e) {
            logger.error("Erro ao calcular 2ª parcela PLR para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular 2ª parcela PLR: " + e.getMessage());
        }

        return resultado;
    }
}
