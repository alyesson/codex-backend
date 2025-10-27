package br.com.codex.v1.service.rh.especiais;

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
public class CalcularMediaAdicionalNoturnoSalarioMaternidadeService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaAdicionalNoturnoSalarioMaternidadeService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularMediaAdicionalNoturnoSalarioMaternidade() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();

            LocalDate dataLimite = LocalDate.now().minusMonths(6);

            // ✅ Buscar média do Adicional Noturno (evento 14) dos últimos 6 meses
            BigDecimal mediaAdicionalNoturnoSalarioMaternidade = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 14, dataLimite);

            // ✅ Aplicar adicional de 20% sobre a média
            BigDecimal valorComAdicional = mediaAdicionalNoturnoSalarioMaternidade.multiply(new BigDecimal("1.20")).setScale(2, RoundingMode.HALF_UP);

            // ✅ Calcular quantidade de horas que a média representa
            BigDecimal horasReferencia = BigDecimal.ZERO;
            if (salarioPorHora != null && salarioPorHora.compareTo(BigDecimal.ZERO) > 0) {
                // Adicional noturno: valor = horas * salarioHora * 0.20
                // Para achar horas: horas = valor / (salarioHora * 0.20)
                horasReferencia = mediaAdicionalNoturnoSalarioMaternidade.divide(salarioPorHora.multiply(new BigDecimal("0.20")), 2, RoundingMode.HALF_UP);
            }

            resultado.put("referencia", horasReferencia);
            resultado.put("vencimentos", valorComAdicional);

            logger.info("Média Adicional Noturno salário maternidade para {}: {} horas = R$ {}", numeroMatricula, horasReferencia, valorComAdicional);

        } catch (Exception e) {
            logger.error("Erro ao calcular média adicional noturno salário maternidade para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Média de Adicional Noturno sobre Salário Maternidade: " + e.getMessage());
        }

        return resultado;
    }
}
