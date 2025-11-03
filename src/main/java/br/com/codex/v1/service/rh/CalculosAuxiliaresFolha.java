package br.com.codex.v1.service.rh;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.domain.repository.FolhaMensalRepository;
import br.com.codex.v1.domain.repository.FolhaQuinzenalRepository;
import br.com.codex.v1.utilitario.Calendario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

@Service
public class CalculosAuxiliaresFolha {
    private static final Logger logger = LoggerFactory.getLogger(CalculosAuxiliaresFolha.class);

    Calendario calendario = new Calendario();

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    public int calcularDiasUteisNoMes(int year, int month) {
        YearMonth anoMes = YearMonth.of(year, month);
        Set<LocalDate> feriados = new HashSet<>();

        // Adicionar feriados (ajuste conforme seu calendário)
        feriados.addAll(calendario.getFeriadosFixos(year));
        feriados.addAll(calendario.getFeriadosMoveis(year));

        int diasUteis = 0;
        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
            LocalDate data = anoMes.atDay(dia);
            if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                diasUteis++;
            }
        }
        return diasUteis;
    }

    public int calcularDiasRepousoNoMes(int year, int month) {
        YearMonth anoMes = YearMonth.of(year, month);
        Set<LocalDate> feriados = new HashSet<>();

        feriados.addAll(calendario.getFeriadosFixos(year));
        feriados.addAll(calendario.getFeriadosMoveis(year));

        int diasRepouso = 0;
        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
            LocalDate data = anoMes.atDay(dia);
            if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                diasRepouso++;
            }
        }
        return diasRepouso;
    }

    public BigDecimal calcularAdicionalPericulosidade(String matricula, BigDecimal salarioBase, int anoAtual) {
        try {
            // Tentar buscar o último valor registrado (evento 47)
            BigDecimal ultimoValorPericulosidade = folhaMensalEventosCalculadaRepository.findUltimoValorPericulosidade(matricula, 47, anoAtual);

            // Se encontrou valor registrado, usa ele
            if (ultimoValorPericulosidade.compareTo(BigDecimal.ZERO) > 0) {
                return ultimoValorPericulosidade;
            }

            // Se não encontrou, calcula 30% do salário base (regra CLT)
            return salarioBase.multiply(new BigDecimal("0.30")).setScale(2, RoundingMode.HALF_UP);

        } catch (Exception e) {
            // Fallback: calcula 30% do salário base
            return salarioBase.multiply(new BigDecimal("0.30")).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal buscarTotalHorasExtrasQuantidadeAno(String matricula, Integer codigoEvento, int ano) {
        try {
            // Buscar a QUANTIDADE de horas extras do ano INTEIRO
            return folhaMensalEventosCalculadaRepository.findSomaQuantidadeHorasExtrasAno(matricula, codigoEvento, ano);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public int calcularMesesTrabalhados13o(LocalDate dataAdmissao, LocalDate dataCalculo) {
        int anoAtual = dataCalculo.getYear();

        // Se admitido em ano anterior, conta todos os meses até o cálculo
        if (dataAdmissao.getYear() < anoAtual) {
            return Math.min(dataCalculo.getMonthValue(), 12); // máximo 12 meses
        }

        // Se admitido no mesmo ano
        if (dataAdmissao.getYear() == anoAtual) {
            int mesAdmissao = dataAdmissao.getMonthValue();
            int mesAtual = dataCalculo.getMonthValue();

            // **CLT: Conta mês se trabalhou pelo menos 15 dias**
            boolean contaMesAdmissao = dataAdmissao.getDayOfMonth() <= 15;

            if (contaMesAdmissao) {
                // Admitido até dia 15, conta o mês inteiro
                return (mesAtual - mesAdmissao) + 1;
            } else {
                // Admitido após dia 15, não conta o mês
                return Math.max(0, mesAtual - mesAdmissao);
            }
        }
        return 0; // Admitido no futuro
    }

    public BigDecimal calcularDecimoTerceiroProporcional(LocalDate dataAdmissao, BigDecimal salarioBase, BigDecimal... adicionais) {
        try {
            LocalDate hoje = LocalDate.now();

            // 1. Calcular meses trabalhados no ano
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 2. Somar todos os adicionais à base
            BigDecimal remuneracaoTotal = salarioBase;
            for (BigDecimal adicional : adicionais) {
                if (adicional != null) {
                    remuneracaoTotal = remuneracaoTotal.add(adicional);
                }
            }

            // 3. Calcular 13º proporcional = (remuneração total / 12) × meses trabalhados
            BigDecimal decimoTerceiroProporcional = remuneracaoTotal
                    .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados));

            return decimoTerceiroProporcional.setScale(2, RoundingMode.HALF_UP);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular 13º proporcional: " + e.getMessage());
        }
    }

    public BigDecimal calcularMediaComissoesAno(String matricula, int ano) {
        try {
            // Buscar soma de comissões de janeiro a dezembro (ou até mês atual)
            BigDecimal somaComissoesAno = BigDecimal.ZERO;
            int mesesComComissao = 0;

            // Considerar até o mês anterior ao cálculo (para pagamento em nov/dez)
            int mesLimite = LocalDate.now().getMonthValue() - 1;
            if (mesLimite == 0) mesLimite = 12;

            for (int mes = 1; mes <= mesLimite; mes++) {
                // Buscar comissões do mês
                BigDecimal comissaoMes = folhaMensalEventosCalculadaRepository.findSomaComissoesPorMesEAno(matricula, ano, mes);

                if (comissaoMes != null && comissaoMes.compareTo(BigDecimal.ZERO) > 0) {
                    somaComissoesAno = somaComissoesAno.add(comissaoMes);
                    mesesComComissao++;
                }
            }

            // Média = Soma das comissões / meses com comissão (ou 12 se habitual)
            BigDecimal mediaComissoes;
            if (mesesComComissao >= 6) {
                // Se recebeu comissão em 6+ meses, considera habitual - divide por 12
                mediaComissoes = somaComissoesAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            } else {
                // Se eventual, divide apenas pelos meses que recebeu
                mediaComissoes = mesesComComissao > 0 ? somaComissoesAno.divide(new BigDecimal(mesesComComissao), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            }

            return mediaComissoes;

        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calcularMediaHorasExtras50Ano(String matricula, int ano) {
        try {
            BigDecimal totalHorasExtras50Ano = BigDecimal.ZERO;
            int mesesComHE50 = 0;

            // Considerar até novembro para cálculo do 13º
            int mesLimite = Math.min(LocalDate.now().getMonthValue() - 1, 11);
            if (mesLimite == 0) mesLimite = 12;

            for (int mes = 1; mes <= mesLimite; mes++) {
                // Buscar quantidade de horas extras 50% do mês
                BigDecimal horasExtras50Mes = folhaMensalEventosCalculadaRepository.findQuantidadeHorasExtras50PorMesEAno(matricula, ano, mes);

                if (horasExtras50Mes != null && horasExtras50Mes.compareTo(BigDecimal.ZERO) > 0) {
                    totalHorasExtras50Ano = totalHorasExtras50Ano.add(horasExtras50Mes);
                    mesesComHE50++;
                }
            }

            // Se recebeu HE 50% em 6+ meses, considera habitual - divide por 12
            // Se eventual, divide apenas pelos meses que trabalhou HE
            BigDecimal mediaHorasExtras50;
            if (mesesComHE50 >= 6) {
                mediaHorasExtras50 = totalHorasExtras50Ano.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            } else if (mesesComHE50 > 0) {
                mediaHorasExtras50 = totalHorasExtras50Ano.divide(new BigDecimal(mesesComHE50), 2, RoundingMode.HALF_UP);
            } else {
                mediaHorasExtras50 = BigDecimal.ZERO;
            }

            return mediaHorasExtras50;

        } catch (Exception e) {
            logger.error("Erro ao calcular média HE 50% para matrícula {}: {}", matricula, e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calcularMediaHorasExtras70Ano(String matricula, int ano) {
        try {
            BigDecimal totalHorasExtras70Ano = BigDecimal.ZERO;
            int mesesComHE70 = 0;

            // Considerar até novembro para cálculo do 13º
            int mesLimite = Math.min(LocalDate.now().getMonthValue() - 1, 11);
            if (mesLimite == 0) mesLimite = 12;

            for (int mes = 1; mes <= mesLimite; mes++) {
                // Buscar quantidade de horas extras 70% do mês
                BigDecimal horasExtras70Mes = folhaMensalEventosCalculadaRepository.findQuantidadeHorasExtras70PorMesEAno(matricula, ano, mes);

                if (horasExtras70Mes != null && horasExtras70Mes.compareTo(BigDecimal.ZERO) > 0) {
                    totalHorasExtras70Ano = totalHorasExtras70Ano.add(horasExtras70Mes);
                    mesesComHE70++;
                }
            }

            // Se recebeu HE 70% em 6+ meses, considera habitual - divide por 12
            // Se eventual, divide apenas pelos meses que trabalhou HE
            BigDecimal mediaHorasExtras70;
            if (mesesComHE70 >= 6) {
                mediaHorasExtras70 = totalHorasExtras70Ano.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            } else if (mesesComHE70 > 0) {
                mediaHorasExtras70 = totalHorasExtras70Ano.divide(new BigDecimal(mesesComHE70), 2, RoundingMode.HALF_UP);
            } else {
                mediaHorasExtras70 = BigDecimal.ZERO;
            }

            return mediaHorasExtras70;

        } catch (Exception e) {
            logger.error("Erro ao calcular média HE 70% para matrícula {}: {}", matricula, e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calcularMediaHorasExtras100Ano(String matricula, int ano) {
        try {
            BigDecimal totalHorasExtras100Ano = BigDecimal.ZERO;
            int mesesComHE100 = 0;

            // Considerar até novembro para cálculo do 13º
            int mesLimite = Math.min(LocalDate.now().getMonthValue() - 1, 11);
            if (mesLimite == 0) mesLimite = 12;

            for (int mes = 1; mes <= mesLimite; mes++) {
                // Buscar quantidade de horas extras 100% do mês
                BigDecimal horasExtras100Mes = folhaMensalEventosCalculadaRepository.findQuantidadeHorasExtras100PorMesEAno(matricula, ano, mes);

                if (horasExtras100Mes != null && horasExtras100Mes.compareTo(BigDecimal.ZERO) > 0) {
                    totalHorasExtras100Ano = totalHorasExtras100Ano.add(horasExtras100Mes);
                    mesesComHE100++;
                }
            }

            // Se recebeu HE 100% em 6+ meses, considera habitual - divide por 12
            // Se eventual, divide apenas pelos meses que trabalhou HE
            BigDecimal mediaHorasExtras100;
            if (mesesComHE100 >= 6) {
                mediaHorasExtras100 = totalHorasExtras100Ano.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            } else if (mesesComHE100 > 0) {
                mediaHorasExtras100 = totalHorasExtras100Ano.divide(new BigDecimal(mesesComHE100), 2, RoundingMode.HALF_UP);
            } else {
                mediaHorasExtras100 = BigDecimal.ZERO;
            }

            return mediaHorasExtras100;

        } catch (Exception e) {
            logger.error("Erro ao calcular média HE 100% para matrícula {}: {}", matricula, e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calcularSomaDSRDiurnoAno(String matricula, int ano) {
        try {
            BigDecimal somaDSRDiurno = BigDecimal.ZERO;

            // Considerar até novembro para cálculo do 13º
            int mesLimite = Math.min(LocalDate.now().getMonthValue() - 1, 11);
            if (mesLimite == 0) mesLimite = 12;

            for (int mes = 1; mes <= mesLimite; mes++) {
                // Buscar DSR Diurno do mês (código 5)
                BigDecimal dsrDiurnoMes = folhaMensalEventosCalculadaRepository.findDSRDiurnoPorMesEAno(matricula, ano, mes);

                if (dsrDiurnoMes != null) {
                    somaDSRDiurno = somaDSRDiurno.add(dsrDiurnoMes);
                }
            }

            return somaDSRDiurno;

        } catch (Exception e) {
            logger.error("Erro ao calcular soma DSR Diurno para matrícula {}: {}", matricula, e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calcularSomaDSRNoturnoAno(String matricula, int ano) {
        try {
            BigDecimal somaDSRNoturno = BigDecimal.ZERO;

            // Considerar até novembro para cálculo do 13º
            int mesLimite = Math.min(LocalDate.now().getMonthValue() - 1, 11);
            if (mesLimite == 0) mesLimite = 12;

            for (int mes = 1; mes <= mesLimite; mes++) {
                // Buscar DSR Noturno do mês (código 25)
                BigDecimal dsrNoturnoMes = folhaMensalEventosCalculadaRepository.findDSRNoturnoPorMesEAno(matricula, ano, mes);

                if (dsrNoturnoMes != null) {
                    somaDSRNoturno = somaDSRNoturno.add(dsrNoturnoMes);
                }
            }

            return somaDSRNoturno;

        } catch (Exception e) {
            logger.error("Erro ao calcular soma DSR Noturno para matrícula {}: {}", matricula, e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal calcularMediaAdicionalNoturnoAno(String matricula, int ano) {
        try {
            BigDecimal totalAdicionalNoturnoAno = BigDecimal.ZERO;
            int mesesComAdicionalNoturno = 0;

            // Considerar até novembro para cálculo do 13º
            int mesLimite = Math.min(LocalDate.now().getMonthValue() - 1, 11);
            if (mesLimite == 0) mesLimite = 12;

            for (int mes = 1; mes <= mesLimite; mes++) {
                // Buscar adicional noturno do mês (código 24)
                BigDecimal adicionalNoturnoMes = folhaMensalEventosCalculadaRepository.findAdicionalNoturnoPorMesEAno(matricula, ano, mes);

                if (adicionalNoturnoMes != null && adicionalNoturnoMes.compareTo(BigDecimal.ZERO) > 0) {
                    totalAdicionalNoturnoAno = totalAdicionalNoturnoAno.add(adicionalNoturnoMes);
                    mesesComAdicionalNoturno++;
                }
            }

            // Se recebeu adicional noturno em 6+ meses, considera habitual - divide por 12
            // Se eventual, divide apenas pelos meses que recebeu
            BigDecimal mediaAdicionalNoturno;
            if (mesesComAdicionalNoturno >= 6) {
                mediaAdicionalNoturno = totalAdicionalNoturnoAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            } else if (mesesComAdicionalNoturno > 0) {
                mediaAdicionalNoturno = totalAdicionalNoturnoAno.divide(new BigDecimal(mesesComAdicionalNoturno), 2, RoundingMode.HALF_UP);
            } else {
                mediaAdicionalNoturno = BigDecimal.ZERO;
            }

            return mediaAdicionalNoturno;

        } catch (Exception e) {
            logger.error("Erro ao calcular média adicional noturno para matrícula {}: {}", matricula, e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public boolean isSegundaParcela13o(LocalDate dataCalculo) {
        // Primeira parcela: até 30 de novembro
        // Segunda parcela: a partir de 1º de dezembro
        return dataCalculo.getMonthValue() == 12 || (dataCalculo.getMonthValue() == 11 && dataCalculo.getDayOfMonth() > 30);
    }
}
