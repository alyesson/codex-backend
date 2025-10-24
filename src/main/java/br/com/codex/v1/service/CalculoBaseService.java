package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.domain.repository.FolhaMensalRepository;
import br.com.codex.v1.domain.repository.TabelaDeducaoInssRepository;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import br.com.codex.v1.domain.rh.TabelaImpostoRenda;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import br.com.codex.v1.utilitario.Calendario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;

@Service
public class CalculoBaseService {
    private static final Logger logger = LoggerFactory.getLogger(CalculoBaseService.class);

    Calendario calendario = new Calendario();

    @Autowired
    private TabelaDeducaoInssRepository tabelaDeducaoInssRepository;

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Autowired
    private FolhaMensalRepository folhaMensalRepository;

    /*public int calcularMesesTrabalhados13o(LocalDate dataAdmissao, LocalDate dataCalculo) {
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
    }*/

    /*public boolean trabalhouPeloMenos15DiasNoMes(LocalDate dataAdmissao, LocalDate dataCalculo, int mes, int ano) {
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
    }*/

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

    public FolhaMensal findByMatriculaColaborador(String numeroMatricula) {
        logger.info("🔎 BUSCANDO FOLHA - Matrícula: {}", numeroMatricula);

        Optional<FolhaMensal> obj = folhaMensalRepository.findByMatriculaColaborador(numeroMatricula);

        if (obj.isPresent()) {
            FolhaMensal folha = obj.get();
            logger.info("✅ Folha ENCONTRADA - Matrícula: {}, ID: {}, Nome: {}",
                    numeroMatricula, folha.getId(), folha.getNomeColaborador());
            return folha;
        } else {
            logger.warn("❌ Folha NÃO ENCONTRADA - Matrícula: {}", numeroMatricula);
            throw new ObjectNotFoundException("Folha mensal não encontrada para matrícula: " + numeroMatricula);
        }
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

    public BigDecimal calcularSalarioFamilia(BigDecimal cota, int filhos) {

        BigDecimal valorTotal = BigDecimal.ZERO;

        try{
            valorTotal = cota.multiply(BigDecimal.valueOf(filhos)).setScale(2, RoundingMode.HALF_UP);

        }catch (Exception e){
            throw new DataIntegrityViolationException("Erro ao calcular salário família: " + e);
        }

        return valorTotal;
    }

    public Map<String, BigDecimal> calcularMediaHE50PrimeiraParcela13(String matricula, BigDecimal salarioPorHora) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();
            int mesAtual = hoje.getMonthValue();
            int mesAnterior = mesAtual - 1;

            // Se janeiro, considera dezembro do ano anterior
            if (mesAnterior == 0) {
                mesAnterior = 12;
                anoAtual = anoAtual - 1;
            }

            // 1. Buscar soma das HE 50% do mês anterior
            BigDecimal somaMesAnterior = folhaMensalEventosCalculadaRepository.findSomaHorasExtrasPorMesEAno(matricula, 98, anoAtual, mesAnterior);

            // 2. Buscar média das HE 50% do ano até o momento
            BigDecimal mediaAnoAteMes = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasAteMes(matricula, 98, anoAtual, mesAnterior);

            // 3. Cálculo da média
            BigDecimal mediaCalculada = BigDecimal.ZERO;
            if (mesAnterior > 0) {
                mediaCalculada = somaMesAnterior.divide(new BigDecimal(mesAnterior), 2, RoundingMode.HALF_UP);
            }

            // 4. Usar a maior média entre mês anterior e média do ano
            BigDecimal mediaFinal = mediaCalculada.max(mediaAnoAteMes);

            // 5. Cálculo do valor base
            BigDecimal valorBase = mediaFinal.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);

