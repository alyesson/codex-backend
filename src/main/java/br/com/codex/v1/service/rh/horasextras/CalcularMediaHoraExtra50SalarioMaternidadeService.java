package br.com.codex.v1.service.rh.horasextras;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.service.CalculoBaseService;
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
public class CalcularMediaHoraExtra50SalarioMaternidadeService {

    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaHoraExtra50SalarioMaternidadeService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularMediaHoraExtra50SalarioMaternidade() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            LocalDate dataLimite = LocalDate.now().minusMonths(6);

            // ✅ Buscar média das horas extras 50% dos últimos 6 meses
            BigDecimal mediaHoraExtraSalarioMaternidade50 = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 98, dataLimite);
            BigDecimal quantidadeMediaHoras = BigDecimal.ZERO;

            if (salarioPorHora != null && salarioPorHora.compareTo(BigDecimal.ZERO) > 0) {
                // ✅ Cálculo: quantidade de horas = valor / (salarioHora * 1.5)
                BigDecimal valorPorHoraComAdicional = salarioPorHora.multiply(new BigDecimal("1.5"));
                quantidadeMediaHoras = mediaHoraExtraSalarioMaternidade50.divide(valorPorHoraComAdicional, 2, RoundingMode.HALF_UP);
            }

            BigDecimal total = mediaHoraExtraSalarioMaternidade50.setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", quantidadeMediaHoras);  // quantidade média de horas
            resultado.put("vencimentos", total);               // média em R$

            logger.info("Média HE 50% salário maternidade para {}: {} horas = R$ {}", numeroMatricula, quantidadeMediaHoras, total);

        } catch (Exception e) {
            logger.error("Erro ao calcular média HE 50% salário maternidade para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Média Horas Extra 50% sobre o Salário Maternidade: " + e.getMessage());
        }

        return resultado;
    }
}