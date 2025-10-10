package br.com.codex.v1.utilitario;

import br.com.codex.v1.domain.dto.ParametrosCalculoDto;
import br.com.codex.v1.domain.dto.ResultadoCalculoDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

public class CalculoDaFolhaProventos {

    private SimpleDateFormat pegaAno = new SimpleDateFormat("yyyy");
    private SimpleDateFormat pegaMes = new SimpleDateFormat("MM");
    private Calendario calendario = new Calendario();
    private Set<LocalDate> feriados = new HashSet<>();

    // Variáveis para armazenar valores intermediários
    private BigDecimal valorReferenciaHoraDiurna;
    private BigDecimal valorReferenciaHoraNoturna;
    private BigDecimal valorReferenciaDsrHoraNoturna;
    private BigDecimal valorHoraExtra50;
    private BigDecimal valorHoraExtra70;
    private BigDecimal valorHoraExtra100;

    // Variáveis para médias
    private BigDecimal media50;
    private BigDecimal media70;
    private BigDecimal media100;
    private BigDecimal mediaValorDsr;
    private BigDecimal mediaAdicionalNoturnoSobreDecimoTerceiro;
    private BigDecimal mediaHorasExtras50SobreDecimoTerceiro;
    private BigDecimal mediaHorasExtras70SobreDecimoTerceiro;
    private BigDecimal mediaHorasExtras100SobreDecimoTerceiro;
    private BigDecimal somaComplemento50;
    private BigDecimal somaComplemento70;
    private BigDecimal somaComplemento100;
    private BigDecimal somaComplementoDsrDiurno;
    private BigDecimal somaComplementoDsrNoturno;
    private BigDecimal calculoMediaDsrSobreDecimoTerceiro;
    private BigDecimal calculoMediaDsrNoturnoSobreDecimoTerceiro;
    private BigDecimal mediaHoraExtraSalarioMaternidade50Porcento;
    private BigDecimal mediaHoraExtraSalarioMaternidade70Porcento;
    private BigDecimal mediaHoraExtraSalarioMaternidade100Porcento;
    private BigDecimal mediaValorDsrDiurnoSalarioMaternidade;
    private BigDecimal mediaValorDsrNoturnoSalarioMaternidade;
    private BigDecimal mediaAdicionalNoturnoSalarioMaternidade;
    private BigDecimal mediaComissoesAno;
    private BigDecimal calculo50MedSob13;
    private BigDecimal calculo70MedSob13;
    private BigDecimal calculo100MedSob13;
    private BigDecimal calculoMedDsrSob13;
    private BigDecimal calculoMedDsrNotSob13;
    private BigDecimal mediaAdicNoturn13;
    private BigDecimal medHextSalMater50;
    private BigDecimal medHextSalMater70;
    private BigDecimal medHextSalMater100;
    private BigDecimal mediaValorDsrDiuSalMatern;
    private BigDecimal mediaValorDsrNotSalmatern;
    private BigDecimal mediaAdicNoturnSalMatern;

    public List<ResultadoCalculoDto> processarEventos(List<ParametrosCalculoDto> parametrosList) {
        List<ResultadoCalculoDto> resultados = new ArrayList<>();

        for (ParametrosCalculoDto parametros : parametrosList) {
            ResultadoCalculoDto resultado = calcularEvento(parametros);
            if (resultado != null) {
                resultados.add(resultado);
            }
        }

        return resultados;
    }

    public ResultadoCalculoDto calcularEvento(ParametrosCalculoDto parametros) {
        return switch (parametros.getCodigoEvento()) {
            case 1 -> calcularHorasNormaisDiurnas(parametros);
            case 2 -> calcularAdiantamentoSalario(parametros);
            case 5 -> calcularHorasRepousoRemuneradoDiurno(parametros);
            case 8 -> calcularHorasAtestadoMedico(parametros);
            case 9 -> calcularDiasAtestadoMedico(parametros);
            case 12 -> calcularHorasNormaisNoturnas(parametros);
            case 14 -> calcularAdicionalNoturno(parametros);
            case 17 -> calcularProLabore(parametros);
            case 19 -> calcularBolsaAuxilio(parametros);
            case 25 -> calcularHorasRepousoRemuneradoNoturno(parametros);
            case 26 -> calcularDsrHoraExtraDiurna50(parametros);
            case 27 -> calcularDsrHoraExtraDiurna70(parametros);
            case 28 -> calcularDsrHoraExtraDiurna100(parametros);
            case 46 -> calcularInsalubridade(parametros);
            case 47 -> calcularPericulosidade(parametros);
            case 51 -> calcularComissao(parametros);
            case 53 -> calcularGratificacao(parametros);
            case 54 -> calcularQuebraCaixa(parametros);
            case 98 -> calcularHorasExtras50(parametros);
            case 99 -> calcularHorasExtras70(parametros);
            case 100 -> calcularHorasExtras100(parametros);
            case 102 -> calcularMediaHorasExtras50SalarioMaternidade(parametros);
            case 103 -> calcularMediaHorasExtras70SalarioMaternidade(parametros);
            case 104 -> calcularMediaHorasExtras100SalarioMaternidade(parametros);
            case 105 -> calcularMediaDsrDiurnoSalarioMaternidade(parametros);
            case 106 -> calcularMediaDsrNoturnoSalarioMaternidade(parametros);
            case 107 -> calcularMediaAdicionalNoturnoSalarioMaternidade(parametros);
            case 130 -> calcularAjudaCusto(parametros);
            case 133 -> calcularSalarioFamilia(parametros);
            case 167 -> calcularPrimeiraParcela13(parametros);
            case 168 -> calcularMediaHorasExtras50PrimeiraParcela13(parametros);
            case 169 -> calcularMediaHorasExtras70PrimeiraParcela13(parametros);
            case 170 -> calcularMediaHorasExtras100PrimeiraParcela13(parametros);
            case 171 -> calcularDecimoTerceiroAdiantado(parametros);
            case 176 -> calcularMediaDsrDiurnoPrimeiraParcela13(parametros);
            case 177 -> calcularMediaDsrNoturnoPrimeiraParcela13(parametros);
            case 178 -> calcularInsalubridadePrimeiraParcela13(parametros);
            case 179 -> calcularPericulosidadePrimeiraParcela13(parametros);
            case 182 -> calcularMediaHorasExtras50SegundaParcela13(parametros);
            case 183 -> calcularMediaHorasExtras70SegundaParcela13(parametros);
            case 184 -> calcularMediaHorasExtras100SegundaParcela13(parametros);
            case 185 -> calcularMediaDsrDiurnoSegundaParcela13(parametros);
            case 186 -> calcularMediaDsrNoturnoSegundaParcela13(parametros);
            case 187 -> calcularInsalubridadeSegundaParcela13(parametros);
            case 188 -> calcularPericulosidadeSegundaParcela13(parametros);
            case 189 -> calcularSegundaParcelaDecimoTerceiro(parametros);
            case 195 -> calcularMediaComissoesDecimoTerceiro(parametros);
            case 201 -> calcularComplementoMediaHoraExtra50DecimoTerceiro(parametros);
            case 202 -> calcularComplementoMediaHoraExtra70DecimoTerceiro(parametros);
            case 203 -> calcularComplementoMediaHoraExtra100DecimoTerceiro(parametros);
            case 204 -> calcularComplementoDsrDiurnoDecimoTerceiro(parametros);
            case 205 -> calcularComplementoDsrNoturnoDecimoTerceiro(parametros);
            case 206 -> calcularMediaAdicionalNoturnoDecimoTerceiro(parametros);
            case 239 -> calcularValeTransporte(parametros);
            case 259 -> calcularValeCreche(parametros);
            case 402 -> calcularFgtsSalario(parametros);
            case 403 -> calcularFgtsDecimoTerceiro(parametros);
            case 409 -> calcularParticipacaoLucros(parametros);
            case 410 -> calcularAbonoSalarial(parametros);
            case 412 -> calcularReembolsoCreche(parametros);
            case 414 -> calcularGratificacaoSemestral(parametros);
            case 416 -> calcularReembolsoViagem(parametros);
            case 417 -> calcularPrimeiraParcelaParticipacaoLucros(parametros);
            case 418 -> calcularSegundaParcelaParticipacaoLucros(parametros);
            case 420 -> calcularPrimeiraParcelaAbonoSalarial(parametros);
            case 421 -> calcularSegundaParcelaAbonoSalarial(parametros);
            default -> criarResultadoNaoImplementado(parametros.getCodigoEvento());
        };
    }

