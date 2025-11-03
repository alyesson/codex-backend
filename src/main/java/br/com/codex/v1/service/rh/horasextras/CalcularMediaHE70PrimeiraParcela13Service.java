package br.com.codex.v1.service.rh.horasextras;

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
public class CalcularMediaHE70PrimeiraParcela13Service {
    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaHE70PrimeiraParcela13Service.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularMediaHE70PrimeiraParcela13() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();

            // ✅ Chamar métudo do CalculoBaseService
            Map<String, BigDecimal> resultadoHE70 = calculoBaseService.calcularMediaHE70PrimeiraParcela13(numeroMatricula, salarioPorHora);

            // ✅ Atualiza o resultado principal
            resultado.putAll(resultadoHE70);

            logger.info("Média HE 70% 1ª parcela 13º calculada para {}: R$ {}", numeroMatricula, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular média HE 70% 1ª parcela 13º para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 70% Sobre 1° Parcela do 13°: " + e.getMessage());
        }

        return resultado;
    }
}
