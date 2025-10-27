package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.CalculoBaseService;
import br.com.codex.v1.service.TabelaDeducaoInssService;
import br.com.codex.v1.service.TabelaImpostoRendaService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularProLaboreService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaService tabelaImpostoRendaService;

    @Autowired
    private TabelaDeducaoInssService tabelaDeducaoInssService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularProLabore() {
        Map<String, BigDecimal> impostos = new HashMap<>();

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal valorProLabore = folha.getSalarioBase();

            if (valorProLabore == null || valorProLabore.compareTo(BigDecimal.ZERO) <= 0) {
                impostos.put("inss", BigDecimal.ZERO);
                impostos.put("irrf", BigDecimal.ZERO);
                impostos.put("baseCalculoINSS", BigDecimal.ZERO);
                impostos.put("baseCalculoIRRF", BigDecimal.ZERO);
                return impostos;
            }

            BigDecimal inssProLabore = calcularINSSProLabore(valorProLabore);
            BigDecimal baseCalculoIRRF = valorProLabore.subtract(inssProLabore).setScale(2, RoundingMode.HALF_UP);
            BigDecimal irrfProLabore = calcularIRRFProLabore(baseCalculoIRRF);

            impostos.put("inss", inssProLabore);
            impostos.put("irrf", irrfProLabore);
            impostos.put("baseCalculoINSS", valorProLabore);
            impostos.put("baseCalculoIRRF", baseCalculoIRRF);
            impostos.put("valorLiquido", valorProLabore.subtract(inssProLabore).subtract(irrfProLabore));
        } catch (Exception e) {
            impostos.put("inss", BigDecimal.ZERO);
            impostos.put("irrf", BigDecimal.ZERO);
            impostos.put("baseCalculoINSS", BigDecimal.ZERO);
            impostos.put("baseCalculoIRRF", BigDecimal.ZERO);
            impostos.put("valorLiquido", BigDecimal.ZERO);
        }

        return impostos;
    }

    private BigDecimal calcularINSSProLabore(BigDecimal valorProLabore) {
        try {
            BigDecimal[] faixasINSS = {
                    new BigDecimal("1412.00"),    // 1ª faixa
                    new BigDecimal("2666.68"),    // 2ª faixa
                    new BigDecimal("4000.03"),    // 3ª faixa
                    new BigDecimal("7786.02")     // 4ª faixa (teto)
            };

            BigDecimal[] aliquotas = {
                    new BigDecimal("7.5"),        // 7.5%
                    new BigDecimal("9.0"),        // 9.0%
                    new BigDecimal("12.0"),       // 12.0%
                    new BigDecimal("14.0")        // 14.0%
            };

            BigDecimal inssCalculado = BigDecimal.ZERO;
            BigDecimal valorRestante = valorProLabore;

            // Cálculo progressivo
            for (int i = 0; i < faixasINSS.length && valorRestante.compareTo(BigDecimal.ZERO) > 0; i++) {
                BigDecimal faixaAtual = faixasINSS[i];
                BigDecimal aliquota = aliquotas[i];

                if (i == 0 || valorProLabore.compareTo(faixasINSS[i-1]) > 0) {
                    BigDecimal baseFaixa;

                    if (i == faixasINSS.length - 1) {
                        // Última faixa - aplica até o teto
                        baseFaixa = valorRestante.min(faixaAtual.subtract(i > 0 ? faixasINSS[i-1] : BigDecimal.ZERO));
                    } else {
                        baseFaixa = valorRestante.min(faixaAtual.subtract(i > 0 ? faixasINSS[i-1] : BigDecimal.ZERO));
                    }

                    BigDecimal contribuicaoFaixa = baseFaixa.multiply(aliquota)
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

                    inssCalculado = inssCalculado.add(contribuicaoFaixa);
                    valorRestante = valorRestante.subtract(baseFaixa);
                }
            }

            // ✅ Limitar ao teto do INSS (valor fixo)
            BigDecimal tetoINSS = new BigDecimal("828.39"); // Teto atual (ajuste conforme ano)
            return inssCalculado.min(tetoINSS).setScale(2, RoundingMode.HALF_UP);

        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal calcularIRRFProLabore(BigDecimal baseCalculo) {
        try {
            if (baseCalculo.compareTo(BigDecimal.ZERO) <= 0) {
                return BigDecimal.ZERO;
            }

            // ✅ Tabela IRRF 2024 (ajuste conforme ano vigente)
            BigDecimal[] faixasIRRF = { new BigDecimal("2259.20"), new BigDecimal("2826.65"), new BigDecimal("3751.05"), new BigDecimal("4664.68")};
            BigDecimal[] aliquotas = { BigDecimal.ZERO, new BigDecimal("7.5"),new BigDecimal("15.0"), new BigDecimal("22.5"), new BigDecimal("27.5")};
            BigDecimal[] deducoes = { BigDecimal.ZERO,new BigDecimal("169.44"), new BigDecimal("381.44"),new BigDecimal("662.77"),new BigDecimal("896.00")};

            int faixa = 0;
            for (int i = 0; i < faixasIRRF.length; i++) {
                if (baseCalculo.compareTo(faixasIRRF[i]) > 0) {
                    faixa = i + 1;
                }
            }

            if (faixa == 0) {
                return BigDecimal.ZERO; // Isento
            }

            BigDecimal aliquota = aliquotas[faixa];
            BigDecimal deducao = deducoes[faixa];

            BigDecimal irrf = baseCalculo.multiply(aliquota)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).subtract(deducao).setScale(2, RoundingMode.HALF_UP);
            return irrf.max(BigDecimal.ZERO);

        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
