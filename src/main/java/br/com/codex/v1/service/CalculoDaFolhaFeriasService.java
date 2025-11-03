package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaFeriasRepository;
import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.domain.repository.FolhaMensalRepository;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.FolhaFerias;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
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
import java.util.Optional;

@Service
public class CalculoDaFolhaFeriasService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoDaFolhaFeriasService.class);

        @Autowired
        private FolhaMensalRepository folhaMensalRepository;

        @Autowired
        private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

        @Autowired
        private CalculoBaseService calculoBaseService;

        @Autowired
        private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

        @Autowired
        private FolhaFeriasRepository folhaFeriasRepository;

        @Setter
        private String numeroMatricula;

        public FolhaFerias findByNumeroMatricula(String matricula){
            Optional<FolhaFerias> obj = folhaFeriasRepository.findByNumeroMatricula(matricula);
            return obj.orElseThrow(() -> new ObjectNotFoundException("Informação não encontrada"));
        }

        public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {
            Map<String, BigDecimal> resultado = new HashMap<>();

            try {
                FolhaFerias ferias = findByNumeroMatricula(numeroMatricula);

                BigDecimal salarioBase = ferias.getSalarioBruto();
                BigDecimal salarioPorHora = ferias.getSalarioHora();
                BigDecimal diasDeFerias = BigDecimal.valueOf(ferias.getTotalDiasFerias());
                Integer quantidadeDeFaltas = ferias.getTotalFaltas();
                Integer numeroDependentes = ferias.getNumeroDependentes();

                // Calcular valores base reutilizáveis
                BigDecimal valorDiasFerias = ferias.getValorDiasFerias() != null ?
                        ferias.getValorDiasFerias() :
                        calcularValorFerias(salarioBase, diasDeFerias);

                switch (codigoEvento) {
                    // 30 Dias de Férias Gozadas
                    case 140 -> calcularFeriasGozadas(resultado, new BigDecimal("30"), salarioBase);

                    // 20 Dias de Férias Gozadas
                    case 141 -> calcularFeriasGozadas(resultado, new BigDecimal("20"), salarioBase);

                    // 15 Dias de Férias Gozadas
                    case 142 -> calcularFeriasGozadas(resultado, new BigDecimal("15"), salarioBase);

                    // 10 Dias de Férias Gozadas
                    case 143 -> calcularFeriasGozadas(resultado, new BigDecimal("10"), salarioBase);

                    // Outros Dias de Férias
                    case 144 -> calcularFeriasGozadas(resultado, diasDeFerias, salarioBase);

                    // 1/3 de Férias
                    case 145 -> {
                        BigDecimal umTerco = valorDiasFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
                        resultado.put("referencia", umTerco);
                        resultado.put("vencimentos", umTerco);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }

                    // Abono Pecuniário (venda de 10 dias)
                    case 146 -> calcularFeriasGozadas(resultado, new BigDecimal("10"), salarioBase);

                    // 1/3 Abono Pecuniário - CORRIGIDO
                    case 147 -> {
                        BigDecimal valorAbono = calcularValorFerias(salarioBase, new BigDecimal("10"));
                        BigDecimal umTercoAbono = valorAbono.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
                        resultado.put("referencia", umTercoAbono);
                        resultado.put("vencimentos", umTercoAbono);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }

                    // Média de Horas Extras
                    case 148 -> {
                        calcularMediaHorasExtras(resultado, 98, salarioPorHora);
                    }

                    case 149 -> {
                        calcularMediaHorasExtras(resultado, 100, salarioPorHora);
                    }

                    case 150 -> {
                        calcularMediaHorasExtras(resultado, 99, salarioPorHora);
                    }

                    // Adicionais
                    case 151 -> {
                        calcularMediaInsalubridade(resultado);
                    }

                    case 152 -> {
                        calcularMediaPericulosidade(resultado, salarioBase);
                    }

                    case 153 -> {
                        calcularMediaComissoes(resultado);
                    }

                    case 154 -> {
                        calcularMediaAdicionalNoturno(resultado);
                    }

                    // Desconto Dias Redução Faltas Férias - CORRIGIDO
                    case 155 -> {
                        BigDecimal diasReduzidos = calcularReducaoFaltasFerias(quantidadeDeFaltas);
                        BigDecimal valorReducao = calcularValorFerias(salarioBase, diasReduzidos);
                        resultado.put("referencia", diasReduzidos);
                        resultado.put("vencimentos", BigDecimal.ZERO);
                        resultado.put("descontos", valorReducao);
                    }

                    // Férias em Dobro
                    case 156 -> {
                        calcularFeriasDobro(resultado, valorDiasFerias, numeroDependentes);
                    }

                    // 1/3 Constitucional Férias em Dobro
                    case 157 -> {
                        BigDecimal umTerco = valorDiasFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
                        resultado.put("referencia", umTerco);
                        resultado.put("vencimentos", umTerco);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }

                    // Descontos INSS e IRRF
                    case 158 -> {
                        calcularDescontoINSS(resultado, valorDiasFerias);
                    }

                    case 159 -> {
                        calcularDescontoIRRF(resultado, valorDiasFerias, numeroDependentes);
                    }

                    // Adiantamento 1° Parcela Décimo Terceiro
                    case 167 -> {
                        BigDecimal decimoTerceiro = salarioBase.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
                        resultado.put("referencia", decimoTerceiro);
                        resultado.put("vencimentos", decimoTerceiro);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }

                    default -> logger.warn("Evento de férias não implementado: {}", codigoEvento);
                }

            } catch (Exception e) {
                logger.error("Erro ao calcular evento de férias {}: {}", codigoEvento, e.getMessage());
                throw new RuntimeException("Erro ao calcular férias: " + e.getMessage());
            }

            return resultado;
        }

        private void calcularFeriasGozadas(Map<String, BigDecimal> resultado, BigDecimal dias, BigDecimal salarioBase) {
            BigDecimal valorFerias = calcularValorFerias(salarioBase, dias);
            resultado.put("referencia", dias);
            resultado.put("vencimentos", valorFerias);
            resultado.put("descontos", BigDecimal.ZERO);
        }

        private void calcularMediaHorasExtras(Map<String, BigDecimal> resultado, Integer codigoEvento, BigDecimal salarioPorHora) {
            BigDecimal media = calcularMediaHorasExtrasFerias(codigoEvento, salarioPorHora);
            resultado.put("referencia", media);
            resultado.put("vencimentos", media);
            resultado.put("descontos", BigDecimal.ZERO);
        }

        private void calcularDescontoINSS(Map<String, BigDecimal> resultado, BigDecimal valorFerias) {
            BigDecimal inssFerias = calculoBaseService.calcularINSS(valorFerias);
            resultado.put("referencia", inssFerias);
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", inssFerias);
        }

        private void calcularDescontoIRRF(Map<String, BigDecimal> resultado, BigDecimal valorFerias, Integer dependentes) {
            BigDecimal inssFerias = calculoBaseService.calcularINSS(valorFerias);
            BigDecimal irrfFerias = calcularIRRFFerias(valorFerias, inssFerias, dependentes);
            resultado.put("referencia", irrfFerias);
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", irrfFerias);
        }

        private void calcularFeriasDobro(Map<String, BigDecimal> resultado, BigDecimal valorFerias, Integer dependentes) {
            BigDecimal umTerco = valorFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
            BigDecimal valorTotal = valorFerias.add(umTerco);
            BigDecimal inssTotal = calculoBaseService.calcularINSS(valorTotal);
            BigDecimal irrfTotal = calcularIRRFFerias(valorTotal, inssTotal, dependentes);

            BigDecimal feriasDobro = valorTotal.subtract(inssTotal).subtract(irrfTotal).multiply(new BigDecimal("2"));
            resultado.put("referencia", feriasDobro);
            resultado.put("vencimentos", feriasDobro);
            resultado.put("descontos", BigDecimal.ZERO);
        }

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

        private void calcularMediaInsalubridade(Map<String, BigDecimal> resultado) {
            BigDecimal media = calcularMediaInsalubridadeFerias();
            resultado.put("referencia", media);
            resultado.put("vencimentos", media);
            resultado.put("descontos", BigDecimal.ZERO);
        }

        private void calcularMediaPericulosidade(Map<String, BigDecimal> resultado, BigDecimal salarioBase) {
            BigDecimal media = calcularMediaPericulosidadeFerias(salarioBase);
            resultado.put("referencia", media);
            resultado.put("vencimentos", media);
            resultado.put("descontos", BigDecimal.ZERO);
        }

        private void calcularMediaComissoes(Map<String, BigDecimal> resultado) {
            BigDecimal media = calcularMediaComissoesFerias();
            resultado.put("referencia", media);
            resultado.put("vencimentos", media);
            resultado.put("descontos", BigDecimal.ZERO);
        }

        private void calcularMediaAdicionalNoturno(Map<String, BigDecimal> resultado) {
            BigDecimal media = calcularMediaAdicionalNoturnoFerias();
            resultado.put("referencia", media);
            resultado.put("vencimentos", media);
            resultado.put("descontos", BigDecimal.ZERO);
        }
}
