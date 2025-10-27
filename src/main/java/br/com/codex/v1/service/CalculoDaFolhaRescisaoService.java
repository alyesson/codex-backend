package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.domain.repository.FolhaRescisaoRepository;
import br.com.codex.v1.domain.repository.TabelaDeducaoInssRepository;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CalculoDaFolhaRescisaoService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoDaFolhaRescisaoService.class);

    @Autowired
    private FolhaRescisaoRepository folhaRescisaoRepository;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Autowired
    private TabelaDeducaoInssRepository tabelaDeducaoInssRepository;

    @Setter
    private String numeroMatricula;

    public FolhaRescisao findByNumeroMatricula(String numeroMatricula) {
        Optional<FolhaRescisao> obj = folhaRescisaoRepository.findByNumeroMatricula(numeroMatricula);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Informação da rescisão não encontrada"));
    }

    public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            FolhaRescisao rescisao = findByNumeroMatricula(numeroMatricula);
            BigDecimal salarioBase = rescisao.getSalarioBase();
            BigDecimal salarioPorHora = rescisao.getSalarioHora();
            LocalDate dataAdmissao = rescisao.getDataDeAdmissao();
            LocalDate dataDemissao = rescisao.getDataDeDemissao();
            Integer diasTrabalhadosMes = rescisao.getDiasTrabalhadosNoMes();
            Integer faltasMes = rescisao.getFaltasNoMes();
            Integer numeroDependentes = rescisao.getNumDependenteIrrf();
            String tipoSalario = rescisao.getTipoDeSalario();
            String tipoDemissao = rescisao.getTipoDeDemissao();

            switch (codigoEvento) {
                // Saldo de Salário
                case 302 -> {
                    BigDecimal saldoSalario = calcularSaldoSalario(salarioBase, diasTrabalhadosMes, tipoSalario, salarioPorHora);
                    resultado.put("referencia", new BigDecimal(diasTrabalhadosMes));
                    resultado.put("vencimentos", saldoSalario);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;
                }

                // Aviso Prévio Trabalhado
                case 303 -> {
                    BigDecimal avisoTrabalhado = calcularAvisoPrevioTrabalhado(diasTrabalhadosMes);
                    resultado.put("referencia", new BigDecimal(diasTrabalhadosMes));
                    resultado.put("vencimentos", avisoTrabalhado);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;
                }

                // Aviso Prévio Indenizado
                case 304 -> {
                    BigDecimal avisoIndenizado = calcularAvisoPrevioIndenizado(salarioBase, dataAdmissao, dataDemissao, tipoSalario, salarioPorHora);
                    resultado.put("referencia", BigDecimal.ONE); // 1 aviso prévio
                    resultado.put("vencimentos", avisoIndenizado);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;
                }

                // Multa do FGTS (40%)
                case 305 -> {
                    BigDecimal multaFGTS = calcularMultaFGTS(salarioBase, dataAdmissao, dataDemissao, tipoDemissao);
                    resultado.put("referencia", new BigDecimal("40")); // 40%
                    resultado.put("vencimentos", multaFGTS);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;
                }

                // Férias Proporcionais
                case 306 -> {
                    BigDecimal feriasProporcionais = calcularFeriasProporcionais(salarioBase, dataAdmissao, dataDemissao, faltasMes, tipoSalario);
                    resultado.put("referencia", BigDecimal.ONE);
                    resultado.put("vencimentos", feriasProporcionais);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;
                }

                // Férias Vencidas
                case 307 -> {
                    BigDecimal feriasVencidas = calcularFeriasVencidas(salarioBase, faltasMes, tipoSalario);
                    resultado.put("referencia", BigDecimal.ONE);
                    resultado.put("vencimentos", feriasVencidas);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;
                }

                // 1/3 de Férias
                case 324 -> {
                    BigDecimal umTercoFerias = salarioBase.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
                    resultado.put("referencia", umTercoFerias);
                    resultado.put("vencimentos", umTercoFerias);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;
                }

                // 13º Proporcional
                case 325 -> {
                    BigDecimal decimoTerceiro = calcularDecimoTerceiroProporcional(salarioBase, dataAdmissao, dataDemissao, faltasMes);
                    resultado.put("referencia", BigDecimal.ONE);
                    resultado.put("vencimentos", decimoTerceiro);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;
                }

                // INSS Sobre Rescisão
                case 313 -> {
                    BigDecimal valorTotalRescisao = calcularValorTotalRescisao(rescisao);
                    BigDecimal inssRescisao = calculoBaseService.calcularINSS(valorTotalRescisao);
                    resultado.put("referencia", inssRescisao);
                    resultado.put("vencimentos", BigDecimal.ZERO);
                    resultado.put("descontos", inssRescisao);

                    return resultado;
                }

                // IRRF Sobre Rescisão
                case 314 -> {
                    BigDecimal valorTotalRescisao = calcularValorTotalRescisao(rescisao);
                    BigDecimal inssRescisao = calculoBaseService.calcularINSS(valorTotalRescisao);
                    BigDecimal irrfRescisao = calcularIRRFRescisao(valorTotalRescisao, inssRescisao, numeroDependentes);
                    resultado.put("referencia", irrfRescisao);
                    resultado.put("vencimentos", BigDecimal.ZERO);
                    resultado.put("descontos", irrfRescisao);

                    return resultado;
                }

                // Salário Família na Rescisão
                case 312 -> {
                    BigDecimal salarioFamilia = calcularSalarioFamiliaRescisao(salarioBase, numeroDependentes, diasTrabalhadosMes);
                    resultado.put("referencia", new BigDecimal(numeroDependentes));
                    resultado.put("vencimentos", salarioFamilia);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;
                }

                default -> {
                    logger.warn("Evento de rescisão não implementado: {}", codigoEvento);
                }
            }

        } catch (Exception e) {
            logger.error("Erro ao calcular evento de rescisão {}: {}", codigoEvento, e.getMessage());
            throw new RuntimeException("Erro ao calcular rescisão: " + e.getMessage());
        }

        return resultado;
    }

    // ========== MÉTODOS AUXILIARES ==========

    private BigDecimal calcularSaldoSalario(BigDecimal salarioBase, Integer diasTrabalhados, String tipoSalario, BigDecimal salarioPorHora) {
        if ("Mensal".equals(tipoSalario)) {
            return salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(diasTrabalhados))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            // Para horistas
            BigDecimal horasPorDia = new BigDecimal("8"); // Padrão 8h/dia
            BigDecimal valorDia = salarioPorHora.multiply(horasPorDia);
            return valorDia.multiply(new BigDecimal(diasTrabalhados))
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal calcularAvisoPrevioTrabalhado(Integer diasTrabalhados) {
        // Aviso prévio trabalhado já está incluso no saldo de salário
        return BigDecimal.ZERO;
    }

    private BigDecimal calcularAvisoPrevioIndenizado(BigDecimal salarioBase, LocalDate dataAdmissao, LocalDate dataDemissao, String tipoSalario, BigDecimal salarioPorHora) {
        Period periodo = Period.between(dataAdmissao, dataDemissao);
        int anosTrabalhados = periodo.getYears();

        // Base: 30 dias + 3 dias por ano trabalhado
        int diasAvisoPrevio = 30 + (3 * anosTrabalhados);

        if ("Mensal".equals(tipoSalario)) {
            return salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(diasAvisoPrevio))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            BigDecimal horasPorDia = new BigDecimal("8");
            BigDecimal valorDia = salarioPorHora.multiply(horasPorDia);
            return valorDia.multiply(new BigDecimal(diasAvisoPrevio)).setScale(2, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal calcularMultaFGTS(BigDecimal salarioBase, LocalDate dataAdmissao, LocalDate dataDemissao, String tipoDemissao) {
        Period periodo = Period.between(dataAdmissao, dataDemissao);
        int mesesTrabalhados = periodo.getYears() * 12 + periodo.getMonths();

        // FGTS mensal (8%)
        BigDecimal fgtsMensal = salarioBase.multiply(new BigDecimal("0.08"));
        BigDecimal totalFGTSDepositado = fgtsMensal.multiply(new BigDecimal(mesesTrabalhados));

        // Multa de 40% para demissão sem justa causa
        if ("SEM_JUSTA_CAUSA".equals(tipoDemissao)) {
            return totalFGTSDepositado.multiply(new BigDecimal("0.40")).setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calcularFeriasProporcionais(BigDecimal salarioBase, LocalDate dataAdmissao, LocalDate dataDemissao, Integer faltas, String tipoSalario) {
        Period periodo = Period.between(dataAdmissao, dataDemissao);
        int mesesTrabalhados = periodo.getMonths();
        int dias = periodo.getDays();

        // Se trabalhou mais de 15 dias no mês, conta o mês
        if (dias >= 15) {
            mesesTrabalhados++;
        }

        // Aplica redução por faltas
        int diasFerias = calcularDiasFeriasPorFaltas(faltas);

        BigDecimal valorFerias = salarioBase.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(mesesTrabalhados)).multiply(new BigDecimal(diasFerias)).divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);

        return valorFerias.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularFeriasVencidas(BigDecimal salarioBase, Integer faltas, String tipoSalario) {
        int diasFerias = calcularDiasFeriasPorFaltas(faltas);

        BigDecimal valorFerias = salarioBase.divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(diasFerias));
        BigDecimal umTerco = valorFerias.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
        return valorFerias.add(umTerco).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularDecimoTerceiroProporcional(BigDecimal salarioBase, LocalDate dataAdmissao, LocalDate dataDemissao, Integer faltas) {
        Period periodo = Period.between(dataAdmissao, dataDemissao);
        int mesesTrabalhados = periodo.getMonths();
        int dias = periodo.getDays();

        // Se trabalhou mais de 15 dias no mês, conta o mês
        if (dias >= 15) {
            mesesTrabalhados++;
        }

        return salarioBase.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(mesesTrabalhados)).setScale(2, RoundingMode.HALF_UP);
    }

    private int calcularDiasFeriasPorFaltas(Integer faltas) {
        if (faltas <= 5) {
            return 30;
        } else if (faltas >= 6 && faltas <= 14) {
            return 24;
        } else if (faltas >= 15 && faltas <= 23) {
            return 18;
        } else if (faltas >= 24 && faltas <= 32) {
            return 12;
        } else {
            return 0;
        }
    }

    private BigDecimal calcularSalarioFamiliaRescisao(BigDecimal salarioBase, Integer dependentes, Integer diasTrabalhados) {
        try {
            BigDecimal valorCota = tabelaDeducaoInssRepository.findTopByOrderById()
                    .map(TabelaDeducaoInss::getSalarioFamilia)
                    .orElse(new BigDecimal("50.00")); // Valor padrão

            if (dependentes > 0 && salarioBase.compareTo(new BigDecimal("1500.00")) <= 0) {
                return valorCota.multiply(new BigDecimal(dependentes)).multiply(new BigDecimal(diasTrabalhados)).divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            logger.error("Erro ao calcular salário família: {}", e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calcularIRRFRescisao(BigDecimal valorTotal, BigDecimal inss, Integer dependentes) {
        BigDecimal baseCalculo = valorTotal.subtract(inss);

        if (dependentes > 0) {
            BigDecimal deducaoDependentes = new BigDecimal("189.59").multiply(new BigDecimal(dependentes));
            baseCalculo = baseCalculo.subtract(deducaoDependentes);
        }

        if (baseCalculo.compareTo(BigDecimal.ZERO) < 0) {
            baseCalculo = BigDecimal.ZERO;
        }

        return calculoBaseService.calcularIRRF(baseCalculo);
    }

    private BigDecimal calcularValorTotalRescisao(FolhaRescisao rescisao) {
        // Simulação do valor total da rescisão para cálculo de INSS/IRRF
        // Na implementação real, somaria todas as verbas rescisórias
        BigDecimal salarioBase = rescisao.getSalarioBase();
        return salarioBase.multiply(new BigDecimal("2")); // Exemplo: 2x o salário base
    }
}