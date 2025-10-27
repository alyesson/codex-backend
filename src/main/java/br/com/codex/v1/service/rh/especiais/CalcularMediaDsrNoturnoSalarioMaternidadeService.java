package br.com.codex.v1.service.rh.especiais;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularMediaDsrNoturnoSalarioMaternidadeService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaDsrNoturnoSalarioMaternidadeService.class);

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularMediaDSRNoturnoSalarioMaternidade() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            LocalDate dataLimite = LocalDate.now().minusMonths(6);

            // ✅ Buscar média do DSR Noturno (evento 25) dos últimos 6 meses
            BigDecimal mediaDsrNoturnoSalarioMaternidade = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 25, dataLimite);
            BigDecimal valorDsrNoturnoSalarioMaternidade = mediaDsrNoturnoSalarioMaternidade.setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", BigDecimal.ZERO);
            resultado.put("vencimentos", valorDsrNoturnoSalarioMaternidade);

            logger.info("Média DSR Noturno salário maternidade para {}: R$ {}", numeroMatricula, valorDsrNoturnoSalarioMaternidade);

        } catch (Exception e) {
            logger.error("Erro ao calcular média DSR Noturno salário maternidade para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Média de DSR Noturno sobre Salário Maternidade: " + e.getMessage());
        }

        return resultado;
    }
}
