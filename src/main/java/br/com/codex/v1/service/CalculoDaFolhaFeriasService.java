package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.CalculoFeriasRepository;
import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.domain.repository.FolhaMensalRepository;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.CalculoFerias;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
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
public class CalculoDaFolhaFeriasService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoDaFolhaFeriasService.class);

        @Autowired
        private FolhaMensalRepository folhaMensalRepository;

        @Autowired
        private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

        @Autowired
        private CalculoFeriasService calculoFeriasService;

        @Autowired
        private CalculoBaseService calculoBaseService;

        @Autowired
        private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

        @Setter
        private String numeroMatricula;

        public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {

            Map<String, BigDecimal> resultado = new HashMap<>();
  
            try {
                CalculoFerias ferias = calculoFeriasService.findByNumeroMatricula(numeroMatricula);
                BigDecimal salarioBase = ferias.getSalarioBruto();
                BigDecimal salarioPorHora = ferias.getSalarioHora();
                BigDecimal diasDeFerias = BigDecimal.valueOf(ferias.getTotalDiasFerias());
                BigDecimal diasDeAbono = BigDecimal.valueOf(ferias.getTotalDiasAbono());
                Integer quantidadeDeFaltas = ferias.getTotalFaltas();
                Integer numeroDependentes = ferias.getNumeroDependentes();

                switch (codigoEvento) {
                    // 30 Dias de Férias Gozadas
                    case 140 -> {
                        BigDecimal diasFerias = new BigDecimal( 30);
                        BigDecimal valorFerias = calcularValorFerias(salarioBase, diasFerias);

                        resultado.put("referencia", diasFerias);
                        resultado.put("vencimentos", valorFerias);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // 20 Dias de Férias Gozadas
                    case 141 -> {
                        BigDecimal valorFerias = calcularValorFerias(salarioBase, new BigDecimal("20"));
                        resultado.put("referencia", new BigDecimal("20"));
                        resultado.put("vencimentos", valorFerias);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // 15 Dias de Férias Gozadas
                    case 142 -> {
                        BigDecimal valorFerias = calcularValorFerias(salarioBase, new BigDecimal("15"));
                        resultado.put("referencia", new BigDecimal("15"));
                        resultado.put("vencimentos", valorFerias);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // 10 Dias de Férias Gozadas
                    case 143 -> {
                        BigDecimal valorFerias = calcularValorFerias(salarioBase, new BigDecimal("10"));
                        resultado.put("referencia", new BigDecimal("10"));
                        resultado.put("vencimentos", valorFerias);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Outros Dias de Férias
                    case 144 -> {
                        BigDecimal valorFerias = calcularValorFerias(salarioBase, diasDeFerias);
                        resultado.put("referencia", diasDeFerias);
                        resultado.put("vencimentos", valorFerias);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // 1/3 de Férias
                    case 145 -> {
                        BigDecimal valorFerias = calcularValorFerias(salarioBase, diasDeFerias);
                        BigDecimal umTerco = valorFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);

                        resultado.put("referencia", umTerco);
                        resultado.put("vencimentos", umTerco);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Abono Pecuniário (venda de 10 dias)
                    case 146 -> {
                        BigDecimal valorAbono = calcularValorFerias(salarioBase, new BigDecimal("10"));
                        resultado.put("referencia", new BigDecimal("10"));
                        resultado.put("vencimentos", valorAbono);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // 1/3 Abono Pecuniário
                    case 147 -> {
                        BigDecimal umTercoAbono = diasDeAbono.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);

                        resultado.put("referencia", umTercoAbono);
                        resultado.put("vencimentos", umTercoAbono);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Média de Horas Extras 50%
                    case 148 -> {
                        BigDecimal mediaHE50 = calcularMediaHorasExtrasFerias(98, salarioPorHora);
                        resultado.put("referencia", mediaHE50);
                        resultado.put("vencimentos", mediaHE50);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Média de Horas Extras 100%
                    case 149 -> {
                        BigDecimal mediaHE100 = calcularMediaHorasExtrasFerias(100, salarioPorHora);
                        resultado.put("referencia", mediaHE100);
                        resultado.put("vencimentos", mediaHE100);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Média de Horas Extras 70%
                    case 150 -> {
                        BigDecimal mediaHE70 = calcularMediaHorasExtrasFerias(99, salarioPorHora);
                        resultado.put("referencia", mediaHE70);
                        resultado.put("vencimentos", mediaHE70);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Insalubridade Sobre Férias
                    case 151 -> {
                        BigDecimal mediaInsalubridade = calcularMediaInsalubridadeFerias();
                        resultado.put("referencia", mediaInsalubridade);
                        resultado.put("vencimentos", mediaInsalubridade);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Periculosidade Sobre Férias - USANDO MÉTuDO ALTERNATIVO
                    case 152 -> {
                        BigDecimal mediaPericulosidade = calcularMediaPericulosidadeFerias(salarioBase);
                        resultado.put("referencia", mediaPericulosidade);
                        resultado.put("vencimentos", mediaPericulosidade);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Comissões Sobre Férias
                    case 153 -> {
                        BigDecimal mediaComissoes = calcularMediaComissoesFerias();
                        resultado.put("referencia", mediaComissoes);
                        resultado.put("vencimentos", mediaComissoes);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Média de Adicional Noturno nas Férias
                    case 154 -> {
                        BigDecimal mediaAdicionalNoturno = calcularMediaAdicionalNoturnoFerias();
                        resultado.put("referencia", mediaAdicionalNoturno);
                        resultado.put("vencimentos", mediaAdicionalNoturno);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Desconto Dias Redução Faltas Férias
                    case 155 -> {

                        BigDecimal diasReduzidos = calcularReducaoFaltasFerias(quantidadeDeFaltas);
                        resultado.put("referencia", diasReduzidos);
                        resultado.put("vencimentos", BigDecimal.ZERO);
                        resultado.put("descontos", diasReduzidos);

                        return resultado;
                    }

                    // Férias em Dobro
                    case 156 -> {
                        BigDecimal valorFerias = ferias.getValorDiasFerias() != null ?
                                ferias.getValorDiasFerias() :
                                calcularValorFerias(salarioBase, diasDeFerias);

                        BigDecimal valorUmTerco = valorFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
                        BigDecimal valorTotal = valorFerias.add(valorUmTerco);
                        BigDecimal inssTotal = calculoBaseService.calcularINSS(valorTotal);
                        BigDecimal irrfTotal = calcularIRRFFerias(valorTotal, inssTotal, numeroDependentes);

                        BigDecimal feriasDobro = calcularFeriasDobro(valorFerias, valorUmTerco, inssTotal, irrfTotal);
                        resultado.put("referencia", feriasDobro);
                        resultado.put("vencimentos", feriasDobro);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // 1/3 Constitucional Férias em Dobro - CORRIGIDO
                    case 157 -> {
                        BigDecimal valorFerias = ferias.getValorDiasFerias() != null ?
                                ferias.getValorDiasFerias() :
                                calcularValorFerias(salarioBase, diasDeFerias);
                        BigDecimal umTerco = valorFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);

                        resultado.put("referencia", umTerco);
                        resultado.put("vencimentos", umTerco);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    // Desconto INSS Sobre Férias - CORRIGIDO
                    case 158 -> {
                        BigDecimal valorFerias = ferias.getValorDiasFerias() != null ?
                                ferias.getValorDiasFerias() :
                                calcularValorFerias(salarioBase, diasDeFerias);
                        BigDecimal inssFerias = calculoBaseService.calcularINSS(valorFerias);

                        resultado.put("referencia", inssFerias);
                        resultado.put("vencimentos", BigDecimal.ZERO);
                        resultado.put("descontos", inssFerias);

                        return resultado;
                    }

                    // Desconto IRRF Sobre Férias - CORRIGIDO
                    case 159 -> {
                        BigDecimal valorFerias = ferias.getValorDiasFerias() != null ?
                                ferias.getValorDiasFerias() :
                                calcularValorFerias(salarioBase, diasDeFerias);
                        BigDecimal inssFerias = calculoBaseService.calcularINSS(valorFerias);
                        BigDecimal irrfFerias = calcularIRRFFerias(valorFerias, inssFerias, numeroDependentes);

                        resultado.put("referencia", irrfFerias);
                        resultado.put("vencimentos", BigDecimal.ZERO);
                        resultado.put("descontos", irrfFerias);

                        return resultado;
                    }

                    // Adiantamento 1° Parcela Décimo Terceiro
                    case 167 -> {
                        BigDecimal decimoTerceiro = salarioBase.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
                        resultado.put("referencia", decimoTerceiro);
                        resultado.put("vencimentos", decimoTerceiro);
                        resultado.put("descontos", BigDecimal.ZERO);

                        return resultado;
                    }

                    default -> {
                        logger.warn("Evento de férias não implementado: {}", codigoEvento);
                    }
                }

            } catch (Exception e) {
                logger.error("Erro ao calcular evento de férias {}: {}", codigoEvento, e.getMessage());
                throw new RuntimeException("Erro ao calcular férias: " + e.getMessage());
            }

            return resultado;
        }

        // ========== MÉTODOS AUXILIARES ==========

        private BigDecimal calcularValorFerias(BigDecimal salarioBase, BigDecimal dias) {
            return salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP).multiply(dias).setScale(2, RoundingMode.HALF_UP);
        }

        private BigDecimal calcularMediaHorasExtrasFerias(Integer codigoEvento, BigDecimal salarioPorHora) {
            try {
                LocalDate dataLimite = LocalDate.now().minusMonths(12);
                BigDecimal mediaHorasExtras = folhaMensalEventosCalculadaRepository
                        .findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, codigoEvento, dataLimite);

                // Ajustar conforme o tipo de hora extra
                BigDecimal fatorMultiplicador = BigDecimal.ONE;
                switch (codigoEvento) {
                    case 98 -> fatorMultiplicador = new BigDecimal("1.5"); // 50%
                    case 99 -> fatorMultiplicador = new BigDecimal("1.7"); // 70%
                    case 100 -> fatorMultiplicador = new BigDecimal("2.0"); // 100%
                }

                return mediaHorasExtras.multiply(salarioPorHora)
                        .multiply(fatorMultiplicador)
                        .setScale(2, RoundingMode.HALF_UP);

            } catch (Exception e) {
                logger.error("Erro ao calcular média HE {}: {}", codigoEvento, e.getMessage());
                return BigDecimal.ZERO;
            }
        }

        private BigDecimal calcularMediaInsalubridadeFerias() {
            try {
                // Usando o métudo existente findUltimoValorInsalubridade como fallback
                int anoAtual = LocalDate.now().getYear();
                BigDecimal ultimoValor = folhaMensalEventosCalculadaRepository
                        .findUltimoValorInsalubridade(numeroMatricula, 46, anoAtual);

                // Se não encontrou, tenta buscar a média dos últimos 6 meses
                if (ultimoValor.compareTo(BigDecimal.ZERO) == 0) {
                    LocalDate dataLimite = LocalDate.now().minusMonths(6);
                    ultimoValor = folhaMensalEventosCalculadaRepository
                            .findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 46, dataLimite);
                }

                return ultimoValor.setScale(2, RoundingMode.HALF_UP);

            } catch (Exception e) {
                logger.error("Erro ao calcular média insalubridade: {}", e.getMessage());
                return BigDecimal.ZERO;
            }
        }

        private BigDecimal calcularMediaPericulosidadeFerias(BigDecimal salarioBase) {
            try {
                // Tenta buscar o último valor registrado
                int anoAtual = LocalDate.now().getYear();
                BigDecimal mediaPericulosidade = folhaMensalEventosCalculadaRepository
                        .findUltimoValorPericulosidade(numeroMatricula, 47, anoAtual);

                // Se não encontrou registro, calcula 30% do salário
                if (mediaPericulosidade.compareTo(BigDecimal.ZERO) == 0) {
                    mediaPericulosidade = salarioBase.multiply(new BigDecimal("0.30"));
                }

                return mediaPericulosidade.setScale(2, RoundingMode.HALF_UP);

            } catch (Exception e) {
                logger.error("Erro ao calcular média periculosidade: {}", e.getMessage());
                return salarioBase.multiply(new BigDecimal("0.30")).setScale(2, RoundingMode.HALF_UP);
            }
        }

        private BigDecimal calcularMediaComissoesFerias() {
            try {
                // Calcula média dos últimos 12 meses manualmente
                LocalDate hoje = LocalDate.now();
                BigDecimal somaComissoes = BigDecimal.ZERO;
                int mesesComComissao = 0;

                for (int i = 0; i < 12; i++) {
                    LocalDate dataMes = hoje.minusMonths(i);
                    int ano = dataMes.getYear();
                    int mes = dataMes.getMonthValue();

                    BigDecimal comissaoMes = folhaMensalEventosCalculadaRepository.findSomaComissoesPorMesEAno(numeroMatricula, ano, mes);

                    if (comissaoMes != null && comissaoMes.compareTo(BigDecimal.ZERO) > 0) {
                        somaComissoes = somaComissoes.add(comissaoMes);
                        mesesComComissao++;
                    }
                }

                // Retorna a média ou zero se não houver comissões
                return mesesComComissao > 0 ?
                        somaComissoes.divide(new BigDecimal(mesesComComissao), 2, RoundingMode.HALF_UP) :
                        BigDecimal.ZERO;

            } catch (Exception e) {
                logger.error("Erro ao calcular média comissões: {}", e.getMessage());
                return BigDecimal.ZERO;
            }
        }

        private BigDecimal calcularMediaAdicionalNoturnoFerias() {
            try {
                // Calcula média dos últimos 12 meses manualmente
                LocalDate hoje = LocalDate.now();
                BigDecimal somaAdicionalNoturno = BigDecimal.ZERO;
                int mesesComAdicional = 0;

                for (int i = 0; i < 12; i++) {
                    LocalDate dataMes = hoje.minusMonths(i);
                    int ano = dataMes.getYear();
                    int mes = dataMes.getMonthValue();

                    BigDecimal adicionalMes = folhaMensalEventosCalculadaRepository.findAdicionalNoturnoPorMesEAno(numeroMatricula, ano, mes);

                    if (adicionalMes != null && adicionalMes.compareTo(BigDecimal.ZERO) > 0) {
                        somaAdicionalNoturno = somaAdicionalNoturno.add(adicionalMes);
                        mesesComAdicional++;
                    }
                }

                // Retorna a média ou zero se não houver adicional noturno
                return mesesComAdicional > 0 ?
                        somaAdicionalNoturno.divide(new BigDecimal(mesesComAdicional), 2, RoundingMode.HALF_UP) :
                        BigDecimal.ZERO;

            } catch (Exception e) {
                logger.error("Erro ao calcular média adicional noturno: {}", e.getMessage());
                return BigDecimal.ZERO;
            }
        }

        private BigDecimal calcularReducaoFaltasFerias(Integer totalFaltas) {
            if (totalFaltas <= 5) {
                return BigDecimal.ZERO;
            } else if (totalFaltas >= 6 && totalFaltas <= 14) {
                return new BigDecimal("24");
            } else if (totalFaltas >= 15 && totalFaltas <= 23) {
                return new BigDecimal("18");
            } else if (totalFaltas >= 24 && totalFaltas <= 32) {
                return new BigDecimal("12");
            } else {
                return new BigDecimal("0");
            }
        }

        private BigDecimal calcularFeriasDobro(BigDecimal valorFerias, BigDecimal umTerco, BigDecimal inssTotal, BigDecimal irrfTotal) {
            BigDecimal baseCalculo = valorFerias.add(umTerco);

            // ✅ CORREÇÃO: Verificar o VALOR TOTAL das férias, não o salário base
            BigDecimal valorTotalFerias = valorFerias.add(umTerco);

            if (valorTotalFerias.compareTo(new BigDecimal("1713.58")) > 0) {
                // Acima do limite - aplica INSS e IRRF
                return baseCalculo.subtract(inssTotal).subtract(irrfTotal).multiply(new BigDecimal("2"));
            } else {
                // Abaixo do limite - aplica apenas INSS
                return baseCalculo.subtract(inssTotal).multiply(new BigDecimal("2"));
            }
        }

        private BigDecimal calcularIRRFFerias(BigDecimal valorFerias, BigDecimal inssFerias, Integer dependentes) {
            BigDecimal baseCalculo = valorFerias.subtract(inssFerias);

            if (dependentes > 0) {
                BigDecimal deducaoDependentes = new BigDecimal("189.59").multiply(new BigDecimal(dependentes));
                baseCalculo = baseCalculo.subtract(deducaoDependentes);
            }

            if (baseCalculo.compareTo(BigDecimal.ZERO) < 0) {
                baseCalculo = BigDecimal.ZERO;
            }

            return calculoBaseService.calcularIRRF(baseCalculo);
        }
}
