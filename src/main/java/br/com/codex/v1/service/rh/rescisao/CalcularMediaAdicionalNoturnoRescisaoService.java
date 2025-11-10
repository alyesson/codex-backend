package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
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
public class CalcularMediaAdicionalNoturnoRescisaoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaAdicionalNoturnoRescisaoService.class);

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularMediaAdicionalNoturnoRescisao() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            LocalDate dataDemissao = rescisao.getDataDeDemissao();
            LocalDate dataAdmissao = rescisao.getDataDeAdmissao();

            // ✅ 1. Calcular a média do adicional noturno dos últimos 12 meses
            BigDecimal mediaAdicionalNoturno = calcularMediaAdicionalNoturno12Meses(dataDemissao, dataAdmissao);

            resultado.put("referencia", BigDecimal.ONE);
            resultado.put("vencimentos", mediaAdicionalNoturno);
            resultado.put("descontos", BigDecimal.ZERO);
            resultado.put("mediaAdicionalNoturno", mediaAdicionalNoturno);

            logger.info("Média adicional noturno calculada para {}: R$ {}", numeroMatricula, mediaAdicionalNoturno);
        } catch (Exception e) {
            logger.error("Erro ao calcular média adicional noturno para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Média do Adicional Noturno: " + e.getMessage());
        }
        return resultado;
    }

    /**
     * Calcula a média do adicional noturno dos últimos 12 meses (ou período menor)
     */
    private BigDecimal calcularMediaAdicionalNoturno12Meses(LocalDate dataDemissao, LocalDate dataAdmissao) {
        // ✅ Período: últimos 12 meses ou desde a admissão (se menor)
        LocalDate dataInicio = dataDemissao.minusMonths(12);

        // Se admitido há menos de 1 ano, ajusta a data início
        if (dataAdmissao.isAfter(dataInicio)) {
            dataInicio = dataAdmissao;
        }

        // ✅ Busca a SOMA do adicional noturno (evento 14) no período
        BigDecimal somaAdicionalNoturno = folhaMensalEventosCalculadaRepository.findSomaAdicionalNoturnoPeriodo(numeroMatricula, 14, dataInicio, dataDemissao);

        // ✅ Calcula quantos meses considerar para a média
        long mesesParaMedia = calcularMesesParaMedia(dataInicio, dataDemissao);

        // ✅ Calcula a média mensal: soma ÷ meses
        BigDecimal mediaAdicionalNoturno = mesesParaMedia > 0 ? somaAdicionalNoturno.divide(BigDecimal.valueOf(mesesParaMedia), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        logger.info("Média adicional noturno para {}: Soma: R$ {}, Meses: {}, Média: R$ {}", numeroMatricula, somaAdicionalNoturno, mesesParaMedia, mediaAdicionalNoturno);
        return mediaAdicionalNoturno;
    }

    /**
     * Calcula quantos meses considerar para a média
     */
    private long calcularMesesParaMedia(LocalDate dataInicio, LocalDate dataFim) {
        return ChronoUnit.MONTHS.between(dataInicio, dataFim) + 1; // +1 para incluir ambos os meses
    }
}