    // Métodos principais de cálculo
    private ResultadoCalculoDto calcularHorasNormaisDiurnas(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(1);

        try {
            LocalTime horaIni = parametros.getHoraEntrada();
            LocalTime horaFim = parametros.getHoraSaida();
            LocalTime hora22 = LocalTime.parse("22:00");
            LocalTime hora13 = LocalTime.parse("13:00");

            BigDecimal horasTrabalhadas = BigDecimal.ZERO;

            if (horaFim.isBefore(hora13)) {
                horasTrabalhadas = calcularHorasDiurnasAntesDas13(horaIni, hora22);
            } else if (horaFim.isBefore(hora22)) {
                horasTrabalhadas = calcularHorasDiurnasCompletas(horaIni, horaFim);
            } else {
                horasTrabalhadas = calcularHorasDiurnasAte22(horaIni, hora22);
            }

            int diasUteis = calcularDiasUteisNoMes();
            BigDecimal quantidade = horasTrabalhadas.multiply(new BigDecimal(diasUteis))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal valorTotal = quantidade.multiply(parametros.getSalarioPorHora())
                    .setScale(2, RoundingMode.HALF_UP);

            // Salva para uso em outros cálculos
            this.valorReferenciaHoraDiurna = quantidade;

            resultado.setQuantidade(quantidade);
            resultado.setValor(valorTotal);
            resultado.setDescricao("Horas Normais Diurnas");

        } catch (Exception e) {
            resultado = criarResultadoErro(1, "Erro no cálculo de Horas Diurnas");
        }

        return resultado;
    }

    private BigDecimal calcularHorasDiurnasAntesDas13(LocalTime horaIni, LocalTime hora22) {
        Duration horaNormal = Duration.between(horaIni, hora22);
        long minutosTotais = horaNormal.toMinutes();
        BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
        return horas.subtract(new BigDecimal("1.0")); // Subtrai 1 hora para almoço
    }

    private BigDecimal calcularHorasDiurnasCompletas(LocalTime horaIni, LocalTime horaFim) {
        Duration horaNormal = Duration.between(horaIni, horaFim);
        long minutosTotais = horaNormal.toMinutes();
        BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
        return horas.subtract(new BigDecimal("1.0")); // Subtrai 1 hora para almoço
    }

    private BigDecimal calcularHorasDiurnasAte22(LocalTime horaIni, LocalTime hora22) {
        Duration horaNormal = Duration.between(horaIni, hora22);
        long minutosTotais = horaNormal.toMinutes();
        BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
        return horas.subtract(new BigDecimal("1.0")); // Subtrai 1 hora para almoço
    }

