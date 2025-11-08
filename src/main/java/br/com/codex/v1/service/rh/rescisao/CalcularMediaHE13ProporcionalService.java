package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularMediaHE13ProporcionalService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaHE13ProporcionalService.class);

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularMediaHE13Proporcional() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaRescisao folha = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            LocalDate dataAdmissao = folha.getDataDeAdmissao();

            // ✅ Data da rescisão (LocalDate.now() no seu contexto)
            LocalDate dataRescisao = LocalDate.now();

            // ✅ Calcular meses trabalhados no ano
            int mesesTrabalhados = calcularMesesTrabalhadosAno(dataAdmissao, dataRescisao);

            // ✅ Calcular média das HE para o 13º proporcional
            Map<String, BigDecimal> mediaHE = calcularMediaHorasExtras13(dataRescisao, mesesTrabalhados);

            // ✅ Calcular valor monetário das HE
            BigDecimal valorTotalHE = calcularValorTotalHE(mediaHE, salarioPorHora);

            // ✅ 50% para primeira parcela do 13º
            BigDecimal primeiraParcela = valorTotalHE.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", valorTotalHE);
            resultado.put("descontos", BigDecimal.ZERO);

            logger.info("Média HE 13º proporcional para {}: {} meses, HE50: {}, HE70: {}, HE100: {}, Valor: R$ {}",
                    numeroMatricula, mesesTrabalhados, mediaHE.get("mediaHE50"), mediaHE.get("mediaHE70"), mediaHE.get("mediaHE100"), primeiraParcela);

        } catch (Exception e) {
            logger.error("Erro ao calcular média HE 13º proporcional para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 13º Proporcional: " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Calcula a média das horas extras para o 13º proporcional
     */
    private Map<String, BigDecimal> calcularMediaHorasExtras13(LocalDate dataRescisao, int mesesTrabalhados) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        // ✅ Define o período: 1º de janeiro até a data da rescisão
        LocalDate dataInicio = LocalDate.of(dataRescisao.getYear(), 1, 1); // 01/01/ano

        // ✅ Busca soma das HE do período usando o métudo por data
        Map<String, BigDecimal> somaHE = folhaMensalEventosCalculadaRepository.findSomaHorasExtrasPorData(numeroMatricula, dataInicio, dataRescisao);

        // ✅ Calcula a média mensal (soma ÷ meses trabalhados)
        BigDecimal mediaHE50 = mesesTrabalhados > 0 ?somaHE.get("he50").divide(BigDecimal.valueOf(mesesTrabalhados), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal mediaHE70 = mesesTrabalhados > 0 ? somaHE.get("he70").divide(BigDecimal.valueOf(mesesTrabalhados), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal mediaHE100 = mesesTrabalhados > 0 ? somaHE.get("he100").divide(BigDecimal.valueOf(mesesTrabalhados), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        resultado.put("mediaHE50", mediaHE50);
        resultado.put("mediaHE70", mediaHE70);
        resultado.put("mediaHE100", mediaHE100);
        resultado.put("somaHE50", somaHE.get("he50"));
        resultado.put("somaHE70", somaHE.get("he70"));
        resultado.put("somaHE100", somaHE.get("he100"));

        return resultado;
    }

    /**
     * Calcula o valor monetário total das horas extras
     */
    private BigDecimal calcularValorTotalHE(Map<String, BigDecimal> mediaHE, BigDecimal salarioPorHora) {

        BigDecimal valorHE50 = mediaHE.get("mediaHE50").multiply(salarioPorHora).multiply(new BigDecimal("1.5"));
        BigDecimal valorHE70 = mediaHE.get("mediaHE70").multiply(salarioPorHora).multiply(new BigDecimal("1.7"));
        BigDecimal valorHE100 = mediaHE.get("mediaHE100").multiply(salarioPorHora).multiply(new BigDecimal("2.0"));

        BigDecimal valorTotal = valorHE50.add(valorHE70).add(valorHE100).setScale(2, RoundingMode.HALF_UP);

        logger.debug("Valor HE: HE50: R$ {}, HE70: R$ {}, HE100: R$ {}, Total: R$ {}", valorHE50, valorHE70, valorHE100, valorTotal);
        return valorTotal;
    }

    /**
     * Calcula meses trabalhados no ano da rescisão
     */
    private int calcularMesesTrabalhadosAno(LocalDate dataAdmissao, LocalDate dataRescisao) {
        int anoRescisao = dataRescisao.getYear();

        // Se admitido em ano anterior, conta todos os meses até a rescisão
        if (dataAdmissao.getYear() < anoRescisao) {
            return dataRescisao.getMonthValue(); // Janeiro até mês da rescisão
        }

        // Se admitido no mesmo ano
        if (dataAdmissao.getYear() == anoRescisao) {
            int mesAdmissao = dataAdmissao.getMonthValue();
            int mesRescisao = dataRescisao.getMonthValue();

            // Conta mês se trabalhou pelo menos 15 dias
            boolean contaMesAdmissao = dataAdmissao.getDayOfMonth() <= 15;

            if (contaMesAdmissao) {
                return (mesRescisao - mesAdmissao) + 1;
            } else {
                return Math.max(0, mesRescisao - mesAdmissao);
            }
        }

        return 0; // Admitido no futuro
    }
}
