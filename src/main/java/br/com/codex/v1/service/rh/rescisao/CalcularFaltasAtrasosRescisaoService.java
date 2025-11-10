package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.rh.FolhaRescisao;
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
public class CalcularFaltasAtrasosRescisaoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularFaltasAtrasosRescisaoService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    /**
     * ✅ Calcula o desconto por faltas no saldo de salário
     */
    public Map<String, BigDecimal> calcularFaltasAtrasosRescisao() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal salarioBase = rescisao.getSalarioBase();
            Integer diasTrabalhados = rescisao.getDiasTrabalhadosNoMes();
            Integer faltas = rescisao.getFaltasNoMes();

            if (faltas == null) faltas = 0;
            if (diasTrabalhados == null) diasTrabalhados = 0;

            // ✅ 1. Calcular valor do dia de trabalho
            BigDecimal valorDia = salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);

            // ✅ 2. Calcular desconto por faltas (cada falta = 1 dia de salário)
            BigDecimal descontoFaltas = valorDia.multiply(new BigDecimal(faltas));

            // ✅ 3. Calcular saldo de salário bruto (dias trabalhados × valor dia)
            BigDecimal saldoSalarioBruto = valorDia.multiply(new BigDecimal(diasTrabalhados));

            resultado.put("referencia", new BigDecimal(faltas));
            resultado.put("vencimentos", BigDecimal.ZERO); // Faltas são descontos, não vencimentos
            resultado.put("descontos", descontoFaltas);
            /*resultado.put("valorDia", valorDia);
            resultado.put("diasTrabalhados", new BigDecimal(diasTrabalhados));
            resultado.put("faltas", new BigDecimal(faltas));
            resultado.put("saldoSalarioBruto", saldoSalarioBruto);
            resultado.put("descontoFaltas", descontoFaltas);*/

            logger.info("Desconto por faltas para {}: Dias trab: {}, Faltas: {}, Valor dia: R$ {}, Desconto: R$ {}",
                    numeroMatricula, diasTrabalhados, faltas, valorDia, descontoFaltas);

        } catch (Exception e) {
            logger.error("Erro ao calcular desconto por faltas para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Desconto por Faltas: " + e.getMessage());
        }
        return resultado;
    }

    /**
     * ✅ Calcula o saldo de salário líquido já com desconto das faltas
     */
    public Map<String, BigDecimal> calcularSaldoSalarioComFaltas() {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal salarioBase = rescisao.getSalarioBase();
            Integer diasTrabalhados = rescisao.getDiasTrabalhadosNoMes();
            Integer faltas = rescisao.getFaltasNoMes();

            if (faltas == null) faltas = 0;
            if (diasTrabalhados == null) diasTrabalhados = 0;

            // ✅ 1. Valor do dia de trabalho
            BigDecimal valorDia = salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);

            // ✅ 2. Saldo bruto (dias trabalhados × valor dia)
            BigDecimal saldoBruto = valorDia.multiply(new BigDecimal(diasTrabalhados));

            // ✅ 3. Desconto por faltas
            BigDecimal descontoFaltas = valorDia.multiply(new BigDecimal(faltas));

            // ✅ 4. Saldo líquido (bruto - descontos)
            BigDecimal saldoLiquido = saldoBruto.subtract(descontoFaltas).max(BigDecimal.ZERO);

            resultado.put("referencia", new BigDecimal(diasTrabalhados));
            resultado.put("vencimentos", saldoLiquido); // Este é o valor a receber
            resultado.put("descontos", descontoFaltas);
            /*resultado.put("valorDia", valorDia);
            resultado.put("diasTrabalhados", new BigDecimal(diasTrabalhados));
            resultado.put("faltas", new BigDecimal(faltas));
            resultado.put("saldoBruto", saldoBruto);
            resultado.put("saldoLiquido", saldoLiquido);*/

            logger.info("Saldo salário com faltas para {}: Dias: {}, Faltas: {}, Bruto: R$ {}, Desconto: R$ {}, Líquido: R$ {}",
                    numeroMatricula, diasTrabalhados, faltas, saldoBruto, descontoFaltas, saldoLiquido);

        } catch (Exception e) {
            logger.error("Erro ao calcular saldo salário com faltas para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Saldo de Salário com Faltas: " + e.getMessage());
        }
        return resultado;
    }

    /**
     * ✅ Calcula apenas o valor do desconto das faltas
     */
    public BigDecimal calcularApenasDescontoFaltas() {
        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal salarioBase = rescisao.getSalarioBase();
            Integer faltas = rescisao.getFaltasNoMes();

            if (faltas == null || faltas == 0) {
                return BigDecimal.ZERO;
            }

            BigDecimal valorDia = salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);
            BigDecimal descontoFaltas = valorDia.multiply(new BigDecimal(faltas));

            logger.info("Apenas desconto faltas para {}: Faltas: {}, Desconto: R$ {}", numeroMatricula, faltas, descontoFaltas);

            return descontoFaltas;
        } catch (Exception e) {
            logger.error("Erro ao calcular apenas desconto por faltas para {}: {}", numeroMatricula, e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}
