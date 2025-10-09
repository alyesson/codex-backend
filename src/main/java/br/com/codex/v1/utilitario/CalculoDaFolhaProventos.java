package br.com.codex.v1.utilitario;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CalculoDaFolhaProventos {


    BigDecimal salarioBase, salarioPorHora, salarioMinimo, hExrtra50, hExrtra70, hExrtra100, valorReferenciaHoraNoturna, valorReferenHoraDiurna, valorReferenciaDsrHoraNoturna, media50, media70, media100,
            mediaValorDsr, mediaAdicNoturn13, calculo50MedSob13, calculo70MedSob13, calculo100MedSob13, somaComplemento50, somaComplemento70, somaComplemento100, somaComplementoDsrDiurn, somaComplementoDsrNoturn,
            calculoMedDsrSob13, calculoMedDsrNotSob13, medHextSalMater50, medHextSalMater70, medHextSalMater100, mediaValorDsrDiuSalMatern, mediaValorDsrNotSalmatern, mediaAdicNoturnSalMatern, mediaComissoesAno;

    Set<LocalDate> feriados = new HashSet<>();

    Date anoAtual;
    SimpleDateFormat pegaAno = new SimpleDateFormat("yyyy");
    SimpleDateFormat pegaMes = new SimpleDateFormat("MM");

    Calendario calend = new Calendario();

    public void escolheEventos() {

        for (int i = 0; i < tabelaCalculoFolha.getRowCount(); i++) {

            int leEvento = Integer.parseInt(tabelaCalculoFolha.getValueAt(i, 0).toString());

            switch (leEvento) {

                case 1 -> {
                    // Calculando horas normais diurnas
                    LocalTime horaIni = LocalTime.parse(horaEntrada.getText());
                    LocalTime horaFim = LocalTime.parse(horaSaida.getText());
                    LocalTime hora22 = LocalTime.parse("22:00");
                    LocalTime hora13 = LocalTime.parse("13:00");

                        /* Este bloco faz o cálculo se a pessoa trabalha em horário normal,
                           por exemplo das 08 às 18, com horário antes das 22:00 */

                    if (horaFim.isBefore(hora13)) {
                        Duration horaNormal = Duration.between(horaIni, hora22); // Aqui eu pego a hora que entrou por exemplo 18:00 e calculo a diferença até às 22:00
                        LocalTime diferencaHoraNormal = LocalTime.ofNanoOfDay(horaNormal.toNanos()); // Aqui tenho a Diferença de horas Diurnas

                        int horas = diferencaHoraNormal.getHour() - 1; // Aqui pego o valor das horas menos o horário de almoço
                        int minutos = diferencaHoraNormal.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourDiurna = String.valueOf(horas) + "." + String.valueOf(minutos);

                        /*-------------------- Este trecho faz a contagem de dias úteis no mês --------------------*/
                        anoAtual = new Date();
                        String leAno = pegaAno.format(anoAtual);
                        String leMes = pegaMes.format(anoAtual);

                        int year = Integer.parseInt(leAno);
                        int month = Integer.parseInt(leMes);
                        int workingDaysDiurno = 0;

                        YearMonth anoMes = YearMonth.of(year, month);

                        feriados.addAll(calend.getFeriadosFixos(year));
                        feriados.addAll(calend.getFeriadosMoveis(year));

                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                workingDaysDiurno++;
                            }
                        }
                        /*------------------------------------------------------------------------------------*/

                        // Calcula o valor das horas diurnas e arredonda para 2 casas decimais
                        valorReferenHoraDiurna = new BigDecimal(hourDiurna).multiply(new BigDecimal(workingDaysDiurno)).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais
                        tabelaCalculoFolha.setValueAt(valorReferenHoraDiurna, i, 2);
                        BigDecimal valorTotalHoraDiurna = valorReferenHoraDiurna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais
                        tabelaCalculoFolha.setValueAt(valorTotalHoraDiurna, i, 3);

                    } else {
                        if (horaFim.isBefore(hora22)) {
                            Duration horaNormal = Duration.between(horaIni, horaFim);
                            LocalTime diferencaHoraNormal = LocalTime.ofNanoOfDay(horaNormal.toNanos()); // Aqui tenho a Diferença de horas Diurnas

                            int horas = diferencaHoraNormal.getHour() - 1; // Aqui pego o valor das horas menos o horário de almoço
                            int minutos = diferencaHoraNormal.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                            String hourDiurna = String.valueOf(horas) + "." + String.valueOf(minutos);

                            /*-------------------- Este trecho faz a contagem de dias úteis no mês --------------------*/
                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);

                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            int workingDaysDiurno = 0;

                            YearMonth anoMes = YearMonth.of(year, month);

                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));

                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                    workingDaysDiurno++;
                                }
                            }
                            /*------------------------------------------------------------------------------------*/

                            // Calcula o valor das horas diurnas e arredonda para 2 casas decimais
                            valorReferenHoraDiurna = new BigDecimal(hourDiurna).multiply(new BigDecimal(workingDaysDiurno)).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais
                            tabelaCalculoFolha.setValueAt(valorReferenHoraDiurna, i, 2);
                            BigDecimal valorTotalHoraDiurna = valorReferenHoraDiurna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais
                            tabelaCalculoFolha.setValueAt(valorTotalHoraDiurna, i, 3);

                        } else {
                            /* Aqui eu pego 22 horas e subtraio pela hora de entrada para calcular o DSR Diurno até às 22:00 */
                            Duration horaNormal = Duration.between(horaIni, hora22);
                            LocalTime diferencaHoraNormal = LocalTime.ofNanoOfDay(horaNormal.toNanos()); // Aqui tenho a Diferença de horas Diurnas

                            int horas = diferencaHoraNormal.getHour() - 1; // Aqui pego o valor das horas menos o horário de almoço
                            int minutos = diferencaHoraNormal.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                            String hourDiurna = String.valueOf(horas) + "." + String.valueOf(minutos);

                            /*-------------------- Este trecho faz a contagem de dias úteis no mês --------------------*/
                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);

                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            int workingDaysDiurno = 0;

                            YearMonth anoMes = YearMonth.of(year, month);

                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));

                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                    workingDaysDiurno++;
                                }
                            }
                            /*------------------------------------------------------------------------------------*/

                            // Calcula o valor das horas diurnas e arredonda para 2 casas decimais
                            valorReferenHoraDiurna = new BigDecimal(hourDiurna).multiply(new BigDecimal(workingDaysDiurno)).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais
                            tabelaCalculoFolha.setValueAt(valorReferenHoraDiurna, i, 2);
                            BigDecimal valorTotalHoraDiurna = valorReferenHoraDiurna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas decimais
                            tabelaCalculoFolha.setValueAt(valorTotalHoraDiurna, i, 3);
                        }
                    }
                }

                case 2 -> {
                    //Calculando Adiantamento de Salário
                    BigDecimal vale = (salarioBase.multiply(new BigDecimal("40"))).divide(new BigDecimal("100").setScale(2, RoundingMode.HALF_UP));
                    tabelaCalculoFolha.setValueAt((vale), i, 3);
                }

                case 5 -> {
                    //Calculando Horas Repouso Remunerado Diurno no mês
                    //BigDecimal hoursInMonth = new BigDecimal(valorHorasPorMes.getText());

                    LocalTime horaIniHRDiurno = LocalTime.parse(horaEntrada.getText());
                    LocalTime horaFimHRDiurno = LocalTime.parse(horaSaida.getText());
                    LocalTime hora22HRDiurno = LocalTime.parse("22:00");
                    LocalTime hora13HRDiurno = LocalTime.parse("13:00");

                    if (horaFimHRDiurno.isBefore(hora13HRDiurno)) {

                        Duration horaNormalHRDiurno = Duration.between(horaIniHRDiurno, hora22HRDiurno); //Aqui eu pego a hora que entrou por exemplo 18:00 e calculo a diferença até às 22:00
                        LocalTime diferencaHoraNormalHRDiurno = LocalTime.ofNanoOfDay(horaNormalHRDiurno.toNanos());// Aqui tenho a Diferenca de horas Diurnas

                        int horasDiurn = diferencaHoraNormalHRDiurno.getHour() - 1; // Aqui pego o valor das horas menos o horário do almoço
                        int minutosDiurn = diferencaHoraNormalHRDiurno.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                        String hourDiurnaDsr = String.valueOf(horasDiurn) + "." + String.valueOf(minutosDiurn);// Horas normais no dia

                        /*--------------------Este trecho faz a contagem de dias úteis no mês--------------------*/
                        anoAtual = new Date();
                        String leAno = pegaAno.format(anoAtual);
                        String leMes = pegaMes.format(anoAtual);

                        int year = Integer.parseInt(leAno);
                        int month = Integer.parseInt(leMes);
                        int notWorkingDaysDir = 0;

                        YearMonth anoMes = YearMonth.of(year, month);

                        feriados.addAll(calend.getFeriadosFixos(year));
                        feriados.addAll(calend.getFeriadosMoveis(year));

                        /*----------Aqui é Encontrado os domingos e Feriados no Mês----------*/
                        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                            LocalDate data = anoMes.atDay(dia);

                            if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                notWorkingDaysDir++;
                            }
                        }
                        //tabelaCalculoFolha.setValueAt(new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr)), i, 2);
                        //BigDecimal dsrHoraDiruna = (new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr))).multiply(salarioPorHora);
                        BigDecimal resultado = new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr)).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(resultado, i, 2);
                        BigDecimal dsrHoraDiruna = resultado.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(dsrHoraDiruna, i, 3);

                    } else {

                        if (horaFimHRDiurno.isBefore(hora22HRDiurno)) {

                            Duration horaNormalHRDiurno = Duration.between(horaIniHRDiurno, horaFimHRDiurno);// Faz a diferença da Hora que Entrou com a Hora que saiu e me dá o total de horas num dia.
                            LocalTime diferencaHoraNormalHRDiurno = LocalTime.ofNanoOfDay(horaNormalHRDiurno.toNanos());// Aqui tenho a Diferenca de horas Diurnas

                            int horasDiurn = diferencaHoraNormalHRDiurno.getHour() - 1; // Aqui pego o valor das horas menos o horário do almoço
                            int minutosDiurn = diferencaHoraNormalHRDiurno.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                            String hourDiurnaDsr = String.valueOf(horasDiurn) + "." + String.valueOf(minutosDiurn);// Horas normais no dia

                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);

                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            int notWorkingDaysDir = 0;

                            YearMonth anoMes = YearMonth.of(year, month);

                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));

                            /*----------Aqui é Encontrado os domingos e Feriados no Mês----------*/
                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                    notWorkingDaysDir++;
                                }
                            }
                            //tabelaCalculoFolha.setValueAt(new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr)), i, 2);
                            //BigDecimal dsrHoraDiruna = (new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr))).multiply(salarioPorHora);
                            BigDecimal resultado = new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr)).setScale(2, RoundingMode.HALF_UP);
                            tabelaCalculoFolha.setValueAt(resultado, i, 2);
                            BigDecimal dsrHoraDiruna = resultado.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                            tabelaCalculoFolha.setValueAt((dsrHoraDiruna), i, 3);
                        } else {
                            Duration horaNormal = Duration.between(horaIniHRDiurno, hora22HRDiurno);// Faz a diferença da Hora que Entrou com a Hora que saiu e me dá o total de horas num dia.
                            LocalTime diferencaHoraNormalHRDiurno = LocalTime.ofNanoOfDay(horaNormal.toNanos());// Aqui tenho a Diferenca de horas Diurnas

                            int horasDiurn = diferencaHoraNormalHRDiurno.getHour() - 1; // Aqui pego o valor das horas menos o horário do almoço
                            int minutosDiurn = diferencaHoraNormalHRDiurno.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                            String hourDiurnaDsr = String.valueOf(horasDiurn) + "." + String.valueOf(minutosDiurn);// Horas normais no dia

                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);

                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            //int workingDaysDir = 0;
                            int notWorkingDaysDir = 0;

                            YearMonth anoMes = YearMonth.of(year, month);

                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));

                            /*----------Aqui é Encontrado os domingos e Feriados no Mês----------*/
                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                    notWorkingDaysDir++;
                                }
                            }
                            //tabelaCalculoFolha.setValueAt(new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr)), i, 2);
                            // BigDecimal dsrHoraDiruna = (new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr))).multiply(salarioPorHora);
                            BigDecimal resultado = new BigDecimal(notWorkingDaysDir).multiply(new BigDecimal(hourDiurnaDsr)).setScale(2, RoundingMode.HALF_UP);
                            tabelaCalculoFolha.setValueAt(resultado, i, 2);
                            BigDecimal dsrHoraDiruna = resultado.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                            tabelaCalculoFolha.setValueAt((dsrHoraDiruna), i, 3);
                        }
                    }
                }

                case 8 -> {
                    //Horas de Atestado Médico
                    BigDecimal horasDeFaltasAtesMed = new BigDecimal(faltasHorasMes.getText());
                    tabelaCalculoFolha.setValueAt(horasDeFaltasAtesMed, i, 2);
                }

                case 9 -> {
                    //Dias de atestado médico
                    BigDecimal horasDeFaltasMedc = new BigDecimal(faltasHorasMes.getText());
                    tabelaCalculoFolha.setValueAt(horasDeFaltasMedc, i, 2);
                }

                case 12 -> {
                    //Calculando horas normais noturnas

                    if (tipoDeJornada.getSelectedItem().equals("12 x 36")) {

                        LocalTime horaIniNot = LocalTime.parse("22:00");
                        LocalTime horaFimNot = LocalTime.parse(horaSaida.getText());
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
                            tabelaCalculoFolha.setValueAt((valorReferenciaHoraNoturna), i, 2);
                            BigDecimal valorTotalHoraNoturna = valorReferenciaHoraNoturna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                            tabelaCalculoFolha.setValueAt((valorTotalHoraNoturna), i, 3);

                        } else {
                            /*Aqui é feito o cálculo quando a hora de saída é antes da meia noite*/
                            Duration das22AteOFim = Duration.between(horaIniNot, horaFimNot);
                            LocalTime diferencadas22AteOFim = LocalTime.ofNanoOfDay(das22AteOFim.toNanos());// Aqui tenho a hora noturna de um dia
                            int horasN = diferencadas22AteOFim.getHour(); // Aqui pego o valor das horas menos o horário de almoço/janta
                            int minutosN = diferencadas22AteOFim.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal
                            String hourNot = String.valueOf(horasN) + "." + String.valueOf(minutosN);
                            BigDecimal valorHoraDepoisDas22 = new BigDecimal(hourNot).multiply(new BigDecimal(1.142857)).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                            /*--------------------Este trecho faz a contagem de dias úteis no mês--------------------*/
                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);
                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            int workingDaysNoturno = 0;
                            YearMonth anoMes = YearMonth.of(year, month);
                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));

                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                    workingDaysNoturno++;
                                }
                            }
                            /*------------------------------------------------------------------------------------*/
                            valorReferenciaHoraNoturna = valorHoraDepoisDas22.multiply(new BigDecimal(workingDaysNoturno)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias úteis vezes o número de horas noturnas no mês.
                            tabelaCalculoFolha.setValueAt((valorReferenciaHoraNoturna), i, 2);
                            BigDecimal valorTotalHoraNoturna = valorReferenciaHoraNoturna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                            tabelaCalculoFolha.setValueAt((valorTotalHoraNoturna), i, 3);
                        }

                    } else {

                        LocalTime horaIniNot = LocalTime.parse("22:00");
                        LocalTime horaFimNot = LocalTime.parse(horaSaida.getText());
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

                            /*--------------------Este trecho faz a contagem de dias úteis no mês--------------------*/
                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);
                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            int workingDaysNoturno = 0;
                            YearMonth anoMes = YearMonth.of(year, month);
                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));

                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                    workingDaysNoturno++;
                                }
                            }
                            /*------------------------------------------------------------------------------------*/
                            valorReferenciaHoraNoturna = valorHoraDepoisDas22.multiply(new BigDecimal(workingDaysNoturno)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias úteis vezes o número de horas noturnas no mês.
                            tabelaCalculoFolha.setValueAt((valorReferenciaHoraNoturna), i, 2);
                            BigDecimal valorTotalHoraNoturna = valorReferenciaHoraNoturna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                            tabelaCalculoFolha.setValueAt((valorTotalHoraNoturna), i, 3);

                        } else {
                            /*Aqui é feito o cálculo quando a hora de saída é antes da meia noite*/
                            Duration das22AteOFimNot = Duration.between(horaIniNot, horaFimNot);
                            LocalTime diferencadas22AteOFim = LocalTime.ofNanoOfDay(das22AteOFimNot.toNanos());// Aqui tenho a hora noturna de um dia
                            int horasN = diferencadas22AteOFim.getHour(); // Aqui pego o valor das horas menos o horário de almoço/janta
                            int minutosN = diferencadas22AteOFim.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal
                            String hourNot = String.valueOf(horasN) + "." + String.valueOf(minutosN);
                            BigDecimal valorHoraDepoisDas22 = new BigDecimal(hourNot).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                            /*--------------------Este trecho faz a contagem de dias úteis no mês--------------------*/
                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);
                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            int workingDaysNoturno = 0;
                            YearMonth anoMes = YearMonth.of(year, month);
                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));
                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                    workingDaysNoturno++;
                                }
                            }
                            /*------------------------------------------------------------------------------------*/
                            valorReferenciaHoraNoturna = valorHoraDepoisDas22.multiply(new BigDecimal(workingDaysNoturno)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias úteis vezes o número de horas noturnas no mês.
                            tabelaCalculoFolha.setValueAt((valorReferenciaHoraNoturna), i, 2);
                            BigDecimal valorTotalHoraNoturna = valorReferenciaHoraNoturna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                            tabelaCalculoFolha.setValueAt((valorTotalHoraNoturna), i, 3);
                        }
                    }
                }

                case 14 -> {
                    //Calculando o Adicional Noturno
                    BigDecimal referenciaAdicioNoturno = valorReferenciaHoraNoturna.add(valorReferenciaDsrHoraNoturna);
                    tabelaCalculoFolha.setValueAt((referenciaAdicioNoturno), i, 2);
                    BigDecimal adicionalNoturno = (referenciaAdicioNoturno.multiply(new BigDecimal(salarioHora.getText()))).multiply(new BigDecimal(valorHorasAdiNoturn.getText()).divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt((adicionalNoturno), i, 3);
                }

                case 17 -> {
                    //Calculando o Pro-Labore
                    BigDecimal proLabore = salarioBase;
                    tabelaCalculoFolha.setValueAt(proLabore, i, 3);
                }

                case 19 -> //Calculando Bolsa Auxílio
                        tabelaCalculoFolha.setValueAt(salarioBase, i, 3);

                case 25 -> {
                    //Calculando Horas Repouso Remunerado Noturno

                    if (tipoDeJornada.getSelectedItem().equals("12 x 36")) {

                        LocalTime horaIniRepRemNot = LocalTime.parse("22:00");
                        LocalTime horaFimRepRemNot = LocalTime.parse(horaSaida.getText());
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
                            tabelaCalculoFolha.setValueAt((valorReferenciaDsrHoraNoturna), i, 2);
                            BigDecimal dsrHoraNoturnaRepRemNot = valorReferenciaDsrHoraNoturna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                            tabelaCalculoFolha.setValueAt((dsrHoraNoturnaRepRemNot), i, 3);
                        } else {

                            Duration das22AteOFimRepRemNot = Duration.between(horaIniRepRemNot, horaFimRepRemNot);
                            LocalTime diferencadas22AteOFimRepRemNot = LocalTime.ofNanoOfDay(das22AteOFimRepRemNot.toNanos());// Aqui tenho a Diferença de horas Noturnas

                            int horasNotDsr = diferencadas22AteOFimRepRemNot.getHour(); // Aqui pego o valor das horas
                            int minutosNotDsr = diferencadas22AteOFimRepRemNot.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                            String hourNotDsr2 = String.valueOf(horasNotDsr) + "." + String.valueOf(minutosNotDsr);
                            BigDecimal valorHoraDepoisDas22RepRemNot = new BigDecimal(hourNotDsr2).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas

                            /*--------------------Este trecho faz a contagem de dias úteis no mês--------------------*/
                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);
                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            int workingDaysNotRepRemNot = 0;
                            YearMonth anoMes = YearMonth.of(year, month);
                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));

                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                    workingDaysNotRepRemNot++;
                                }
                            }
                            valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal(workingDaysNotRepRemNot)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
                            tabelaCalculoFolha.setValueAt((valorReferenciaDsrHoraNoturna), i, 2);
                            BigDecimal dsrHoraNoturnaRepRemNot = valorReferenciaDsrHoraNoturna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                            tabelaCalculoFolha.setValueAt((dsrHoraNoturnaRepRemNot), i, 3);
                        }
                    } else {
                        LocalTime horaIniNotRepRemNot = LocalTime.parse("22:00");
                        LocalTime horaFimNotRepRemNot = LocalTime.parse(horaSaida.getText());
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
                            /*--------------------Este trecho faz a contagem de dias úteis no mês--------------------*/
                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);
                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            int workingDaysNotRepRemNot = 0;
                            YearMonth anoMes = YearMonth.of(year, month);
                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));

                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                    workingDaysNotRepRemNot++;
                                }
                            }
                            valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal(workingDaysNotRepRemNot)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
                            tabelaCalculoFolha.setValueAt((valorReferenciaDsrHoraNoturna), i, 2);
                            BigDecimal dsrHoraNoturnaRepRemNot = valorReferenciaDsrHoraNoturna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                            tabelaCalculoFolha.setValueAt((dsrHoraNoturnaRepRemNot), i, 3);
                        } else {

                            Duration das22AteOFimRepRemNot = Duration.between(horaIniNotRepRemNot, horaFimNotRepRemNot);
                            LocalTime diferencadas22AteOFimRepRemNot = LocalTime.ofNanoOfDay(das22AteOFimRepRemNot.toNanos());// Aqui tenho a Diferença de horas Noturnas;

                            int horasNotDsrRepRemNot = diferencadas22AteOFimRepRemNot.getHour(); // Aqui pego o valor das horas
                            int minutosNotDsrRepRemNot = diferencadas22AteOFimRepRemNot.getMinute() / 60; // Aqui pego o valor dos minutos e transformo em centesimal

                            String hourNotDsrRepRemNot = String.valueOf(horasNotDsrRepRemNot) + "." + String.valueOf(minutosNotDsrRepRemNot);
                            BigDecimal valorHoraDepoisDas22RepRemNot = new BigDecimal(hourNotDsrRepRemNot).multiply(new BigDecimal("1.142857")).setScale(2, RoundingMode.HALF_UP); // Aqui é encontrado a quantidade da horas noturnas
                            /*--------------------Este trecho faz a contagem de dias úteis no mês--------------------*/
                            anoAtual = new Date();
                            String leAno = pegaAno.format(anoAtual);
                            String leMes = pegaMes.format(anoAtual);
                            int year = Integer.parseInt(leAno);
                            int month = Integer.parseInt(leMes);
                            int workingDaysNotRepRemNot = 0;
                            YearMonth anoMes = YearMonth.of(year, month);
                            feriados.addAll(calend.getFeriadosFixos(year));
                            feriados.addAll(calend.getFeriadosMoveis(year));

                            for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                                LocalDate data = anoMes.atDay(dia);

                                if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                                    workingDaysNotRepRemNot++;
                                }
                            }
                            valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal(workingDaysNotRepRemNot)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
                            tabelaCalculoFolha.setValueAt((valorReferenciaDsrHoraNoturna), i, 2);
                            BigDecimal dsrHoraNoturnaRepRemNot = valorReferenciaDsrHoraNoturna.multiply(new BigDecimal(salarioHora.getText())).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                            tabelaCalculoFolha.setValueAt((dsrHoraNoturnaRepRemNot), i, 3);
                        }
                    }
                }

                case 26 -> {
                    //Calculando DSR Sobre Hora Extra Diurna 50%
                    /*-------------------------Calculando Horas Diurnas Úteis---------------------------*/
                    anoAtual = new Date();
                    String leAno50 = pegaAno.format(anoAtual);
                    String leMes50 = pegaMes.format(anoAtual);
                    int year = Integer.parseInt(leAno50);
                    int month = Integer.parseInt(leMes50);
                    int workingDaysDiurn50 = 0;
                    int workingDaysNaoDiurn50 = 0;
                    YearMonth anoMes50 = YearMonth.of(year, month);
                    feriados.addAll(calend.getFeriadosFixos(year));
                    feriados.addAll(calend.getFeriadosMoveis(year));
                    for (int dia = 1; dia <= anoMes50.lengthOfMonth(); dia++) {
                        LocalDate data = anoMes50.atDay(dia);

                        if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                            workingDaysDiurn50++;
                        }
                    }
                    /*-------------------------Calculando Horas Diurnas Não Úteis---------------------------*/
                    for (int diaNUt = 1; diaNUt <= anoMes50.lengthOfMonth(); diaNUt++) {
                        LocalDate dataNUt = anoMes50.atDay(diaNUt);

                        if (dataNUt.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(dataNUt)) {
                            workingDaysNaoDiurn50++;
                        }
                        /*----------------------------------------------------------------------------------*/
                    }
                    BigDecimal dsrSobHExtrDiurna = (hExrtra50.divide(new BigDecimal(workingDaysDiurn50), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(workingDaysNaoDiurn50));
                    tabelaCalculoFolha.setValueAt((hExrtra50.divide(new BigDecimal(workingDaysDiurn50))), i, 2);
                    tabelaCalculoFolha.setValueAt((dsrSobHExtrDiurna), i, 3);
                }

                case 27 -> {
                    //Calculando DSR Sobre Hora Extra Diurna 70%
                    /*-------------------------Calculando Horas Diurnas Úteis---------------------------*/
                    anoAtual = new Date();
                    String leAno70 = pegaAno.format(anoAtual);
                    String leMes70 = pegaMes.format(anoAtual);
                    int year70 = Integer.parseInt(leAno70);
                    int month70 = Integer.parseInt(leMes70);
                    int workingDaysDiurn70 = 0;
                    int workingDaysNaoDiurn70 = 0;
                    YearMonth anoMes = YearMonth.of(year70, month70);
                    feriados.addAll(calend.getFeriadosFixos(year70));
                    feriados.addAll(calend.getFeriadosMoveis(year70));
                    for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                        LocalDate data = anoMes.atDay(dia);

                        if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                            workingDaysDiurn70++;
                        }
                    }
                    /*-------------------------Calculando Horas Diurnas Não Úteis---------------------------*/
                    for (int diaNUt = 1; diaNUt <= anoMes.lengthOfMonth(); diaNUt++) {
                        LocalDate dataNUt = anoMes.atDay(diaNUt);

                        if (dataNUt.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(dataNUt)) {
                            workingDaysNaoDiurn70++;
                        }
                        /*----------------------------------------------------------------------------------*/
                    }
                    BigDecimal dsrSobHExtrDiurna70 = (hExrtra70.divide(new BigDecimal(workingDaysDiurn70), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(workingDaysNaoDiurn70));
                    tabelaCalculoFolha.setValueAt((hExrtra70.divide(new BigDecimal(workingDaysDiurn70))), i, 2);
                    tabelaCalculoFolha.setValueAt((dsrSobHExtrDiurna70), i, 3);
                }

                case 28 -> {
                    //Calculando DSR Sobre Hora Extra Diurna 100%
                    /*-------------------------Calculando Horas Diurnas Úteis---------------------------*/
                    anoAtual = new Date();
                    String leAno100 = pegaAno.format(anoAtual);
                    String leMes100 = pegaMes.format(anoAtual);
                    int year100 = Integer.parseInt(leAno100);
                    int month100 = Integer.parseInt(leMes100);
                    int workingDaysDiurn100 = 0;
                    int workingDaysNaoDiurn100 = 0;
                    YearMonth anoMes100 = YearMonth.of(year100, month100);
                    feriados.addAll(calend.getFeriadosFixos(year100));
                    feriados.addAll(calend.getFeriadosMoveis(year100));
                    for (int dia = 1; dia <= anoMes100.lengthOfMonth(); dia++) {
                        LocalDate data = anoMes100.atDay(dia);

                        if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                            workingDaysDiurn100++;
                        }
                    }
                    /*-------------------------Calculando Horas Diurnas Não Úteis---------------------------*/
                    for (int diaNUt = 1; diaNUt <= anoMes100.lengthOfMonth(); diaNUt++) {
                        LocalDate dataNUt = anoMes100.atDay(diaNUt);

                        if (dataNUt.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(dataNUt)) {
                            workingDaysNaoDiurn100++;
                        }
                        /*----------------------------------------------------------------------------------*/
                    }
                    BigDecimal dsrSobHExtrDiurna100 = (hExrtra100.divide(new BigDecimal(workingDaysDiurn100))).multiply(new BigDecimal(workingDaysNaoDiurn100)).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt((hExrtra100.divide(new BigDecimal(workingDaysDiurn100))).setScale(2, RoundingMode.HALF_UP), i, 2);
                    tabelaCalculoFolha.setValueAt((dsrSobHExtrDiurna100), i, 3);
                }

                case 46 -> {
                    //Calculando a Insalubridade
                    obtemSalarioMin();
                    BigDecimal porceInsalubre = new BigDecimal(insalubridade.getText());
                    BigDecimal valInsalubre = (salarioMinimo.multiply(porceInsalubre)).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt((valInsalubre), i, 3);
                }

                case 47 -> {
                    //Calculando a Periculosidade
                    BigDecimal porcentoPericuloso = new BigDecimal(periculosidade.getText());
                    BigDecimal valorPericuloso = (salarioBase.multiply(porcentoPericuloso)).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt(valorPericuloso, i, 3);
                }

                case 51 -> {
                    //Calculando a Comissão
                    BigDecimal comissao = new BigDecimal(valorDaComissao.getText());
                    BigDecimal vendasMes = new BigDecimal(valorVendasMes.getText());
                    BigDecimal valorComissao = (comissao.multiply(vendasMes)).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt(vendasMes, i, 2);
                    tabelaCalculoFolha.setValueAt(valorComissao, i, 3);
                }

                case 53 -> {
                    //Calculando a Gratificação
                    BigDecimal valorGratifica = new BigDecimal(valorDaGratificacao.getText());
                    tabelaCalculoFolha.setValueAt(valorGratifica, i, 3);
                }

                case 54 -> {
                    //Calculando a Quebra Caixa
                    BigDecimal valorQuebra = ((new BigDecimal(valorQuebraCaix.getText())).multiply(valorReferenHoraDiurna)).divide(new BigDecimal(valorHorasPorMes.getText())).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt(valorQuebra, i, 3);
                }

                case 98 -> {
                    //Quantidade de horas extras 50% feitas no mês.
                    if (insalubridade.getText().equals("0.00")) {

                        BigDecimal totalHoraExtra50 = new BigDecimal(valorHoraExtra50.getText());
                        tabelaCalculoFolha.setValueAt((totalHoraExtra50), i, 2);
                        hExrtra50 = (salarioPorHora.multiply(new BigDecimal("1.5"))).multiply(totalHoraExtra50);
                        tabelaCalculoFolha.setValueAt((hExrtra50), i, 3);

                    } else {

                        BigDecimal horasTrabNoMes = new BigDecimal(valorHorasPorMes.getText());
                        obtemSalarioMin();
                        BigDecimal porcentoInsalubre = new BigDecimal(insalubridade.getText()); //Pega o percentual insalubre
                        BigDecimal valorInsalubre = ((salarioMinimo.multiply(porcentoInsalubre)).divide(new BigDecimal("100"))).divide(horasTrabNoMes).setScale(2, RoundingMode.HALF_UP); // Calcula o valor da insalubridade e divide o valor pelas hora strabalhadas no mês

                        BigDecimal totalHoraExtra50 = new BigDecimal(valorHoraExtra50.getText());
                        tabelaCalculoFolha.setValueAt((totalHoraExtra50), i, 2);
                        hExrtra50 = ((salarioPorHora.add(valorInsalubre)).multiply(new BigDecimal("1.5"))).multiply(totalHoraExtra50).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt((hExrtra50), i, 3);
                    }
                }

                case 99 -> {
                    //Quantidade de horas extras 70% feitas no mês.
                    if (insalubridade.getText().equals("0.00")) {

                        BigDecimal totalHoraExtra70 = new BigDecimal(valorHoraExtra70.getText());
                        tabelaCalculoFolha.setValueAt((totalHoraExtra70), i, 2);
                        hExrtra70 = (salarioPorHora.multiply(new BigDecimal("1.7"))).multiply(totalHoraExtra70).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt((hExrtra70), i, 3);

                    } else {

                        BigDecimal horasTrabNoMes = new BigDecimal(valorHorasPorMes.getText());
                        obtemSalarioMin();
                        BigDecimal porcentoInsalubre = new BigDecimal(insalubridade.getText()); //Pega o percentual insalubre
                        BigDecimal valorInsalubre = ((salarioMinimo.multiply(porcentoInsalubre)).divide(new BigDecimal("100"))).divide(horasTrabNoMes, 2, RoundingMode.HALF_UP); // Calcula o valor da insalubridade e divide o valor pelas hora strabalhadas no mês

                        BigDecimal totalHoraExtra70 = new BigDecimal(valorHoraExtra70.getText());
                        tabelaCalculoFolha.setValueAt((totalHoraExtra70), i, 2);
                        hExrtra70 = ((salarioPorHora.add(valorInsalubre)).multiply(new BigDecimal("1.7"))).multiply(totalHoraExtra70).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt((hExrtra70), i, 3);

                    }
                }

                case 100 -> {
                    //Quantidade de horas extras 100% feitas no mês.
                    if (insalubridade.getText().equals("0.00")) {

                        BigDecimal totalHoraExtra100 = new BigDecimal(valorHoraExtra100.getText());
                        tabelaCalculoFolha.setValueAt((totalHoraExtra100), i, 2);
                        hExrtra100 = (salarioPorHora.multiply(new BigDecimal("2"))).multiply(totalHoraExtra100);
                        tabelaCalculoFolha.setValueAt((hExrtra100), i, 3);

                    } else {

                        BigDecimal horasTrabNoMes = new BigDecimal(valorHorasPorMes.getText());
                        obtemSalarioMin();
                        BigDecimal porcentoInsalubre = new BigDecimal(insalubridade.getText()); //Pega o percentual insalubre
                        BigDecimal valorInsalubre = ((salarioMinimo.multiply(porcentoInsalubre)).divide(new BigDecimal(100))).divide(horasTrabNoMes).setScale(2, RoundingMode.HALF_UP); // Calcula o valor da insalubridade e divide o valor pelas hora strabalhadas no mês

                        BigDecimal totalHoraExtra100 = new BigDecimal(valorHoraExtra100.getText());
                        tabelaCalculoFolha.setValueAt((totalHoraExtra100), i, 2);
                        hExrtra100 = ((salarioPorHora.add(valorInsalubre)).multiply(new BigDecimal(2))).multiply(totalHoraExtra100).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt((hExrtra100), i, 3);
                    }
                }

                case 101 -> {
                    //Calculando o Salário Maternidade
                    BigDecimal salarioMaternidade = salarioBase;
                    tabelaCalculoFolha.setValueAt(salarioMaternidade, i, 3);
                }

                case 102 -> {
                    //Calculando Média Horas Extras 50% Sobre Salário Maternidade
                    int numMatriMHex50SlMater = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    ConexaoBD connectFolhaMHex50SlMater = new ConexaoBD();
                    connectFolhaMHex50SlMater.conecta();
                    try {
                        ResultSet rstMHex50SlMater;
                        try ( PreparedStatement psMHex50SlMater = connectFolhaMHex50SlMater.conexao.prepareStatement("Select Avg(valorProvento) as 'mediHorasExtrasSmatern' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? order by idEvento desc limit 6")) {
                            psMHex50SlMater.setInt(1, numMatriMHex50SlMater);
                            psMHex50SlMater.setInt(2, Integer.parseInt("98"));// Mudar aqui quando a lista for atualizada
                            rstMHex50SlMater = psMHex50SlMater.executeQuery();
                            if (rstMHex50SlMater.next()) {
                                medHextSalMater50 = rstMHex50SlMater.getBigDecimal("mediHorasExtrasSmatern");
                            }
                        }
                        rstMHex50SlMater.close();
                        BigDecimal calcMedHExtSMatern = salarioPorHora.multiply(medHextSalMater50).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calcMedHExtSMatern, i, 2);
                        BigDecimal toalHexSalMatern50 = calcMedHExtSMatern.add(calcMedHExtSMatern.multiply(new BigDecimal("0.5"))).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(toalHexSalMatern50, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média Horas Extra 50% sobre o Salário Maternidade " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMHex50SlMater.desconecta();
                }

                case 103 -> {
                    //Calculando Média Horas Extras 70% Sobre Salário Maternidade
                    int numMatriMHex70SlMater = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    ConexaoBD connectFolhaMHex70SlMater = new ConexaoBD();
                    connectFolhaMHex70SlMater.conecta();
                    try {
                        ResultSet rstMHex70SlMater;
                        try ( PreparedStatement psMHex70SlMater = connectFolhaMHex70SlMater.conexao.prepareStatement("Select Avg(valorProvento) as 'mediHorasExtras70Smatern' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? order by idEvento desc limit 6")) {
                            psMHex70SlMater.setInt(1, numMatriMHex70SlMater);
                            psMHex70SlMater.setInt(2, Integer.parseInt("99"));// Mudar aqui quando a lista for atualizada
                            rstMHex70SlMater = psMHex70SlMater.executeQuery();
                            if (rstMHex70SlMater.next()) {
                                medHextSalMater70 = rstMHex70SlMater.getBigDecimal("mediHorasExtras70Smatern");
                            }
                        }
                        rstMHex70SlMater.close();
                        BigDecimal calcMedHExt70SMatern = salarioPorHora.multiply(medHextSalMater70).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calcMedHExt70SMatern, i, 2);
                        BigDecimal toalHexSalMatern70 = calcMedHExt70SMatern.add(calcMedHExt70SMatern.multiply(new BigDecimal("0.7"))).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(toalHexSalMatern70, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média Horas Extra 70% sobre o Salário Maternidade " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMHex70SlMater.desconecta();
                }

                case 104 -> {
                    //Calculando Média Horas Extras 100% Sobre Salário Maternidade
                    int numMatriMHex100SlMater = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    ConexaoBD connectFolhaMHex100SlMater = new ConexaoBD();
                    connectFolhaMHex100SlMater.conecta();
                    try {
                        ResultSet rstMHex100SlMater;
                        try ( PreparedStatement psMHex100SlMater = connectFolhaMHex100SlMater.conexao.prepareStatement("Select Avg(valorProvento) as 'mediHorasExtras100Smatern' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? order by idEvento desc limit 6")) {
                            psMHex100SlMater.setInt(1, numMatriMHex100SlMater);
                            psMHex100SlMater.setInt(2, Integer.parseInt("100"));// Mudar aqui quando a lista for atualizada
                            rstMHex100SlMater = psMHex100SlMater.executeQuery();
                            if (rstMHex100SlMater.next()) {
                                medHextSalMater100 = rstMHex100SlMater.getBigDecimal("mediHorasExtras100Smatern");
                            }
                        }
                        rstMHex100SlMater.close();
                        BigDecimal calcMedHExt100SMatern = salarioPorHora.multiply(medHextSalMater100).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calcMedHExt100SMatern, i, 2);
                        BigDecimal toalHexSalMatern100 = calcMedHExt100SMatern.add(calcMedHExt100SMatern.multiply(new BigDecimal("1"))).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(toalHexSalMatern100, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média Horas Extra 100% sobre o Salário Maternidade " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMHex100SlMater.desconecta();
                }

                case 105 -> {
                    //Calculando Média de DSR Diurno Sobre Salário Maternidade
                    int numMatriDsrDiurSlMat = Integer.parseInt(numerMatric.getText());
                    ConexaoBD connectFolhaDsrDiurSlMat = new ConexaoBD();
                    connectFolhaDsrDiurSlMat.conecta();
                    try {
                        ResultSet rstDsrDiurSlMat;
                        try ( PreparedStatement psDsrDiurSlMat = connectFolhaDsrDiurSlMat.conexao.prepareStatement("Select AVG(valorProvento) as 'somaValorDsrDiurSalMatern' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? order by idEvento desc limit 6")) {
                            psDsrDiurSlMat.setInt(1, numMatriDsrDiurSlMat);
                            psDsrDiurSlMat.setInt(2, Integer.parseInt("5")); // Trocar esse número quando for atualizar a planilha com os números dos eventos
                            rstDsrDiurSlMat = psDsrDiurSlMat.executeQuery();
                            if (rstDsrDiurSlMat.next()) {
                                mediaValorDsrDiuSalMatern = rstDsrDiurSlMat.getBigDecimal("somaValorDsrDiurSalMatern");
                            }
                        }
                        rstDsrDiurSlMat.close();
                        tabelaCalculoFolha.setValueAt(mediaValorDsrDiuSalMatern, i, 2);
                        tabelaCalculoFolha.setValueAt(mediaValorDsrDiuSalMatern, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de DSR Diurno Sobre 2° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaDsrDiurSlMat.desconecta();
                }

                case 106 -> {
                    //Calculando Média de DSR Noturno Sobre Salário Maternidade
                    int numMatriDsrNotSlMat = Integer.parseInt(numerMatric.getText());
                    ConexaoBD connectFolhaDsrNotSlMat = new ConexaoBD();
                    connectFolhaDsrNotSlMat.conecta();
                    try {
                        ResultSet rstDsrNotSlMat;
                        try ( PreparedStatement psDsrNotSlMat = connectFolhaDsrNotSlMat.conexao.prepareStatement("Select AVG(valorProvento) as 'somaValorDsrNoturnSalMatern' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? order by idEvento desc limit 6")) {
                            psDsrNotSlMat.setInt(1, numMatriDsrNotSlMat);
                            psDsrNotSlMat.setInt(2, Integer.parseInt("25")); // Trocar esse número quando for atualizar a planilha com os números dos eventos
                            rstDsrNotSlMat = psDsrNotSlMat.executeQuery();
                            if (rstDsrNotSlMat.next()) {
                                mediaValorDsrNotSalmatern = rstDsrNotSlMat.getBigDecimal("somaValorDsrNoturnSalMatern");
                            }
                        }
                        rstDsrNotSlMat.close();
                        tabelaCalculoFolha.setValueAt(mediaValorDsrNotSalmatern, i, 2);
                        tabelaCalculoFolha.setValueAt(mediaValorDsrNotSalmatern, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de DSR Noturno Sobre 2° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaDsrNotSlMat.desconecta();
                }

                case 107 -> {
                    //Média Adicional Noturno Sobre Salário Maternidade
                    int numMatriMAdNotSMater = Integer.parseInt(numerMatric.getText());
                    ConexaoBD connectFolhaMAdNotSMater = new ConexaoBD();
                    connectFolhaMAdNotSMater.conecta();
                    try {
                        ResultSet rstMAdNotSMater;
                        try ( PreparedStatement psMAdNotSMater = connectFolhaMAdNotSMater.conexao.prepareStatement("Select AVG(valorProvento) as 'mediaAdicNotSalMatern' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? order by idEvento desc limit 6")) {
                            psMAdNotSMater.setInt(1, numMatriMAdNotSMater);
                            psMAdNotSMater.setInt(2, Integer.parseInt("14"));// Mudar aqui quando a lista for atualizada
                            rstMAdNotSMater = psMAdNotSMater.executeQuery();
                            if (rstMAdNotSMater.next()) {
                                mediaAdicNoturnSalMatern = rstMAdNotSMater.getBigDecimal("mediaAdicNotSalMatern");
                            }
                        }
                        rstMAdNotSMater.close();
                        BigDecimal calcAdiciNoturSalMatern = mediaAdicNoturnSalMatern.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal calculoAdiciNoturSalMatern = calcAdiciNoturSalMatern.add(calcAdiciNoturSalMatern.multiply(new BigDecimal("0.2"))).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calcAdiciNoturSalMatern, i, 2);
                        tabelaCalculoFolha.setValueAt(calculoAdiciNoturSalMatern, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Adicional Noturno Sobre Salário Maternidade " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMAdNotSMater.desconecta();
                }

                case 130 -> {
                    //Calculando Ajuda de Custo
                    BigDecimal ajudaCost = new BigDecimal(ajudaDeCusto.getText());
                    tabelaCalculoFolha.setValueAt(ajudaCost, i, 3);
                }

                case 133 -> {
                    //Calculando Salário Família

                    BigDecimal valorDoSalarioBase = salarioBase;

                    int filhos = Integer.parseInt(dependentesIrrf.getText());
                    BigDecimal cota1, cota2, val1, val2;
                    BigDecimal valorTotal = new BigDecimal("0");
                    BigDecimal valorSalFam = new BigDecimal("0");
                    ConexaoBD conSalFam = new ConexaoBD();
                    conSalFam.conecta();
                    try {
                        ResultSet rSalFam;
                        try ( PreparedStatement pStm = conSalFam.conexao.prepareStatement("Select * from rhu_sfml01")) {
                            rSalFam = pStm.executeQuery();

                            if (rSalFam.next()) {

                                val1 = rSalFam.getBigDecimal("faixa1");
                                val2 = rSalFam.getBigDecimal("faixa2");
                                cota1 = rSalFam.getBigDecimal("cota1");
                                cota2 = rSalFam.getBigDecimal("cota2");

                                // Usando compareTo para realizar comparações
                                if (valorDoSalarioBase.compareTo(val2) > 0 && valorDoSalarioBase.compareTo(val1) <= 0) {
                                    valorTotal = cota1;
                                } else if (valorDoSalarioBase.compareTo(val2) <= 0) {
                                    valorTotal = cota2;
                                }

                                valorSalFam = valorTotal.multiply(BigDecimal.valueOf(filhos)).setScale(2, RoundingMode.HALF_UP);
                            } else {
                            }
                        }
                        rSalFam.close();
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular salário família: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conSalFam.desconecta();
                    tabelaCalculoFolha.setValueAt(valorSalFam, i, 3);
                }

                case 167 -> {
                    //Calculando Primeira Parcela 13°
                    BigDecimal decimoTerc1Parcel = salarioBase.multiply(new BigDecimal("0.5"));
                    tabelaCalculoFolha.setValueAt(decimoTerc1Parcel, i, 3);
                }

                case 168 -> {
                    //Calculando Média de Horas Extras 50% Sobre 1° Parcela do 13°
                    int numMatri50 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtual50 = pegaMes.format(anoAtual);
                    String dataAnoAtual50 = pegaAno.format(anoAtual);
                    int validaMes50 = Integer.parseInt(dataMesAtual50) - 1;
                    String validaMes250 = String.valueOf(validaMes50);
                    ConexaoBD connectFolha50 = new ConexaoBD();
                    connectFolha50.conecta();
                    try {
                        ResultSet rst50;
                        try ( PreparedStatement pstment50 = connectFolha50.conexao.prepareStatement("Select Sum(valorProvento) as 'media50Ano' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstment50.setInt(1, numMatri50);
                            pstment50.setInt(2, Integer.parseInt("98"));// Mudar aqui quando a lista for atualizada
                            pstment50.setDate(3, java.sql.Date.valueOf(dataAnoAtual50));
                            pstment50.setDate(4, java.sql.Date.valueOf(validaMes250));
                            rst50 = pstment50.executeQuery();
                            if (rst50.next()) {
                                media50 = rst50.getBigDecimal("media50Ano");
                            }
                        }
                        rst50.close();
                        BigDecimal calc50MedSob13 = (media50.divide(new BigDecimal(validaMes50))).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calc50MedSob13, i, 2);
                        BigDecimal valorMedia50HoraExtra = ((calc50MedSob13.add(calc50MedSob13.multiply(new BigDecimal("0.5")))).multiply(new BigDecimal("50"))).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(valorMedia50HoraExtra, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Horas Extras 50% Sobre 1° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolha50.desconecta();
                }

                case 169 -> {
                    //Calculando Média de Horas Extras 70% Sobre 1° Parcela do 13°
                    int numMatri70 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtual70 = pegaMes.format(anoAtual);
                    String dataAnoAtual70 = pegaAno.format(anoAtual);
                    BigDecimal validaMes70 = new BigDecimal(dataMesAtual70).subtract(new BigDecimal("1.0"));
                    String validaMes270 = String.valueOf(validaMes70);
                    ConexaoBD connectFolha70 = new ConexaoBD();
                    connectFolha70.conecta();
                    try {
                        ResultSet rst70;
                        try ( PreparedStatement pstment70 = connectFolha70.conexao.prepareStatement("Select Sum(valorProvento) as 'media70Ano' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstment70.setInt(1, numMatri70);
                            pstment70.setInt(2, Integer.parseInt("99"));// Mudar aqui quando a lista for atualizada
                            pstment70.setDate(3, java.sql.Date.valueOf(dataAnoAtual70));
                            pstment70.setDate(4, java.sql.Date.valueOf(validaMes270));
                            rst70 = pstment70.executeQuery();
                            if (rst70.next()) {
                                media70 = rst70.getBigDecimal("media70Ano");
                            }
                        }
                        rst70.close();
                        BigDecimal calc70MedSob13 = (media70.divide(validaMes70)).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calc70MedSob13, i, 2);
                        BigDecimal valorMedia70HoraExtra = ((calc70MedSob13.add(calc70MedSob13.multiply(new BigDecimal("0.7")))).multiply(new BigDecimal("50"))).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(valorMedia70HoraExtra, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Horas Extras 70% Sobre 1° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolha70.desconecta();
                }

                case 170 -> {
                    //Calculando Média de Horas Extras 100% Sobre 1° Parcela do 13°
                    int numMatri100 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtual100 = pegaMes.format(anoAtual);
                    String dataAnoAtual100 = pegaAno.format(anoAtual);
                    BigDecimal validaMes100 = new BigDecimal(dataMesAtual100).subtract(new BigDecimal("1.0"));
                    String validaMes2100 = String.valueOf(validaMes100);
                    ConexaoBD connectFolha100 = new ConexaoBD();
                    connectFolha100.conecta();
                    try {
                        ResultSet rst100;
                        try ( PreparedStatement pstment100 = connectFolha100.conexao.prepareStatement("Select Sum(valorProvento) as 'media100Ano' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstment100.setInt(1, numMatri100);
                            pstment100.setInt(2, Integer.parseInt("100"));// Mudar aqui quando a lista for atualizada
                            pstment100.setDate(3, java.sql.Date.valueOf(dataAnoAtual100));
                            pstment100.setDate(4, java.sql.Date.valueOf(validaMes2100));
                            rst100 = pstment100.executeQuery();
                            if (rst100.next()) {
                                media100 = rst100.getBigDecimal("media100Ano");
                            }
                        }
                        rst100.close();
                        BigDecimal calc100MedSob13 = (media100.divide(validaMes100)).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calc100MedSob13, i, 2);
                        BigDecimal valorMedia100HoraExtra = ((calc100MedSob13.add(calc100MedSob13.multiply(new BigDecimal("1")))).multiply(new BigDecimal("50"))).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(valorMedia100HoraExtra, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Horas Extras 100% Sobre 1° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolha100.desconecta();
                }

                case 171 -> //Calculando Décimo terceiro Adiantado
                        tabelaCalculoFolha.setValueAt(salarioBase, i, 3);

                case 176 -> {
                    //Calculando Média de DSR Diurno Sobre 1° Parcela do 13°
                    int numMatriDsrD13 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataAnoAtualDsrD13 = pegaAno.format(anoAtual); //pega o ano atual yyyy
                    String dataMesAtualDsrD13 = pegaMes.format(anoAtual); //pega o mês atual MM
                    BigDecimal validaMesDsrD13 = new BigDecimal(dataMesAtualDsrD13).subtract(new BigDecimal("1.0"));
                    String validaMes2DsrD13 = String.valueOf(validaMesDsrD13); //Aqui eu transformo a subtração em cima em String para poder fazer o cálculo correto da data que serias 10.
                    ConexaoBD connectFolhaDsrD13 = new ConexaoBD();
                    connectFolhaDsrD13.conecta();
                    try {
                        ResultSet rstDsrD13;
                        try ( PreparedStatement pstment = connectFolhaDsrD13.conexao.prepareStatement("Select Sum(valorProvento) as 'somaValorDsrDiur' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstment.setInt(1, numMatriDsrD13);
                            pstment.setInt(2, Integer.parseInt("5")); // Trocar esse número quando for atualizar a planilha com os números dos eventos
                            pstment.setDate(3, java.sql.Date.valueOf(dataAnoAtualDsrD13));
                            pstment.setDate(4, java.sql.Date.valueOf(validaMes2DsrD13));
                            rstDsrD13 = pstment.executeQuery();
                            if (rstDsrD13.next()) {
                                mediaValorDsr = rstDsrD13.getBigDecimal("somaValorDsrDiur");
                            }
                        }
                        rstDsrD13.close();
                        BigDecimal calcMedDsrSob13 = mediaValorDsr.divide(validaMesDsrD13).setScale(2, RoundingMode.HALF_UP); //Cálculo da média DSR Diruno de janeiro a outubro (no caso fazendo a conta em novembro para pagamento da 1° parc. do 13°.
                        tabelaCalculoFolha.setValueAt(calcMedDsrSob13, i, 2);
                        BigDecimal valorTotalDsrDiurno = calcMedDsrSob13.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP); // Aqui divide o resultado Obtido pela Metade.
                        tabelaCalculoFolha.setValueAt(valorTotalDsrDiurno, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de DSR Diurno Sobre 1° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaDsrD13.desconecta();
                }

                case 177 -> {
                    //Calculando Média de DSR Noturno Sobre 1° Parcela do 13°
                    int numMatriDsrN13 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataAnoAtualDsrN13 = pegaAno.format(anoAtual); //pega o ano atual yyyy
                    String dataMesAtualDsrN13 = pegaMes.format(anoAtual); //pega o mês atual MM
                    BigDecimal validaMesDsrN13 = new BigDecimal(dataMesAtualDsrN13).subtract(new BigDecimal("1.0"));
                    String validaMes2DsrN13 = String.valueOf(validaMesDsrN13); //Aqui eu transformo a subtração em cima em String para poder fazer o cálculo correto da data que serias 10.
                    ConexaoBD connectFolhaDsrN13 = new ConexaoBD();
                    connectFolhaDsrN13.conecta();
                    try {
                        ResultSet rstDsrN13;
                        try ( PreparedStatement pstmentDsrN13 = connectFolhaDsrN13.conexao.prepareStatement("Select Sum(valorProvento) as 'mediaValorDsrNotur' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstmentDsrN13.setInt(1, numMatriDsrN13);
                            pstmentDsrN13.setInt(2, Integer.parseInt("25")); // Trocar esse número quando for atualizar a planilha com os números dos eventos
                            pstmentDsrN13.setDate(3, java.sql.Date.valueOf(dataAnoAtualDsrN13));
                            pstmentDsrN13.setDate(4, java.sql.Date.valueOf(validaMes2DsrN13));
                            rstDsrN13 = pstmentDsrN13.executeQuery();
                            if (rstDsrN13.next()) {
                                mediaValorDsr = rstDsrN13.getBigDecimal("mediaValorDsrNotur");
                            }
                        }
                        rstDsrN13.close();
                        BigDecimal calcMedDsrNotSob13 = mediaValorDsr.divide(validaMesDsrN13).setScale(2, RoundingMode.HALF_UP);//Cálculo da média DSR Noturno de janeiro a outubro (no caso fazendo a conta em novembro para pagamento da 1° parc. do 13°.
                        tabelaCalculoFolha.setValueAt(calcMedDsrNotSob13, i, 2);
                        BigDecimal valorTotalDsrNoturno = calcMedDsrNotSob13.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);// Aqui divide o resultado Obtido pela Metade.
                        tabelaCalculoFolha.setValueAt(valorTotalDsrNoturno, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de DSR Noturno Sobre 1° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaDsrN13.desconecta();
                }

                case 178 -> {
                    //Calculando Insalubridade sobre Primeira Parcela do 13°
                    anoAtual = new Date();
                    String dataAnoAtualIns13 = pegaAno.format(anoAtual); //yyyy
                    int numMatriIns13 = Integer.parseInt(numerMatric.getText());
                    ConexaoBD connectFolhaIns13 = new ConexaoBD();
                    connectFolhaIns13.conecta();
                    try {
                        ResultSet rstIns13;
                        PreparedStatement pstmentIns13 = connectFolhaIns13.conexao.prepareStatement("Select valorProvento from rhu_calf02 where where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=?");
                        pstmentIns13.setInt(1, numMatriIns13);
                        pstmentIns13.setInt(2, Integer.parseInt("46"));
                        pstmentIns13.setDate(3, java.sql.Date.valueOf(dataAnoAtualIns13));
                        rstIns13 = pstmentIns13.executeQuery();

                        if (rstIns13.last()) {
                            BigDecimal ultValorInsalubrerstIns13 = rstIns13.getBigDecimal("valorProvento");
                            BigDecimal valorAdicInsalubrerstIns13 = ultValorInsalubrerstIns13.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);
                            tabelaCalculoFolha.setValueAt(valorAdicInsalubrerstIns13, i, 3);
                        }
                        rstIns13.close();
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Insalubridade Sobre 1° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaIns13.desconecta();
                }

                case 179 -> {
                    //Calculando Periculosidade sobre Primeira Parcela do 13°
                    anoAtual = new Date();
                    String dataAnoAtualPeric13 = pegaAno.format(anoAtual); //yyyy
                    int numMatriPeric13 = Integer.parseInt(numerMatric.getText());
                    ConexaoBD connectFolhaPeric13 = new ConexaoBD();
                    connectFolhaPeric13.conecta();
                    try {
                        ResultSet rstPeric13;
                        PreparedStatement pstmentPeric13 = connectFolhaPeric13.conexao.prepareStatement("Select valorProvento from rhu_calf02 where where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=?");
                        pstmentPeric13.setInt(1, numMatriPeric13);
                        pstmentPeric13.setInt(2, Integer.parseInt("47"));
                        pstmentPeric13.setDate(3, java.sql.Date.valueOf(dataAnoAtualPeric13));
                        rstPeric13 = pstmentPeric13.executeQuery();

                        if (rstPeric13.last()) {
                            BigDecimal ultValorPericulosi = rstPeric13.getBigDecimal("valorProvento");
                            BigDecimal valorAdicpericulosi = ultValorPericulosi.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);
                            tabelaCalculoFolha.setValueAt(valorAdicpericulosi, i, 3);
                        }
                        rstPeric13.close();
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular periculosidade Sobre 1° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaPeric13.desconecta();
                }

                case 182 -> {
                    //Calculando Média de Horas Extras 50% Sobre 2° Parcela do 13°
                    int numMatriMHEx5013 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtualMHEx5013 = pegaMes.format(anoAtual);
                    String dataAnoAtualMHEx5013 = pegaAno.format(anoAtual);
                    BigDecimal validaMesMHEx5013 = new BigDecimal(dataMesAtualMHEx5013).subtract(new BigDecimal("1.0"));
                    String validaMes2MHEx5013 = String.valueOf(validaMesMHEx5013);
                    ConexaoBD connectFolhaMHEx5013 = new ConexaoBD();
                    connectFolhaMHEx5013.conecta();
                    try {
                        ResultSet rstMHEx5013;
                        try ( PreparedStatement pstmentMHEx5013 = connectFolhaMHEx5013.conexao.prepareStatement("Select Sum(valorProvento) as 'media50Ano' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstmentMHEx5013.setInt(1, numMatriMHEx5013);
                            pstmentMHEx5013.setInt(2, 98);
                            pstmentMHEx5013.setDate(3, java.sql.Date.valueOf(dataAnoAtualMHEx5013));
                            pstmentMHEx5013.setDate(4, java.sql.Date.valueOf(validaMes2MHEx5013));
                            rstMHEx5013 = pstmentMHEx5013.executeQuery();
                            if (rstMHEx5013.next()) {
                                media50 = rstMHEx5013.getBigDecimal("media50Ano");
                            }
                        }
                        rstMHEx5013.close();
                        BigDecimal calc50MedSob13 = (media50.divide(validaMesMHEx5013)).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        calculo50MedSob13 = calc50MedSob13.add(calc50MedSob13.multiply(new BigDecimal("0.5"))).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(media50, i, 2);
                        tabelaCalculoFolha.setValueAt(calculo50MedSob13, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Horas Extras 50% Sobre 2° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMHEx5013.desconecta();
                }

                case 183 -> {
                    //Calculando Média de Horas Extras 70% Sobre 2° Parcela do 13°
                    int numMatriMHEx7013 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtualMHEx7013 = pegaMes.format(anoAtual);
                    String dataAnoAtualMHEx7013 = pegaAno.format(anoAtual);
                    BigDecimal validaMesMHEx7013 = new BigDecimal(dataMesAtualMHEx7013).subtract(new BigDecimal("1.0"));
                    String validaMes2MHEx7013 = String.valueOf(validaMesMHEx7013);
                    ConexaoBD connectFolhaMHEx7013 = new ConexaoBD();
                    connectFolhaMHEx7013.conecta();
                    try {
                        ResultSet rstMHEx7013;
                        try ( PreparedStatement pstmentMHEx7013 = connectFolhaMHEx7013.conexao.prepareStatement("Select Sum(valorProvento) as 'media70Ano' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstmentMHEx7013.setInt(1, numMatriMHEx7013);
                            pstmentMHEx7013.setInt(2, 99);
                            pstmentMHEx7013.setDate(3, java.sql.Date.valueOf(dataAnoAtualMHEx7013));
                            pstmentMHEx7013.setDate(4, java.sql.Date.valueOf(validaMes2MHEx7013));
                            rstMHEx7013 = pstmentMHEx7013.executeQuery();
                            if (rstMHEx7013.next()) {
                                media70 = rstMHEx7013.getBigDecimal("media70Ano");
                            }
                        }
                        rstMHEx7013.close();
                        BigDecimal calc70MedSob13 = (media70.divide(validaMesMHEx7013)).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        calculo70MedSob13 = calc70MedSob13.add(calc70MedSob13.multiply(new BigDecimal("0.7"))).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(media70, i, 2);
                        tabelaCalculoFolha.setValueAt(calculo70MedSob13, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Horas Extras 70% Sobre 2° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMHEx7013.desconecta();
                }

                case 184 -> {
                    //Calculando Média de Horas Extras 100% Sobre 2° Parcela do 13°
                    int numMatriMHEx10013 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtualMHEx10013 = pegaMes.format(anoAtual);
                    String dataAnoAtualMHEx10013 = pegaAno.format(anoAtual);
                    BigDecimal validaMesMHEx10013 = new BigDecimal(dataMesAtualMHEx10013).subtract(new BigDecimal("1.0"));
                    String validaMes2MHEx10013 = String.valueOf(validaMesMHEx10013);
                    ConexaoBD connectFolhaMHEx10013 = new ConexaoBD();
                    connectFolhaMHEx10013.conecta();
                    try {
                        ResultSet rstMHEx10013;
                        try ( PreparedStatement pstmentMHEx10013 = connectFolhaMHEx10013.conexao.prepareStatement("Select Sum(valorProvento) as 'media100Ano' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstmentMHEx10013.setInt(1, numMatriMHEx10013);
                            pstmentMHEx10013.setInt(2, 100);
                            pstmentMHEx10013.setDate(3, java.sql.Date.valueOf(dataAnoAtualMHEx10013));
                            pstmentMHEx10013.setDate(4, java.sql.Date.valueOf(validaMes2MHEx10013));
                            rstMHEx10013 = pstmentMHEx10013.executeQuery();
                            if (rstMHEx10013.next()) {
                                media100 = rstMHEx10013.getBigDecimal("media100Ano");
                            }
                        }
                        rstMHEx10013.close();
                        BigDecimal calc100MedSob13 = (media100.divide(validaMesMHEx10013)).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        calculo100MedSob13 = calc100MedSob13.add(calc100MedSob13.multiply(new BigDecimal("1"))).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(media100, i, 2);
                        tabelaCalculoFolha.setValueAt(calculo100MedSob13, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Horas Extras 100% Sobre 2° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMHEx10013.desconecta();
                }

                case 185 -> {
                    //Calculando Média de DSR Diurno Sobre 2° Parcela do 13°
                    int numMatriMDSDiurn213 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataAnoAtualMDSDiurn213 = pegaAno.format(anoAtual); //pega o ano atual yyyy
                    String dataMesAtualMDSDiurn213 = pegaMes.format(anoAtual); //pega o mês atual MM
                    BigDecimal validaMesMDSDiurn213 = new BigDecimal(dataMesAtualMDSDiurn213).subtract(new BigDecimal("1.0"));
                    String validaMes2MDSDiurn213 = String.valueOf(validaMesMDSDiurn213); //Aqui eu transformo a subtração em cima em String para poder fazer o cálculo correto da data que serias 11.
                    ConexaoBD connectFolhaMDSDiurn213 = new ConexaoBD();
                    connectFolhaMDSDiurn213.conecta();
                    try {
                        ResultSet rstMDSDiurn213;
                        try ( PreparedStatement pstmentMDSDiurn213 = connectFolhaMDSDiurn213.conexao.prepareStatement("Select Sum(valorProvento) as 'somaValorDsrDiur' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstmentMDSDiurn213.setInt(1, numMatriMDSDiurn213);
                            pstmentMDSDiurn213.setInt(2, Integer.parseInt("5")); // Trocar esse número quando for atualizar a planilha com os números dos eventos
                            pstmentMDSDiurn213.setDate(3, java.sql.Date.valueOf(dataAnoAtualMDSDiurn213));
                            pstmentMDSDiurn213.setDate(4, java.sql.Date.valueOf(validaMes2MDSDiurn213));
                            rstMDSDiurn213 = pstmentMDSDiurn213.executeQuery();
                            if (rstMDSDiurn213.next()) {
                                mediaValorDsr = rstMDSDiurn213.getBigDecimal("somaValorDsrDiur");
                            }
                        }
                        rstMDSDiurn213.close();
                        calculoMedDsrSob13 = mediaValorDsr.divide(validaMesMDSDiurn213).setScale(2, RoundingMode.HALF_UP);//Cálculo da média DSR Noturno de janeiro a novembro (no caso fazendo a conta em dezembro para pagamento da 2° parc. do 13°).
                        tabelaCalculoFolha.setValueAt(mediaValorDsr, i, 2);
                        tabelaCalculoFolha.setValueAt(calculoMedDsrSob13, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de DSR Diurno Sobre 2° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMDSDiurn213.desconecta();
                }

                case 186 -> {
                    //Calculando Média de DSR Noturno Sobre 2° Parcela do 13°
                    int numMatriMDsrNot213 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataAnoAtualMDsrNot213 = pegaAno.format(anoAtual); //pega o ano atual yyyy
                    String dataMesAtualMDsrNot213 = pegaMes.format(anoAtual); //pega o mês atual MM
                    BigDecimal validaMesMDsrNot213 = new BigDecimal(dataMesAtualMDsrNot213).subtract(new BigDecimal("1.0"));
                    String validaMes2MDsrNot213 = String.valueOf(validaMesMDsrNot213); //Aqui eu transformo a subtração em cima em String para poder fazer o cálculo correto da data que serias 11.
                    ConexaoBD connectFolhaMDsrNot213 = new ConexaoBD();
                    connectFolhaMDsrNot213.conecta();
                    try {
                        ResultSet rstMDsrNot213;
                        try ( PreparedStatement pstmentMDsrNot213 = connectFolhaMDsrNot213.conexao.prepareStatement("Select Sum(valorProvento) as 'mediaValorDsrNotur' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstmentMDsrNot213.setInt(1, numMatriMDsrNot213);
                            pstmentMDsrNot213.setInt(2, Integer.parseInt("25")); // Trocar esse número quando for atualizar a planilha com os números dos eventos
                            pstmentMDsrNot213.setDate(3, java.sql.Date.valueOf(dataAnoAtualMDsrNot213));
                            pstmentMDsrNot213.setDate(4, java.sql.Date.valueOf(validaMes2MDsrNot213));
                            rstMDsrNot213 = pstmentMDsrNot213.executeQuery();
                            if (rstMDsrNot213.next()) {
                                mediaValorDsr = rstMDsrNot213.getBigDecimal("mediaValorDsrNotur");
                            }
                        }
                        rstMDsrNot213.close();
                        calculoMedDsrNotSob13 = mediaValorDsr.divide(validaMesMDsrNot213).setScale(2, RoundingMode.HALF_UP); //Cálculo da média DSR Noturno de janeiro a novembro (no caso fazendo a conta em dezembro para pagamento da 2° parc. do 13°).
                        tabelaCalculoFolha.setValueAt(mediaValorDsr, i, 2);
                        tabelaCalculoFolha.setValueAt(calculoMedDsrNotSob13, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de DSR Noturno Sobre 2° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMDsrNot213.desconecta();
                }

                case 187 -> {
                    //Calculando Insalubridade sobre Segunda Parcela do 13°
                    anoAtual = new Date();
                    String dataAnoAtualInsSegParc13 = pegaAno.format(anoAtual); //yyyy
                    int numMatriInsSegParc13 = Integer.parseInt(numerMatric.getText());
                    ConexaoBD connectFolhaInsSegParc13 = new ConexaoBD();
                    connectFolhaInsSegParc13.conecta();
                    try {
                        ResultSet rstInsSegParc13;
                        PreparedStatement pstmentInsSegParc13 = connectFolhaInsSegParc13.conexao.prepareStatement("Select valorProvento from rhu_calf02 where where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=?");
                        pstmentInsSegParc13.setInt(1, numMatriInsSegParc13);
                        pstmentInsSegParc13.setInt(2, Integer.parseInt("46"));
                        pstmentInsSegParc13.setDate(3, java.sql.Date.valueOf(dataAnoAtualInsSegParc13));
                        rstInsSegParc13 = pstmentInsSegParc13.executeQuery();

                        if (rstInsSegParc13.last()) {
                            BigDecimal ultValorInsalubre = rstInsSegParc13.getBigDecimal("valorProvento");
                            tabelaCalculoFolha.setValueAt(ultValorInsalubre, i, 2);
                            tabelaCalculoFolha.setValueAt(ultValorInsalubre, i, 3);
                        }
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Insalubridade Sobre 2° Parcela do 13°" + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaInsSegParc13.desconecta();
                }

                case 188 -> {
                    //Calculando Periculosidade sobre Segunda Parcela do 13°
                    anoAtual = new Date();
                    String dataAnoAtualPerSegParc13 = pegaAno.format(anoAtual); //yyyy
                    int numMatriPerSegParc13 = Integer.parseInt(numerMatric.getText());
                    ConexaoBD connectFolhaPerSegParc13 = new ConexaoBD();
                    connectFolhaPerSegParc13.conecta();
                    try {
                        ResultSet rstPerSegParc13;
                        PreparedStatement pstmentPerSegParc13 = connectFolhaPerSegParc13.conexao.prepareStatement("Select valorProvento from rhu_calf02 where where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=?");
                        pstmentPerSegParc13.setInt(1, numMatriPerSegParc13);
                        pstmentPerSegParc13.setInt(2, Integer.parseInt("47"));
                        pstmentPerSegParc13.setDate(3, java.sql.Date.valueOf(dataAnoAtualPerSegParc13));
                        rstPerSegParc13 = pstmentPerSegParc13.executeQuery();

                        if (rstPerSegParc13.last()) {
                            BigDecimal ultValorPericulosi = rstPerSegParc13.getBigDecimal("valorProvento");
                            tabelaCalculoFolha.setValueAt(ultValorPericulosi, i, 2);
                            tabelaCalculoFolha.setValueAt(ultValorPericulosi, i, 3);
                        }
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular periculosidade Sobre 2° Parcela do 13°" + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaPerSegParc13.desconecta();
                }

                case 189 -> {
                    //Calculando 2° Parcela Décimo terceiro
                    BigDecimal decimoTerc1Parcel2 = salarioBase.divide(new BigDecimal("2")).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt(decimoTerc1Parcel2, i, 3);
                }

                case 195 -> {
                    //Calculando 13º Salário Final Média Comissões
                    Date data = new Date();
                    int numMatriMedComiss = Integer.parseInt(numerMatric.getText());

                    String pegaValorDoAnoMedComiss = (pegaAno.format(data));

                    int valorAno = Integer.parseInt(pegaValorDoAnoMedComiss);

                    String dataDe = String.valueOf(valorAno) + "-01" + "-01";
                    String dataAte = String.valueOf(valorAno) + "-12" + "-31";

                    ConexaoBD connectFolhaMedComiss = new ConexaoBD();
                    connectFolhaMedComiss.conecta();
                    try {
                        ResultSet rstMedCom;
                        try ( PreparedStatement psMedCom = connectFolhaMedComiss.conexao.prepareStatement("Select AVG(valorProvento) as 'mediaComissoes' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and dataProcessamento between=? and dataProcessamento=?")) {
                            psMedCom.setInt(1, numMatriMedComiss);
                            psMedCom.setInt(2, Integer.parseInt("51"));
                            psMedCom.setDate(3, java.sql.Date.valueOf(dataDe));
                            psMedCom.setDate(4, java.sql.Date.valueOf(dataAte));
                            rstMedCom = psMedCom.executeQuery();
                            if (rstMedCom.next()) {
                                mediaComissoesAno = rstMedCom.getBigDecimal("mediaComissoes");
                            }
                        }
                        rstMedCom.close();
                        tabelaCalculoFolha.setValueAt(mediaComissoesAno, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Comissões sobre o 13°" + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMedComiss.desconecta();
                }

                case 201 -> {
                    //Calculando o Complemento Média Hora Extra 50% do 13°
                    int numMatriComMHex5013 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtualComMHex5013 = pegaMes.format(anoAtual);
                    String dataAnoAtualComMHex5013 = pegaAno.format(anoAtual);
                    ConexaoBD connectFolhaComMHex5013 = new ConexaoBD();
                    connectFolhaComMHex5013.conecta();
                    try {
                        ResultSet rstComMHex5013;
                        try ( PreparedStatement psComMHex5013 = connectFolhaComMHex5013.conexao.prepareStatement("Select Sum(valorProvento) as 'mediaComHorExtra' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            psComMHex5013.setInt(1, numMatriComMHex5013);
                            psComMHex5013.setInt(2, Integer.parseInt("98"));// Mudar aqui quando a lista for atualizada
                            psComMHex5013.setDate(3, java.sql.Date.valueOf(dataAnoAtualComMHex5013));
                            psComMHex5013.setDate(4, java.sql.Date.valueOf(dataMesAtualComMHex5013));
                            rstComMHex5013 = psComMHex5013.executeQuery();
                            if (rstComMHex5013.next()) {
                                somaComplemento50 = rstComMHex5013.getBigDecimal("mediaComHorExtra");
                            }
                        }
                        rstComMHex5013.close();
                        BigDecimal calcuValorComMHex5013 = (somaComplemento50.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal calculaValorComMHex5013 = calcuValorComMHex5013.add(calcuValorComMHex5013.multiply(new BigDecimal("0.5"))).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal valorComplementoHExtra50 = calculaValorComMHex5013.subtract(calculo50MedSob13);
                        tabelaCalculoFolha.setValueAt(valorComplementoHExtra50, i, 3);

                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média Hora Extra 50% para complemento de 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaComMHex5013.desconecta();
                }

                case 202 -> {
                    //Calculando o Complemento Média Hora Extra 70% do 13°
                    int numMatriComMHex70013 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtualComMHex70013 = pegaMes.format(anoAtual);
                    String dataAnoAtualComMHex70013 = pegaAno.format(anoAtual);
                    ConexaoBD connectFolhaComMHex70013 = new ConexaoBD();
                    connectFolhaComMHex70013.conecta();
                    try {
                        ResultSet rstComMHex70013;
                        try ( PreparedStatement psComMHex70013 = connectFolhaComMHex70013.conexao.prepareStatement("Select Sum(valorProvento) as 'mediaComHorExtra' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            psComMHex70013.setInt(1, numMatriComMHex70013);
                            psComMHex70013.setInt(2, Integer.parseInt("99"));// Mudar aqui quando a lista for atualizada
                            psComMHex70013.setDate(3, java.sql.Date.valueOf(dataAnoAtualComMHex70013));
                            psComMHex70013.setDate(4, java.sql.Date.valueOf(dataMesAtualComMHex70013));
                            rstComMHex70013 = psComMHex70013.executeQuery();
                            if (rstComMHex70013.next()) {
                                somaComplemento70 = rstComMHex70013.getBigDecimal("mediaComHorExtra");
                            }
                        }
                        rstComMHex70013.close();
                        BigDecimal calcuValor70 = (somaComplemento70.divide(new BigDecimal("12"))).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal calculaValor70 = calcuValor70.add(calcuValor70.multiply(new BigDecimal("0.7"))).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal valorComplementoHExtra70 = calculaValor70.subtract(calculo70MedSob13);
                        tabelaCalculoFolha.setValueAt(valorComplementoHExtra70, i, 3);

                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média Hora Extra 70% para complemento de 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaComMHex70013.desconecta();
                }

                case 203 -> {
                    //Calculando o Complemento Média Hora Extra 100% do 13°
                    int numMatriComMHex100013 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtualComMHex100013 = pegaMes.format(anoAtual);
                    String dataAnoAtualComMHex100013 = pegaAno.format(anoAtual);
                    ConexaoBD connectFolhaComMHex100013 = new ConexaoBD();
                    connectFolhaComMHex100013.conecta();
                    try {
                        ResultSet rstComMHex100013;
                        try ( PreparedStatement psComMHex100013 = connectFolhaComMHex100013.conexao.prepareStatement("Select Sum(valorProvento) as 'mediaComHorExtra' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            psComMHex100013.setInt(1, numMatriComMHex100013);
                            psComMHex100013.setInt(2, Integer.parseInt("100"));// Mudar aqui quando a lista for atualizada
                            psComMHex100013.setDate(3, java.sql.Date.valueOf(dataAnoAtualComMHex100013));
                            psComMHex100013.setDate(4, java.sql.Date.valueOf(dataMesAtualComMHex100013));
                            rstComMHex100013 = psComMHex100013.executeQuery();
                            if (rstComMHex100013.next()) {
                                somaComplemento100 = rstComMHex100013.getBigDecimal("mediaComHorExtra");
                            }
                        }
                        rstComMHex100013.close();
                        BigDecimal calcuValor100 = (somaComplemento100.divide(new BigDecimal("12"))).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal calculaValor100 = calcuValor100.add(calcuValor100.multiply(new BigDecimal("1"))).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal valorComplementoHExtra100 = calculaValor100.subtract(calculo100MedSob13);
                        tabelaCalculoFolha.setValueAt(valorComplementoHExtra100, i, 3);

                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média Hora Extra 100% para complemento de 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaComMHex100013.desconecta();
                }

                case 204 -> {
                    //Calculando Complemento DSR Diurno Sobre o 13°
                    int numMatriComMDsrDir13 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtualComMDsrDir13 = pegaMes.format(anoAtual);
                    String dataAnoAtualComMDsrDir13 = pegaAno.format(anoAtual);
                    ConexaoBD connectFolhaComMDsrDir13 = new ConexaoBD();
                    connectFolhaComMDsrDir13.conecta();
                    try {
                        ResultSet rstComMDsrDir13;
                        try ( PreparedStatement psComMDsrDir13 = connectFolhaComMDsrDir13.conexao.prepareStatement("Select Sum(valorProvento) as 'mediaComDsrDiur' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            psComMDsrDir13.setInt(1, numMatriComMDsrDir13);
                            psComMDsrDir13.setInt(2, Integer.parseInt("5"));// Mudar aqui quando a lista for atualizada
                            psComMDsrDir13.setDate(3, java.sql.Date.valueOf(dataAnoAtualComMDsrDir13));
                            psComMDsrDir13.setDate(4, java.sql.Date.valueOf(dataMesAtualComMDsrDir13));
                            rstComMDsrDir13 = psComMDsrDir13.executeQuery();
                            if (rstComMDsrDir13.next()) {
                                somaComplementoDsrDiurn = rstComMDsrDir13.getBigDecimal("mediaComDsrDiur");
                            }
                        }
                        rstComMDsrDir13.close();
                        BigDecimal calculaValorComDsrDiurn = somaComplementoDsrDiurn.divide(new BigDecimal("12")).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal valorComplementoDsrDiurn = calculaValorComDsrDiurn.subtract(calculoMedDsrSob13);
                        tabelaCalculoFolha.setValueAt(valorComplementoDsrDiurn, i, 3);

                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média Dsr Diurno para complemento de 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaComMDsrDir13.desconecta();
                }

                case 205 -> {
                    //Calculando Complemento DSR Noturno Sobre o 13°
                    int numMatriComMDsrNot13 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtualComMDsrNot13 = pegaMes.format(anoAtual);
                    String dataAnoAtualComMDsrNot13 = pegaAno.format(anoAtual);
                    ConexaoBD connectFolhaComMDsrNot13 = new ConexaoBD();
                    connectFolhaComMDsrNot13.conecta();
                    try {
                        ResultSet rstComMDsrNot13;
                        try ( PreparedStatement psComMDsrNot13 = connectFolhaComMDsrNot13.conexao.prepareStatement("Select Sum(valorProvento) as 'mediaComDsrNoturn' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            psComMDsrNot13.setInt(1, numMatriComMDsrNot13);
                            psComMDsrNot13.setInt(2, Integer.parseInt("25"));// Mudar aqui quando a lista for atualizada
                            psComMDsrNot13.setDate(3, java.sql.Date.valueOf(dataAnoAtualComMDsrNot13));
                            psComMDsrNot13.setDate(4, java.sql.Date.valueOf(dataMesAtualComMDsrNot13));
                            rstComMDsrNot13 = psComMDsrNot13.executeQuery();
                            if (rstComMDsrNot13.next()) {
                                somaComplementoDsrNoturn = rstComMDsrNot13.getBigDecimal("mediaComDsrNoturn");
                            }
                        }
                        rstComMDsrNot13.close();
                        BigDecimal calculaValorComDsrNoturn = somaComplementoDsrNoturn.divide(new BigDecimal("12")).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal valorComplementoDsrNoturn = calculaValorComDsrNoturn.subtract(calculoMedDsrNotSob13);
                        tabelaCalculoFolha.setValueAt(valorComplementoDsrNoturn, i, 3);

                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média Dsr Noturno para complemento de 13°: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaComMDsrNot13.desconecta();
                }

                case 206 -> {
                    //Média Adicional Noturno do 13° Salário
                    int numMatriMedAdNot13 = Integer.parseInt(numerMatric.getText());
                    anoAtual = new Date();
                    String dataMesAtualMedAdNot13 = pegaMes.format(anoAtual);
                    String dataAnoAtualMedAdNot13 = pegaAno.format(anoAtual);
                    BigDecimal validaMesMedAdNot13 = new BigDecimal(dataMesAtualMedAdNot13).subtract(new BigDecimal("1.0"));
                    String validaMes2MedAdNot13 = String.valueOf(validaMesMedAdNot13);
                    ConexaoBD connectFolhaMedAdNot13 = new ConexaoBD();
                    connectFolhaMedAdNot13.conecta();
                    try {
                        ResultSet rstMedAdNot13;
                        try ( PreparedStatement pstmentMedAdNot13 = connectFolhaMedAdNot13.conexao.prepareStatement("Select Sum(valorProvento) as 'mediaAdicNoturn' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=? and MONTH(dataProcessamento)=?")) {
                            pstmentMedAdNot13.setInt(1, numMatriMedAdNot13);
                            pstmentMedAdNot13.setInt(2, Integer.parseInt("14"));// Mudar aqui quando a lista for atualizada
                            pstmentMedAdNot13.setDate(3, java.sql.Date.valueOf(dataAnoAtualMedAdNot13));
                            pstmentMedAdNot13.setDate(4, java.sql.Date.valueOf(validaMes2MedAdNot13));
                            rstMedAdNot13 = pstmentMedAdNot13.executeQuery();
                            if (rstMedAdNot13.next()) {
                                mediaAdicNoturn13 = rstMedAdNot13.getBigDecimal("mediaAdicNoturn");
                            }
                        }
                        rstMedAdNot13.close();
                        BigDecimal calcAdiciNotur = (mediaAdicNoturn13.divide(validaMesMedAdNot13)).multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal calculoAdiciNotur = calcAdiciNotur.add(calcAdiciNotur.multiply(new BigDecimal("0.2"))).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calculoAdiciNotur, i, 2);
                        BigDecimal valorMediaAdicioNoturn = calculoAdiciNotur.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(valorMediaAdicioNoturn, i, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Adicional Noturno Sobre 1° Parcela do 13° " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMedAdNot13.desconecta();
                }

                case 239 -> {
                    //Calculando Vale Transporte
                    if (tipoDeJornada.getSelectedItem().equals("6 x 1")) {
                        anoAtual = new Date();
                        String leAnoVt = pegaAno.format(anoAtual);
                        String leMesVt = pegaMes.format(anoAtual);

                        int yearVt = Integer.parseInt(leAnoVt);
                        int monthVt = Integer.parseInt(leMesVt);
                        int workingDays1 = 0;

                        YearMonth anoMesVt = YearMonth.of(yearVt, monthVt);

                        feriados.addAll(calend.getFeriadosFixos(yearVt));
                        feriados.addAll(calend.getFeriadosMoveis(yearVt));

                        for (int dia = 1; dia <= anoMesVt.lengthOfMonth(); dia++) {
                            LocalDate dataNow = anoMesVt.atDay(dia);

                            if (dataNow.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(dataNow)) {
                                workingDays1++;
                            }
                        }
                        BigDecimal valorVTrans = (new BigDecimal(workingDays1)).multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(workingDays1, i, 2);
                        tabelaCalculoFolha.setValueAt((valorVTrans), i, 3);

                    } else if (tipoDeJornada.getSelectedItem().equals("4 x 2")) {

                        BigDecimal valorVTrans = new BigDecimal("20").multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(20, i, 2);
                        tabelaCalculoFolha.setValueAt((valorVTrans), i, 3);

                    } else if (tipoDeJornada.getSelectedItem().equals("5 x 1")) {
                        anoAtual = new Date();
                        String leAnoVt = pegaAno.format(anoAtual);
                        String leMesVt = pegaMes.format(anoAtual);

                        int yearVt = Integer.parseInt(leAnoVt);
                        int monthVt = Integer.parseInt(leMesVt);

                        YearMonth anoMesVt = YearMonth.of(yearVt, monthVt);

                        for (int dia = 1; dia <= anoMesVt.lengthOfMonth(); dia++) {

                            switch (dia) {
                                case 28 -> {
                                    BigDecimal valorVTrans28 = new BigDecimal("25").multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                                    tabelaCalculoFolha.setValueAt(25, i, 2);
                                    tabelaCalculoFolha.setValueAt((valorVTrans28), i, 3);
                                }
                                case 30 -> {
                                    BigDecimal valorVTrans30 = new BigDecimal("26").multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                                    tabelaCalculoFolha.setValueAt(30, i, 2);
                                    tabelaCalculoFolha.setValueAt((valorVTrans30), i, 3);
                                }
                                case 31 -> {
                                    BigDecimal valorVTrans31 = new BigDecimal("27").multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                                    tabelaCalculoFolha.setValueAt(31, i, 2);
                                    tabelaCalculoFolha.setValueAt((valorVTrans31), i, 3);
                                }
                                default -> {
                                }
                            }
                        }

                    } else if (tipoDeJornada.getSelectedItem().equals("5 x 2")) {

                        anoAtual = new Date();
                        String leAnoVt = pegaAno.format(anoAtual);
                        String leMesVt = pegaMes.format(anoAtual);

                        int yearVt = Integer.parseInt(leAnoVt);
                        int monthVt = Integer.parseInt(leMesVt);

                        int workingDays2 = 0;

                        YearMonth anoMesVt = YearMonth.of(yearVt, monthVt);

                        feriados.addAll(calend.getFeriadosFixos(yearVt));
                        feriados.addAll(calend.getFeriadosMoveis(yearVt));

                        for (int dia = 1; dia <= anoMesVt.lengthOfMonth(); dia++) {
                            LocalDate dataNow = anoMesVt.atDay(dia);

                            // Conta apenas os dias úteis (de segunda a sexta-feira) e exclui feriados
                            if (dataNow.getDayOfWeek() != DayOfWeek.SATURDAY &&
                                    dataNow.getDayOfWeek() != DayOfWeek.SUNDAY &&
                                    !feriados.contains(dataNow)) {
                                workingDays2++;
                            }
                        }
                        // Cálculo final do vale-transporte, usando os dias úteis
                        BigDecimal valorVTrans = new BigDecimal(workingDays2).multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(workingDays2, i, 2);
                        tabelaCalculoFolha.setValueAt(valorVTrans, i, 3);

                    } else if (tipoDeJornada.getSelectedItem().equals("12 x 36")) {

                        BigDecimal valorVTrans1236 = new BigDecimal("15").multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(15, i, 2);
                        tabelaCalculoFolha.setValueAt((valorVTrans1236), i, 3);

                    } else if (tipoDeJornada.getSelectedItem().equals("24 x 48")) {
                        anoAtual = new Date();
                        String leAnoVt = pegaAno.format(anoAtual);
                        String leMesVt = pegaMes.format(anoAtual);

                        int yearVt = Integer.parseInt(leAnoVt);
                        int monthVt = Integer.parseInt(leMesVt);

                        YearMonth anoMesVt = YearMonth.of(yearVt, monthVt);

                        for (int dia = 1; dia <= anoMesVt.lengthOfMonth(); dia++) {

                            switch (dia) {
                                case 28 -> {
                                    BigDecimal valorVTrans282448 = new BigDecimal("10").multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                                    tabelaCalculoFolha.setValueAt(dia, i, 2);
                                    tabelaCalculoFolha.setValueAt((valorVTrans282448), i, 3);
                                }
                                case 30 -> {
                                    BigDecimal valorVTrans302448 = new BigDecimal("10").multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                                    tabelaCalculoFolha.setValueAt(dia, i, 2);
                                    tabelaCalculoFolha.setValueAt((valorVTrans302448), i, 3);
                                }
                                case 31 -> {
                                    BigDecimal valorVTrans312448 = new BigDecimal("11").multiply(new BigDecimal(valeTransport.getText())).setScale(2, RoundingMode.HALF_UP);
                                    tabelaCalculoFolha.setValueAt(dia, i, 2);
                                    tabelaCalculoFolha.setValueAt((valorVTrans312448), i, 3);
                                }
                                default -> {
                                }
                            }
                        }
                    }
                }

                case 259 -> {
                    //Cálculo Vale Creche
                    BigDecimal valorValeCreche = new BigDecimal(valeCreche.getText());
                    tabelaCalculoFolha.setValueAt(valorValeCreche, i, 3);
                }

                case 402 -> {
                    //Calculando FGTS Sobre o Salário
                    String descricaoDoCargo = descricaoCargo.getText();

                    if (descricaoDoCargo.contains("Aprendiz")) {
                        BigDecimal calculaFgts = salarioBase.multiply(new BigDecimal("0.02")).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calculaFgts, i, 3);
                    } else {
                        BigDecimal calculaFgts = salarioBase.multiply(new BigDecimal("0.08")).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calculaFgts, i, 3);
                    }
                }

                case 403 -> {
                    //Calculando FGTS Sobre 13º Salário
                    String descriDoCargo = descricaoCargo.getText();

                    if (descriDoCargo.contains("Aprendiz")) {
                        BigDecimal calculaFgts = (salarioBase.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(0.02)).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calculaFgts, i, 3);
                    } else {
                        BigDecimal calculaFgts = (salarioBase.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(0.08)).setScale(2, RoundingMode.HALF_UP);
                        tabelaCalculoFolha.setValueAt(calculaFgts, i, 3);
                    }
                }

                case 409 -> {
                    //Participação Nos Lucros e Resultados
                    BigDecimal plr = new BigDecimal(partLucrosResult.getText());
                    tabelaCalculoFolha.setValueAt(plr, i, 3);
                }

                case 410 -> {
                    //Abono Salarial
                    BigDecimal AbSal = new BigDecimal(abonoSalarial.getText());
                    tabelaCalculoFolha.setValueAt(AbSal, i, 3);
                }

                case 412 -> {
                    //Cálculo Reembolso Creche
                    BigDecimal reembValeCreche = new BigDecimal(valeCreche.getText());
                    tabelaCalculoFolha.setValueAt(reembValeCreche, i, 3);
                }

                case 414 -> {
                    //Cálculo Gratificação Semestral
                    BigDecimal valorGratificaSemest = new BigDecimal(valorDaGratificacao.getText());
                    tabelaCalculoFolha.setValueAt(valorGratificaSemest, i, 3);
                }

                case 416 -> {
                    //Cálculo Reembolso Viagem
                    BigDecimal rembViagem = new BigDecimal(reembolsoViagem.getText());
                    tabelaCalculoFolha.setValueAt(rembViagem, i, 3);
                }

                case 417 -> {
                    //1° Parcela Participação Nos Lucros e Resultados
                    BigDecimal plr1 = new BigDecimal(partLucrosResult.getText());
                    tabelaCalculoFolha.setValueAt(plr1, i, 3);
                }

                case 418 -> {
                    //2° Parcela Participação Nos Lucros e Resultados
                    BigDecimal plr2 = new BigDecimal(partLucrosResult.getText());
                    tabelaCalculoFolha.setValueAt(plr2, i, 3);
                }

                case 420 -> {
                    //1° Parcela Abono Salarial
                    BigDecimal AbSal1 = new BigDecimal(abonoSalarial.getText());
                    tabelaCalculoFolha.setValueAt(AbSal1, i, 3);
                }

                case 421 -> {
                    //2° Parcela Abono Salarial
                    BigDecimal AbSal2 = new BigDecimal(abonoSalarial.getText());
                    tabelaCalculoFolha.setValueAt(AbSal2, i, 3);
                }
                default -> {
                }
            }
        }
    }
}