            // 6. Cálculo do valor com adicional 50% (equivalente ao valorMedia50HoraExtra)
            BigDecimal valorComAdicional = valorBase
                    .multiply(new BigDecimal("1.5")) // +50%
                    .multiply(new BigDecimal("0.5")) // 50% da parcela do 13º
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", valorBase);
            resultado.put("vencimentos", valorComAdicional);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 50% Sobre 1ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaHE70PrimeiraParcela13(String matricula, BigDecimal salarioPorHora) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();
            int mesAtual = hoje.getMonthValue();
            int mesAnterior = mesAtual - 1;

            // Se janeiro, considera dezembro do ano anterior
            if (mesAnterior == 0) {
                mesAnterior = 12;
                anoAtual = anoAtual - 1;
            }

            // 1. Buscar soma das HE 70% do mês anterior
            BigDecimal somaMesAnterior = folhaMensalEventosCalculadaRepository.findSomaHorasExtrasPorMesEAno(matricula, 99, anoAtual, mesAnterior);

            // 2. Buscar média das HE 70% do ano até o momento
            BigDecimal mediaAnoAteMes = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasAteMes(matricula, 99, anoAtual, mesAnterior);

            // 3. Cálculo da média
            BigDecimal mediaCalculada = BigDecimal.ZERO;
            if (mesAnterior > 0) {
                mediaCalculada = somaMesAnterior.divide(new BigDecimal(mesAnterior), 2, RoundingMode.HALF_UP);
            }

            // 4. Usar a maior média entre mês anterior e média do ano
            BigDecimal mediaFinal = mediaCalculada.max(mediaAnoAteMes);

