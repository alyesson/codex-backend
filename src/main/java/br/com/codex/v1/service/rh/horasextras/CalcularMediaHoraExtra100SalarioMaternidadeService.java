package br.com.codex.v1.service.rh.horasextras;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.service.rh.CalculoBaseService;
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
public class CalcularMediaHoraExtra100SalarioMaternidadeService {

    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaHoraExtra100SalarioMaternidadeService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularMediaHoraExtra100SalarioMaternidade() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            LocalDate dataLimite = LocalDate.now().minusMonths(6);

            // ✅ Buscar média das horas extras 100% dos últimos 6 meses
            BigDecimal mediaHoraExtraSalarioMaternidade100 = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 100, dataLimite);
            BigDecimal quantidadeMediaHoras = BigDecimal.ZERO;

            if (salarioPorHora != null && salarioPorHora.compareTo(BigDecimal.ZERO) > 0) {
                // ✅ Cálculo: quantidade de horas = valor / (salarioHora * 2.0)
                BigDecimal adicional = new BigDecimal("2.0");
                quantidadeMediaHoras = mediaHoraExtraSalarioMaternidade100.divide(salarioPorHora.multiply(adicional), 2, RoundingMode.HALF_UP);
            }

            BigDecimal total = mediaHoraExtraSalarioMaternidade100.setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", quantidadeMediaHoras);  // quantidade média de horas
            resultado.put("vencimentos", total);               // média em R$

            logger.info("Média HE 100% salário maternidade para {}: {} horas = R$ {}", numeroMatricula, quantidadeMediaHoras, total);

        } catch (Exception e) {
            logger.error("Erro ao calcular média HE 100% salário maternidade para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Média Horas Extra 100% sobre o Salário Maternidade: " + e.getMessage());
        }
        return resultado;
    }
}