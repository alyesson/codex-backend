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
public class CalcularParticipacaoLucrosService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularParticipacaoLucrosService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularParticipacaoLucros() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal participacaoLucrosResultado = folha.getParticipacaoLucrosResultado();

            // ✅ Validação do valor
            if (participacaoLucrosResultado == null || participacaoLucrosResultado.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("Sem participação nos lucros para matrícula {}", numeroMatricula);
                return resultado;
            }

            resultado.put("referencia", participacaoLucrosResultado);
            resultado.put("vencimentos", participacaoLucrosResultado);

            logger.info("Participação nos lucros calculada para {}: R$ {}", numeroMatricula, participacaoLucrosResultado);

        } catch (Exception e) {
            logger.error("Erro ao calcular participação nos lucros para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular participação nos lucros: " + e.getMessage());
        }

        return resultado;
    }
}
