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
public class CalcularMediaHoraExtra50PrimeiraParcela13Service {
    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaHoraExtra50PrimeiraParcela13Service.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularMediaHE50PrimeiraParcela13() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();

            // ✅ Chamar métudo do CalculoBaseService
            Map<String, BigDecimal> resultadoHE50 = calculoBaseService.calcularMediaHE50PrimeiraParcela13(numeroMatricula, salarioPorHora);

            // ✅ Atualiza o resultado principal
            resultado.putAll(resultadoHE50);

            logger.info("Média HE 50% 1ª parcela 13º calculada para {}: R$ {}", numeroMatricula, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular média HE 50% 1ª parcela 13º para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 50% Sobre 1° Parcela do 13°: " + e.getMessage());
        }

        return resultado;
    }
}