package br.com.codex.v1.service.rh;

import br.com.codex.v1.domain.repository.*;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaQuinzenal;
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

    @Autowired
    private TabelaDeducaoInssRepository tabelaDeducaoInssRepository;

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Autowired
    private CalculosAuxiliaresFolha calculosAuxiliaresFolha;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Autowired
    private FolhaMensalRepository folhaMensalRepository;

    @Autowired
    FolhaQuinzenalRepository folhaQuinzenalRepository;

    public FolhaMensal findByMatriculaColaborador(String numeroMatricula) {
        Optional<FolhaMensal> obj = folhaMensalRepository.findByMatriculaColaborador(numeroMatricula);
        if (obj.isPresent()) {
            return obj.get();
        } else {
            throw new ObjectNotFoundException("Folha mensal não encontrada para matrícula: " + numeroMatricula);
        }
    }

    public FolhaQuinzenal findByMatriculaFuncionario(String numeroMatricula) {
        Optional<FolhaQuinzenal> obj = folhaQuinzenalRepository.findByMatriculaColaborador(numeroMatricula);
        if (obj.isPresent()) {
            return obj.get();
        } else {
            throw new ObjectNotFoundException("Folha quinzenal não encontrada para matrícula: " + numeroMatricula);
        }
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
            BigDecimal valorComAdicional = valorBase.multiply(new BigDecimal("1.7")).multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);

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
            BigDecimal valorComAdicional = valorBase.multiply(new BigDecimal("2.0")).multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", valorBase);
            resultado.put("vencimentos", valorComAdicional);
            resultado.put("descontos", BigDecimal.ZERO);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular Média de Horas Extras 1000% Sobre 1ª Parcela do 13º: " + e.getMessage());
        }

        return resultado;
    }

    public Map<String, BigDecimal> calcularMediaDSRDiurnoPrimeiraParcela13(String matricula, BigDecimal salarioBase, Integer mesesTrabalhados) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            LocalDate hoje = LocalDate.now();
            int anoAtual = hoje.getYear();

            // 1. Somar DSR Diurno de janeiro a novembro
            // Código evento 5 = "Horas repouso remunerado diurno" (DSR Diurno)
            BigDecimal somaDsrDiurnoAno = BigDecimal.ZERO;
            int mesesConsiderados = 0;

            for (int mes = 1; mes <= 11; mes++) { // Janeiro a novembro
                BigDecimal dsrDiurnoMes = folhaMensalEventosCalculadaRepository.findSomaEventoPorMesEAno(matricula, 5, anoAtual, mes);

                if (dsrDiurnoMes != null && dsrDiurnoMes.compareTo(BigDecimal.ZERO) > 0) {
                    somaDsrDiurnoAno = somaDsrDiurnoAno.add(dsrDiurnoMes);
                    mesesConsiderados++;
                }
            }

            // 2. Calcular média mensal (soma / 11 meses)
            BigDecimal mediaMensalDsrDiurno = mesesConsiderados > 0 ? somaDsrDiurnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

            // 3. Adicionar ao salário bruto para base do 13º
            BigDecimal baseCalculo13 = salarioBase.add(mediaMensalDsrDiurno);

            // 4. Calcular 1/12 do valor (proporção mensal do 13º)
            BigDecimal decimoTerceiroMensal = baseCalculo13.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

            // 5. Calcular valor proporcional do 13º
            BigDecimal decimoTerceiroProporcional = decimoTerceiroMensal.multiply(new BigDecimal(mesesTrabalhados));

            // 6. Primeira parcela = 50% do valor proporcional
            BigDecimal primeiraParcela = decimoTerceiroProporcional.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados)); // meses trabalhados
            resultado.put("vencimentos", primeiraParcela);                // valor 1ª parcela
            resultado.put("descontos", BigDecimal.ZERO);

            logger.debug("Cálculo DSR Diurno 13º - Média: R$ {}, Base: R$ {}, " +  "13º mensal: R$ {}, 13º proporcional: R$ {}, 1ª parcela: R$ {}",
                    mediaMensalDsrDiurno, baseCalculo13, decimoTerceiroMensal, decimoTerceiroProporcional, primeiraParcela);

        } catch (Exception e) {
            logger.error("Erro ao calcular Média de DSR Diurno Sobre 1ª Parcela do 13º para matrícula {}", matricula, e);
            throw new RuntimeException("Erro ao calcular Média de DSR Diurno Sobre 1ª Parcela do 13º: " + e.getMessage());
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
            BigDecimal adicionalPericulosidade = calculosAuxiliaresFolha.calcularAdicionalPericulosidade(matricula, salarioBase, anoAtual);

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
            BigDecimal valorHoraNormal = salarioBase.divide(horasTrabalhadasPorMes, 4, RoundingMode.HALF_UP);
            BigDecimal valorHoraExtra50 = valorHoraNormal.multiply(new BigDecimal("1.5")).setScale(4, RoundingMode.HALF_UP);
            BigDecimal totalHorasExtrasAno = calculosAuxiliaresFolha.buscarTotalHorasExtrasQuantidadeAno(matricula, 98, anoAtual);
            BigDecimal mediaMensalHoras = totalHorasExtrasAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
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

            BigDecimal valorHoraNormal = salarioBase.divide(horasTrabalhadasPorMes, 4, RoundingMode.HALF_UP);
            BigDecimal valorHoraExtra70 = valorHoraNormal.multiply(new BigDecimal("1.7")).setScale(4, RoundingMode.HALF_UP);
            BigDecimal totalHorasExtrasAno = calculosAuxiliaresFolha.buscarTotalHorasExtrasQuantidadeAno(matricula, 99, anoAtual);
            BigDecimal mediaMensalHoras = totalHorasExtrasAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
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

            BigDecimal valorHoraNormal = salarioBase.divide(horasTrabalhadasPorMes, 4, RoundingMode.HALF_UP);
            BigDecimal valorHoraExtra100 = valorHoraNormal.multiply(new BigDecimal("2.0")).setScale(4, RoundingMode.HALF_UP);
            BigDecimal totalHorasExtrasAno = calculosAuxiliaresFolha.buscarTotalHorasExtrasQuantidadeAno(matricula, 100, anoAtual);
            BigDecimal mediaMensalHoras = totalHorasExtrasAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
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

            BigDecimal somaDSRAno = folhaMensalEventosCalculadaRepository.findSomaValorHorasExtrasAno(matricula, 5, anoAtual);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
            BigDecimal mediaDSR;

            if (mesesTrabalhados == 12) {
                mediaDSR = somaDSRAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP);
            } else {
                // Trabalhou período parcial: (soma/11) × (meses/12)
                mediaDSR = somaDSRAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(mesesTrabalhados))
                        .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            }

            BigDecimal mediaDSROpcaoB = somaDSRAno.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados)).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
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

            BigDecimal somaDSRNoturnoAno = folhaMensalEventosCalculadaRepository.findSomaValorHorasExtrasAno(matricula, 25, anoAtual);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
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

            BigDecimal valorInsalubridade = folhaMensalEventosCalculadaRepository.findUltimoValorInsalubridade(matricula, 46, anoAtual);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
            BigDecimal baseCalculo13 = salarioBase.add(valorInsalubridade);
            BigDecimal decimoTerceiroProporcional = calculosAuxiliaresFolha.calcularDecimoTerceiroProporcional(dataAdmissao, salarioBase, valorInsalubridade);
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

            BigDecimal valorInsalubridade = folhaMensalEventosCalculadaRepository.findUltimoValorInsalubridade(matricula, 46, anoAtual);
            if (valorInsalubridade == null) {
                valorInsalubridade = BigDecimal.ZERO;
            }

            BigDecimal valorPericulosidade = folhaMensalEventosCalculadaRepository.findUltimoValorPericulosidade(matricula, 47, anoAtual);
            if (valorPericulosidade == null) {
                valorPericulosidade = calculosAuxiliaresFolha.calcularAdicionalPericulosidade(matricula, salarioBase, anoAtual);
            }

            // 2. Calcular médias de horas extras para a base do 13º
            BigDecimal mediaHE50 = calcularMediaHE50SegundaParcela13(matricula, salarioBase, horasTrabalhadasPorMes).get("vencimentos");
            BigDecimal mediaHE70 = calcularMediaHE70SegundaParcela13(matricula, salarioBase, horasTrabalhadasPorMes).get("vencimentos");
            BigDecimal mediaHE100 = calcularMediaHE100SegundaParcela13(matricula, salarioBase, horasTrabalhadasPorMes).get("vencimentos");

            BigDecimal totalMediaHE = mediaHE50.add(mediaHE70).add(mediaHE100);

            BigDecimal decimoTerceiroProporcional = calculosAuxiliaresFolha.calcularDecimoTerceiroProporcional(dataAdmissao, salarioBase, valorInsalubridade, valorPericulosidade, totalMediaHE);
            BigDecimal primeiraParcela = decimoTerceiroProporcional.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal segundaParcelaBruta = decimoTerceiroProporcional.subtract(primeiraParcela).setScale(2, RoundingMode.HALF_UP);
            Map<String, BigDecimal> descontos = calcularDescontos13o(segundaParcelaBruta, numeroDependentes);
            BigDecimal totalDescontos = descontos.get("total");
            //BigDecimal segundaParcelaLiquida = segundaParcelaBruta.subtract(totalDescontos);

            // 7. Montar resultado
            resultado.put("referencia", new BigDecimal(calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje)));
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

            BigDecimal mediaComissoes = calculosAuxiliaresFolha.calcularMediaComissoesAno(matricula, anoAtual);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
            BigDecimal baseCalculo = salarioBase.add(mediaComissoes);
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

            BigDecimal valorHoraNormal = salarioBase.divide(horasPorMes, 4, RoundingMode.HALF_UP);
            BigDecimal mediaHorasExtras50 = calculosAuxiliaresFolha.calcularMediaHorasExtras50Ano(matricula, anoAtual);
            BigDecimal valorHoraExtra50 = valorHoraNormal.multiply(new BigDecimal("1.5")); // +50%
            BigDecimal valorMediaHE50 = mediaHorasExtras50.multiply(valorHoraExtra50).setScale(2, RoundingMode.HALF_UP);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
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

            BigDecimal valorHoraNormal = salarioBase.divide(horasPorMes, 4, RoundingMode.HALF_UP);
            BigDecimal mediaHorasExtras70 = calculosAuxiliaresFolha.calcularMediaHorasExtras70Ano(matricula, anoAtual);
            BigDecimal valorHoraExtra70 = valorHoraNormal.multiply(new BigDecimal("1.7")); // +70%
            BigDecimal valorMediaHE70 = mediaHorasExtras70.multiply(valorHoraExtra70).setScale(2, RoundingMode.HALF_UP);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
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

            BigDecimal valorHoraNormal = salarioBase.divide(horasPorMes, 4, RoundingMode.HALF_UP);
            BigDecimal mediaHorasExtras100 = calculosAuxiliaresFolha.calcularMediaHorasExtras100Ano(matricula, anoAtual);
            BigDecimal valorHoraExtra100 = valorHoraNormal.multiply(new BigDecimal("2.0")); // +100%
            BigDecimal valorMediaHE100 = mediaHorasExtras100.multiply(valorHoraExtra100).setScale(2, RoundingMode.HALF_UP);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
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

            BigDecimal somaDSRDiurnoAno = calculosAuxiliaresFolha.calcularSomaDSRDiurnoAno(matricula, anoAtual);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
            BigDecimal mediaDSRDiurno;
            if (mesesTrabalhados == 12) {
                // Trabalhou ano completo: divide por 11 (meses com DSR)
                mediaDSRDiurno = somaDSRDiurnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP);
            } else {
                // Trabalhou período parcial: (soma/11) × (meses/12)
                mediaDSRDiurno = somaDSRDiurnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(mesesTrabalhados)).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            }
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

            BigDecimal somaDSRNoturnoAno = calculosAuxiliaresFolha.calcularSomaDSRNoturnoAno(matricula, anoAtual);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
            BigDecimal mediaDSRNoturno;
            if (mesesTrabalhados == 12) {
                // Trabalhou ano completo: divide por 11 (meses com DSR)
                mediaDSRNoturno = somaDSRNoturnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP);
            } else {
                // Trabalhou período parcial: (soma/11) × (meses/12)
                mediaDSRNoturno = somaDSRNoturnoAno.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(mesesTrabalhados)).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            }
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

            BigDecimal mediaMensalAdicionalNoturno  = calculosAuxiliaresFolha.calcularMediaAdicionalNoturnoAno(matricula, anoAtual);
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje);
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

            BigDecimal valorPericulosidade = folhaMensalEventosCalculadaRepository.findUltimoValorPericulosidade(matricula, 47, anoAtual);
            if (valorPericulosidade.compareTo(BigDecimal.ZERO) == 0) {
                valorPericulosidade = salarioBase.multiply(new BigDecimal("0.30"))
                        .setScale(2, RoundingMode.HALF_UP);
            }
            BigDecimal decimoTerceiroProporcional = calculosAuxiliaresFolha.calcularDecimoTerceiroProporcional(dataAdmissao, salarioBase, valorPericulosidade);

            resultado.put("referencia", new BigDecimal(calculosAuxiliaresFolha.calcularMesesTrabalhados13o(dataAdmissao, hoje)));
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

    public Map<String, BigDecimal> calcularINSSDecimoTerceiro(LocalDate datAdmissao, LocalDate dataCalculo, BigDecimal salarioBase) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {

            // 1. Calcular meses trabalhados no ano
            int mesesTrabalhados = calculosAuxiliaresFolha.calcularMesesTrabalhados13o(datAdmissao, dataCalculo);

            if (mesesTrabalhados == 0) {
                logger.info("Colaborador não tem direito a 13º salário");
                return resultado;
            }

            // 2. Calcular valor bruto do 13º
            BigDecimal valorBruto13o = salarioBase.divide(new BigDecimal("12"), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados));

            // 3. Verificar se é primeira ou segunda parcela
            boolean isSegundaParcela = calculosAuxiliaresFolha.isSegundaParcela13o(dataCalculo);

            if (!isSegundaParcela) {
                logger.info("Primeira parcela do 13º - INSS não incide");
                return resultado;
            }

            // 4. Calcular INSS sobre o valor bruto do 13º (segunda parcela)
            BigDecimal valorINSS = calcularINSS(valorBruto13o);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("descontos", valorINSS);

            logger.info("INSS 13º calculado: R$ {} para {} meses trabalhados", valorINSS, mesesTrabalhados);

        } catch (Exception e) {
            logger.error("Erro ao calcular INSS do 13º", e);
        }

        return resultado;
    }
}