            // 5. Cálculo do valor base
            BigDecimal valorBase = mediaFinal.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);

            // 6. Cálculo do valor com adicional 70%
            BigDecimal valorComAdicional = valorBase
                    .multiply(new BigDecimal("1.7")) // +70%
                    .multiply(new BigDecimal("0.5")) // 50% da parcela do 13º
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", valorBase);
            resultado.put("vencimentos", valorComAdicional);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 70% Sobre 1ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaHE100PrimeiraParcela13(String matricula, BigDecimal salarioPorHora) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();
            int mesAtual = hoje.getMonthValue();
            int mesAnterior = mesAtual - 1;

            // Se janeiro, considera dezembro do ano anterior
            if (mesAnterior == 0) {
                mesAnterior = 12;
                anoAtual = anoAtual - 1;
            }

            // 1. Buscar soma das HE 100% do mês anterior
            BigDecimal somaMesAnterior = folhaMensalEventosCalculadaRepository.findSomaHorasExtrasPorMesEAno(matricula, 100, anoAtual, mesAnterior);

            // 2. Buscar média das HE 100% do ano até o momento
            BigDecimal mediaAnoAteMes = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasAteMes(matricula, 100, anoAtual, mesAnterior);

            // 3. Cálculo da média
            BigDecimal mediaCalculada = BigDecimal.ZERO;
            if (mesAnterior > 0) {
                mediaCalculada = somaMesAnterior.divide(new BigDecimal(mesAnterior), 2, RoundingMode.HALF_UP);
            }

            // 4. Usar a maior média entre mês anterior e média do ano
            BigDecimal mediaFinal = mediaCalculada.max(mediaAnoAteMes);

            // 5. Cálculo do valor base
            BigDecimal valorBase = mediaFinal.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);

            // 6. Cálculo do valor com adicional 100%
            BigDecimal valorComAdicional = valorBase
                    .multiply(new BigDecimal("2.0")) // +100%
                    .multiply(new BigDecimal("0.5")) // 50% da parcela do 13º
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", valorBase);
            resultado.put("vencimentos", valorComAdicional);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 1000% Sobre 1ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaDSRNoturnoPrimeiraParcela13(String matricula, BigDecimal salarioBase, Integer mesesTrabalhados) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. Somar DSR Noturno de janeiro a novembro
            BigDecimal somaDsrNoturnoAno = BigDecimal.ZERO;
            int mesesConsiderados = 0;

            for (int mes = 1; mes <= 11; mes++) { // Janeiro a novembro
                BigDecimal dsrNoturnoMes = folhaMensalEventosCalculadaRepository.findSomaEventoPorMesEAno(matricula, 25, anoAtual, mes);

                if (dsrNoturnoMes.compareTo(BigDecimal.ZERO) > 0) {
                    somaDsrNoturnoAno = somaDsrNoturnoAno.add(dsrNoturnoMes);
                    mesesConsiderados++;
                }
            }

            // 2. Calcular média mensal (soma / 11 meses)
            BigDecimal mediaMensalDsrNoturno = mesesConsiderados > 0 ? somaDsrNoturnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

            // 3. Adicionar ao salário bruto para base do 13º
            BigDecimal baseCalculo13 = salarioBase.add(mediaMensalDsrNoturno);

            // 4. Calcular 1/12 do valor (proporção mensal do 13º)
            BigDecimal decimoTerceiroMensal = baseCalculo13.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

            // 5. Calcular valor proporcional do 13º
            BigDecimal decimoTerceiroProporcional = decimoTerceiroMensal.multiply(new BigDecimal(mesesTrabalhados));

            // 6. Primeira parcela = 50% do valor proporcional
            BigDecimal primeiraParcela = decimoTerceiroProporcional.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados)); // meses trabalhados
            resultado.put("vencimentos", primeiraParcela);                // valor 1ª parcela
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de DSR Noturno Sobre 1ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularInsalubridadePrimeiraParcela13(String matricula, BigDecimal salarioBase, Integer mesesTrabalhados) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. Buscar o valor do adicional de insalubridade (evento 46)
            // Normalmente é um valor fixo mensal, então pegamos o último valor
            BigDecimal adicionalInsalubridade = folhaMensalEventosCalculadaRepository.findUltimoValorInsalubridade(matricula, 46, anoAtual);

            // Se não encontrou insalubridade, usa zero
            if (adicionalInsalubridade == null) {
                adicionalInsalubridade = BigDecimal.ZERO;
            }

            // 2. Somar insalubridade à remuneração base
            BigDecimal remuneracaoTotal = salarioBase.add(adicionalInsalubridade);

            // 3. Calcular 13º proporcional
            BigDecimal decimoTerceiroProporcional = remuneracaoTotal
                    .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados));

            // 4. Primeira parcela = 50% do valor proporcional (SEM descontos)
            BigDecimal primeiraParcela = decimoTerceiroProporcional
                    .multiply(new BigDecimal("0.5"))
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", primeiraParcela);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Insalubridade Sobre 1ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularPericulosidadePrimeiraParcela13(String matricula, BigDecimal salarioBase, Integer mesesTrabalhados) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. Buscar o valor do adicional de periculosidade (evento 47)
            // OU calcular 30% do salário base (conforme a regra)
            BigDecimal adicionalPericulosidade = calcularAdicionalPericulosidade(matricula, salarioBase, anoAtual);

            // 2. Somar periculosidade à remuneração base
            BigDecimal remuneracaoTotal = salarioBase.add(adicionalPericulosidade);

            // 5. Calcular 13º proporcional
            BigDecimal decimoTerceiroProporcional = remuneracaoTotal.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(mesesTrabalhados));

            // 6. Primeira parcela = 50% do valor proporcional (SEM descontos)
            BigDecimal primeiraParcela = decimoTerceiroProporcional.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados)); // meses trabalhados
            resultado.put("vencimentos", primeiraParcela);                // valor 1ª parcela
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Periculosidade Sobre 1ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaHE50SegundaParcela13(String matricula, BigDecimal salarioBase, BigDecimal horasTrabalhadasPorMes) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // **1. Calcular valor da hora normal (CLT: 220 horas)**
            BigDecimal valorHoraNormal = salarioBase.divide(horasTrabalhadasPorMes, 4, RoundingMode.HALF_UP);

            // **2. Calcular valor da hora extra com 50%**
            BigDecimal valorHoraExtra50 = valorHoraNormal.multiply(new BigDecimal("1.5")).setScale(4, RoundingMode.HALF_UP);

            // **3. ✅ Buscar TOTAL DE HORAS EXTRAS do ano INTEIRO (jan a dez)**
            BigDecimal totalHorasExtrasAno = buscarTotalHorasExtrasQuantidadeAno(matricula, 98, anoAtual);

            // **4. Calcular MÉDIA MENSAL de horas extras**
            BigDecimal mediaMensalHoras = totalHorasExtrasAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

            // **5. Calcular VALOR da média mensal de horas extras**
            BigDecimal valorMediaMensalHE = mediaMensalHoras.multiply(valorHoraExtra50).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", mediaMensalHoras);
            resultado.put("vencimentos", valorMediaMensalHE);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 50% para 2ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaHE70SegundaParcela13(String matricula, BigDecimal salarioBase, BigDecimal horasTrabalhadasPorMes) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // **1. Calcular valor da hora normal (CLT: 220 horas)**
            BigDecimal valorHoraNormal = salarioBase.divide(horasTrabalhadasPorMes, 4, RoundingMode.HALF_UP);

            // **2. Calcular valor da hora extra com 70%**
            BigDecimal valorHoraExtra70 = valorHoraNormal.multiply(new BigDecimal("1.7")).setScale(4, RoundingMode.HALF_UP);

            // **3. ✅ Buscar TOTAL DE HORAS EXTRAS do ano INTEIRO (jan a dez)**
            BigDecimal totalHorasExtrasAno = buscarTotalHorasExtrasQuantidadeAno(matricula, 99, anoAtual);

            // **4. Calcular MÉDIA MENSAL de horas extras**
            BigDecimal mediaMensalHoras = totalHorasExtrasAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

            // **5. Calcular VALOR da média mensal de horas extras**
            BigDecimal valorMediaMensalHE = mediaMensalHoras.multiply(valorHoraExtra70).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", mediaMensalHoras);
            resultado.put("vencimentos", valorMediaMensalHE);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 70% para 2ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaHE100SegundaParcela13(String matricula, BigDecimal salarioBase, BigDecimal horasTrabalhadasPorMes) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // **1. Calcular valor da hora normal (CLT: 220 horas)**
            BigDecimal valorHoraNormal = salarioBase.divide(horasTrabalhadasPorMes, 4, RoundingMode.HALF_UP);

            // **2. Calcular valor da hora extra com 100%**
            BigDecimal valorHoraExtra100 = valorHoraNormal.multiply(new BigDecimal("2.0")).setScale(4, RoundingMode.HALF_UP);

            // **3. ✅ Buscar TOTAL DE HORAS EXTRAS do ano INTEIRO (jan a dez)**
            BigDecimal totalHorasExtrasAno = buscarTotalHorasExtrasQuantidadeAno(matricula, 100, anoAtual);

            // **4. Calcular MÉDIA MENSAL de horas extras**
            BigDecimal mediaMensalHoras = totalHorasExtrasAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

            // **5. Calcular VALOR da média mensal de horas extras**
            BigDecimal valorMediaMensalHE = mediaMensalHoras.multiply(valorHoraExtra100).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", mediaMensalHoras);
            resultado.put("vencimentos", valorMediaMensalHE);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 100% para 2ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaDSRSegundaParcela13(String matricula, LocalDate dataAdmissao) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. Buscar SOMA de todos os DSRs do ano (evento 5 = DSR Diurno)
            BigDecimal somaDSRAno = folhaMensalEventosCalculadaRepository.findSomaValorHorasExtrasAno(matricula, 5, anoAtual);

            // 2. Calcular meses trabalhados no ano
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 3. **CLT: Duas formas de cálculo (ambas válidas)**
            BigDecimal mediaDSR;

            // Opção A: Dividir por 11 e multiplicar por meses trabalhados
            if (mesesTrabalhados == 12) {
                mediaDSR = somaDSRAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP);
            } else {
                // Trabalhou período parcial: (soma/11) × (meses/12)
                mediaDSR = somaDSRAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(mesesTrabalhados))
                        .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            }

            // Opção B: Dividir por 12 e multiplicar por avos (meses trabalhados)
            BigDecimal mediaDSROpcaoB = somaDSRAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados))
                    .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

            // Usar a maior média (beneficia o trabalhador)
            mediaDSR = mediaDSR.max(mediaDSROpcaoB);

            resultado.put("referencia", BigDecimal.valueOf(mesesTrabalhados));
            resultado.put("vencimentos", mediaDSR);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de DSR para 2ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaDSRNoturnoSegundaParcela13(String matricula, LocalDate dataAdmissao) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. Buscar SOMA de DSRs NOTURNOS do ano (evento 25 = DSR Noturno)
            BigDecimal somaDSRNoturnoAno = folhaMensalEventosCalculadaRepository.findSomaValorHorasExtrasAno(matricula, 25, anoAtual);

            // 2. Calcular meses trabalhados
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 3. Cálculo da média DSR Noturno (mesma lógica do DSR Diurno)
            BigDecimal mediaDSRNoturno;

            if (mesesTrabalhados == 12) {
                // Trabalhou ano completo: divide por 11
                mediaDSRNoturno = somaDSRNoturnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP);
            } else {
                // Trabalhou período parcial: (soma/11) × (meses/12)
                mediaDSRNoturno = somaDSRNoturnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(mesesTrabalhados))
                        .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            }

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", mediaDSRNoturno);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de DSR Noturno para 2ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularInsalubridadeSegundaParcela13(String matricula, BigDecimal salarioBase, LocalDate dataAdmissao) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. Buscar valor da insalubridade (evento 46)
            BigDecimal valorInsalubridade = folhaMensalEventosCalculadaRepository.findUltimoValorInsalubridade(matricula, 46, anoAtual);

            // 2. Calcular meses trabalhados
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 3. **CLT: Insalubridade integra base do 13º**
            BigDecimal baseCalculo13 = salarioBase.add(valorInsalubridade);

            // **2ª parcela = 50% (COM descontos)**
            BigDecimal decimoTerceiroProporcional = calcularDecimoTerceiroProporcional(dataAdmissao, salarioBase, valorInsalubridade);
            BigDecimal segundaParcelaBruta = decimoTerceiroProporcional.multiply(new BigDecimal("0.5"));

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", segundaParcelaBruta);  // ✅ PAGA na 2ª parcela
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Insalubridade para 2ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularSegundaParcela13(String matricula, BigDecimal salarioBase, LocalDate dataAdmissao, Integer numeroDependentes, BigDecimal horasTrabalhadasPorMes) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. Buscar todos os adicionais que compõem a base do 13.º
            BigDecimal valorInsalubridade = folhaMensalEventosCalculadaRepository.findUltimoValorInsalubridade(matricula, 46, anoAtual);
            if (valorInsalubridade == null) {
                valorInsalubridade = BigDecimal.ZERO;
            }

            BigDecimal valorPericulosidade = folhaMensalEventosCalculadaRepository.findUltimoValorPericulosidade(matricula, 47, anoAtual);
            if (valorPericulosidade == null) {
                valorPericulosidade = calcularAdicionalPericulosidade(matricula, salarioBase, anoAtual);
            }

            // 2. Calcular médias de horas extras para a base do 13º
            BigDecimal mediaHE50 = calcularMediaHE50SegundaParcela13(matricula, salarioBase, horasTrabalhadasPorMes).get("vencimentos");
            BigDecimal mediaHE70 = calcularMediaHE70SegundaParcela13(matricula, salarioBase, horasTrabalhadasPorMes).get("vencimentos");
            BigDecimal mediaHE100 = calcularMediaHE100SegundaParcela13(matricula, salarioBase, horasTrabalhadasPorMes).get("vencimentos");

            BigDecimal totalMediaHE = mediaHE50.add(mediaHE70).add(mediaHE100);

            // 3. Calcular 13º proporcional COMPLETO
            BigDecimal decimoTerceiroProporcional = calcularDecimoTerceiroProporcional(dataAdmissao, salarioBase, valorInsalubridade, valorPericulosidade, totalMediaHE);

            // 4. Calcular 1ª parcela (já paga - 50% sem descontos)
            BigDecimal primeiraParcela = decimoTerceiroProporcional.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);

            // 5. Calcular 2ª parcela BRUTA
            BigDecimal segundaParcelaBruta = decimoTerceiroProporcional.subtract(primeiraParcela).setScale(2, RoundingMode.HALF_UP);

            // 6. Aplicar descontos (INSS e IRRF) considerando dependentes
            Map<String, BigDecimal> descontos = calcularDescontos13o(segundaParcelaBruta, numeroDependentes);
            BigDecimal totalDescontos = descontos.get("total");
            //BigDecimal segundaParcelaLiquida = segundaParcelaBruta.subtract(totalDescontos);

            // 7. Montar resultado
            resultado.put("referencia", new BigDecimal(calcularMesesTrabalhados13o(dataAdmissao, hoje)));
            resultado.put("vencimentos", segundaParcelaBruta);
            resultado.put("descontos", totalDescontos);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular 2ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularDecimoTerceiroComMediaComissoes(String matricula, BigDecimal salarioBase, LocalDate dataAdmissao) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. CALCULAR MÉDIA DAS COMISSÕES DO ANO
            BigDecimal mediaComissoes = calcularMediaComissoesAno(matricula, anoAtual);

            // 2. CALCULAR MESES TRABALHADOS
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 3. BASE DE CÁLCULO = Salário Base + Média de Comissões
            BigDecimal baseCalculo = salarioBase.add(mediaComissoes);

            // 4. 13º PROPORCIONAL = (Base / 12) × Meses Trabalhados
            BigDecimal decimoTerceiroProporcional = baseCalculo.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados)).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", decimoTerceiroProporcional);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular 13º com média de comissões: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularComplementoMediaHE5013(String matricula, BigDecimal salarioBase, LocalDate dataAdmissao,BigDecimal horasPorMes) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. CALCULAR VALOR DA HORA NORMAL
            BigDecimal valorHoraNormal = salarioBase.divide(horasPorMes, 4, RoundingMode.HALF_UP);

            // 2. CALCULAR MÉDIA DE HORAS EXTRAS 50% DO ANO
            BigDecimal mediaHorasExtras50 = calcularMediaHorasExtras50Ano(matricula, anoAtual);

            // 3. CALCULAR VALOR DA MÉDIA DE HE 50%
            BigDecimal valorHoraExtra50 = valorHoraNormal.multiply(new BigDecimal("1.5")); // +50%
            BigDecimal valorMediaHE50 = mediaHorasExtras50.multiply(valorHoraExtra50).setScale(2, RoundingMode.HALF_UP);

            // 4. CALCULAR MESES TRABALHADOS
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 5. COMPLEMENTO DO 13º = (Valor média HE 50% / 12) × meses trabalhados
            BigDecimal complemento13 = valorMediaHE50.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados)).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", complemento13);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular complemento média HE 50% do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularComplementoMediaHE7013(String matricula, BigDecimal salarioBase, LocalDate dataAdmissao,BigDecimal horasPorMes) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. CALCULAR VALOR DA HORA NORMAL
            BigDecimal valorHoraNormal = salarioBase.divide(horasPorMes, 4, RoundingMode.HALF_UP);

            // 2. CALCULAR MÉDIA DE HORAS EXTRAS 70% DO ANO
            BigDecimal mediaHorasExtras70 = calcularMediaHorasExtras70Ano(matricula, anoAtual);

            // 3. CALCULAR VALOR DA MÉDIA DE HE 70%
            BigDecimal valorHoraExtra70 = valorHoraNormal.multiply(new BigDecimal("1.7")); // +70%
            BigDecimal valorMediaHE70 = mediaHorasExtras70.multiply(valorHoraExtra70).setScale(2, RoundingMode.HALF_UP);

            // 4. CALCULAR MESES TRABALHADOS
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 5. COMPLEMENTO DO 13º = (Valor média HE 70% / 12) × meses trabalhados
            BigDecimal complemento13 = valorMediaHE70.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados)).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", complemento13);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular complemento média HE 70% do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularComplementoMediaHE10013(String matricula, BigDecimal salarioBase, LocalDate dataAdmissao,BigDecimal horasPorMes) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. CALCULAR VALOR DA HORA NORMAL
            BigDecimal valorHoraNormal = salarioBase.divide(horasPorMes, 4, RoundingMode.HALF_UP);

            // 2. CALCULAR MÉDIA DE HORAS EXTRAS 100% DO ANO
            BigDecimal mediaHorasExtras100 = calcularMediaHorasExtras100Ano(matricula, anoAtual);

            // 3. CALCULAR VALOR DA MÉDIA DE HE 100%
            BigDecimal valorHoraExtra100 = valorHoraNormal.multiply(new BigDecimal("2.0")); // +100%
            BigDecimal valorMediaHE100 = mediaHorasExtras100.multiply(valorHoraExtra100).setScale(2, RoundingMode.HALF_UP);

            // 4. CALCULAR MESES TRABALHADOS
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 5. COMPLEMENTO DO 13º = (Valor média HE 100% / 12) × meses trabalhados
            BigDecimal complemento13 = valorMediaHE100.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados)).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", complemento13);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular complemento média HE 100% do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularComplementoDSRDiurno13(String matricula, LocalDate dataAdmissao) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. CALCULAR SOMA DOS DSRs DIURNOS DO ANO
            BigDecimal somaDSRDiurnoAno = calcularSomaDSRDiurnoAno(matricula, anoAtual);

            // 2. CALCULAR MESES TRABALHADOS
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 3. CALCULAR MÉDIA MENSAL DE DSR DIURNO
            BigDecimal mediaDSRDiurno;
            if (mesesTrabalhados == 12) {
                // Trabalhou ano completo: divide por 11 (meses com DSR)
                mediaDSRDiurno = somaDSRDiurnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP);
            } else {
                // Trabalhou período parcial: (soma/11) × (meses/12)
                mediaDSRDiurno = somaDSRDiurnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(mesesTrabalhados)).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            }

            // 4. COMPLEMENTO DO 13º = Média mensal de DSR Diurno
            BigDecimal complemento13 = mediaDSRDiurno;

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", complemento13);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular complemento DSR Diurno do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularComplementoDSRNoturno13(String matricula, LocalDate dataAdmissao) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. CALCULAR SOMA DOS DSRs NOTURNOS DO ANO
            BigDecimal somaDSRNoturnoAno = calcularSomaDSRNoturnoAno(matricula, anoAtual);

            // 2. CALCULAR MESES TRABALHADOS
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 3. CALCULAR MÉDIA MENSAL DE DSR NOTURNO
            BigDecimal mediaDSRNoturno;
            if (mesesTrabalhados == 12) {
                // Trabalhou ano completo: divide por 11 (meses com DSR)
                mediaDSRNoturno = somaDSRNoturnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP);
            } else {
                // Trabalhou período parcial: (soma/11) × (meses/12)
                mediaDSRNoturno = somaDSRNoturnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(mesesTrabalhados)).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            }

            // 4. COMPLEMENTO DO 13º = Média mensal de DSR Noturno
            BigDecimal complemento13 = mediaDSRNoturno;

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", complemento13);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular complemento DSR Noturno do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaAdicionalNoturno13(String matricula, LocalDate dataAdmissao) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. CALCULAR MÉDIA DO ADICIONAL NOTURNO DO ANO
            BigDecimal mediaMensalAdicionalNoturno  = calcularMediaAdicionalNoturnoAno(matricula, anoAtual);

            // 2. CALCULAR MESES TRABALHADOS
            int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, hoje);

            // 3. VALOR A INCORPORAR NO 13º = Média mensal × (meses trabalhados / 12)
            BigDecimal valorIncorporar13 = mediaMensalAdicionalNoturno .multiply(new BigDecimal(mesesTrabalhados)).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", valorIncorporar13);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular média adicional noturno do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularPericulosidadeSegundaParcela13(String matricula, BigDecimal salarioBase, LocalDate dataAdmissao) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. Buscar valor da periculosidade (evento 47) OU calcular 30%
            BigDecimal valorPericulosidade = folhaMensalEventosCalculadaRepository.findUltimoValorPericulosidade(matricula, 47, anoAtual);

            // Se não encontrou, calcula 30% do salário base
            if (valorPericulosidade.compareTo(BigDecimal.ZERO) == 0) {
                valorPericulosidade = salarioBase.multiply(new BigDecimal("0.30"))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            // 2. Calcular 13º proporcional
            BigDecimal decimoTerceiroProporcional = calcularDecimoTerceiroProporcional(dataAdmissao, salarioBase, valorPericulosidade);

            resultado.put("referencia", new BigDecimal(calcularMesesTrabalhados13o(dataAdmissao, hoje)));
            resultado.put("vencimentos", decimoTerceiroProporcional);  // ✅ PAGA na 2ª parcela
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Periculosidade para 2ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    private Map<String, BigDecimal> calcularDescontos13o(BigDecimal valorBruto, Integer numeroDependentes) {
        Map<String, BigDecimal> descontos = new HashMap<>();

        try {
            // 1. Cálculo do INSS
            //BigDecimal inss = calculoBaseService.calcularINSS(valorBruto);
            BigDecimal inss = calcularINSS(valorBruto);

            // 2. Base para IRRF = Valor Bruto - INSS - Dedução por Dependente
            BigDecimal baseIRRF = valorBruto.subtract(inss);

            // Aplicar dedução por dependente se houver
            if (numeroDependentes != null && numeroDependentes > 0) {
                Optional<TabelaImpostoRenda> tabelaIrrfOpt = tabelaImpostoRendaRepository.findTopByOrderById();
                if (tabelaIrrfOpt.isPresent()) {
                    BigDecimal deducaoDependentes = tabelaIrrfOpt.get().getDeducaoPorDependente()
                            .multiply(BigDecimal.valueOf(numeroDependentes));
                    baseIRRF = baseIRRF.subtract(deducaoDependentes);
                }
            }

            // Garantir que base não seja negativa
            if (baseIRRF.compareTo(BigDecimal.ZERO) < 0) {
                baseIRRF = BigDecimal.ZERO;
            }

            // 3. Cálculo do IRRF
            //BigDecimal irrf = calculoBaseService.calcularIRRF(baseIRRF);
            BigDecimal irrf = calcularIRRF(baseIRRF);

            descontos.put("inss", inss);
            descontos.put("irrf", irrf);
            descontos.put("total", inss.add(irrf));

        } catch (Exception e) {
            descontos.put("inss", BigDecimal.ZERO);
            descontos.put("irrf", BigDecimal.ZERO);
            descontos.put("total", BigDecimal.ZERO);
        }

        return descontos;
    }

    private BigDecimal calcularAdicionalPericulosidade(String matricula, BigDecimal salarioBase, int anoAtual) {
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

    private BigDecimal buscarTotalHorasExtrasQuantidadeAno(String matricula, Integer codigoEvento, int ano) {
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

    private BigDecimal calcularMediaComissoesAno(String matricula, int ano) {
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

    private BigDecimal calcularMediaHorasExtras50Ano(String matricula, int ano) {
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

    private BigDecimal calcularMediaHorasExtras70Ano(String matricula, int ano) {
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

    private BigDecimal calcularMediaHorasExtras100Ano(String matricula, int ano) {
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

    private BigDecimal calcularSomaDSRDiurnoAno(String matricula, int ano) {
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

    private BigDecimal calcularSomaDSRNoturnoAno(String matricula, int ano) {
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

    private BigDecimal calcularMediaAdicionalNoturnoAno(String matricula, int ano) {
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
}
