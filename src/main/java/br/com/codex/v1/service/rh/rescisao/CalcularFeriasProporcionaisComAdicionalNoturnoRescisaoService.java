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
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularFeriasProporcionaisComAdicionalNoturnoRescisaoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularFeriasProporcionaisComAdicionalNoturnoRescisaoService.class);

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Autowired
    private CalcularFeriasRescisaoService calcularDiasFeriasPorFaltas;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularFeriasProporcionaisComAdicionalNoturno(BigDecimal salarioBase, LocalDate dataAdmissao, LocalDate dataDemissao, Integer faltas, String tipoSalario) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            Period periodo = Period.between(dataAdmissao, dataDemissao);
            int mesesTrabalhados = periodo.getMonths();
            int dias = periodo.getDays();

            // Se trabalhou mais de 15 dias no mês, conta o mês
            if (dias >= 15) {
                mesesTrabalhados++;
            }

            // Aplica redução por faltas
            int diasFerias = calcularDiasFeriasPorFaltas.calcularDiasFeriasPorFaltas(faltas);

            // **PASSO 1: Calcular a MÉDIA do adicional noturno do período**
            BigDecimal mediaAdicionalNoturno = calcularMediaAdicionalNoturnoPeriodo(dataDemissao, dataAdmissao);

            // **PASSO 2: Calcular a base de cálculo (salário + adicional noturno)**
            BigDecimal baseCalculo = salarioBase.add(mediaAdicionalNoturno);

            // **PASSO 3: Calcular o valor das férias proporcionais**
            // (base cálculo ÷ 12 × meses trabalhados)
            BigDecimal valorFeriasProporcionais = baseCalculo.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(mesesTrabalhados));

            // Ajustar pelos dias de férias (sua lógica original)
            BigDecimal valorFerias = valorFeriasProporcionais.multiply(BigDecimal.valueOf(diasFerias)).divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);

            // **PASSO 4: Calcular o TERÇO CONSTITUCIONAL**
            BigDecimal tercoConstitucional = valorFerias.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);

            // **VALOR TOTAL (férias + terço)**
            BigDecimal valorTotal = valorFerias.add(tercoConstitucional);

            // Resultado
            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", valorTotal); // Já inclui o terço

            logger.info("Férias proporcionais com adicional noturno para {}: {} meses, {} dias, Média Adic. Noturno: R$ {}, Valor: R$ {}", numeroMatricula, mesesTrabalhados, diasFerias, mediaAdicionalNoturno, valorTotal);
        } catch (Exception e) {
            logger.error("Erro ao calcular férias proporcionais com adicional noturno para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular férias proporcionais com adicional noturno: " + e.getMessage());
        }
        return resultado;
    }

    private BigDecimal calcularMediaAdicionalNoturnoPeriodo(LocalDate dataRescisao, LocalDate dataAdmissao) {
        try {
            LocalDate dataInicio = dataRescisao.minusMonths(12);

            // Se admitido há menos de 1 ano, ajusta a data início para a data de admissão
            if (dataAdmissao.isAfter(dataInicio)) {
                dataInicio = dataAdmissao;
            }

            // **SOMA dos valores de adicional noturno do período**
            // Código 14 é para adicional noturno - confirme na sua base
            BigDecimal somaAdicionalNoturno = folhaMensalEventosCalculadaRepository.findSomaAdicionalNoturnoPeriodo(numeroMatricula, 14, dataInicio, dataRescisao);

            // **Calcula quantos meses considerar para a média**
            long mesesPeriodo = ChronoUnit.MONTHS.between(dataInicio, dataRescisao) + 1;

            // Se não encontrou valores históricos, retorna zero
            if (somaAdicionalNoturno.compareTo(BigDecimal.ZERO) == 0) {
                logger.info("Não foram encontrados valores de adicional noturno para {} no período", numeroMatricula);
                return BigDecimal.ZERO;
            }

            // **Calcula a média (soma ÷ meses do período)**
            BigDecimal mediaAdicionalNoturno = somaAdicionalNoturno.divide(BigDecimal.valueOf(mesesPeriodo), 2, RoundingMode.HALF_UP);

            logger.info("Média adicional noturno para {}: R$ {} (soma: R$ {}, meses: {})",numeroMatricula, mediaAdicionalNoturno, somaAdicionalNoturno, mesesPeriodo);
            return mediaAdicionalNoturno;

        } catch (Exception e) {
            logger.warn("Erro ao calcular média de adicional noturno para {}, retornando zero: {}", numeroMatricula, e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}
