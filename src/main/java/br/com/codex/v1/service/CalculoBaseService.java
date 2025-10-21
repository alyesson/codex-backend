package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.TabelaDeducaoInssRepository;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import br.com.codex.v1.utilitario.Calendario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CalculoBaseService {

    Calendario calendario = new Calendario();

    @Autowired
    private TabelaDeducaoInssRepository tabelaDeducaoInssRepository;

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    public int calcularMesesTrabalhados13o(LocalDate dataAdmissao, LocalDate dataCalculo) {
        int anoAtual = dataCalculo.getYear();
        int mesesTrabalhados = 0;

        // Verificar cada mês do ano
        for (int mes = 1; mes <= 12; mes++) {
            LocalDate primeiroDiaMes = LocalDate.of(anoAtual, mes, 1);
            LocalDate ultimoDiaMes = primeiroDiaMes.withDayOfMonth(primeiroDiaMes.lengthOfMonth());

            // Verificar se o funcionário trabalhou pelo menos 15 dias neste mês
            if (trabalhouPeloMenos15DiasNoMes(dataAdmissao, dataCalculo, mes, anoAtual)) {
                mesesTrabalhados++;
            }
        }

        return mesesTrabalhados;
    }

    public boolean trabalhouPeloMenos15DiasNoMes(LocalDate dataAdmissao, LocalDate dataCalculo, int mes, int ano) {
        LocalDate primeiroDiaMes = LocalDate.of(ano, mes, 1);
        LocalDate ultimoDiaMes = primeiroDiaMes.withDayOfMonth(primeiroDiaMes.lengthOfMonth());

        // Se o mês for no futuro, não conta
        if (primeiroDiaMes.isAfter(dataCalculo)) {
            return false;
        }

        // Se foi admitido durante o mês
        if (dataAdmissao.getYear() == ano && dataAdmissao.getMonthValue() == mes) {
            LocalDate dataInicio = dataAdmissao;
            LocalDate dataFim = ultimoDiaMes.isAfter(dataCalculo) ? dataCalculo : ultimoDiaMes;

            // Calcular dias trabalhados no mês
            long diasTrabalhados = calcularDiasUteisTrabalhados(dataInicio, dataFim);
            return diasTrabalhados >= 15;
        }

        // Se foi admitido antes do mês e não foi demitido
        if (dataAdmissao.isBefore(primeiroDiaMes)) {
            LocalDate dataFim = ultimoDiaMes.isAfter(dataCalculo) ? dataCalculo : ultimoDiaMes;
            long diasTrabalhados = calcularDiasUteisTrabalhados(primeiroDiaMes, dataFim);
            return diasTrabalhados >= 15;
        }

        return false;
    }

    public long calcularDiasUteisTrabalhados(LocalDate dataInicio, LocalDate dataFim) {
        long diasTrabalhados = 0;
        LocalDate data = dataInicio;

        while (!data.isAfter(dataFim)) {
            // Considera apenas dias de semana (segunda a sexta)
            if (data.getDayOfWeek() != DayOfWeek.SATURDAY && data.getDayOfWeek() != DayOfWeek.SUNDAY) {
                diasTrabalhados++;
            }
            data = data.plusDays(1);
        }

        return diasTrabalhados;
    }

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

    public BigDecimal calcularINSS(BigDecimal valorBruto) {

        Optional<TabelaDeducaoInss> valorCota = tabelaDeducaoInssRepository.findTopByOrderById();

        if(valorCota.isEmpty()){
            throw new DataIntegrityViolationException("Nenhuma cota foi cadastrada na tabela de dedução do INSS");
        }

        BigDecimal[] faixas = {
                new BigDecimal(String.valueOf(valorCota.get().getFaixaSalario1())),
                new BigDecimal(String.valueOf(valorCota.get().getFaixaSalario2())),
                new BigDecimal(String.valueOf(valorCota.get().getFaixaSalario3())),
                new BigDecimal(String.valueOf(valorCota.get().getFaixaSalario4()))
        };

        BigDecimal[] aliquotas = {
                new BigDecimal(String.valueOf(valorCota.get().getAliquota1())),
                new BigDecimal(String.valueOf(valorCota.get().getAliquota2())),
                new BigDecimal(String.valueOf(valorCota.get().getAliquota3())),
                new BigDecimal(String.valueOf(valorCota.get().getAliquota4()))
        };

        BigDecimal inss = java.math.BigDecimal.ZERO;
        BigDecimal valorRestante = valorBruto;

        for (int i = 0; i < faixas.length; i++) {
            if (valorRestante.compareTo(java.math.BigDecimal.ZERO) <= 0) break;

            BigDecimal baseCalculo;
            if (i == 0) {
                baseCalculo = valorRestante.min(faixas[i]);
            } else {
                BigDecimal diferencaFaixa = faixas[i].subtract(faixas[i-1]);
                baseCalculo = valorRestante.min(diferencaFaixa);
            }

            inss = inss.add(baseCalculo.multiply(aliquotas[i]));
            valorRestante = valorRestante.subtract(baseCalculo);
        }

        // Teto do INSS
        BigDecimal tetoINSS = new BigDecimal("908.85");
        return inss.min(tetoINSS).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularIRRF(BigDecimal baseCalculo) {
        try {
            Optional<TabelaImpostoRenda> tabelaIrrfOpt = tabelaImpostoRendaRepository.findTopByOrderById();

            if (tabelaIrrfOpt.isEmpty()) {
                throw new RuntimeException("Tabela IRRF não encontrada");
            }

            TabelaImpostoRenda tabelaIrrf = tabelaIrrfOpt.get();

            // 1ª Faixa: Até faixaSalario1 - Isento
            if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario1()) <= 0) {
                return java.math.BigDecimal.ZERO;
            }
            // 2ª Faixa: Até faixaSalario2
            else if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario2()) <= 0) {
                return baseCalculo.multiply(tabelaIrrf.getAliquota1())
                        .subtract(tabelaIrrf.getParcelaDeduzir1())
                        .setScale(2, RoundingMode.HALF_UP);
            }
            // 3ª Faixa: Até faixaSalario3
            else if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario3()) <= 0) {
                return baseCalculo.multiply(tabelaIrrf.getAliquota2())
                        .subtract(tabelaIrrf.getParcelaDeduzir2())
                        .setScale(2, RoundingMode.HALF_UP);
            }
            // 4ª Faixa: Até faixaSalario4
            else if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario4()) <= 0) {
                return baseCalculo.multiply(tabelaIrrf.getAliquota3())
                        .subtract(tabelaIrrf.getParcelaDeduzir3())
                        .setScale(2, RoundingMode.HALF_UP);
            }
            // 5ª Faixa: Até faixaSalario5
            else if (baseCalculo.compareTo(tabelaIrrf.getFaixaSalario5()) <= 0) {
                return baseCalculo.multiply(tabelaIrrf.getAliquota4())
                        .subtract(tabelaIrrf.getParcelaDeduzir4())
                        .setScale(2, RoundingMode.HALF_UP);
            }
            // Acima da 5ª Faixa
            else {
                return baseCalculo.multiply(tabelaIrrf.getAliquota5())
                        .subtract(tabelaIrrf.getParcelaDeduzir5())
                        .setScale(2, RoundingMode.HALF_UP);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular IRRF: " + e.getMessage());
        }
    }

    public BigDecimal calcularDescontoPorHoras(BigDecimal valorHora, BigDecimal faltas, LocalTime entrada, LocalTime saida) {

        Duration duracao = Duration.between(entrada, saida);

        int horas = duracao.toHoursPart();
        BigDecimal minutos = BigDecimal.valueOf(duracao.toMinutesPart()).divide(BigDecimal.valueOf(60), 2,RoundingMode.HALF_UP).setScale(2,RoundingMode.HALF_UP);
        BigDecimal horasTotais = BigDecimal.valueOf(horas).add(minutos);

        return valorHora.multiply(horasTotais).multiply(faltas).setScale(1, RoundingMode.HALF_UP);
    }

}
