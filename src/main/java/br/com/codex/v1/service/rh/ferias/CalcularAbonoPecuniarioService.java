package br.com.codex.v1.service.rh.ferias;

import br.com.codex.v1.domain.rh.FolhaFerias;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularAbonoPecuniarioService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularAbonoPecuniarioService.class);

    //@Autowired
    //private CalculoBaseService calculoBaseService;

    @Autowired
    private CalcularFeriasService calcularFeriasService;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularAbonoPecuniario(Integer diasVendidos) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            // Validar dias vendidos
            if (diasVendidos == null || diasVendidos <= 0 || diasVendidos > 10) {
                logger.warn("Dias de abono pecuniário inválidos: {}", diasVendidos);
                return resultado;
            }

            // Obter dados do colaborador
            FolhaFerias folha = calcularFeriasService.findByMatriculaFuncionario(numeroMatricula);
            BigDecimal salarioMensal = folha.getSalarioBruto();

            // Calcular abono pecuniário
            BigDecimal valorAbono = calcularValorAbonoPecuniario(salarioMensal, diasVendidos);

            resultado.put("referencia", new BigDecimal(diasVendidos));
            resultado.put("vencimentos", valorAbono);
            logger.info("Abono pecuniário calculado: R$ {} para {} dias (Salário: R$ {})", valorAbono, diasVendidos, salarioMensal);

        } catch (Exception e) {
            logger.error("Erro ao calcular abono pecuniário para matrícula {}", numeroMatricula, e);
        }
        return resultado;
    }

    /**
     * Calcula o valor do abono pecuniário conforme regra:
     * 1. Valor dos dias = (Salário / 30) × dias vendidos
     * 2. Adicional de 1/3 = Valor dos dias ÷ 3
     * 3. Total = Valor dos dias + Adicional de 1/3
     */
    private BigDecimal calcularValorAbonoPecuniario(BigDecimal salarioMensal, Integer diasVendidos) {
        // 1. Calcular valor dos dias vendidos
        BigDecimal valorDia = salarioMensal.divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP);
        BigDecimal valorDiasVendidos = valorDia.multiply(new BigDecimal(diasVendidos));

        // 2. Calcular adicional de 1/3 constitucional
        BigDecimal umTerco = new BigDecimal("1").divide(new BigDecimal("3"), 4, RoundingMode.HALF_UP);
        BigDecimal adicionalUmTerco = valorDiasVendidos.multiply(umTerco);

        // 3. Calcular valor total do abono
        BigDecimal valorTotalAbono = valorDiasVendidos.add(adicionalUmTerco);
        logger.debug("Cálculo detalhado - Valor dia: R$ {}, Dias vendidos: {}, " + "Valor dias: R$ {}, Adicional 1/3: R$ {}, Total: R$ {}", valorDia, diasVendidos, valorDiasVendidos, adicionalUmTerco, valorTotalAbono);
        return valorTotalAbono.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Métudo sobrecarregado para calcular com dias padrão (máximo 10)
     */
    public Map<String, BigDecimal> calcularAbonoPecuniario() {
        // Por padrão, calcula com 10 dias (máximo permitido)
        return calcularAbonoPecuniario(10);
    }

    /**
     * Valida se o colaborador pode vender dias de férias
     */
    public boolean podeVenderAbonoPecuniario(Integer diasVendidos) {
        if (diasVendidos == null || diasVendidos <= 0 || diasVendidos > 10) {
            return false;
        }

        try {
            FolhaFerias folha = calcularFeriasService.findByMatriculaFuncionario(numeroMatricula);

            // Verificar se tem pelo menos 15 dias de férias para poder vender
            // (considerando que precisa manter mínimo de 15 dias de férias)
            Integer diasFeriasTotais = folha.getTotalDiasAbono(); // Supondo que existe este campo
            return diasFeriasTotais != null && diasFeriasTotais >= (15 + diasVendidos);

        } catch (Exception e) {
            logger.error("Erro ao validar abono pecuniário", e);
            return false;
        }
    }
}