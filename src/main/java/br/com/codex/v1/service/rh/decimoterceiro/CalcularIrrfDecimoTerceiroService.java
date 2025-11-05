package br.com.codex.v1.service.rh.decimoterceiro;

import br.com.codex.v1.domain.repository.TabelaDeducaoInssRepository;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.service.rh.CalculosAuxiliaresFolha;
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
import java.util.Optional;

@Service
public class CalcularIrrfDecimoTerceiroService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularIrrfDecimoTerceiroService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Autowired
    private TabelaDeducaoInssRepository tabelaDeducaoInssRepository;

    @Autowired
    private CalculosAuxiliaresFolha calculosAuxiliaresFolha;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularIrrfDecimoTerceiro() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            // 1. Calcular valor da 2ª parcela do 13º (base de cálculo do IRRF)
            BigDecimal baseCalculoIrrf = calcularBaseCalculoIrrf13o();

            if (baseCalculoIrrf.compareTo(BigDecimal.ZERO) <= 0) {
                return resultado;
            }

            // 2. Calcular IRRF sobre a base
            BigDecimal valorIrrf = calcularIrrfSobreBase(baseCalculoIrrf);
            resultado.put("referencia", baseCalculoIrrf.setScale(2, RoundingMode.HALF_UP));
            resultado.put("descontos", valorIrrf.setScale(2, RoundingMode.HALF_UP));
            logger.info("IRRF 13º calculado: R$ {} sobre base de R$ {}", valorIrrf, baseCalculoIrrf);

        } catch (Exception e) {
            logger.error("Erro ao calcular IRRF do 13º para matrícula {}", numeroMatricula, e);
        }
        return resultado;
    }

    /**
     * Calcula a base de cálculo do IRRF para a 2ª parcela do 13º
     */
    private BigDecimal calcularBaseCalculoIrrf13o() {
        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioBase = folha.getSalarioBase();

            LocalDate dataAdmissao = folha.getDataAdmissao();
            LocalDate hoje = LocalDate.now();
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);

            if (mesesTrabalhados == 0) {
                return BigDecimal.ZERO;
            }

            // 1. Calcular valor total do 13º
            BigDecimal valor13oMensal = salarioBase.divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP);
            BigDecimal valorTotal13o = valor13oMensal.multiply(new BigDecimal(mesesTrabalhados));

            // 2. Subtrair a 1ª parcela (já paga em novembro)
            BigDecimal primeiraParcela = valorTotal13o.multiply(new BigDecimal("0.5"));
            BigDecimal segundaParcela = valorTotal13o.subtract(primeiraParcela);

            // 3. Subtrair INSS da 2ª parcela (se houver)
            BigDecimal inssSegundaParcela = calcularInss13o(segundaParcela);
            BigDecimal baseCalculoIrrf = segundaParcela.subtract(inssSegundaParcela);

            // 4. Subtrair dedução por dependentes
            BigDecimal deducaoDependentes = calcularDeducaoDependentes();
            baseCalculoIrrf = baseCalculoIrrf.subtract(deducaoDependentes);

            return baseCalculoIrrf.max(BigDecimal.ZERO); // Não pode ser negativo

        } catch (Exception e) {
            logger.error("Erro ao calcular base IRRF 13º", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calcula INSS sobre o 13º salário usando tabela do banco
     */
    private BigDecimal calcularInss13o(BigDecimal valor13o) {
        try {
            // Busca a tabela atual do INSS
            Optional<TabelaDeducaoInss> tabelaInssOpt = tabelaDeducaoInssRepository.findTopByOrderById();

            if (tabelaInssOpt.isEmpty()) {
                logger.warn("Tabela INSS não encontrada, usando cálculo alternativo");
                return BigDecimal.ZERO;
            }

            TabelaDeducaoInss tabelaInss = tabelaInssOpt.get();
            BigDecimal valorCalculo = valor13o;
            BigDecimal valorINSS = BigDecimal.ZERO;

            // Faixas do INSS da tabela do banco
            BigDecimal[] faixasINSS = {
                    tabelaInss.getFaixaSalario1(),
                    tabelaInss.getFaixaSalario2(),
                    tabelaInss.getFaixaSalario3(),
                    tabelaInss.getFaixaSalario4()
            };

            BigDecimal[] aliquotasINSS = {
                    tabelaInss.getAliquota1(),
                    tabelaInss.getAliquota2(),
                    tabelaInss.getAliquota3(),
                    tabelaInss.getAliquota4()
            };

            for (int i = 0; i < faixasINSS.length; i++) {
                if (valorCalculo.compareTo(BigDecimal.ZERO) <= 0) break;

                BigDecimal baseCalculo;
                if (i == 0) {
                    baseCalculo = valorCalculo.min(faixasINSS[i]);
                } else if (i == faixasINSS.length - 1) {
                    baseCalculo = valorCalculo.min(faixasINSS[i].subtract(faixasINSS[i-1]));
                } else {
                    baseCalculo = valorCalculo.min(faixasINSS[i].subtract(faixasINSS[i-1]));
                }

                BigDecimal aliquota = aliquotasINSS[i].divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                valorINSS = valorINSS.add(baseCalculo.multiply(aliquota));
                valorCalculo = valorCalculo.subtract(baseCalculo);
            }

            // Teto do INSS (última faixa × aliquota máxima)
            BigDecimal tetoINSS = faixasINSS[3].multiply(aliquotasINSS[3].divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
            return valorINSS.min(tetoINSS).setScale(2, RoundingMode.HALF_UP);

        } catch (Exception e) {
            logger.error("Erro ao calcular INSS 13º com tabela do banco, usando cálculo alternativo", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calcula IRRF sobre a base de cálculo usando tabela do banco
     */
    private BigDecimal calcularIrrfSobreBase(BigDecimal baseCalculo) {
        try {
            // Busca a tabela atual do IRRF
            Optional<TabelaImpostoRenda> tabelaIrrfOpt = tabelaImpostoRendaRepository.findTopByOrderById();

            if (tabelaIrrfOpt.isEmpty()) {
                logger.warn("Tabela IRRF não encontrada, usando cálculo alternativo");
                return calcularIrrfAlternativo(baseCalculo);
            }

            TabelaImpostoRenda tabelaIrrf = tabelaIrrfOpt.get();

            // Determina a faixa com base na base de cálculo
            if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario1()) <= 0) {
                return BigDecimal.ZERO; // Isento
            } else if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario2()) <= 0) {
                // Aplica fórmula: (Base × Alíquota) - Parcela a Deduzir
                BigDecimal irrf = baseCalculo.multiply(tabelaIrrf.getAliquota1())
                        .subtract(tabelaIrrf.getParcelaDeduzir1());
                return irrf.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            } else if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario3()) <= 0) {
                BigDecimal irrf = baseCalculo.multiply(tabelaIrrf.getAliquota2())
                        .subtract(tabelaIrrf.getParcelaDeduzir2());
                return irrf.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            } else if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario4()) <= 0) {
                BigDecimal irrf = baseCalculo.multiply(tabelaIrrf.getAliquota3())
                        .subtract(tabelaIrrf.getParcelaDeduzir3());
                return irrf.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            } else if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario5()) <= 0) {
                BigDecimal irrf = baseCalculo.multiply(tabelaIrrf.getAliquota4())
                        .subtract(tabelaIrrf.getParcelaDeduzir4());
                return irrf.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            } else {
                BigDecimal irrf = baseCalculo.multiply(tabelaIrrf.getAliquota5())
                        .subtract(tabelaIrrf.getParcelaDeduzir5());
                return irrf.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            }

        } catch (Exception e) {
            logger.error("Erro ao calcular IRRF com tabela do banco, usando cálculo alternativo", e);
            return calcularIrrfAlternativo(baseCalculo);
        }
    }

    /**
     * Cálculo alternativo do IRRF caso a tabela não esteja disponível
     */
    private BigDecimal calcularIrrfAlternativo(BigDecimal baseCalculo) {
        // Tabela IRRF 2024 como fallback
        BigDecimal[][] faixasIRRF = {
                {new BigDecimal("2259.20"), new BigDecimal("0.00"), new BigDecimal("0.00")},
                {new BigDecimal("2826.65"), new BigDecimal("0.075"), new BigDecimal("169.44")},
                {new BigDecimal("3751.05"), new BigDecimal("0.15"), new BigDecimal("381.44")},
                {new BigDecimal("4664.68"), new BigDecimal("0.225"), new BigDecimal("662.77")},
                {new BigDecimal("999999.99"), new BigDecimal("0.275"), new BigDecimal("896.00")}
        };

        for (int i = 0; i < faixasIRRF.length; i++) {
            BigDecimal limiteFaixa = faixasIRRF[i][0];
            BigDecimal aliquota = faixasIRRF[i][1];
            BigDecimal parcelaDeduzir = faixasIRRF[i][2];

            if (baseCalculo.compareTo(limiteFaixa) <= 0) {
                if (i == 0) { // Isento
                    return BigDecimal.ZERO;
                }
                BigDecimal irrf = baseCalculo.multiply(aliquota).subtract(parcelaDeduzir);
                return irrf.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Calcula dedução por dependentes
     */
    private BigDecimal calcularDeducaoDependentes() {
        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            Integer numeroDependentes = folha.getDependentesIrrf();

            if (numeroDependentes == null || numeroDependentes == 0) {
                return BigDecimal.ZERO;
            }

            // Busca o valor da dedução por dependente da tabela do IRRF
            Optional<TabelaImpostoRenda> tabelaIrrfOpt = tabelaImpostoRendaRepository.findTopByOrderById();

            if (tabelaIrrfOpt.isPresent()) {
                TabelaImpostoRenda tabelaIrrf = tabelaIrrfOpt.get();
                BigDecimal valorDeducaoPorDependente = tabelaIrrf.getDeducaoPorDependente();
                return valorDeducaoPorDependente.multiply(new BigDecimal(numeroDependentes));
            } else {
                // Valor padrão como fallback
                BigDecimal valorDeducaoPorDependente = new BigDecimal("189.59");
                return valorDeducaoPorDependente.multiply(new BigDecimal(numeroDependentes));
            }

        } catch (Exception e) {
            logger.warn("Erro ao calcular dedução dependentes, considerando zero");
            return BigDecimal.ZERO;
        }
    }
}
