package br.com.codex.v1.service.rh.especiais;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularComplementoDsrDiurno13Service {
    private static final Logger logger = LoggerFactory.getLogger(CalcularComplementoDsrDiurno13Service.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularComplementoDsrDiurno13() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            LocalDate dataAdmissao = folha.getDataAdmissao();

            // ✅ Chamar métudo do CalculoBaseService
            Map<String, BigDecimal> resultadoDSRDiurno = calculoBaseService.calcularComplementoDSRDiurno13(numeroMatricula, dataAdmissao);

            // ✅ Atualiza o resultado principal
            resultado.putAll(resultadoDSRDiurno);

            logger.info("Complemento DSR Diurno 13º calculado para {}: R$ {}", numeroMatricula, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular complemento DSR Diurno 13º para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular complemento DSR Diurno do 13º: " + e.getMessage());
        }

        return resultado;
    }
}
