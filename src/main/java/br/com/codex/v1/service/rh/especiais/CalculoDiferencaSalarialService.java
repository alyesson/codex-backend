package br.com.codex.v1.service.rh.especiais;

import br.com.codex.v1.domain.repository.FolhaMensalCalculadaRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalculoDiferencaSalarialService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoDiferencaSalarialService.class);

    @Autowired
    private FolhaMensalCalculadaRepository folhaMensalCalculadaRepository;

    @Setter
    private String numeroMatricula;

    /**
     * Calcula diferença salarial entre mês atual e anterior
     */
    public Map<String, BigDecimal> calcularDiferencaSalarial(Integer codigoEvento) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            // Buscar folhas do mês atual e anterior
            Map<String, BigDecimal> salarios = buscarSalariosMensais();
            BigDecimal salarioAtual = salarios.get("atual");
            BigDecimal salarioAnterior = salarios.get("anterior");

            if (salarioAtual == null || salarioAnterior == null) {
                logger.warn("Não foi possível encontrar salários para cálculo da diferença");
                return resultadoVazio();
            }

            switch (codigoEvento) {
                // Diferença Salarial Positiva (Provento)
                case 45 -> calcularDiferencaPositiva(resultado, salarioAtual, salarioAnterior);

                // Diferença Salarial Negativa (Desconto)
                case 260 -> calcularDiferencaNegativa(resultado, salarioAtual, salarioAnterior);

                default -> logger.warn("Código de evento não implementado: {}", codigoEvento);
            }

        } catch (Exception e) {
            logger.error("Erro ao calcular diferença salarial: {}", e.getMessage());
            return resultadoVazio();
        }

        return resultado;
    }

    /**
     * Busca salários do mês atual e anterior
     */
    private Map<String, BigDecimal> buscarSalariosMensais() {
        Map<String, BigDecimal> salarios = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();
            int mesAtual = hoje.getMonthValue();

            // Buscar mês atual
            Optional<FolhaMensal> folhaAtual = folhaMensalCalculadaRepository.findByMatriculaColaboradorAndMesAndAno(numeroMatricula, mesAtual, anoAtual);

            // Buscar mês anterior
            LocalDate mesAnteriorDate = hoje.minusMonths(1);
            int anoAnterior = mesAnteriorDate.getYear();
            int mesAnterior = mesAnteriorDate.getMonthValue();

            Optional<FolhaMensal> folhaAnterior = folhaMensalCalculadaRepository.findByMatriculaColaboradorAndMesAndAno(numeroMatricula, mesAnterior, anoAnterior);

            // Extrair salários
            if (folhaAtual.isPresent()) {
                salarios.put("atual", folhaAtual.get().getSalarioBase());
            }

            if (folhaAnterior.isPresent()) {
                salarios.put("anterior", folhaAnterior.get().getSalarioBase());
            }

        } catch (Exception e) {
            logger.error("Erro ao buscar salários mensais: {}", e.getMessage());
        }

        return salarios;
    }

    /**
     * Calcula diferença POSITIVA (Provento)
     */
    private void calcularDiferencaPositiva(Map<String, BigDecimal> resultado,
                                           BigDecimal salarioAtual,
                                           BigDecimal salarioAnterior) {

        if (salarioAtual.compareTo(salarioAnterior) > 0) {
            BigDecimal diferenca = salarioAtual.subtract(salarioAnterior);

            resultado.put("referencia", diferenca);
            resultado.put("vencimentos", diferenca); // ✅ PROVENTO
            resultado.put("descontos", BigDecimal.ZERO);

            logger.info("Diferença positiva calculada: R$ {}", diferenca);
        } else {
            // Se não há diferença positiva, retorna zeros
            resultado.put("referencia", BigDecimal.ZERO);
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", BigDecimal.ZERO);
        }
    }

    /**
     * Calcula diferença NEGATIVA (Desconto)
     */
    private void calcularDiferencaNegativa(Map<String, BigDecimal> resultado, BigDecimal salarioAtual, BigDecimal salarioAnterior) {

        if (salarioAtual.compareTo(salarioAnterior) < 0) {
            BigDecimal diferenca = salarioAnterior.subtract(salarioAtual);

            resultado.put("referencia", diferenca);
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", diferenca); // ✅ DESCONTO

            logger.info("Diferença negativa calculada: R$ {}", diferenca);
        } else {
            // Se não há diferença negativa, retorna zeros
            resultado.put("referencia", BigDecimal.ZERO);
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", BigDecimal.ZERO);
        }
    }

    private Map<String, BigDecimal> resultadoVazio() {
        Map<String, BigDecimal> vazio = new HashMap<>();
        vazio.put("referencia", BigDecimal.ZERO);
        vazio.put("vencimentos", BigDecimal.ZERO);
        vazio.put("descontos", BigDecimal.ZERO);
        return vazio;
    }
}
