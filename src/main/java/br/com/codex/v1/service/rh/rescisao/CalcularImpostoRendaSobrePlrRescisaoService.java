package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.repository.CadastroColaboradoresRepository;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.CadastroColaboradores;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.service.rh.impostos.CalcularDescontoImpostoRendaService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalcularImpostoRendaSobrePlrRescisaoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularImpostoRendaSobrePlrRescisaoService.class);

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularImpostoRendaSobrePlrRescisao(BigDecimal valorPlr) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            // **PASSO 1: Buscar a tabela de IRRF cadastrada no sistema**
            TabelaImpostoRenda tabelaIrrf = obterTabelaIrrfCadastrada();

            if (tabelaIrrf == null) {
                logger.warn("Tabela IRRF não encontrada para cálculo da PLR da matrícula {}", numeroMatricula);
                return resultado;
            }

            validarTabelaIrrf(tabelaIrrf);

            // **PASSO 2: Calcular IRRF sobre PLR usando a tabela do sistema**
            BigDecimal valorIRRF = calcularIRRFPlrComTabelaSistema(valorPlr, tabelaIrrf);

            // **PASSO 3: Montar resultado**
            resultado.put("referencia", obterAliquotaAplicada(valorPlr, tabelaIrrf));
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", valorIRRF);
            /*resultado.put("valorPlrBruto", valorPlr);
            resultado.put("valorPlrLiquido", valorPlr.subtract(valorIRRF));
            resultado.put("valorIRRF", valorIRRF);
            resultado.put("aliquotaAplicada", obterAliquotaAplicada(valorPlr, tabelaIrrf));*/

            logger.info("IRRF sobre PLR calculado para {} usando tabela do sistema: PLR R$ {}, IRRF R$ {}", numeroMatricula, valorPlr, valorIRRF);
        } catch (Exception e) {
            logger.error("Erro ao calcular o desconto do imposto de renda para matrícula {}", numeroMatricula, e);
        }
        return resultado;
    }

    private TabelaImpostoRenda obterTabelaIrrfCadastrada() {
        return tabelaImpostoRendaRepository.findTopByOrderById().orElseThrow(() -> new RuntimeException("Tabela IRRF não encontrada no sistema"));
    }

    private BigDecimal calcularIRRFPlrComTabelaSistema(BigDecimal valorPlr, TabelaImpostoRenda tabela) {
        BigDecimal aliquota = BigDecimal.ZERO;
        BigDecimal parcelaDeduzir = BigDecimal.ZERO;

        // **Determina a faixa baseada na tabela do sistema**
        if (valorPlr.compareTo(tabela.getFaixaSalario1()) <= 0) {
            // Faixa 1: Isento
            return BigDecimal.ZERO;
        } else if (valorPlr.compareTo(tabela.getFaixaSalario2()) <= 0) {
            // Faixa 2
            aliquota = tabela.getAliquota1();
            parcelaDeduzir = obterParcelaDeduzirDaTabela(1, tabela);
        } else if (valorPlr.compareTo(tabela.getFaixaSalario3()) <= 0) {
            // Faixa 3
            aliquota = tabela.getAliquota2();
            parcelaDeduzir = obterParcelaDeduzirDaTabela(2, tabela);
        } else if (valorPlr.compareTo(tabela.getFaixaSalario4()) <= 0) {
            // Faixa 4
            aliquota = tabela.getAliquota3();
            parcelaDeduzir = obterParcelaDeduzirDaTabela(3, tabela);
        } else if (valorPlr.compareTo(tabela.getFaixaSalario5()) <= 0) {
            // Faixa 5
            aliquota = tabela.getAliquota4();
            parcelaDeduzir = obterParcelaDeduzirDaTabela(4, tabela);
        } else {
            // Acima da última faixa
            aliquota = tabela.getAliquota5();
            parcelaDeduzir = obterParcelaDeduzirDaTabela(5, tabela);
        }

        // **Fórmula: (PLR × aliquota) - parcela a deduzir**
        BigDecimal impostoCalculado = valorPlr.multiply(aliquota.divide(new BigDecimal("100"),2, RoundingMode.HALF_UP));
        BigDecimal valorIRRF = impostoCalculado.subtract(parcelaDeduzir);

        // Não pode retornar valor negativo
        return valorIRRF.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal obterParcelaDeduzirDaTabela(int faixa, TabelaImpostoRenda tabela) {
        // Supondo que sua entidade tenha campos como parcelaDeduzir1, parcelaDeduzir2, etc.
        // Ajuste conforme os nomes reais dos campos na sua entidade TabelaImpostoRenda

        return switch (faixa) {
            case 1 -> tabela.getParcelaDeduzir1() != null ? tabela.getParcelaDeduzir1() : BigDecimal.ZERO;
            case 2 -> tabela.getParcelaDeduzir2() != null ? tabela.getParcelaDeduzir2() : BigDecimal.ZERO;
            case 3 -> tabela.getParcelaDeduzir3() != null ? tabela.getParcelaDeduzir3() : BigDecimal.ZERO;
            case 4 -> tabela.getParcelaDeduzir4() != null ? tabela.getParcelaDeduzir4() : BigDecimal.ZERO;
            case 5 -> tabela.getParcelaDeduzir5() != null ? tabela.getParcelaDeduzir5() : BigDecimal.ZERO;
            default -> BigDecimal.ZERO;
        };
    }

    private BigDecimal obterAliquotaAplicada(BigDecimal valorPlr, TabelaImpostoRenda tabela) {
        if (valorPlr.compareTo(tabela.getFaixaSalario1()) <= 0) {
            return BigDecimal.ZERO;
        } else if (valorPlr.compareTo(tabela.getFaixaSalario2()) <= 0) {
            return tabela.getAliquota1();
        } else if (valorPlr.compareTo(tabela.getFaixaSalario3()) <= 0) {
            return tabela.getAliquota2();
        } else if (valorPlr.compareTo(tabela.getFaixaSalario4()) <= 0) {
            return tabela.getAliquota3();
        } else if (valorPlr.compareTo(tabela.getFaixaSalario5()) <= 0) {
            return tabela.getAliquota4();
        } else {
            return tabela.getAliquota5();
        }
    }

    public void validarTabelaIrrf(TabelaImpostoRenda tabela) {
        if (tabela.getFaixaSalario1() == null || tabela.getAliquota1() == null) {
            throw new RuntimeException("Tabela IRRF incompleta: faixas ou alíquotas não cadastradas");
        }
        logger.info("Tabela IRRF validada: Faixa1: R$ {}, Alíquota1: {}%", tabela.getFaixaSalario1(), tabela.getAliquota1());
    }
}
