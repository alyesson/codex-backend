package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.domain.repository.FolhaMensalRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FolhaMensalService {

    @Autowired
    private FolhaMensalRepository folhaMensalRepository;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    public FolhaMensal findByMatriculaColaborador(String numeroMatricula) {
        Optional<FolhaMensal> obj = folhaMensalRepository.findByMatriculaColaborador(numeroMatricula);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Informação não encontrada"));
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

            resultado.put("referencia", new BigDecimal(mesesTrabalhados)); // meses trabalhados
            resultado.put("vencimentos", primeiraParcela);                // valor 1ª parcela
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
            BigDecimal totalHorasExtrasAno = buscarTotalHorasExtrasQuantidadeAno(matricula, 98, anoAtual);

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
            BigDecimal totalHorasExtrasAno = buscarTotalHorasExtrasQuantidadeAno(matricula, 98, anoAtual);

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


    // Métudo para calcular a 2ª parcela do 13º
    public Map<String, BigDecimal> calcularSegundaParcela13ComHE(String matricula, BigDecimal salarioBase, BigDecimal mediaMensalHE, Integer mesesTrabalhados) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        try {
            // 1. Calcular 13º integral com a média de HE
            BigDecimal decimoTerceiroIntegral = salarioBase.add(mediaMensalHE);

            // 2. Calcular meses trabalhados
            //int mesesTrabalhados = calcularMesesTrabalhados13o(folhaMensalService.findByMatriculaColaborador(matricula).getDataAdmissao(),LocalDate.now());

            // 3. Calcular 13º proporcional
            BigDecimal decimoTerceiroProporcional = decimoTerceiroIntegral.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(mesesTrabalhados));

            // 4. Subtrair 1ª parcela (já paga)
            BigDecimal primeiraParcela = decimoTerceiroProporcional.multiply(new BigDecimal("0.5"));
            BigDecimal segundaParcelaBruta = decimoTerceiroProporcional.subtract(primeiraParcela);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", segundaParcelaBruta);
            resultado.put("descontos", null);
            resultado.put("bruto", segundaParcelaBruta);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular 2ª Parcela do 13º com HE: " + e.getMessage());
        }
        return resultado;
    }


}
