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
public class CalcularReembolsoViagemService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularReembolsoViagemService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularReembolsoViagem() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal reembolsoViagem = folha.getReembolsoViagem();

            // ✅ Validação do valor
            if (reembolsoViagem == null || reembolsoViagem.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("Sem reembolso de viagem para matrícula {}", numeroMatricula);
                return resultado;
            }

            resultado.put("referencia", reembolsoViagem);
            resultado.put("vencimentos", reembolsoViagem);

            logger.info("Reembolso viagem calculado para {}: R$ {}", numeroMatricula, reembolsoViagem);

        } catch (Exception e) {
            logger.error("Erro ao calcular reembolso viagem para matrícula {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular reembolso viagem: " + e.getMessage());
        }

        return resultado;
    }
}
