package br.com.codex.v1.service;

import br.com.codex.v1.utilitario.Calendario;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CalculoDaFolhaProventosService {

    @Autowired
    private TabelaImpostoRendaService tabelaImpostoRendaService;

    @Autowired
    private CadastroColaboradoresService cadastroColaboradoresService;

    Calendario calendario = new Calendario();
    Set<LocalDate> feriados = new HashSet<>();

    Integer codigoEvento, numeroDependentes;
    LocalTime horaEntrada;
    LocalTime horaSaida;
    String tipoJornada;

    @Setter
    String numeroMatricula;

    String descricaoCargo;
    BigDecimal valorHoraExtra50, valorHoraExtra70, valorHoraExtra100, percentualInsalubridade, percentualPericulosidade,
    valorComissao, valeCreche, valorVendasMes, ajudaCusto, valorQuebraCaixa, valorGratificacao,horasPorMes,
    valorValeTransporte, valorValeCreche, valorReferenciaHoraNoturna, valorReferenciaHoraDiurna, valorReferenciaDsrHoraNoturna,
    media50, media70, media100, mediaValorDsr, mediaAdicionalNoturnoSobreDecimoTerceiro,
    mediaHorasExtras50SobreDecimoTerceiro, mediaHorasExtras70SobreDecimoTerceiro, mediaHorasExtras100SobreDecimoTerceiro,
    somaComplemento50,somaComplemento70,somaComplemento100, somaComplementoDsrDiurno,somaComplementoDsrNoturno,
    calculoMediaDsrSobreDecioTerceiro, calculoMediaDsrNoturnoSobreDecimoTerceiro, mediaHoraExtraSalarioMaternidade50Porcento,
    mediaHoraExtraSalarioMaternidade70Porcento, mediaHoraExtraSalarioMaternidade100Porcento, mediaValorDsrDiurnoSalarioMaternidade,
    mediaValorDsrNoturnoSalarioMaternidade, mediaAdicionalNoturnoSalarioMaternidade, mediaComissoesAno,
    participacaoLucros, abonoSalarial, salarioBase, salarioPorHora;

    private BigDecimal obtemSalarioMinimo(){
        return tabelaImpostoRendaService.getSalarioMinimo();
    }

    public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        horaEntrada = cadastroColaboradoresService.findByNumeroMatricula(numeroMatricula).getHorarioEntrada();
        horaSaida = cadastroColaboradoresService.findByNumeroMatricula(numeroMatricula).getHorarioSaida();
        salarioBase = cadastroColaboradoresService.findByNumeroMatricula(numeroMatricula).getSalarioColaborador();
        valorValeTransporte = cadastroColaboradoresService.findByNumeroMatricula(numeroMatricula).getValeTransporteCusto();

        switch (codigoEvento) {

            case 1 -> {

                LocalTime horaIni = horaEntrada;
                LocalTime horaFim = horaSaida;
                LocalTime hora22 = LocalTime.parse("22:00");
                LocalTime hora13 = LocalTime.parse("13:00");

                // Este bloco faz o cálculo se a pessoa trabalha em horário normal, por exemplo das 08 às 18, com horário antes das 22:00

                if (horaFim.isBefore(hora13)) {
                    Duration horaNormal = Duration.between(horaIni, hora22); // Aqui eu pego a hora que entrou por exemplo 18:00 e calculo a diferença até às 22:00
                    LocalTime diferencaHoraNormal = LocalTime.ofNanoOfDay(horaNormal.toNanos()); // Aqui tenho a Diferença de horas Diurnas

                    int horas = diferencaHoraNormal.getHour() - 1; // Aqui pego o valor das horas menos o horário de almoço
                    int minutos = diferencaHoraNormal.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                    String hourDiurna = String.valueOf(horas) + "." + String.valueOf(minutos);

                    //-------------------- Este trecho faz a contagem de dias úteis no mês

                    int year = LocalDate.now().getYear();
                    int month = LocalDate.now().getMonthValue();
                    int workingDaysDiurno = 0;

                    YearMonth anoMes = YearMonth.of(year, month);

                    feriados.addAll(calendario.getFeriadosFixos(year));
                    feriados.addAll(calendario.getFeriadosMoveis(year));

                    for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                        LocalDate data = anoMes.atDay(dia);

                        if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                            workingDaysDiurno++;
                        }
                    }
                    /*------------------------------------------------------------------------------------*/

                    // Calcula o valor das horas diurnas e arredonda para 2 casas decimais
                    valorReferenciaHoraDiurna = new BigDecimal(hourDiurna).multiply(new BigDecimal(workingDaysDiurno)).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais
                    BigDecimal valorTotalHoraDiurna = valorReferenciaHoraDiurna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais

                    resultado.put("referencia", valorReferenciaHoraDiurna);
                    resultado.put("vencimentos", valorTotalHoraDiurna);
                    resultado.put("descontos", BigDecimal.ZERO);

                } else {
                    if (horaFim.isBefore(hora22)) {
                        Duration horaNormal = Duration.between(horaIni, horaFim);
                        LocalTime diferencaHoraNormal = LocalTime.ofNanoOfDay(horaNormal.toNanos()); // Aqui tenho a Diferença de horas Diurnas

                        int horas = diferencaHoraNormal.getHour() - 1; // Aqui pego o valor das horas menos o horário de almoço
                        int minutos = diferencaHoraNormal.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourDiurna = String.valueOf(horas) + "." + String.valueOf(minutos);

                        //-------------------- Este trecho faz a contagem de dias úteis no mês
                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int workingDaysDiurno = 0;

                        YearMonth anoMes = YearMonth.of(year, month);

                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));

                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                workingDaysDiurno++;
                            }
                        }
                        /*------------------------------------------------------------------------------------*/

                        // Calcula o valor das horas diurnas e arredonda para 2 casas decimais
                        valorReferenciaHoraDiurna = new BigDecimal(hourDiurna).multiply(new BigDecimal(workingDaysDiurno)).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais
                        BigDecimal valorTotalHoraDiurna = valorReferenciaHoraDiurna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais

                        resultado.put("referencia", valorReferenciaHoraDiurna);
                        resultado.put("vencimentos", valorTotalHoraDiurna);
                        resultado.put("descontos", BigDecimal.ZERO);

                    } else {
                        /* Aqui eu pego 22 horas e subtraio pela hora de entrada para calcular o DSR Diurno até às 22:00 */
                        Duration horaNormal = Duration.between(horaIni, hora22);
                        LocalTime diferencaHoraNormal = LocalTime.ofNanoOfDay(horaNormal.toNanos()); // Aqui tenho a Diferença de horas Diurnas

                        int horas = diferencaHoraNormal.getHour() - 1; // Aqui pego o valor das horas menos o horário de almoço
                        int minutos = diferencaHoraNormal.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourDiurna = String.valueOf(horas) + "." + String.valueOf(minutos);

                        //-------------------- Este trecho faz a contagem de dias úteis no mês
                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int workingDaysDiurno = 0;

                        YearMonth anoMes = YearMonth.of(year, month);

                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));

                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                workingDaysDiurno++;
                            }
                        }

                        // Calcula o valor das horas diurnas e arredonda para 2 casas decimais
                        valorReferenciaHoraDiurna = new BigDecimal(hourDiurna).multiply(new BigDecimal(workingDaysDiurno)).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais
                        BigDecimal valorTotalHoraDiurna = valorReferenciaHoraDiurna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais

                        resultado.put("referencia", valorReferenciaHoraDiurna);
                        resultado.put("vencimentos", valorTotalHoraDiurna);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }
                }

            }
            case 130 -> {
                //Calculando Ajuda de Custo
                resultado.put("referencia", valorValeTransporte);
                resultado.put("vencimentos", valorValeTransporte);
                resultado.put("descontos", BigDecimal.ZERO);
            }
        }
        return resultado;

    }
}
