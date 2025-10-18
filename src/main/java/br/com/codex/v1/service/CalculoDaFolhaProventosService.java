package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
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
    private TabelaDeducaoInssService tabelaDeducaoInssService;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Autowired
    private FolhaMensalService folhaMensalService;

    Calendario calendario = new Calendario();
    Set<LocalDate> feriados = new HashSet<>();

    Integer numeroDependentes;
    LocalTime horaEntrada;
    LocalTime horaSaida;
    String tipoJornada;

    LocalDate dataAdmissao;

    @Setter
    String numeroMatricula;

    String descricaoCargo;
    BigDecimal quantidadeHoraExtra50, quantidadeHoraExtra70, quantidadeHoraExtra100, percentualInsalubridade, percentualPericulosidade,
    percentualAdicionalNoturno, valorComissao, valorVendasMes, valorQuebraCaixa, valorGratificacao,horasPorMes,
    valorValeTransporte, valorReferenciaHoraNoturna, valorReferenciaHoraDiurna, valorReferenciaDsrHoraNoturna,
    valorSalarioMinimo, valorCotaSalarioFamilia, salarioBase, salarioPorHora, faltasHorasMes, horasTrabalhadasPorMes;

    private BigDecimal obtemSalarioMinimo(){
        return tabelaImpostoRendaService.getSalarioMinimo();
    }

    public Map<String, BigDecimal> escolheEventos(Integer codigoEvento) {

        Map<String, BigDecimal> resultado = new HashMap<>();

        horaEntrada = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getHoraEntrada();
        horaSaida = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getHoraSaida();

        salarioPorHora = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getSalarioHora();
        valorValeTransporte = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getValorValeTransporte();
        faltasHorasMes = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getFaltasHorasMes();
        tipoJornada = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getJornada();
        percentualAdicionalNoturno = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getPercentualAdicionalNoturno();
        quantidadeHoraExtra50 = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getHorasExtras50();
        quantidadeHoraExtra70 = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getHorasExtras70();
        quantidadeHoraExtra100 = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getHorasExtras100();

        valorComissao = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getComissao();
        valorVendasMes = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getValorVendaMes();
        valorQuebraCaixa =  folhaMensalService.findByMatriculaColaborador(numeroMatricula).getQuebraCaixa();
        valorGratificacao = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getGratificacao();
        valorSalarioMinimo = tabelaImpostoRendaService.getSalarioMinimo();
        valorCotaSalarioFamilia = tabelaDeducaoInssService.getSalarioFamilia();

        switch (codigoEvento) {

            //Calculando Horas Normais Diurnas
            case 1 -> {

                LocalTime horaIni = horaEntrada;
                LocalTime horaFim = horaSaida;
                LocalTime hora22 = LocalTime.parse("22:00");
                LocalTime hora13 = LocalTime.parse("13:00");

                // Este bloco faz o cálculo se a pessoa trabalha em horário normal, por exemplo, das 08 às 18, com horário antes das 22:00

                if (horaFim.isBefore(hora13)) {
                    Duration horaNormal = Duration.between(horaIni, hora22); // Aqui eu pego a hora que entrou, por exemplo, 18:00 e calculo a diferença até às 22:00
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

            //Calculando Adiantamento de Salário (40%)
            case 2 -> {
                //Calculando Adiantamento de Salário
                BigDecimal adiantamentoSalarial = (salarioBase.multiply(new BigDecimal("40"))).divide(new BigDecimal("100"));
                resultado.put("referencia", new BigDecimal(40));
                resultado.put("vencimentos", adiantamentoSalarial);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando Horas Repouso Remunerado Diurno (DSR) no mês
            case 5 -> {

                LocalTime horaIniHRDiurno = horaEntrada;
                LocalTime horaFimHRDiurno = horaSaida;
                LocalTime hora22HRDiurno = LocalTime.parse("22:00");
                LocalTime hora13HRDiurno = LocalTime.parse("13:00");

                if (horaFimHRDiurno.isBefore(hora13HRDiurno)) {

                    Duration horaNormalHRDiurno = Duration.between(horaIniHRDiurno, hora22HRDiurno); //Aqui eu pego a hora que entrou por exemplo 18:00 e calculo a diferença até às 22:00
                    LocalTime diferencaHoraNormalHRDiurno = LocalTime.ofNanoOfDay(horaNormalHRDiurno.toNanos());// Aqui tenho a Diferenca de horas Diurnas

                    int horasDiurnas = diferencaHoraNormalHRDiurno.getHour() - 1; // Aqui pego o valor das horas menos o horário do almoço
                    int minutosDiurnos = diferencaHoraNormalHRDiurno.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                    String hourDiurnaDsr = String.valueOf(horasDiurnas) + "." + String.valueOf(minutosDiurnos);// Horas normais no dia

                    int year = LocalDate.now().getYear();
                    int month = LocalDate.now().getMonthValue();
                    int notWorkingDaysDir = 0;

                    YearMonth anoMes = YearMonth.of(year, month);

                    feriados.addAll(calendario.getFeriadosFixos(year));
                    feriados.addAll(calendario.getFeriadosMoveis(year));

                    //--------Aqui é Encontrado os domingos e Feriados no Mês
                    for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                        LocalDate data = anoMes.atDay(dia);

                        if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                            notWorkingDaysDir++;
                        }
                    }
                    BigDecimal valorReferenciaDsrDiurno = new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr));
                    BigDecimal dsrHoraDiurna = (new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr))).multiply(salarioPorHora);

                    resultado.put("referencia", valorReferenciaDsrDiurno);
                    resultado.put("vencimentos", dsrHoraDiurna);
                    resultado.put("descontos", BigDecimal.ZERO);

                } else {

                    if (horaFimHRDiurno.isBefore(hora22HRDiurno)) {

                        Duration horaNormalHRDiurno = Duration.between(horaIniHRDiurno, horaFimHRDiurno);// Faz a diferença da Hora que Entrou com a Hora que saiu e me dá o total de horas num dia.
                        LocalTime diferencaHoraNormalHRDiurno = LocalTime.ofNanoOfDay(horaNormalHRDiurno.toNanos());// Aqui tenho a Diferenca de horas Diurnas

                        int horasDiurnas = diferencaHoraNormalHRDiurno.getHour() - 1; // Aqui pego o valor das horas menos o horário do almoço
                        int minutosDiurnos = diferencaHoraNormalHRDiurno.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourDiurnaDsr = String.valueOf(horasDiurnas) + "." + String.valueOf(minutosDiurnos);// Horas normais no dia

                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int notWorkingDaysDir = 0;

                        YearMonth anoMes = YearMonth.of(year, month);

                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));

                        //----------Aqui é Encontrado os domingos e Feriados no Mês
                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                notWorkingDaysDir++;
                            }
                        }
                        BigDecimal valorReferenciaDsrDiurno = new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr));
                        BigDecimal dsrHoraDiurna = (new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr))).multiply(salarioPorHora);

                        resultado.put("referencia", valorReferenciaDsrDiurno);
                        resultado.put("vencimentos", dsrHoraDiurna);
                        resultado.put("descontos", BigDecimal.ZERO);
                    } else {
                        Duration horaNormal = Duration.between(horaIniHRDiurno, hora22HRDiurno);// Faz a diferença da Hora que Entrou com a Hora que saiu e me dá o total de horas num dia.
                        LocalTime diferencaHoraNormalHRDiurno = LocalTime.ofNanoOfDay(horaNormal.toNanos());// Aqui tenho a Diferenca de horas Diurnas

                        int horasDiurnas = diferencaHoraNormalHRDiurno.getHour() - 1; // Aqui pego o valor das horas menos o horário do almoço
                        int minutosDiurnos = diferencaHoraNormalHRDiurno.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourDiurnaDsr = String.valueOf(horasDiurnas) + "." + String.valueOf(minutosDiurnos);// Horas normais no dia

                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int notWorkingDaysDir = 0;

                        YearMonth anoMes = YearMonth.of(year, month);

                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));

                        //----------Aqui é Encontrado os domingos e Feriados no Mês
                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                notWorkingDaysDir++;
                            }
                        }
                        BigDecimal valorReferenciaDsrDiurno = new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr));
                        BigDecimal dsrHoraDiurna = (new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr))).multiply(salarioPorHora);

                        resultado.put("referencia", valorReferenciaDsrDiurno);
                        resultado.put("vencimentos", dsrHoraDiurna);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }
                }
            }

            //Horas de Atestado Médico
            case 8 -> {
             BigDecimal horasDeFaltasAtestadoMedico = faltasHorasMes;
                resultado.put("referencia", horasDeFaltasAtestadoMedico);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Dias de atestado médico
            case 9 -> {
              BigDecimal horasDeFaltasMedico = faltasHorasMes;
                resultado.put("referencia", horasDeFaltasMedico);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando horas normais noturnas
            case 12 -> {
                if (tipoJornada.equals("12 x 36")) {

                    LocalTime horaIniNot = LocalTime.parse("22:00");
                    LocalTime horaFimNot = horaSaida;
                    LocalTime hora13Not = LocalTime.parse("13:00");
                    LocalTime hora20Not = LocalTime.parse("20:00");

                    int valorHoraSaidaNot = horaFimNot.getHour();

                    if (horaFimNot.isBefore(hora13Not)) { // Aqui ele compara a hora de saída se for até meia noite

                        Duration das22AteOFim = Duration.between(hora20Not, horaIniNot); //Fórmula 22:00 - 20:00 + x, onde x será o valor horário de saída
                        LocalTime diferencadas22AteOFim = LocalTime.ofNanoOfDay(das22AteOFim.toNanos());// Aqui tenho a hora noturna de um dia
                        int horasN = diferencadas22AteOFim.getHour() + valorHoraSaidaNot; // Aqui pego o valor das horas menos o horário de almoço/janta
                        int minutosN = diferencadas22AteOFim.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal
                        String hourNot = String.valueOf(horasN) + "." + String.valueOf(minutosN);
                        BigDecimal valorHoraDepoisDas22 = (new BigDecimal(hourNot)).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                        valorReferenciaHoraNoturna = valorHoraDepoisDas22.multiply(new BigDecimal("15"));/*workingDaysNoturno;*/ // Aqui é calculado os dias úteis vezes o número de horas noturnas no mês.
                        BigDecimal valorTotalHoraNoturna = valorReferenciaHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                        resultado.put("referencia", valorReferenciaHoraNoturna);
                        resultado.put("vencimentos", valorTotalHoraNoturna);
                        resultado.put("descontos", BigDecimal.ZERO);

                    } else {
                        /*Aqui é feito o cálculo quando a hora de saída é antes da meia noite*/
                        Duration das22AteOFim = Duration.between(horaIniNot, horaFimNot);
                        LocalTime diferencadas22AteOFim = LocalTime.ofNanoOfDay(das22AteOFim.toNanos());// Aqui tenho a hora noturna de um dia
                        int horasN = diferencadas22AteOFim.getHour(); // Aqui pego o valor das horas menos o horário de almoço/janta
                        int minutosN = diferencadas22AteOFim.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal
                        String hourNot = String.valueOf(horasN) + "." + String.valueOf(minutosN);
                        BigDecimal valorHoraDepoisDas22 = new BigDecimal(hourNot).multiply(new BigDecimal(1.142857)).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                        //--------Este trecho faz a contagem de dias úteis no mês
                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int workingDaysNoturno = 0;

                        YearMonth anoMes = YearMonth.of(year, month);
                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));

                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                workingDaysNoturno++;
                            }
                        }
                        /*------------------------------------------------------------------------------------*/
                        valorReferenciaHoraNoturna = valorHoraDepoisDas22.multiply(new BigDecimal(workingDaysNoturno)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias úteis vezes o número de horas noturnas no mês.
                        BigDecimal valorTotalHoraNoturna = valorReferenciaHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                        resultado.put("referencia", valorReferenciaHoraNoturna);
                        resultado.put("vencimentos", valorTotalHoraNoturna);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }

                } else {

                    LocalTime horaIniNot = LocalTime.parse("22:00");
                    LocalTime horaFimNot = horaSaida;
                    LocalTime hora13Not = LocalTime.parse("13:00");
                    LocalTime hora20Not = LocalTime.parse("20:00");

                    int valorHoraSaidaNot = horaFimNot.getHour();

                    if (horaFimNot.isBefore(hora13Not)) { // Aqui ele compara a hora de saída se for até meia noite

                        Duration das22AteOFim = Duration.between(hora20Not, horaIniNot); //Fórmula 22:00 - 20:00 + x, onde x será o valor horário de saída
                        LocalTime diferencadas22AteOFim = LocalTime.ofNanoOfDay(das22AteOFim.toNanos());// Aqui tenho a hora noturna de um dia
                        int horasN = diferencadas22AteOFim.getHour() + valorHoraSaidaNot; // Aqui pego o valor das horas menos o horário de almoço/janta
                        int minutosN = diferencadas22AteOFim.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal
                        String hourNot = String.valueOf(horasN) + "." + String.valueOf(minutosN);
                        BigDecimal valorHoraDepoisDas22 = (new BigDecimal(hourNot)).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                        //---------Este trecho faz a contagem de dias úteis no mês
                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int workingDaysNoturno = 0;

                        YearMonth anoMes = YearMonth.of(year, month);
                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));

                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                workingDaysNoturno++;
                            }
                        }
                        /*------------------------------------------------------------------------------------*/
                        valorReferenciaHoraNoturna = valorHoraDepoisDas22.multiply(new BigDecimal(workingDaysNoturno)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias úteis vezes o número de horas noturnas no mês.
                        BigDecimal valorTotalHoraNoturna = valorReferenciaHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                        resultado.put("referencia", valorReferenciaHoraNoturna);
                        resultado.put("vencimentos", valorTotalHoraNoturna);
                        resultado.put("descontos", BigDecimal.ZERO);

                    } else {
                        /*Aqui é feito o cálculo quando a hora de saída é antes da meia noite*/
                        Duration das22AteOFimNot = Duration.between(horaIniNot, horaFimNot);
                        LocalTime diferencadas22AteOFim = LocalTime.ofNanoOfDay(das22AteOFimNot.toNanos());// Aqui tenho a hora noturna de um dia
                        int horasN = diferencadas22AteOFim.getHour(); // Aqui pego o valor das horas menos o horário de almoço/janta
                        int minutosN = diferencadas22AteOFim.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal
                        String hourNot = String.valueOf(horasN) + "." + String.valueOf(minutosN);
                        BigDecimal valorHoraDepoisDas22 = new BigDecimal(hourNot).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                        //------Este trecho faz a contagem de dias úteis no mês
                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int workingDaysNoturno = 0;

                        YearMonth anoMes = YearMonth.of(year, month);
                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));
                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                workingDaysNoturno++;
                            }
                        }
                        /*------------------------------------------------------------------------------------*/
                        valorReferenciaHoraNoturna = valorHoraDepoisDas22.multiply(new BigDecimal(workingDaysNoturno)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias úteis vezes o número de horas noturnas no mês.
                        BigDecimal valorTotalHoraNoturna = valorReferenciaHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                        resultado.put("referencia", valorReferenciaHoraNoturna);
                        resultado.put("vencimentos", valorTotalHoraNoturna);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }
                }
            }

            //Calculando o Adicional Noturno
            case 14 -> {

                BigDecimal referenciaAdicionalNoturno = valorReferenciaHoraNoturna.add(valorReferenciaDsrHoraNoturna);
                BigDecimal adicionalNoturno = (referenciaAdicionalNoturno.multiply(salarioPorHora).multiply(percentualAdicionalNoturno).divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP);
                resultado.put("referencia", referenciaAdicionalNoturno);
                resultado.put("vencimentos", adicionalNoturno);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando o Pro-Labore
            case 17 -> {
                BigDecimal proLabore = salarioBase;
                resultado.put("vencimentos", proLabore);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando Bolsa Auxílio
            case 19 -> {
                BigDecimal bolsaAuxilio = salarioBase;
                resultado.put("vencimentos", bolsaAuxilio);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando Horas Repouso Remunerado (DSR) Noturno
            case 25 -> {
                if (tipoJornada.equals("12 x 36")) {

                    LocalTime horaIniRepRemNot = LocalTime.parse("22:00");
                    LocalTime horaFimRepRemNot = horaSaida;
                    LocalTime hora13RepRemNot = LocalTime.parse("13:00");
                    LocalTime hora20RepRemNot = LocalTime.parse("20:00");

                    int valorHoraSaidaNotRepRemNot = horaFimRepRemNot.getHour();

                    if (horaFimRepRemNot.isBefore(hora13RepRemNot)) {

                        Duration das22AteOFimRepRemNot = Duration.between(hora20RepRemNot, horaIniRepRemNot); //Fórmula 22:00 - 20:00 + x, onde x será o valor horário de saída
                        LocalTime diferencadas22AteOFimRepRemNot = LocalTime.ofNanoOfDay(das22AteOFimRepRemNot.toNanos()); // Aqui tenho a hora noturna de um dia

                        int horasNotDsr = diferencadas22AteOFimRepRemNot.getHour() + valorHoraSaidaNotRepRemNot; //Aqui pego o valor e somo com as horas de saída
                        int minutosNotDsr = diferencadas22AteOFimRepRemNot.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourNotDsr = String.valueOf(horasNotDsr) + "." + String.valueOf(minutosNotDsr);
                        BigDecimal valorHoraDepoisDas22RepRemNot = new BigDecimal(hourNotDsr).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                        valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal("15")).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
                        BigDecimal dsrHoraNoturnaRepousoRemuneradoNoturno = valorReferenciaDsrHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                        resultado.put("referencia", valorReferenciaDsrHoraNoturna);
                        resultado.put("vencimentos", dsrHoraNoturnaRepousoRemuneradoNoturno);
                        resultado.put("descontos", BigDecimal.ZERO);
                    } else {

                        Duration das22AteOFimRepRemNot = Duration.between(horaIniRepRemNot, horaFimRepRemNot);
                        LocalTime diferencadas22AteOFimRepRemNot = LocalTime.ofNanoOfDay(das22AteOFimRepRemNot.toNanos());// Aqui tenho a Diferença de horas Noturnas

                        int horasNotDsr = diferencadas22AteOFimRepRemNot.getHour(); // Aqui pego o valor das horas
                        int minutosNotDsr = diferencadas22AteOFimRepRemNot.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourNotDsr2 = String.valueOf(horasNotDsr) + "." + String.valueOf(minutosNotDsr);
                        BigDecimal valorHoraDepoisDas22RepRemNot = new BigDecimal(hourNotDsr2).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                        //------Este trecho faz a contagem de dias úteis no mês
                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int workingDaysNotRepRemNot = 0;

                        YearMonth anoMes = YearMonth.of(year, month);
                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));

                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                workingDaysNotRepRemNot++;
                            }
                        }
                        valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal(workingDaysNotRepRemNot)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
                        BigDecimal dsrHoraNoturnaRepousoRemuneradoNoturno = valorReferenciaDsrHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                        resultado.put("referencia", valorReferenciaDsrHoraNoturna);
                        resultado.put("vencimentos", dsrHoraNoturnaRepousoRemuneradoNoturno);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }
                } else {
                    LocalTime horaIniNotRepRemNot = LocalTime.parse("22:00");
                    LocalTime horaFimNotRepRemNot = horaSaida;
                    LocalTime hora13NotRepRemNot = LocalTime.parse("13:00");
                    LocalTime hora20NotRepRemNot = LocalTime.parse("20:00");

                    int valorHoraSaidaNotRepRemNot = horaFimNotRepRemNot.getHour();

                    if (horaFimNotRepRemNot.isBefore(hora13NotRepRemNot)) {

                        Duration das22AteOFimRepRemNot = Duration.between(hora20NotRepRemNot, horaIniNotRepRemNot); //Fórmula 22:00 - 20:00 + x, onde x será o valor horário de saída
                        LocalTime diferencadas22AteOFimRepRemNot = LocalTime.ofNanoOfDay(das22AteOFimRepRemNot.toNanos()); // Aqui tenho a hora noturna de um dia

                        int horasNotDsrRepRemNot = diferencadas22AteOFimRepRemNot.getHour() + valorHoraSaidaNotRepRemNot; //Aqui pego o valor e somo com as horas de saída
                        int minutosNotDsrRepRemNot = diferencadas22AteOFimRepRemNot.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourNotDsrRepRemNot = String.valueOf(horasNotDsrRepRemNot) + "." + String.valueOf(minutosNotDsrRepRemNot);
                        BigDecimal valorHoraDepoisDas22RepRemNot = new BigDecimal(hourNotDsrRepRemNot).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                        //---------Este trecho faz a contagem de dias úteis no mês
                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int workingDaysNotRepRemNot = 0;

                        YearMonth anoMes = YearMonth.of(year, month);
                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));

                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                workingDaysNotRepRemNot++;
                            }
                        }
                        valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal(workingDaysNotRepRemNot)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
                        BigDecimal dsrHoraNoturnaRepousoRemuneradoNoturno = valorReferenciaDsrHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                        resultado.put("referencia", valorReferenciaDsrHoraNoturna);
                        resultado.put("vencimentos", dsrHoraNoturnaRepousoRemuneradoNoturno);
                        resultado.put("descontos", BigDecimal.ZERO);
                    } else {

                        Duration das22AteOFimRepRemNot = Duration.between(horaIniNotRepRemNot, horaFimNotRepRemNot);
                        LocalTime diferencadas22AteOFimRepRemNot = LocalTime.ofNanoOfDay(das22AteOFimRepRemNot.toNanos());// Aqui tenho a Diferença de horas Noturnas;

                        int horasNotDsrRepRemNot = diferencadas22AteOFimRepRemNot.getHour(); // Aqui pego o valor das horas
                        int minutosNotDsrRepRemNot = diferencadas22AteOFimRepRemNot.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourNotDsrRepRemNot = String.valueOf(horasNotDsrRepRemNot) + "." + String.valueOf(minutosNotDsrRepRemNot);
                        BigDecimal valorHoraDepoisDas22RepRemNot = new BigDecimal(hourNotDsrRepRemNot).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                        //-------Este trecho faz a contagem de dias úteis no mês
                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int workingDaysNotRepRemNot = 0;

                        YearMonth anoMes = YearMonth.of(year, month);
                        feriados.addAll(calendario.getFeriadosFixos(year));
                        feriados.addAll(calendario.getFeriadosMoveis(year));

                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                workingDaysNotRepRemNot++;
                            }
                        }
                        valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal(workingDaysNotRepRemNot)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
                        BigDecimal dsrHoraNoturnaRepousoRemuneradoNoturno = valorReferenciaDsrHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                        resultado.put("referencia", valorReferenciaDsrHoraNoturna);
                        resultado.put("vencimentos", dsrHoraNoturnaRepousoRemuneradoNoturno);
                        resultado.put("descontos", BigDecimal.ZERO);
                    }
                }
            }

            //Calculando DSR Sobre Hora Extra Diurna 50%
            case 26 -> {

                //----Calculando Horas Diurnas Úteis
                int year = LocalDate.now().getYear();
                int month = LocalDate.now().getMonthValue();
                int workingDaysDiurn50 = 0;
                int workingDaysNaoDiurn50 = 0;

                YearMonth anoMes50 = YearMonth.of(year, month);
                feriados.addAll(calendario.getFeriadosFixos(year));
                feriados.addAll(calendario.getFeriadosMoveis(year));
                for (int dia = 1; dia <= anoMes50.lengthOfMonth(); dia++) {
                    LocalDate data = anoMes50.atDay(dia);

                    if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                        workingDaysDiurn50++;
                    }
                }
                //-------Calculando Horas Diurnas Não Úteis
                for (int diaNUt = 1; diaNUt <= anoMes50.lengthOfMonth(); diaNUt++) {
                    LocalDate dataNUt = anoMes50.atDay(diaNUt);

                    if (dataNUt.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(dataNUt)) {
                        workingDaysNaoDiurn50++;
                    }
                }
                BigDecimal quantidadeHorasExtrasDiurnas50 = quantidadeHoraExtra50.divide(new BigDecimal(workingDaysDiurn50), 2, RoundingMode.HALF_UP);
                BigDecimal dsrSobreHoraExtraDiurna = (quantidadeHoraExtra50.divide(new BigDecimal(workingDaysDiurn50), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(workingDaysNaoDiurn50));
                resultado.put("referencia", quantidadeHorasExtrasDiurnas50);
                resultado.put("vencimentos", dsrSobreHoraExtraDiurna);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando DSR Sobre Hora Extra Diurna 70%
            case 27 -> {

                //---------Calculando Horas Diurnas Úteis
                int year = LocalDate.now().getYear();
                int month = LocalDate.now().getMonthValue();
                int workingDaysDiurn70 = 0;
                int workingDaysNaoDiurn70 = 0;

                YearMonth anoMes = YearMonth.of(year, month);
                feriados.addAll(calendario.getFeriadosFixos(year));
                feriados.addAll(calendario.getFeriadosMoveis(year));
                for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                    LocalDate data = anoMes.atDay(dia);

                    if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                        workingDaysDiurn70++;
                    }
                }
                //----------Calculando Horas Diurnas Não Úteis
                for (int diaNUt = 1; diaNUt <= anoMes.lengthOfMonth(); diaNUt++) {
                    LocalDate dataNUt = anoMes.atDay(diaNUt);

                    if (dataNUt.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(dataNUt)) {
                        workingDaysNaoDiurn70++;
                    }
                }
                BigDecimal quantidadeHorasExtrasDiurnas70 = quantidadeHoraExtra70.divide(new BigDecimal(workingDaysDiurn70),2, RoundingMode.HALF_UP);
                BigDecimal dsrSobreHoraExtraDiurna70 = (quantidadeHoraExtra70.divide(new BigDecimal(workingDaysDiurn70), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(workingDaysNaoDiurn70));
                resultado.put("referencia", quantidadeHorasExtrasDiurnas70);
                resultado.put("vencimentos", dsrSobreHoraExtraDiurna70);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando DSR Sobre Hora Extra Diurna 100%
            case 28 -> {

                //------------Calculando Horas Diurnas Úteis--
                int year = LocalDate.now().getYear();
                int month = LocalDate.now().getMonthValue();
                int workingDaysDiurn100 = 0;
                int workingDaysNaoDiurn100 = 0;

                YearMonth anoMes100 = YearMonth.of(year, month);
                feriados.addAll(calendario.getFeriadosFixos(year));
                feriados.addAll(calendario.getFeriadosMoveis(year));
                for (int dia = 1; dia <= anoMes100.lengthOfMonth(); dia++) {
                    LocalDate data = anoMes100.atDay(dia);

                    if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                        workingDaysDiurn100++;
                    }
                }
                //---------------Calculando Horas Diurnas Não Úteis/
                for (int diaNUt = 1; diaNUt <= anoMes100.lengthOfMonth(); diaNUt++) {
                    LocalDate dataNUt = anoMes100.atDay(diaNUt);

                    if (dataNUt.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(dataNUt)) {
                        workingDaysNaoDiurn100++;
                    }
                }
                BigDecimal quantidadeHorasExtrasDiurnas100 = quantidadeHoraExtra100.divide(new BigDecimal(workingDaysDiurn100), 2, RoundingMode.HALF_UP);
                BigDecimal dsrSobreHExtraDiurna100 = (quantidadeHoraExtra100.divide(new BigDecimal(workingDaysDiurn100), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(workingDaysNaoDiurn100));
                resultado.put("referencia", quantidadeHorasExtrasDiurnas100);
                resultado.put("vencimentos", dsrSobreHExtraDiurna100);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando a Insalubridade
            case 46 -> {
                   BigDecimal porcentagemInsalubre = percentualInsalubridade;
                BigDecimal valorInsalubre = (valorSalarioMinimo.multiply(porcentagemInsalubre)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
                resultado.put("vencimentos", valorInsalubre);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando a Periculosidade
            case 47 -> {
                BigDecimal porcentagemPericuloso = percentualPericulosidade;
                BigDecimal valorPericuloso = (salarioBase.multiply(porcentagemPericuloso)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
                resultado.put("vencimentos", valorPericuloso);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando a Comissão
            case 51 -> {
                BigDecimal comissao = valorComissao;
                BigDecimal vendasMes = valorVendasMes;
                BigDecimal valorComissao = (comissao.multiply(vendasMes)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
                resultado.put("referencia", vendasMes);
                resultado.put("vencimentos", valorComissao);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando a Gratificação
            case 53 -> {
                BigDecimal valorGratifica = valorGratificacao;
                resultado.put("vencimentos", valorGratifica);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando a Quebra Caixa
            case 54 -> {
                BigDecimal valorQuebCaixa = (valorQuebraCaixa.multiply(valorReferenciaHoraDiurna)).divide(horasPorMes,2, RoundingMode.HALF_UP);
                resultado.put("vencimentos", valorQuebCaixa);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando horas extras 50% feitas no mês.
            case 98 -> {

                if (percentualInsalubridade.compareTo(BigDecimal.ZERO) == 0) {

                    BigDecimal totalHoraExtra50 = quantidadeHoraExtra50;
                    BigDecimal valorHoraExtra50Mes = (salarioPorHora.multiply(new BigDecimal("1.5"))).multiply(totalHoraExtra50);
                    resultado.put("referencia", totalHoraExtra50);
                    resultado.put("vencimentos", valorHoraExtra50Mes);
                    resultado.put("descontos", BigDecimal.ZERO);

                } else {

                    BigDecimal horasTrabNoMes = horasPorMes;
                    BigDecimal porcentoInsalubre = percentualInsalubridade; //Pega o percentual insalubre
                    BigDecimal valorInsalubre = ((valorSalarioMinimo.multiply(porcentoInsalubre)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP)).divide(horasTrabNoMes,2, RoundingMode.HALF_UP); // Calcula o valor da insalubridade e divide o valor pelas hora strabalhadas no mês

                    BigDecimal totalHoraExtra50 = quantidadeHoraExtra50;
                    BigDecimal valorHoraExtra50Mes = ((salarioPorHora.add(valorInsalubre)).multiply(new BigDecimal("1.5"))).multiply(totalHoraExtra50).setScale(2, RoundingMode.HALF_UP);
                    resultado.put("referencia", totalHoraExtra50);
                    resultado.put("vencimentos", valorHoraExtra50Mes);
                    resultado.put("descontos", BigDecimal.ZERO);
                }
            }

            //Calculando horas extras 70% feitas no mês
            case 99 -> {
                if (percentualInsalubridade.compareTo(BigDecimal.ZERO) == 0) {

                    BigDecimal totalHoraExtra70 = quantidadeHoraExtra70;
                    BigDecimal valorHoraExtra70Mes = (salarioPorHora.multiply(new BigDecimal("1.7"))).multiply(totalHoraExtra70).setScale(2, RoundingMode.HALF_UP);
                    resultado.put("referencia", totalHoraExtra70);
                    resultado.put("vencimentos", valorHoraExtra70Mes);
                    resultado.put("descontos", BigDecimal.ZERO);

                } else {

                    BigDecimal horasTrabNoMes = horasPorMes;
                    BigDecimal porcentoInsalubre = percentualInsalubridade; //Pega o percentual insalubre
                    BigDecimal valorInsalubre = ((valorSalarioMinimo.multiply(porcentoInsalubre)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP)).divide(horasTrabNoMes, 2, RoundingMode.HALF_UP); // Calcula o valor da insalubridade e divide o valor pelas hora strabalhadas no mês

                    BigDecimal totalHoraExtra70 = quantidadeHoraExtra70;
                    BigDecimal valorHoraExtra70Mes = ((salarioPorHora.add(valorInsalubre)).multiply(new BigDecimal("1.7"))).multiply(totalHoraExtra70).setScale(2, RoundingMode.HALF_UP);
                    resultado.put("referencia", totalHoraExtra70);
                    resultado.put("vencimentos", valorHoraExtra70Mes);
                    resultado.put("descontos", BigDecimal.ZERO);

                }
            }

            //Calculando horas extras 100% feitas no mês.
            case 100 -> {

                if ((percentualInsalubridade.compareTo(BigDecimal.ZERO) == 0)) {

                    BigDecimal totalHoraExtra100 = quantidadeHoraExtra70;
                    BigDecimal valorHoraExtra100Mes = (salarioPorHora.multiply(new BigDecimal("2"))).multiply(totalHoraExtra100);
                    resultado.put("referencia", totalHoraExtra100);
                    resultado.put("vencimentos", valorHoraExtra100Mes);
                    resultado.put("descontos", BigDecimal.ZERO);

                } else {

                    BigDecimal horasTrabNoMes = horasPorMes;
                    BigDecimal porcentoInsalubre = percentualInsalubridade; //Pega o percentual insalubre
                    BigDecimal valorInsalubre = ((valorSalarioMinimo.multiply(porcentoInsalubre)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)).divide(horasTrabNoMes,2, RoundingMode.HALF_UP); // Calcula o valor da insalubridade e divide o valor pelas hora strabalhadas no mês

                    BigDecimal totalHoraExtra100 = quantidadeHoraExtra100;
                    BigDecimal valorHoraExtra100Mes= ((salarioPorHora.add(valorInsalubre)).multiply(new BigDecimal(2))).multiply(totalHoraExtra100).setScale(2, RoundingMode.HALF_UP);
                    resultado.put("referencia", totalHoraExtra100);
                    resultado.put("vencimentos", valorHoraExtra100Mes);
                    resultado.put("descontos", BigDecimal.ZERO);
                }
            }

            //Calculando o Salário Maternidade
            case 101 -> {

                BigDecimal salarioMaternidade = salarioBase;
                resultado.put("vencimentos", salarioMaternidade);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando Média Horas Extras 50% Sobre Salário Maternidade
            case 102 -> {
                try {
                    LocalDate dataLimite = LocalDate.now().minusMonths(6);

                    BigDecimal mediaHoraExtraSalarioMaternidade50 = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 98, dataLimite);
                    BigDecimal quantidadeMediaHoras = BigDecimal.ZERO;

                    if (salarioPorHora.compareTo(BigDecimal.ZERO) > 0) {
                        // CORREÇÃO: Para achar quantidade de horas = valor / (salarioHora * 1.5)
                        // HE 50%: valor = horas * salarioHora * 1.5
                        BigDecimal valorPorHoraComAdicional = salarioPorHora.multiply(new BigDecimal("1.5"));
                        quantidadeMediaHoras = mediaHoraExtraSalarioMaternidade50.divide(valorPorHoraComAdicional, 2, RoundingMode.HALF_UP);
                    }

                    // CORREÇÃO: O valor já é a média em R$, está correto
                    BigDecimal total = mediaHoraExtraSalarioMaternidade50.setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", quantidadeMediaHoras);  // quantidade média de horas
                    resultado.put("vencimentos", total);               // média em R$
                    resultado.put("descontos", BigDecimal.ZERO);

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média Horas Extra 50% sobre o Salário Maternidade: " + e.getMessage());
                }
                return resultado;
            }

            //Calculando Média Horas Extras70% Sobre Salário Maternidade
            case 103 -> {
                try {
                    LocalDate dataLimite = LocalDate.now().minusMonths(6);

                    // CORREÇÃO: Evento 70 para HE 70% (padrão mais comum)
                    BigDecimal mediaHoraExtraSalarioMaternidade70 = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 99, dataLimite);
                    BigDecimal quantidadeMediaHoras = BigDecimal.ZERO;

                    if (salarioPorHora.compareTo(BigDecimal.ZERO) > 0) {
                        // CORREÇÃO: Para achar quantidade de horas = valor / (salarioHora * 1.7)
                        // HE 70%: valor = horas * salarioHora * 1.7
                        BigDecimal valorPorHoraComAdicional = salarioPorHora.multiply(new BigDecimal("1.7"));
                        quantidadeMediaHoras = mediaHoraExtraSalarioMaternidade70.divide(valorPorHoraComAdicional, 2, RoundingMode.HALF_UP);
                    }

                    BigDecimal total = mediaHoraExtraSalarioMaternidade70.setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", quantidadeMediaHoras);  // quantidade média de horas
                    resultado.put("vencimentos", total);               // média em R$
                    resultado.put("descontos", BigDecimal.ZERO);

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média Horas Extra 70% sobre o Salário Maternidade: " + e.getMessage());
                }
                return resultado;
            }

            //Calculando Média Horas Extras100% Sobre Salário Maternidade
            case 104 -> {
                try {
                    LocalDate dataLimite = LocalDate.now().minusMonths(6);

                    // Busca a média dos últimos 6 meses de horas extras 70%
                    BigDecimal mediaHoraExtraSalarioMaternidade50 = folhaMensalEventosCalculadaRepository.findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 100, dataLimite);

                    BigDecimal adicional = new BigDecimal("2.0");
                    BigDecimal quantidadeMediaHoras = BigDecimal.ZERO;

                    if (salarioPorHora.compareTo(BigDecimal.ZERO) > 0) {
                        quantidadeMediaHoras = mediaHoraExtraSalarioMaternidade50.divide(salarioPorHora.multiply(adicional), 2, RoundingMode.HALF_UP);
                    }

                    BigDecimal total = mediaHoraExtraSalarioMaternidade50.setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", quantidadeMediaHoras);  // quantidade média de horas
                    resultado.put("vencimentos", total);               // média em R$
                    resultado.put("descontos", BigDecimal.ZERO);

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média Horas Extra 100% sobre o Salário Maternidade: " + e.getMessage());
                }
                return resultado;
            }

            // Calculando Média de DSR Diurno Sobre Salário Maternidade
            case 105 -> {
                try {
                    LocalDate dataLimite = LocalDate.now().minusMonths(6);

                    // CORREÇÃO: Usar métudo com data limite e evento como String
                    BigDecimal mediaDsrDiurnoSalarioMaternidade = folhaMensalEventosCalculadaRepository
                            .findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 5, dataLimite);

                    BigDecimal valorDsrDiurnoSalarioMaternidade = mediaDsrDiurnoSalarioMaternidade.setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", BigDecimal.ZERO);
                    resultado.put("vencimentos", valorDsrDiurnoSalarioMaternidade);
                    resultado.put("descontos", BigDecimal.ZERO);

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de DSR Diurno sobre Salário Maternidade: " + e.getMessage());
                }
                return resultado;
            }

            // Calculando Média de DSR Noturno Sobre Salário Maternidade
            case 106 -> {
                try {
                    LocalDate dataLimite = LocalDate.now().minusMonths(6);

                    BigDecimal mediaDsrNoturnoSalarioMaternidade = folhaMensalEventosCalculadaRepository
                            .findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 25, dataLimite);

                    BigDecimal valorDsrNoturnoSalarioMaternidade = mediaDsrNoturnoSalarioMaternidade.setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", BigDecimal.ZERO);
                    resultado.put("vencimentos", valorDsrNoturnoSalarioMaternidade);
                    resultado.put("descontos", BigDecimal.ZERO);

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de DSR Noturno sobre Salário Maternidade: " + e.getMessage());
                }
                return resultado;
            }

            // Média Adicional Noturno Sobre Salário Maternidade
            case 107 -> {
                try {
                    LocalDate dataLimite = LocalDate.now().minusMonths(6);

                    // Busca a média em R$ dos últimos 6 meses
                    BigDecimal mediaAdicionalNoturnoSalarioMaternidade = folhaMensalEventosCalculadaRepository
                            .findMediaHorasExtrasUltimosSeisMeses(numeroMatricula, 14, dataLimite);

                    // CORREÇÃO: Se a média já está em R$, não precisa multiplicar por salarioPorHora
                    // Apenas aplica o adicional de 20% sobre a média
                    BigDecimal valorComAdicional = mediaAdicionalNoturnoSalarioMaternidade
                            .multiply(new BigDecimal("1.20")).setScale(2, RoundingMode.HALF_UP);

                    // Para referência, calcula quantas horas a média representa
                    BigDecimal horasReferencia = BigDecimal.ZERO;
                    if (salarioPorHora.compareTo(BigDecimal.ZERO) > 0) {
                        // Adicional noturno é 20% por hora, então: valor = horas * salarioHora * 0.20
                        // Para achar horas: horas = valor / (salarioHora * 0.20)
                        horasReferencia = mediaAdicionalNoturnoSalarioMaternidade.divide(salarioPorHora.multiply(new BigDecimal("0.20")), 2, RoundingMode.HALF_UP);
                    }

                    resultado.put("referencia", horasReferencia);
                    resultado.put("vencimentos", valorComAdicional);
                    resultado.put("descontos", BigDecimal.ZERO);

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Adicional Noturno sobre Salário Maternidade: " + e.getMessage());
                }
                return resultado;
            }

            //Calculando Salário Família
            case 133 -> {
                try {
                    BigDecimal valorSalarioFamilia = folhaMensalService.calcularSalarioFamilia(valorCotaSalarioFamilia, numeroDependentes);

                    resultado.put("referencia", BigDecimal.valueOf(numeroDependentes)); // Quantidade de filhos
                    resultado.put("vencimentos", valorSalarioFamilia);              // Valor total
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular salário família: " + e.getMessage());
                }
            }

            //Calculando Ajuda de Custo
            case 130 -> {
                resultado.put("referencia", valorValeTransporte);
                resultado.put("vencimentos", valorValeTransporte);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando Primeira Parcela 13°
            case 167 -> {
                try {
                    LocalDate dataAdmissao = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getDataAdmissao();

                    // Calcular meses trabalhados considerando a regra dos 15 dias
                    int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao, LocalDate.now());

                    // Cálculo proporcional do 13º
                    BigDecimal valorMensal = salarioBase.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
                    BigDecimal valorProporcional = valorMensal.multiply(new BigDecimal(mesesTrabalhados));
                    BigDecimal primeiraParcela = valorProporcional.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", new BigDecimal(mesesTrabalhados));
                    resultado.put("vencimentos", primeiraParcela);
                    resultado.put("descontos", BigDecimal.ZERO);

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular primeira parcela do 13º: " + e.getMessage());
                }
                return resultado;
            }

            //Calculando Média de Horas Extras 50% Sobre 1° Parcela do 13°
            case 168 -> {
                try {
                      Map<String, BigDecimal> resultadoHE50 = folhaMensalService.calcularMediaHE50PrimeiraParcela13(numeroMatricula, salarioPorHora);

                    // Atualiza o resultado principal
                    resultado.putAll(resultadoHE50);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 50% Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 70% Sobre 1° Parcela do 13°
            case 169 -> {
                try {
                    Map<String, BigDecimal> resultadoHE70 = folhaMensalService.calcularMediaHE70PrimeiraParcela13(numeroMatricula, salarioPorHora);

                    // Atualiza o resultado principal
                    resultado.putAll(resultadoHE70);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 70% Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 100% Sobre 1° Parcela do 13°
            case 170 -> {
                try {
                    Map<String, BigDecimal> resultadoHE100 = folhaMensalService.calcularMediaHE100PrimeiraParcela13(numeroMatricula, salarioPorHora);

                    // Atualiza o resultado principal
                    resultado.putAll(resultadoHE100);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 100% Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Décimo terceiro cheio Adiantado
            case 171 ->{
                resultado.put("vencimentos", salarioBase);
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando Média de DSR Noturno Sobre 1° Parcela do 13°
            case 177 -> {

                LocalDate hoje = LocalDate.now();
                int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao,hoje);

                try {
                    Map<String, BigDecimal> resultadoDSRNoturno = folhaMensalService.calcularMediaDSRNoturnoPrimeiraParcela13(numeroMatricula, salarioBase, mesesTrabalhados);
                    resultado.putAll(resultadoDSRNoturno);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de DSR Noturno Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Insalubridade sobre Primeira Parcela do 13°
            case 178 -> {

                LocalDate hoje = LocalDate.now();
                int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao,hoje);

                try {

                    Map<String, BigDecimal> resultadoInsalubridade = folhaMensalService.calcularInsalubridadePrimeiraParcela13(numeroMatricula, salarioBase, mesesTrabalhados);
                    resultado.putAll(resultadoInsalubridade);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Insalubridade Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Periculosidade sobre Primeira Parcela do 13°
            case 179 -> {

                LocalDate hoje = LocalDate.now();
                int mesesTrabalhados = calcularMesesTrabalhados13o(dataAdmissao,hoje);

                try {
                  Map<String, BigDecimal> resultadoPericulosidade = folhaMensalService.calcularPericulosidadePrimeiraParcela13(numeroMatricula, salarioBase, mesesTrabalhados);
                    resultado.putAll(resultadoPericulosidade);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Periculosidade Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 50% Sobre 2° Parcela do 13°
            case 182 -> {
                try {
                    Map<String, BigDecimal> resultadoMediaHE50 = folhaMensalService.calcularMediaHE50SegundaParcela13(numeroMatricula, salarioBase, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoMediaHE50);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 50% para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 70% Sobre 2° Parcela do 13°
            case 183 -> {
                try {
                    Map<String, BigDecimal> resultadoMediaHE70 = folhaMensalService.calcularMediaHE70SegundaParcela13(numeroMatricula, salarioBase, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoMediaHE70);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 70% para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 100% Sobre 2° Parcela do 13°
            case 184 -> {
                try {
                    Map<String, BigDecimal> resultadoMediaHE100 = folhaMensalService.calcularMediaHE100SegundaParcela13(numeroMatricula, salarioBase, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoMediaHE100);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 100% para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de DSR Diurno Sobre 2° Parcela do 13°
            case 185 -> {
                dataAdmissao = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getDataAdmissao();
                try {
                     Map<String, BigDecimal> resultadoMediaDsr13 = folhaMensalService.calcularMediaDSRSegundaParcela13(numeroMatricula, dataAdmissao);
                     resultado.putAll(resultadoMediaDsr13);
                     return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de DSR para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de DSR Noturno Sobre 2° Parcela do 13°
            case 186 ->{
                dataAdmissao = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getDataAdmissao();
                try {
                    Map<String, BigDecimal> resultadoMediaDsrNoturno13 = folhaMensalService.calcularMediaDSRNoturnoSegundaParcela13(numeroMatricula, dataAdmissao);
                    resultado.putAll(resultadoMediaDsrNoturno13);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de DSR Noturno para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Insalubridade sobre Segunda Parcela do 13°
            case 187 ->{
                dataAdmissao = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getDataAdmissao();
                try {
                    Map<String, BigDecimal> resultadoInsalubreSegundaParcela13 = folhaMensalService.calcularInsalubridadeSegundaParcela13(numeroMatricula, salarioBase, dataAdmissao);
                    resultado.putAll(resultadoInsalubreSegundaParcela13);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Insalubridade sobre Segunda Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Periculosidade sobre Segunda Parcela do 13°
            case 188 ->{
                horasTrabalhadasPorMes = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getHorasMes();
                numeroDependentes = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getDependentesIrrf();
                salarioBase = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getSalarioBase();
                dataAdmissao = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getDataAdmissao();
                try {
                    Map<String, BigDecimal> resultadoPericulosoSegundaParcela13 = folhaMensalService.calcularPericulosidadeSegundaParcela13(numeroMatricula, salarioBase, dataAdmissao,horasTrabalhadasPorMes);
                    resultado.putAll(resultadoPericulosoSegundaParcela13);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Periculosidade sobre Segunda Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando a Segunda Parcela do 13°
            case 189 -> {
                salarioBase = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getSalarioBase();
                dataAdmissao = folhaMensalService.findByMatriculaColaborador(numeroMatricula).getDataAdmissao();
                try {
                    Map<String, BigDecimal> resultadoSegundaParcela13 = folhaMensalService.calcularSegundaParcela13(numeroMatricula, salarioBase, dataAdmissao, numeroDependentes);
                    resultado.putAll(resultadoSegundaParcela13);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Segunda Parcela do 13°: " + e.getMessage());
                }
            }
        }
        return resultado;
    }

    //Outros métodos
    private int calcularMesesTrabalhados13o(LocalDate dataAdmissao, LocalDate dataCalculo) {
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

    private boolean trabalhouPeloMenos15DiasNoMes(LocalDate dataAdmissao, LocalDate dataCalculo, int mes, int ano) {
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

    private long calcularDiasUteisTrabalhados(LocalDate dataInicio, LocalDate dataFim) {
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

}
