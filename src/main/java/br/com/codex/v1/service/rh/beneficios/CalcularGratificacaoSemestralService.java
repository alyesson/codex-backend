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
public class CalcularGratificacaoSemestralService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularGratificacaoSemestralService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularGratificacaoSemestral() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal gratificacaoSemestral = folha.getGratificacao();

            // ✅ Validação do valor
            if (gratificacaoSemestral == null || gratificacaoSemestral.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("Sem gratificação semestral para matrícula {}", numeroMatricula);
                return resultado;
            }

            resultado.put("referencia", gratificacaoSemestral);
            resultado.put("vencimentos", gratificacaoSemestral);

            logger.info("Gratificação semestral calculada para {}: R$ {}", numeroMatricula, gratificacaoSemestral);

        } catch (Exception e) {
            logger.error("Erro ao calcular gratificação semestral para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular gratificação semestral: " + e.getMessage());
        }

        return resultado;
    }
}
