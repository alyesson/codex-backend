package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class CalcularMediaPericulosidade12Meses {
    private static final Logger logger = LoggerFactory.getLogger(CalcularMediaPericulosidade12Meses.class);

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Setter
    String numeroMatricula;

    public BigDecimal calcularMediaPericulosidade12Meses(LocalDate dataRescisao, LocalDate dataAdmissao, BigDecimal salarioBase) {
        try {
            LocalDate dataInicio = dataRescisao.minusMonths(12);

            // Se admitido há menos de 1 ano, ajusta a data início
            if (dataAdmissao.isAfter(dataInicio)) {
                dataInicio = dataAdmissao;
            }

            // **OPÇÃO A: Buscar a SOMA dos valores históricos de periculosidade**
            BigDecimal somaPericulosidade = folhaMensalEventosCalculadaRepository.findSomaPericulosidadePeriodo(numeroMatricula, 47, dataInicio, dataRescisao);

            // Se não encontrou valores históricos, calcular com base no salário atual
            if (somaPericulosidade.compareTo(BigDecimal.ZERO) == 0) {
                // **OPÇÃO B: Calcular periculosidade como 30% do salário base**
                BigDecimal periculosidadeMensal = salarioBase.multiply(new BigDecimal("0.30"));

                // Calcular quantos meses considerar (máximo 12)
                long mesesParaMedia = Math.min(12, ChronoUnit.MONTHS.between(dataInicio, dataRescisao) + 1);
                somaPericulosidade = periculosidadeMensal.multiply(BigDecimal.valueOf(mesesParaMedia));
            }

            // Calcular a média
            long mesesParaMedia = Math.min(12, ChronoUnit.MONTHS.between(dataInicio, dataRescisao) + 1);
            BigDecimal mediaPericulosidade = somaPericulosidade.divide(BigDecimal.valueOf(mesesParaMedia), 2, RoundingMode.HALF_UP);

            logger.info("Média periculosidade para {}: R$ {} (soma: R$ {}, meses: {})", numeroMatricula, mediaPericulosidade, somaPericulosidade, mesesParaMedia);
            return mediaPericulosidade;

        } catch (Exception e) {
            logger.warn("Erro ao calcular média de periculosidade para {}, usando cálculo padrão: {}", numeroMatricula, e.getMessage());

            // Fallback: calcular como 30% do salário base
            return salarioBase.multiply(new BigDecimal("0.30"));
        }
    }
}
