package br.com.codex.v1.service.rh.horarios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
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
public class CalcularHorasRepousoRemuneradoNoturnoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    Calendario calendario = new Calendario();
    Set<LocalDate> feriados = new HashSet<>();

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal>calcularHorasRepousoRemuneradoNoturno(){
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
        String tipoJornada = folha.getJornada();
        BigDecimal salarioPorHora =  folha.getSalarioHora();

        if (tipoJornada.equals("12 x 36")) {

            LocalTime horaIniRepRemNot = LocalTime.parse("22:00");
            LocalTime horaFimRepRemNot = folha.getHoraSaida();
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

                BigDecimal valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal("15")).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
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
                BigDecimal valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal(workingDaysNotRepRemNot)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
                BigDecimal dsrHoraNoturnaRepousoRemuneradoNoturno = valorReferenciaDsrHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                resultado.put("referencia", valorReferenciaDsrHoraNoturna);
                resultado.put("vencimentos", dsrHoraNoturnaRepousoRemuneradoNoturno);
                resultado.put("descontos", BigDecimal.ZERO);
            }
        } else {
            LocalTime horaIniNotRepRemNot = LocalTime.parse("22:00");
            LocalTime horaFimNotRepRemNot = folha.getHoraSaida();
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
                BigDecimal valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal(workingDaysNotRepRemNot)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
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
                BigDecimal valorReferenciaDsrHoraNoturna = valorHoraDepoisDas22RepRemNot.multiply(new BigDecimal(workingDaysNotRepRemNot)).setScale(2, RoundingMode.HALF_UP); // Aqui é calculado os dias não úteis vezes o número de horas noturnas no mês.
                BigDecimal dsrHoraNoturnaRepousoRemuneradoNoturno = valorReferenciaDsrHoraNoturna.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP); //Aqui é calculado o valor das horas noturnas no mês vezes o valor do salário/hora.
                resultado.put("referencia", valorReferenciaDsrHoraNoturna);
                resultado.put("vencimentos", dsrHoraNoturnaRepousoRemuneradoNoturno);
                resultado.put("descontos", BigDecimal.ZERO);
            }
        }

        return resultado;
    }
}