    private ResultadoCalculoDto calcularAdiantamentoSalario(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(2);

        BigDecimal adiantamento = parametros.getSalarioBase().multiply(new BigDecimal("0.4"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(adiantamento);
        resultado.setDescricao("Adiantamento de Salário");

        return resultado;
    }

    private ResultadoCalculoDto calcularHorasRepousoRemuneradoDiurno(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(5);

        try {
            LocalTime horaIni = parametros.getHoraEntrada();
            LocalTime horaFim = parametros.getHoraSaida();
            LocalTime hora22 = LocalTime.parse("22:00");
            LocalTime hora13 = LocalTime.parse("13:00");

            BigDecimal horasPorDia = BigDecimal.ZERO;

            if (horaFim.isBefore(hora13)) {
                horasPorDia = calcularHorasDiurnasAntesDas13(horaIni, hora22);
            } else if (horaFim.isBefore(hora22)) {
                horasPorDia = calcularHorasDiurnasCompletas(horaIni, horaFim);
            } else {
                horasPorDia = calcularHorasDiurnasAte22(horaIni, hora22);
            }

            int diasNaoUteis = calcularDiasNaoUteisNoMes();
            BigDecimal quantidade = horasPorDia.multiply(new BigDecimal(diasNaoUteis))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal valorTotal = quantidade.multiply(parametros.getSalarioPorHora())
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.setQuantidade(quantidade);
            resultado.setValor(valorTotal);
            resultado.setDescricao("Horas Repouso Remunerado Diurno");

        } catch (Exception e) {
            resultado = criarResultadoErro(5, "Erro no cálculo de Horas Repouso Remunerado Diurno");
        }

        return resultado;
    }

    private ResultadoCalculoDto calcularHorasAtestadoMedico(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(8);

        // Implementação simplificada - assumindo que as horas de atestado são fornecidas
        BigDecimal horasAtestado = parametros.getHorasPorMes() != null ?
                parametros.getHorasPorMes() : BigDecimal.ZERO;

        resultado.setQuantidade(horasAtestado);
        resultado.setValor(horasAtestado.multiply(parametros.getSalarioPorHora()));
        resultado.setDescricao("Horas de Atestado Médico");

        return resultado;
    }

    private ResultadoCalculoDto calcularDiasAtestadoMedico(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(9);

        // Implementação simplificada
        BigDecimal diasAtestado = BigDecimal.ONE; // Valor padrão
        BigDecimal valorDia = parametros.getSalarioBase().divide(new BigDecimal("30"), 2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = diasAtestado.multiply(valorDia);

        resultado.setQuantidade(diasAtestado);
        resultado.setValor(valorTotal);
        resultado.setDescricao("Dias de Atestado Médico");

        return resultado;
    }

    private ResultadoCalculoDto calcularHorasNormaisNoturnas(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(12);

        try {
            LocalTime horaIni = LocalTime.parse("22:00");
            LocalTime horaFim = parametros.getHoraSaida();
            LocalTime hora05 = LocalTime.parse("05:00");

            BigDecimal horasNoturnas = BigDecimal.ZERO;

            if ("12 x 36".equals(parametros.getTipoJornada())) {
                if (horaFim.isBefore(hora05)) {
                    // Cálculo para jornada 12x36 com saída antes das 5h
                    Duration periodoNoturno = Duration.between(horaIni, hora05);
                    long minutosTotais = periodoNoturno.toMinutes();
                    BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
                    horasNoturnas = horas.multiply(new BigDecimal("1.142857")); // Fator de redução
                } else {
                    Duration periodoNoturno = Duration.between(horaIni, horaFim);
                    long minutosTotais = periodoNoturno.toMinutes();
                    BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
                    horasNoturnas = horas.multiply(new BigDecimal("1.142857"));
                }
            } else {
                // Cálculo para outras jornadas
                if (horaFim.isBefore(hora05)) {
                    Duration periodoNoturno = Duration.between(horaIni, hora05);
                    long minutosTotais = periodoNoturno.toMinutes();
                    BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
                    horasNoturnas = horas.multiply(new BigDecimal("1.142857"));
                } else {
                    Duration periodoNoturno = Duration.between(horaIni, horaFim);
                    long minutosTotais = periodoNoturno.toMinutes();
                    BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
                    horasNoturnas = horas.multiply(new BigDecimal("1.142857"));
                }
            }

            int diasUteis = calcularDiasUteisNoMes();
            BigDecimal quantidade = horasNoturnas.multiply(new BigDecimal(diasUteis))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal valorTotal = quantidade.multiply(parametros.getSalarioPorHora())
                    .setScale(2, RoundingMode.HALF_UP);

            this.valorReferenciaHoraNoturna = quantidade;

            resultado.setQuantidade(quantidade);
            resultado.setValor(valorTotal);
            resultado.setDescricao("Horas Normais Noturnas");

        } catch (Exception e) {
            resultado = criarResultadoErro(12, "Erro no cálculo de Horas Noturnas");
        }

        return resultado;
    }

    private ResultadoCalculoDto calcularAdicionalNoturno(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(14);

        BigDecimal referenciaAdicionalNoturno = valorReferenciaHoraNoturna
                .add(valorReferenciaDsrHoraNoturna != null ? valorReferenciaDsrHoraNoturna : BigDecimal.ZERO);

        BigDecimal adicionalNoturno = referenciaAdicionalNoturno.multiply(parametros.getSalarioPorHora())
                .multiply(new BigDecimal("0.2"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(referenciaAdicionalNoturno);
        resultado.setValor(adicionalNoturno);
        resultado.setDescricao("Adicional Noturno");

        return resultado;
    }

    private ResultadoCalculoDto calcularProLabore(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(17);

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(parametros.getSalarioBase());
        resultado.setDescricao("Pró-Labore");

        return resultado;
    }

    private ResultadoCalculoDto calcularBolsaAuxilio(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(19);

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(parametros.getSalarioBase());
        resultado.setDescricao("Bolsa Auxílio");

        return resultado;
    }

    private ResultadoCalculoDto calcularHorasRepousoRemuneradoNoturno(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(25);

        try {
            LocalTime horaIni = LocalTime.parse("22:00");
            LocalTime horaFim = parametros.getHoraSaida();
            LocalTime hora05 = LocalTime.parse("05:00");

            BigDecimal horasNoturnasPorDia = BigDecimal.ZERO;

            if ("12 x 36".equals(parametros.getTipoJornada())) {
                if (horaFim.isBefore(hora05)) {
                    Duration periodoNoturno = Duration.between(horaIni, hora05);
                    long minutosTotais = periodoNoturno.toMinutes();
                    BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
                    horasNoturnasPorDia = horas.multiply(new BigDecimal("1.142857"));
                } else {
                    Duration periodoNoturno = Duration.between(horaIni, horaFim);
                    long minutosTotais = periodoNoturno.toMinutes();
                    BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
                    horasNoturnasPorDia = horas.multiply(new BigDecimal("1.142857"));
                }
            } else {
                if (horaFim.isBefore(hora05)) {
                    Duration periodoNoturno = Duration.between(horaIni, hora05);
                    long minutosTotais = periodoNoturno.toMinutes();
                    BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
                    horasNoturnasPorDia = horas.multiply(new BigDecimal("1.142857"));
                } else {
                    Duration periodoNoturno = Duration.between(horaIni, horaFim);
                    long minutosTotais = periodoNoturno.toMinutes();
                    BigDecimal horas = new BigDecimal(minutosTotais).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
                    horasNoturnasPorDia = horas.multiply(new BigDecimal("1.142857"));
                }
            }

            int diasNaoUteis = calcularDiasNaoUteisNoMes();
            BigDecimal quantidade = horasNoturnasPorDia.multiply(new BigDecimal(diasNaoUteis))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal valorTotal = quantidade.multiply(parametros.getSalarioPorHora())
                    .setScale(2, RoundingMode.HALF_UP);

            this.valorReferenciaDsrHoraNoturna = quantidade;

            resultado.setQuantidade(quantidade);
            resultado.setValor(valorTotal);
            resultado.setDescricao("Horas Repouso Remunerado Noturno");

        } catch (Exception e) {
            resultado = criarResultadoErro(25, "Erro no cálculo de Horas Repouso Remunerado Noturno");
        }

        return resultado;
    }

    private ResultadoCalculoDto calcularDsrHoraExtraDiurna50(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(26);

        try {
            int diasUteis = calcularDiasUteisNoMes();
            int diasNaoUteis = calcularDiasNaoUteisNoMes();

            BigDecimal mediaDiaria = this.valorHoraExtra50 != null ?
                    this.valorHoraExtra50.divide(new BigDecimal(diasUteis), 2, RoundingMode.HALF_UP) :
                    BigDecimal.ZERO;

            BigDecimal dsrSobreHoraExtra = mediaDiaria.multiply(new BigDecimal(diasNaoUteis))
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.setQuantidade(mediaDiaria);
            resultado.setValor(dsrSobreHoraExtra);
            resultado.setDescricao("DSR Sobre Hora Extra Diurna 50%");

        } catch (Exception e) {
            resultado = criarResultadoErro(26, "Erro no cálculo de DSR Hora Extra 50%");
        }

        return resultado;
    }

    private ResultadoCalculoDto calcularDsrHoraExtraDiurna70(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(27);

        try {
            int diasUteis = calcularDiasUteisNoMes();
            int diasNaoUteis = calcularDiasNaoUteisNoMes();

            BigDecimal mediaDiaria = this.valorHoraExtra70 != null ?
                    this.valorHoraExtra70.divide(new BigDecimal(diasUteis), 2, RoundingMode.HALF_UP) :
                    BigDecimal.ZERO;

            BigDecimal dsrSobreHoraExtra = mediaDiaria.multiply(new BigDecimal(diasNaoUteis))
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.setQuantidade(mediaDiaria);
            resultado.setValor(dsrSobreHoraExtra);
            resultado.setDescricao("DSR Sobre Hora Extra Diurna 70%");

        } catch (Exception e) {
            resultado = criarResultadoErro(27, "Erro no cálculo de DSR Hora Extra 70%");
        }

        return resultado;
    }

    private ResultadoCalculoDto calcularDsrHoraExtraDiurna100(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(28);

        try {
            int diasUteis = calcularDiasUteisNoMes();
            int diasNaoUteis = calcularDiasNaoUteisNoMes();

            BigDecimal mediaDiaria = this.valorHoraExtra100 != null ?
                    this.valorHoraExtra100.divide(new BigDecimal(diasUteis), 2, RoundingMode.HALF_UP) :
                    BigDecimal.ZERO;

            BigDecimal dsrSobreHoraExtra = mediaDiaria.multiply(new BigDecimal(diasNaoUteis))
                    .setScale(2, RoundingMode.HALF_UP);

            resultado.setQuantidade(mediaDiaria);
            resultado.setValor(dsrSobreHoraExtra);
            resultado.setDescricao("DSR Sobre Hora Extra Diurna 100%");

        } catch (Exception e) {
            resultado = criarResultadoErro(28, "Erro no cálculo de DSR Hora Extra 100%");
        }

        return resultado;
    }

    private ResultadoCalculoDto calcularInsalubridade(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(46);

        BigDecimal percentual = parametros.getPercentualInsalubridade() != null ?
                parametros.getPercentualInsalubridade() : BigDecimal.ZERO;

        BigDecimal valorInsalubridade = parametros.getSalarioMinimo().multiply(percentual)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        resultado.setQuantidade(percentual);
        resultado.setValor(valorInsalubridade);
        resultado.setDescricao("Insalubridade");

        return resultado;
    }

    private ResultadoCalculoDto calcularPericulosidade(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(47);

        BigDecimal percentual = parametros.getPercentualPericulosidade() != null ?
                parametros.getPercentualPericulosidade() : BigDecimal.ZERO;

        BigDecimal valorPericulosidade = parametros.getSalarioBase().multiply(percentual)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        resultado.setQuantidade(percentual);
        resultado.setValor(valorPericulosidade);
        resultado.setDescricao("Periculosidade");

        return resultado;
    }

    private ResultadoCalculoDto calcularComissao(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(51);

        BigDecimal percentualComissao = parametros.getValorComissao() != null ?
                parametros.getValorComissao() : BigDecimal.ZERO;

        BigDecimal valorVendas = parametros.getValorVendasMes() != null ?
                parametros.getValorVendasMes() : BigDecimal.ZERO;

        BigDecimal valorComissao = valorVendas.multiply(percentualComissao)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorVendas);
        resultado.setValor(valorComissao);
        resultado.setDescricao("Comissão");

        return resultado;
    }

    private ResultadoCalculoDto calcularGratificacao(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(53);

        BigDecimal valorGratificacao = parametros.getValorGratificacao() != null ?
                parametros.getValorGratificacao() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(valorGratificacao);
        resultado.setDescricao("Gratificação");

        return resultado;
    }

    private ResultadoCalculoDto calcularQuebraCaixa(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(54);

        BigDecimal valorQuebraCaixa = parametros.getValorQuebraCaixa() != null ?
                parametros.getValorQuebraCaixa() : BigDecimal.ZERO;

        BigDecimal horasPorMes = parametros.getHorasPorMes() != null ?
                parametros.getHorasPorMes() : new BigDecimal("220");

        BigDecimal valorQuebra = valorQuebraCaixa.multiply(this.valorReferenciaHoraDiurna)
                .divide(horasPorMes, 2, RoundingMode.HALF_UP);

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(valorQuebra);
        resultado.setDescricao("Quebra Caixa");

        return resultado;
    }

    private ResultadoCalculoDto calcularHorasExtras50(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(98);

        BigDecimal totalHoraExtra50 = parametros.getValorHoraExtra50() != null ?
                parametros.getValorHoraExtra50() : BigDecimal.ZERO;

        BigDecimal valorHoraExtra;
        if (parametros.getPercentualInsalubridade() != null &&
                parametros.getPercentualInsalubridade().compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal horasTrabNoMes = parametros.getHorasPorMes() != null ?
                    parametros.getHorasPorMes() : new BigDecimal("220");

            BigDecimal valorInsalubre = parametros.getSalarioMinimo()
                    .multiply(parametros.getPercentualInsalubridade())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                    .divide(horasTrabNoMes, 2, RoundingMode.HALF_UP);

            valorHoraExtra = parametros.getSalarioPorHora().add(valorInsalubre)
                    .multiply(new BigDecimal("1.5"))
                    .multiply(totalHoraExtra50)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            valorHoraExtra = parametros.getSalarioPorHora()
                    .multiply(new BigDecimal("1.5"))
                    .multiply(totalHoraExtra50)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        this.valorHoraExtra50 = valorHoraExtra;

        resultado.setQuantidade(totalHoraExtra50);
        resultado.setValor(valorHoraExtra);
        resultado.setDescricao("Horas Extras 50%");

        return resultado;
    }

    private ResultadoCalculoDto calcularHorasExtras70(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(99);

        BigDecimal totalHoraExtra70 = parametros.getValorHoraExtra70() != null ?
                parametros.getValorHoraExtra70() : BigDecimal.ZERO;

        BigDecimal valorHoraExtra;
        if (parametros.getPercentualInsalubridade() != null &&
                parametros.getPercentualInsalubridade().compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal horasTrabNoMes = parametros.getHorasPorMes() != null ?
                    parametros.getHorasPorMes() : new BigDecimal("220");

            BigDecimal valorInsalubre = parametros.getSalarioMinimo()
                    .multiply(parametros.getPercentualInsalubridade())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                    .divide(horasTrabNoMes, 2, RoundingMode.HALF_UP);

            valorHoraExtra = parametros.getSalarioPorHora().add(valorInsalubre)
                    .multiply(new BigDecimal("1.7"))
                    .multiply(totalHoraExtra70)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            valorHoraExtra = parametros.getSalarioPorHora()
                    .multiply(new BigDecimal("1.7"))
                    .multiply(totalHoraExtra70)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        this.valorHoraExtra70 = valorHoraExtra;

        resultado.setQuantidade(totalHoraExtra70);
        resultado.setValor(valorHoraExtra);
        resultado.setDescricao("Horas Extras 70%");

        return resultado;
    }

    private ResultadoCalculoDto calcularHorasExtras100(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(100);

        BigDecimal totalHoraExtra100 = parametros.getValorHoraExtra100() != null ?
                parametros.getValorHoraExtra100() : BigDecimal.ZERO;

        BigDecimal valorHoraExtra;
        if (parametros.getPercentualInsalubridade() != null &&
                parametros.getPercentualInsalubridade().compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal horasTrabNoMes = parametros.getHorasPorMes() != null ?
                    parametros.getHorasPorMes() : new BigDecimal("220");

            BigDecimal valorInsalubre = parametros.getSalarioMinimo()
                    .multiply(parametros.getPercentualInsalubridade())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                    .divide(horasTrabNoMes, 2, RoundingMode.HALF_UP);

            valorHoraExtra = parametros.getSalarioPorHora().add(valorInsalubre)
                    .multiply(new BigDecimal("2.0"))
                    .multiply(totalHoraExtra100)
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            valorHoraExtra = parametros.getSalarioPorHora()
                    .multiply(new BigDecimal("2.0"))
                    .multiply(totalHoraExtra100)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        this.valorHoraExtra100 = valorHoraExtra;

        resultado.setQuantidade(totalHoraExtra100);
        resultado.setValor(valorHoraExtra);
        resultado.setDescricao("Horas Extras 100%");

        return resultado;
    }

    // Métodos de Salário Maternidade
    private ResultadoCalculoDto calcularMediaHorasExtras50SalarioMaternidade(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(102);

        BigDecimal mediaHorasExtras = buscarMediaHorasExtrasBanco(parametros, 98);
        BigDecimal valorCalculado = parametros.getSalarioPorHora().multiply(mediaHorasExtras)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.5")))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(valorTotal);
        resultado.setDescricao("Média Horas Extras 50% Salário Maternidade");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaHorasExtras70SalarioMaternidade(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(103);

        BigDecimal mediaHorasExtras = buscarMediaHorasExtrasBanco(parametros, 99);
        BigDecimal valorCalculado = parametros.getSalarioPorHora().multiply(mediaHorasExtras)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.7")))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(valorTotal);
        resultado.setDescricao("Média Horas Extras 70% Salário Maternidade");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaHorasExtras100SalarioMaternidade(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(104);

        BigDecimal mediaHorasExtras = buscarMediaHorasExtrasBanco(parametros, 100);
        BigDecimal valorCalculado = parametros.getSalarioPorHora().multiply(mediaHorasExtras)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("1.0")))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(valorTotal);
        resultado.setDescricao("Média Horas Extras 100% Salário Maternidade");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaDsrDiurnoSalarioMaternidade(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(105);

        BigDecimal mediaDsrDiurno = buscarMediaEventoBanco(parametros, 5);

        resultado.setQuantidade(mediaDsrDiurno);
        resultado.setValor(mediaDsrDiurno);
        resultado.setDescricao("Média DSR Diurno Salário Maternidade");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaDsrNoturnoSalarioMaternidade(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(106);

        BigDecimal mediaDsrNoturno = buscarMediaEventoBanco(parametros, 25);

        resultado.setQuantidade(mediaDsrNoturno);
        resultado.setValor(mediaDsrNoturno);
        resultado.setDescricao("Média DSR Noturno Salário Maternidade");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaAdicionalNoturnoSalarioMaternidade(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(107);

        BigDecimal mediaAdicionalNoturno = buscarMediaEventoBanco(parametros, 14);
        BigDecimal valorCalculado = mediaAdicionalNoturno.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.2")))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(valorTotal);
        resultado.setDescricao("Média Adicional Noturno Salário Maternidade");

        return resultado;
    }

    private ResultadoCalculoDto calcularAjudaCusto(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(130);

        BigDecimal ajudaCusto = parametros.getAjudaCusto() != null ?
                parametros.getAjudaCusto() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(ajudaCusto);
        resultado.setDescricao("Ajuda de Custo");

        return resultado;
    }

    private ResultadoCalculoDto calcularSalarioFamilia(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(133);

        Integer numeroDependentes = parametros.getNumeroDependentes() != null ?
                parametros.getNumeroDependentes() : 0;

        // Valores fictícios - devem ser obtidos da tabela oficial
        BigDecimal valorPorDependente = new BigDecimal("50.00");
        BigDecimal valorTotal = valorPorDependente.multiply(new BigDecimal(numeroDependentes));

        resultado.setQuantidade(new BigDecimal(numeroDependentes));
        resultado.setValor(valorTotal);
        resultado.setDescricao("Salário Família");

        return resultado;
    }

    // Métodos do 13º Salário
    private ResultadoCalculoDto calcularPrimeiraParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(167);

        BigDecimal decimoTerceiro = parametros.getSalarioBase().multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(decimoTerceiro);
        resultado.setDescricao("Primeira Parcela 13º Salário");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaHorasExtras50PrimeiraParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(168);

        BigDecimal mediaAnual = buscarMediaAnualHorasExtras(parametros, 98);
        int mesesTrabalhados = 10; // Janeiro a Outubro
        BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal(mesesTrabalhados), 2, RoundingMode.HALF_UP);
        BigDecimal valorCalculado = mediaMensal.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorComAcrescimo = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.5")));
        BigDecimal valorFinal = valorComAcrescimo.multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Média Horas Extras 50% 1ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaHorasExtras70PrimeiraParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(169);

        BigDecimal mediaAnual = buscarMediaAnualHorasExtras(parametros, 99);
        int mesesTrabalhados = 10;
        BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal(mesesTrabalhados), 2, RoundingMode.HALF_UP);
        BigDecimal valorCalculado = mediaMensal.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorComAcrescimo = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.7")));
        BigDecimal valorFinal = valorComAcrescimo.multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Média Horas Extras 70% 1ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaHorasExtras100PrimeiraParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(170);

        BigDecimal mediaAnual = buscarMediaAnualHorasExtras(parametros, 100);
        int mesesTrabalhados = 10;
        BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal(mesesTrabalhados), 2, RoundingMode.HALF_UP);
        BigDecimal valorCalculado = mediaMensal.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorComAcrescimo = valorCalculado.add(valorCalculado.multiply(new BigDecimal("1.0")));
        BigDecimal valorFinal = valorComAcrescimo.multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Média Horas Extras 100% 1ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularDecimoTerceiroAdiantado(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(171);

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(parametros.getSalarioBase());
        resultado.setDescricao("Décimo Terceiro Adiantado");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaDsrDiurnoPrimeiraParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(176);

        BigDecimal mediaDsrDiurno = buscarMediaEventoBanco(parametros, 5);
        BigDecimal valorFinal = mediaDsrDiurno.multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(mediaDsrDiurno);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Média DSR Diurno 1ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaDsrNoturnoPrimeiraParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(177);

        BigDecimal mediaDsrNoturno = buscarMediaEventoBanco(parametros, 25);
        BigDecimal valorFinal = mediaDsrNoturno.multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(mediaDsrNoturno);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Média DSR Noturno 1ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularInsalubridadePrimeiraParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(178);

        BigDecimal valorInsalubridade = calcularInsalubridade(parametros).getValor();
        BigDecimal valorFinal = valorInsalubridade.multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Insalubridade 1ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularPericulosidadePrimeiraParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(179);

        BigDecimal valorPericulosidade = calcularPericulosidade(parametros).getValor();
        BigDecimal valorFinal = valorPericulosidade.multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Periculosidade 1ª Parcela 13º");

        return resultado;
    }

    // Métodos da Segunda Parcela do 13º
    private ResultadoCalculoDto calcularMediaHorasExtras50SegundaParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(182);

        BigDecimal mediaAnual = buscarMediaAnualHorasExtras(parametros, 98);
        int mesesTrabalhados = 11; // Janeiro a Novembro
        BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal(mesesTrabalhados), 2, RoundingMode.HALF_UP);
        BigDecimal valorCalculado = mediaMensal.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFinal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.5")))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(mediaMensal);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Média Horas Extras 50% 2ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaHorasExtras70SegundaParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(183);

        BigDecimal mediaAnual = buscarMediaAnualHorasExtras(parametros, 99);
        int mesesTrabalhados = 11;
        BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal(mesesTrabalhados), 2, RoundingMode.HALF_UP);
        BigDecimal valorCalculado = mediaMensal.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFinal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.7")))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(mediaMensal);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Média Horas Extras 70% 2ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaHorasExtras100SegundaParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(184);

        BigDecimal mediaAnual = buscarMediaAnualHorasExtras(parametros, 100);
        int mesesTrabalhados = 11;
        BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal(mesesTrabalhados), 2, RoundingMode.HALF_UP);
        BigDecimal valorCalculado = mediaMensal.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFinal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("1.0")))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(mediaMensal);
        resultado.setValor(valorFinal);
        resultado.setDescricao("Média Horas Extras 100% 2ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaDsrDiurnoSegundaParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(185);

        BigDecimal mediaDsrDiurno = buscarMediaEventoBanco(parametros, 5);

        resultado.setQuantidade(mediaDsrDiurno);
        resultado.setValor(mediaDsrDiurno);
        resultado.setDescricao("Média DSR Diurno 2ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaDsrNoturnoSegundaParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(186);

        BigDecimal mediaDsrNoturno = buscarMediaEventoBanco(parametros, 25);

        resultado.setQuantidade(mediaDsrNoturno);
        resultado.setValor(mediaDsrNoturno);
        resultado.setDescricao("Média DSR Noturno 2ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularInsalubridadeSegundaParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(187);

        BigDecimal valorInsalubridade = calcularInsalubridade(parametros).getValor();

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(valorInsalubridade);
        resultado.setDescricao("Insalubridade 2ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularPericulosidadeSegundaParcela13(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(188);

        BigDecimal valorPericulosidade = calcularPericulosidade(parametros).getValor();

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(valorPericulosidade);
        resultado.setDescricao("Periculosidade 2ª Parcela 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularSegundaParcelaDecimoTerceiro(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(189);

        BigDecimal decimoTerceiro = parametros.getSalarioBase().multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(decimoTerceiro);
        resultado.setDescricao("Segunda Parcela 13º Salário");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaComissoesDecimoTerceiro(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(195);

        BigDecimal mediaComissoes = buscarMediaComissoesAnual(parametros);

        resultado.setQuantidade(mediaComissoes);
        resultado.setValor(mediaComissoes);
        resultado.setDescricao("Média Comissões 13º Salário");

        return resultado;
    }

    // Métodos de Complemento do 13º
    private ResultadoCalculoDto calcularComplementoMediaHoraExtra50DecimoTerceiro(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(201);

        BigDecimal mediaAnual = buscarMediaAnualHorasExtras(parametros, 98);
        BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal valorCalculado = mediaMensal.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.5")))
                .setScale(2, RoundingMode.HALF_UP);

        // Cálculo simplificado do complemento
        BigDecimal complemento = valorTotal.multiply(new BigDecimal("0.1"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(complemento);
        resultado.setDescricao("Complemento Média Hora Extra 50% 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularComplementoMediaHoraExtra70DecimoTerceiro(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(202);

        BigDecimal mediaAnual = buscarMediaAnualHorasExtras(parametros, 99);
        BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal valorCalculado = mediaMensal.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.7")))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal complemento = valorTotal.multiply(new BigDecimal("0.1"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(complemento);
        resultado.setDescricao("Complemento Média Hora Extra 70% 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularComplementoMediaHoraExtra100DecimoTerceiro(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(203);

        BigDecimal mediaAnual = buscarMediaAnualHorasExtras(parametros, 100);
        BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal valorCalculado = mediaMensal.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("1.0")))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal complemento = valorTotal.multiply(new BigDecimal("0.1"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(complemento);
        resultado.setDescricao("Complemento Média Hora Extra 100% 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularComplementoDsrDiurnoDecimoTerceiro(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(204);

        BigDecimal mediaDsrDiurno = buscarMediaEventoBanco(parametros, 5);
        BigDecimal complemento = mediaDsrDiurno.multiply(new BigDecimal("0.1"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(mediaDsrDiurno);
        resultado.setValor(complemento);
        resultado.setDescricao("Complemento DSR Diurno 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularComplementoDsrNoturnoDecimoTerceiro(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(205);

        BigDecimal mediaDsrNoturno = buscarMediaEventoBanco(parametros, 25);
        BigDecimal complemento = mediaDsrNoturno.multiply(new BigDecimal("0.1"))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(mediaDsrNoturno);
        resultado.setValor(complemento);
        resultado.setDescricao("Complemento DSR Noturno 13º");

        return resultado;
    }

    private ResultadoCalculoDto calcularMediaAdicionalNoturnoDecimoTerceiro(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(206);

        BigDecimal mediaAdicionalNoturno = buscarMediaEventoBanco(parametros, 14);
        BigDecimal valorCalculado = mediaAdicionalNoturno.multiply(parametros.getSalarioPorHora())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorCalculado.add(valorCalculado.multiply(new BigDecimal("0.2")))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(valorCalculado);
        resultado.setValor(valorTotal);
        resultado.setDescricao("Média Adicional Noturno 13º");

        return resultado;
    }

    // Métodos de Benefícios
    private ResultadoCalculoDto calcularValeTransporte(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(239);

        BigDecimal valorValeTransporte = parametros.getValorValeTransporte() != null ?
                parametros.getValorValeTransporte() : BigDecimal.ZERO;

        int diasUteis = calcularDiasUteisNoMes();
        BigDecimal valorTotal = valorValeTransporte.multiply(new BigDecimal(diasUteis))
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(new BigDecimal(diasUteis));
        resultado.setValor(valorTotal);
        resultado.setDescricao("Vale Transporte");

        return resultado;
    }

    private ResultadoCalculoDto calcularValeCreche(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(259);

        BigDecimal valeCreche = parametros.getValeCreche() != null ?
                parametros.getValeCreche() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(valeCreche);
        resultado.setDescricao("Vale Creche");

        return resultado;
    }

    // Métodos do FGTS
    private ResultadoCalculoDto calcularFgtsSalario(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(402);

        BigDecimal percentualFgts = parametros.getDescricaoCargo() != null &&
                parametros.getDescricaoCargo().contains("Aprendiz") ?
                new BigDecimal("0.02") : new BigDecimal("0.08");

        BigDecimal valorFgts = parametros.getSalarioBase().multiply(percentualFgts)
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(percentualFgts);
        resultado.setValor(valorFgts);
        resultado.setDescricao("FGTS Sobre Salário");

        return resultado;
    }

    private ResultadoCalculoDto calcularFgtsDecimoTerceiro(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(403);

        BigDecimal percentualFgts = parametros.getDescricaoCargo() != null &&
                parametros.getDescricaoCargo().contains("Aprendiz") ?
                new BigDecimal("0.02") : new BigDecimal("0.08");

        BigDecimal baseCalculo = parametros.getSalarioBase().divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
        BigDecimal valorFgts = baseCalculo.multiply(percentualFgts)
                .setScale(2, RoundingMode.HALF_UP);

        resultado.setQuantidade(percentualFgts);
        resultado.setValor(valorFgts);
        resultado.setDescricao("FGTS Sobre 13º Salário");

        return resultado;
    }

    // Métodos de Participação nos Lucros e Abono
    private ResultadoCalculoDto calcularParticipacaoLucros(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(409);

        BigDecimal plr = parametros.getParticipacaoLucros() != null ?
                parametros.getParticipacaoLucros() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(plr);
        resultado.setDescricao("Participação nos Lucros");

        return resultado;
    }

    private ResultadoCalculoDto calcularAbonoSalarial(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(410);

        BigDecimal abonoSalarial = parametros.getAbonoSalarial() != null ?
                parametros.getAbonoSalarial() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(abonoSalarial);
        resultado.setDescricao("Abono Salarial");

        return resultado;
    }

    private ResultadoCalculoDto calcularReembolsoCreche(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(412);

        BigDecimal reembolsoCreche = parametros.getValeCreche() != null ?
                parametros.getValeCreche() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(reembolsoCreche);
        resultado.setDescricao("Reembolso Creche");

        return resultado;
    }

    private ResultadoCalculoDto calcularGratificacaoSemestral(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(414);

        BigDecimal gratificacaoSemestral = parametros.getValorGratificacao() != null ?
                parametros.getValorGratificacao() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(gratificacaoSemestral);
        resultado.setDescricao("Gratificação Semestral");

        return resultado;
    }

    private ResultadoCalculoDto calcularReembolsoViagem(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(416);

        BigDecimal reembolsoViagem = parametros.getAjudaCusto() != null ?
                parametros.getAjudaCusto() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(reembolsoViagem);
        resultado.setDescricao("Reembolso Viagem");

        return resultado;
    }

    // Métodos de Parcelas
    private ResultadoCalculoDto calcularPrimeiraParcelaParticipacaoLucros(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(417);

        BigDecimal plr = parametros.getParticipacaoLucros() != null ?
                parametros.getParticipacaoLucros() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(plr);
        resultado.setDescricao("1ª Parcela Participação nos Lucros");

        return resultado;
    }

    private ResultadoCalculoDto calcularSegundaParcelaParticipacaoLucros(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(418);

        BigDecimal plr = parametros.getParticipacaoLucros() != null ?
                parametros.getParticipacaoLucros() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(plr);
        resultado.setDescricao("2ª Parcela Participação nos Lucros");

        return resultado;
    }

    private ResultadoCalculoDto calcularPrimeiraParcelaAbonoSalarial(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(420);

        BigDecimal abonoSalarial = parametros.getAbonoSalarial() != null ?
                parametros.getAbonoSalarial() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(abonoSalarial);
        resultado.setDescricao("1ª Parcela Abono Salarial");

        return resultado;
    }

    private ResultadoCalculoDto calcularSegundaParcelaAbonoSalarial(ParametrosCalculoDto parametros) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(421);

        BigDecimal abonoSalarial = parametros.getAbonoSalarial() != null ?
                parametros.getAbonoSalarial() : BigDecimal.ZERO;

        resultado.setQuantidade(BigDecimal.ONE);
        resultado.setValor(abonoSalarial);
        resultado.setDescricao("2ª Parcela Abono Salarial");

        return resultado;
    }

    // Métodos auxiliares
    private int calcularDiasUteisNoMes() {
        LocalDate hoje = LocalDate.now();
        YearMonth anoMes = YearMonth.of(hoje.getYear(), hoje.getMonth());
        int workingDays = 0;

        feriados.addAll(calendario.getFeriadosFixos(hoje.getYear()));
        feriados.addAll(calendario.getFeriadosMoveis(hoje.getYear()));

        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
            LocalDate data = anoMes.atDay(dia);
            if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                workingDays++;
            }
        }
        return workingDays;
    }

    private int calcularDiasNaoUteisNoMes() {
        LocalDate hoje = LocalDate.now();
        YearMonth anoMes = YearMonth.of(hoje.getYear(), hoje.getMonth());
        int nonWorkingDays = 0;

        feriados.addAll(calendario.getFeriadosFixos(hoje.getYear()));
        feriados.addAll(calendario.getFeriadosMoveis(hoje.getYear()));

        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
            LocalDate data = anoMes.atDay(dia);
            if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                nonWorkingDays++;
            }
        }
        return nonWorkingDays;
    }

    // Métodos auxiliares para banco de dados (implementar conforme necessidade)
    private BigDecimal buscarMediaHorasExtrasBanco(ParametrosCalculoDto parametros, int codigoEvento) {
        // Implementar conexão com banco para buscar média
        // Retornar valor padrão por enquanto
        return new BigDecimal("10.00");
    }

    private BigDecimal buscarMediaEventoBanco(ParametrosCalculoDto parametros, int codigoEvento) {
        // Implementar conexão com banco para buscar média
        // Retornar valor padrão por enquanto
        return new BigDecimal("5.00");
    }

    private BigDecimal buscarMediaAnualHorasExtras(ParametrosCalculoDto parametros, int codigoEvento) {
        // Implementar conexão com banco para buscar média anual
        // Retornar valor padrão por enquanto
        return new BigDecimal("100.00");
    }

    private BigDecimal buscarMediaComissoesAnual(ParametrosCalculoDto parametros) {
        // Implementar conexão com banco para buscar média anual de comissões
        // Retornar valor padrão por enquanto
        return new BigDecimal("500.00");
    }

    private ResultadoCalculoDto criarResultadoNaoImplementado(Integer codigoEvento) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(codigoEvento);
        resultado.setQuantidade(BigDecimal.ZERO);
        resultado.setValor(BigDecimal.ZERO);
        resultado.setDescricao("Evento não implementado: " + codigoEvento);
        return resultado;
    }

    private ResultadoCalculoDto criarResultadoErro(Integer codigoEvento, String mensagem) {
        ResultadoCalculoDto resultado = new ResultadoCalculoDto();
        resultado.setCodigoEvento(codigoEvento);
        resultado.setQuantidade(BigDecimal.ZERO);
        resultado.setValor(BigDecimal.ZERO);
        resultado.setDescricao("ERRO: " + mensagem);
        return resultado;
    }
}