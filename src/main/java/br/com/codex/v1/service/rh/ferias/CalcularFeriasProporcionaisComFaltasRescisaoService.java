package br.com.codex.v1.service.rh.ferias;

import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularFeriasProporcionaisComFaltasRescisaoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularFeriasProporcionaisComFaltasRescisaoService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularFeriasProporcionaisComFaltas() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal salarioBase = rescisao.getSalarioBase();
            LocalDate dataAdmissao = rescisao.getDataDeAdmissao();
            LocalDate dataDemissao = rescisao.getDataDeDemissao();
            Integer faltas = rescisao.getFaltasNoMes(); // Total de faltas injustificadas

            // ✅ 1. Calcular meses trabalhados no período aquisitivo
            int mesesTrabalhados = calcularMesesProporcionais(dataAdmissao, dataDemissao);

            // ✅ 2. Determinar dias de férias com base nas faltas
            int diasFerias = determinarDiasFeriasPorFaltas(faltas);

            // ✅ 3. Calcular valor das férias proporcionais
            Map<String, BigDecimal> calculoFerias = calcularValorFeriasProporcionais(salarioBase, mesesTrabalhados, diasFerias);

            resultado.putAll(calculoFerias);
            /*resultado.put("mesesTrabalhados", new BigDecimal(mesesTrabalhados));
            resultado.put("diasFerias", new BigDecimal(diasFerias));
            resultado.put("faltasConsideradas", new BigDecimal(faltas));*/

            logger.info("Férias proporcionais com faltas calculadas para {}: {} meses, {} faltas, {} dias, Valor: R$ {}",
                    numeroMatricula, mesesTrabalhados, faltas, diasFerias, resultado.get("vencimentos"));

        } catch (Exception e) {
            logger.error("Erro ao calcular férias proporcionais com faltas para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Férias Proporcionais com Faltas: " + e.getMessage());
        }
        return resultado;
    }

    /**
     * ✅ Determina os dias de férias com base nas faltas injustificadas
     */
    private int determinarDiasFeriasPorFaltas(Integer faltas) {
        if (faltas == null) faltas = 0;

        // Tabela de conversão de faltas para dias de férias
        if (faltas <= 5) {
            return 30; // Até 5 faltas: 30 dias
        } else if (faltas >= 6 && faltas <= 14) {
            return 24; // 6-14 faltas: 24 dias
        } else if (faltas >= 15 && faltas <= 23) {
            return 18; // 15-23 faltas: 18 dias
        } else if (faltas >= 24 && faltas <= 32) {
            return 12; // 24-32 faltas: 12 dias
        } else {
            return 0; // Mais de 32 faltas: perde o direito
        }
    }

    /**
     * ✅ Calcula o valor das férias proporcionais
     */
    private Map<String, BigDecimal> calcularValorFeriasProporcionais(BigDecimal salarioBase, int mesesTrabalhados, int diasFerias) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        // Se perdeu o direito às férias
        if (diasFerias == 0) {
            resultado.put("referencia", BigDecimal.ZERO);
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", BigDecimal.ZERO);
            return resultado;
        }

        // ✅ 1. Calcular valor de um dia de trabalho
        BigDecimal valorDia = salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);

        // ✅ 2. Calcular férias proporcionais (salário ÷ 12 × meses)
        BigDecimal feriasProporcionais = salarioBase
                .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(mesesTrabalhados));

        // ✅ 3. Ajustar pelos dias de férias (proporção)
        // Ex: se tem direito a 24 dias em vez de 30, aplica a proporção 24/30
        BigDecimal proporcaoDias = new BigDecimal(diasFerias)
                .divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP);

        BigDecimal valorFeriasAjustado = feriasProporcionais.multiply(proporcaoDias)
                .setScale(2, RoundingMode.HALF_UP);

        // ✅ 4. Calcular terço constitucional (1/3)
        BigDecimal tercoConstitucional = valorFeriasAjustado
                .divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);

        // ✅ 5. Valor total (férias + terço)
        BigDecimal valorTotal = valorFeriasAjustado.add(tercoConstitucional);

        resultado.put("referencia", new BigDecimal(mesesTrabalhados));
        resultado.put("vencimentos", valorTotal);
        resultado.put("descontos", BigDecimal.ZERO);
        return resultado;
    }

    /**
     * ✅ Calcula meses proporcionais (regra dos 15 dias)
     */
    private int calcularMesesProporcionais(LocalDate dataAdmissao, LocalDate dataDemissao) {
        long meses = ChronoUnit.MONTHS.between(dataAdmissao, dataDemissao);

        // Verifica se trabalhou 15+ dias no último mês
        LocalDate inicioUltimoMes = dataDemissao.withDayOfMonth(1);
        long diasTrabalhadosUltimoMes = ChronoUnit.DAYS.between(inicioUltimoMes, dataDemissao);

        if (diasTrabalhadosUltimoMes >= 15) {
            meses++; // Conta o mês completo
        }

        return (int) meses;
    }

    /**
     * ✅ Métudo alternativo: cálculo por dias específicos
     */
    /*private Map<String, BigDecimal> calcularFeriasPorDiasEspecificos(BigDecimal salarioBase, int mesesTrabalhados, int diasFerias) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        if (diasFerias == 0) {
            resultado.put("referencia", BigDecimal.ZERO);
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", BigDecimal.ZERO);
            return resultado;
        }

        // ✅ 1. Valor de um dia de trabalho
        BigDecimal valorDia = salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);

        // ✅ 2. Valor das férias (dias × valor dia)
        BigDecimal valorFerias = valorDia.multiply(new BigDecimal(diasFerias));

        // ✅ 3. Terço constitucional
        BigDecimal tercoConstitucional = valorFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);

        // ✅ 4. Valor total
        BigDecimal valorTotal = valorFerias.add(tercoConstitucional);

        // ✅ 5. Proporcional pelos meses trabalhados
        BigDecimal proporcaoMeses = new BigDecimal(mesesTrabalhados)
                .divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP);

        BigDecimal valorFinal = valorTotal.multiply(proporcaoMeses).setScale(2, RoundingMode.HALF_UP);

        resultado.put("referencia", new BigDecimal(diasFerias));
        resultado.put("vencimentos", valorFinal);
        resultado.put("descontos", BigDecimal.ZERO);
        resultado.put("valorDia", valorDia);
        resultado.put("valorFeriasIntegral", valorTotal);
        resultado.put("proporcaoMeses", proporcaoMeses);

        return resultado;
    }*/
}
