package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaQuinzenal;
import br.com.codex.v1.service.rh.beneficios.CalcularBolsaAuxílioService;
import br.com.codex.v1.service.rh.beneficios.CalcularGratificacaoService;
import br.com.codex.v1.service.rh.beneficios.CalcularProLaboreService;
import br.com.codex.v1.service.rh.especiais.CalcularComissaoService;
import br.com.codex.v1.service.rh.especiais.CalcularInsalubridadeService;
import br.com.codex.v1.service.rh.especiais.CalcularPericulosidadeService;
import br.com.codex.v1.service.rh.horarios.*;
import br.com.codex.v1.service.rh.horasextras.CalcularDsrSobreHoraExtraDiurna100PorcentoService;
import br.com.codex.v1.service.rh.horasextras.CalcularDsrSobreHoraExtraDiurna50PorcentoService;
import br.com.codex.v1.service.rh.horasextras.CalcularDsrSobreHoraExtraDiurna70PorcentoService;
import br.com.codex.v1.utilitario.Calendario;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    private static final Logger logger = LoggerFactory.getLogger(CalculoDaFolhaProventosService.class);

    @Autowired
    private TabelaImpostoRendaService tabelaImpostoRendaService;

    @Autowired
    private TabelaDeducaoInssService tabelaDeducaoInssService;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Autowired
    protected CalcularHorasNormaisDiurnasService calcularHorasNormaisDiurnasService;

    @Autowired
    protected CalcularAdiantamentoSalario40PorcentoService calcularAdiantamentoSalario40PorcentoService;

    @Autowired
    protected CalcularHorasRepousoRemuneradoDiurnoService calcularHorasRepousoRemuneradoDiurnoService;

    @Autowired
    protected CalcularHorasAtestadoMedicoService calcularHorasAtestadoMedicoService;

    @Autowired
    protected CalcularDiasAtestadoMedicoService calcularDiasAtestadoMedicoService;

    @Autowired
    protected CalcularHorasNormaisNoturnasService calcularHorasNormaisNoturnasService;

    @Autowired
    protected CalcularAdicionalNoturnoService calcularAdicionalNoturnoService;

    @Autowired
    protected CalcularProLaboreService calcularProLaboreService;

    @Autowired
    protected CalcularBolsaAuxílioService calcularBolsaAuxilioService;

    @Autowired
    protected  CalcularHorasRepousoRemuneradoNoturnoService calcularHorasRepousoRemuneradoNoturnoService;

    @Autowired
    protected CalcularDsrSobreHoraExtraDiurna50PorcentoService calcularDsrSobreHoraExtraDiurna50PorcentoService;

    @Autowired
    protected CalcularDsrSobreHoraExtraDiurna70PorcentoService calcularDsrSobreHoraExtraDiurna70PorcentoService;

    @Autowired
    protected CalcularDsrSobreHoraExtraDiurna100PorcentoService calcularDsrSobreHoraExtraDiurna100PorcentoService;

    @Autowired
    protected CalcularInsalubridadeService calcularInsalubridadeService;

    @Autowired
    protected CalcularPericulosidadeService calcularPericulosidadeService;

    @Autowired
    protected CalcularComissaoService calcularComissaoService;

    @Autowired
    protected CalcularGratificacaoService calculoGratificacaoService;

    Calendario calendario = new Calendario();
    Set<LocalDate> feriados = new HashSet<>();

    @Setter
    String numeroMatricula;

    @Setter
    String tipoSalario;
    BigDecimal valorReferenteHoraDiurna;

    private BigDecimal obtemSalarioMinimo(){
        return tabelaImpostoRendaService.getSalarioMinimo();
    }

    public Map<String, BigDecimal> escolheEventos(Integer codigoEvento, String tipoSalario) {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        switch (codigoEvento) {

            //Calculando Horas Normais Diurnas
            ✅case 1 -> {
                calcularHorasNormaisDiurnasService.calculaHorasNormaisDiurnas();
                /*try {
                   FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);

                    assert folha != null;
                    LocalTime horaIni = folha.getHoraEntrada();
                    LocalTime horaFim = folha.getHoraSaida();
                    BigDecimal salarioPorHora = folha.getSalarioHora();

                    LocalTime hora22 = LocalTime.parse("22:00");
                    LocalTime hora13 = LocalTime.parse("13:00");

                    // ✅ 1. CALCULAR HORAS DIURNAS TRABALHADAS POR DIA
                    Duration horasTrabalhadas;

                    if (horaFim.isBefore(hora13)) {
                        // Turno matutino (termina antes das 13h)
                        horasTrabalhadas = Duration.between(horaIni, horaFim);
                    } else if (horaFim.isBefore(hora22)) {
                        // Turno diurno normal (termina antes das 22h)
                        horasTrabalhadas = Duration.between(horaIni, horaFim);
                    } else {
                        // Turno que ultrapassa às 22h - considera apenas até 22h para horas diurnas
                        horasTrabalhadas = Duration.between(horaIni, hora22);
                    }

                    // ✅ 2. SUBTRAIR HORÁRIO DE ALMOÇO (1 hora) e converter para decimal
                    horasTrabalhadas = horasTrabalhadas.minusHours(1);

                    double horasDecimais = horasTrabalhadas.toHours() + horasTrabalhadas.toMinutesPart() / 60.0;

                    BigDecimal horasPorDia = BigDecimal.valueOf(horasDecimais).setScale(2, RoundingMode.HALF_UP);

                    // ✅ 3. CALCULAR DIAS ÚTEIS NO MÊS - UMA ÚNICA VEZ
                    int year = LocalDate.now().getYear();
                    int month = LocalDate.now().getMonthValue();
                    int diasUteis = 0;

                    YearMonth anoMes = YearMonth.of(year, month);
                    Set<LocalDate> feriados = new HashSet<>();
                    feriados.addAll(calendario.getFeriadosFixos(year));
                    feriados.addAll(calendario.getFeriadosMoveis(year));

                    for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                        LocalDate data = anoMes.atDay(dia);
                        if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                            diasUteis++;
                        }
                    }

                    // ✅ 4. CÁLCULO FINAL DAS HORAS DIURNAS
                    BigDecimal totalHorasMes = horasPorDia.multiply(new BigDecimal(diasUteis)).setScale(2, RoundingMode.HALF_UP);

                    valorReferenteHoraDiurna = totalHorasMes.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", totalHorasMes);     // Total de horas no mês
                    resultado.put("vencimentos", valorReferenteHoraDiurna);       // Valor total em R$
                    resultado.put("descontos", BigDecimal.ZERO);
                    System.out.println("💰 Calculado Horas Normais: " + valorReferenteHoraDiurna);

                    return resultado;


                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular horas diurnas: " + e.getMessage());
                }*/
            }

            //Calculando Adiantamento de Salário (40%)
            ✅case 2 -> {
                calcularAdiantamentoSalario40PorcentoService.calculaAdiantamentoSalarial40Porcento();

                /*FolhaQuinzenal folha = calculoBaseService.findByMatriculaFuncionario(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();

                BigDecimal adiantamentoSalarial = (salarioBase.multiply(new BigDecimal("40"))).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                resultado.put("referencia", new BigDecimal(40));
                resultado.put("vencimentos", adiantamentoSalarial);
                resultado.put("descontos", BigDecimal.ZERO);*/
            }

            //Calculando Horas Repouso Remunerado Diurno (DSR) no mês
            ✅case 5 -> {
                calcularHorasRepousoRemuneradoDiurnoService.calcularHorasRepousoRemuneradoDiurno();
                /*try {
                    FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                    BigDecimal salarioPorHora = folha.getSalarioHora();
                    LocalTime horaIniHRDiurno = folha.getHoraEntrada();
                    LocalTime horaFimHRDiurno = folha.getHoraSaida();

                    LocalTime hora22HRDiurno = LocalTime.parse("22:00");
                    LocalTime hora13HRDiurno = LocalTime.parse("13:00");

                    // ✅ 1. CALCULAR HORAS DIURNAS (lógica simplificada)
                    Duration horasTrabalhadasDiurnas;

                    if (horaFimHRDiurno.isBefore(hora13HRDiurno)) {
                        // Turno totalmente diurno (termina antes das 13h)
                        horasTrabalhadasDiurnas = Duration.between(horaIniHRDiurno, horaFimHRDiurno);
                    } else if (horaFimHRDiurno.isBefore(hora22HRDiurno)) {
                        // Turno diurno (termina antes das 22h)
                        horasTrabalhadasDiurnas = Duration.between(horaIniHRDiurno, horaFimHRDiurno);
                    } else {
                        // Turno que ultrapassa as 22h - considera apenas até 22h
                        horasTrabalhadasDiurnas = Duration.between(horaIniHRDiurno, hora22HRDiurno);
                    }

                    // ✅ 2. SUBTRAIR HORÁRIO DE ALMOÇO (1 hora)
                    horasTrabalhadasDiurnas = horasTrabalhadasDiurnas.minusHours(1);

                    // Converter para horas decimais (ex: 7.5 horas)
                    double horasDiurnasDecimais = horasTrabalhadasDiurnas.toHours() +
                            horasTrabalhadasDiurnas.toMinutesPart() / 60.0;

                    BigDecimal horasDiurnasPorDia = BigDecimal.valueOf(horasDiurnasDecimais)
                            .setScale(2, RoundingMode.HALF_UP);

                    // ✅ 3. CALCULAR DIAS DE REPOUSO (domingos + feriados) - UMA ÚNICA VEZ
                    int year = LocalDate.now().getYear();
                    int month = LocalDate.now().getMonthValue();
                    int diasRepouso = 0;

                    YearMonth anoMes = YearMonth.of(year, month);
                    Set<LocalDate> feriados = new HashSet<>();
                    feriados.addAll(calendario.getFeriadosFixos(year));
                    feriados.addAll(calendario.getFeriadosMoveis(year));

                    for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
                        LocalDate data = anoMes.atDay(dia);
                        if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                            diasRepouso++;
                        }
                    }

                    // ✅ 4. CÁLCULO FINAL DO DSR
                    BigDecimal totalHorasDSR = horasDiurnasPorDia.multiply(new BigDecimal(diasRepouso))
                            .setScale(2, RoundingMode.HALF_UP);

                    BigDecimal valorDSR = totalHorasDSR.multiply(salarioPorHora)
                            .setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", totalHorasDSR);     // Total de horas no DSR
                    resultado.put("vencimentos", valorDSR);         // Valor em R$
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular DSR Diurno: " + e.getMessage());
                }*/
            }

            //Horas de Atestado Médico
            ✅case 8 -> {
                calcularHorasAtestadoMedicoService.calcularHorasAtestadoMedico();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal horasDeFaltasAtestadoMedico = folha.getFaltasHorasMes();
                resultado.put("referencia", horasDeFaltasAtestadoMedico);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;*/
            }

            //Dias de atestado médico
            ✅case 9 -> {
                calcularDiasAtestadoMedicoService.diasAtestadoMedico();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal horasDeFaltasMedico = folha.getFaltasHorasMes();
                resultado.put("referencia", horasDeFaltasMedico);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;*/
            }

            //Calculando horas normais noturnas
            ✅case 12 -> {
                calcularHorasNormaisNoturnasService.calcularHorasNormaisNoturnas();
                /*try {
                    FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                    BigDecimal salarioPorHora = folha.getSalarioHora();
                    LocalTime horaFim = folha.getHoraSaida();

                    LocalTime hora22 = LocalTime.parse("22:00");
                    LocalTime hora05 = LocalTime.parse("05:00"); // Fim do período noturno

                    // ✅ 1. CALCULAR HORAS NOTURNAS POR DIA
                    BigDecimal horasNoturnasPorDia = BigDecimal.ZERO;

                    if (horaFim.isAfter(hora22) || horaFim.isBefore(hora05)) {
                        // Trabalha no período noturno
                        Duration horasNoturnas;

                        if (horaFim.isBefore(hora05)) {
                            // Trabalha da madrugada até antes das 5h
                            horasNoturnas = Duration.between(LocalTime.MIDNIGHT, horaFim)
                                    .plus(Duration.between(hora22, LocalTime.MAX)); // 22h até 23:59
                        } else {
                            // Trabalha das 22h até hora de saída
                            horasNoturnas = Duration.between(hora22, horaFim);
                        }

                        // Converter para horas decimais
                        double horasDecimais = horasNoturnas.toHours() +
                                horasNoturnas.toMinutesPart() / 60.0;

                        // Aplicar fator de redução (52,5 minutos por hora noturna)
                        horasNoturnasPorDia = BigDecimal.valueOf(horasDecimais * 1.142857)
                                .setScale(2, RoundingMode.HALF_UP);
                    }

                    // ✅ 2. CALCULAR DIAS ÚTEIS NO MÊS - UMA ÚNICA VEZ
                    int year = LocalDate.now().getYear();
                    int month = LocalDate.now().getMonthValue();
                    int diasUteis = calculoBaseService.calcularDiasUteisNoMes(year, month);

                    // ✅ 3. CÁLCULO FINAL DAS HORAS NOTURNAS
                    BigDecimal totalHorasNoturnasMes = horasNoturnasPorDia.multiply(new BigDecimal(diasUteis)).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal valorTotal = totalHorasNoturnasMes.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", totalHorasNoturnasMes);
                    resultado.put("vencimentos", valorTotal);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular horas noturnas: " + e.getMessage());
                }*/
            }

            //Calculando o Adicional Noturno
            ✅case 14 -> {
                calcularAdicionalNoturnoService.calcularAdicionalNoturno();
                /*try {
                    FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                    BigDecimal salarioPorHora = folha.getSalarioHora();
                    BigDecimal percentualAdicionalNoturno = folha.getPercentualAdicionalNoturno();
                    LocalTime horaFim = folha.getHoraSaida();

                    LocalTime hora22 = LocalTime.parse("22:00");
                    LocalTime hora05 = LocalTime.parse("05:00");

                    // ✅ 1. CALCULAR HORAS NOTURNAS TRABALHADAS (mesma lógica do caso 12)
                    BigDecimal horasNoturnasTrabalhadas = BigDecimal.ZERO;

                    if (horaFim.isAfter(hora22) || horaFim.isBefore(hora05)) {
                        Duration horasNoturnas;

                        if (horaFim.isBefore(hora05)) {
                            // Trabalha da madrugada até antes das 5h
                            horasNoturnas = Duration.between(LocalTime.MIDNIGHT, horaFim)
                                    .plus(Duration.between(hora22, LocalTime.MAX));
                        } else {
                            // Trabalha das 22h até hora de saída
                            horasNoturnas = Duration.between(hora22, horaFim);
                        }

                        double horasDecimais = horasNoturnas.toHours() + horasNoturnas.toMinutesPart() / 60.0;
                        horasNoturnasTrabalhadas = BigDecimal.valueOf(horasDecimais * 1.142857)
                                .setScale(2, RoundingMode.HALF_UP);
                    }

                    // ✅ 2. CALCULAR HORAS NOTURNAS NO DSR
                    BigDecimal horasNoturnasDSR = BigDecimal.ZERO;
                    if (horasNoturnasTrabalhadas.compareTo(BigDecimal.ZERO) > 0) {
                        int year = LocalDate.now().getYear();
                        int month = LocalDate.now().getMonthValue();
                        int diasRepouso = calculoBaseService.calcularDiasRepousoNoMes(year, month);

                        horasNoturnasDSR = horasNoturnasTrabalhadas
                                .multiply(new BigDecimal(diasRepouso))
                                .setScale(2, RoundingMode.HALF_UP);
                    }

                    // ✅ 3. CÁLCULO FINAL DO ADICIONAL NOTURNO
                    BigDecimal totalHorasNoturnas = horasNoturnasTrabalhadas.add(horasNoturnasDSR);

                    BigDecimal valorAdicionalNoturno = totalHorasNoturnas
                            .multiply(salarioPorHora)
                            .multiply(percentualAdicionalNoturno)
                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

                    resultado.put("referencia", totalHorasNoturnas);
                    resultado.put("vencimentos", valorAdicionalNoturno);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular adicional noturno: " + e.getMessage());
                }*/
            }

            //Calculando o Pro-Labore
            ✅case 17 -> {
                calcularProLaboreService.calcularProLabore();
                /*try {
                    FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                    BigDecimal proLabore = folha.getSalarioBase();

                    // ✅ Validação do valor
                    if (proLabore == null || proLabore.compareTo(BigDecimal.ZERO) <= 0) {
                        proLabore = BigDecimal.ZERO;
                    }

                    // ✅ Para Pro-Labore, geralmente há descontos de INSS e IRRF

                    resultado.put("referencia", BigDecimal.ONE); // Referência = 1 (valor fixo)
                    resultado.put("vencimentos", proLabore);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (Exception e) {
                    throw new DataIntegrityViolationException("Erro ao calcular pró-labore: " +e);
                }*/
            }

            //Calculando Bolsa Auxílio
            ✅case 19 -> {
                calcularBolsaAuxilioService.calcularBolsaAuxilio();

                /*BigDecimal bolsaAuxilio;
                try {
                    if(tipoSalario.equals("Quinzenal")){
                        FolhaQuinzenal folha = calculoBaseService.findByMatriculaFuncionario(numeroMatricula);
                        bolsaAuxilio = folha.getSalarioBase();
                    }else{
                        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                        bolsaAuxilio = folha.getSalarioBase();
                    }

                    if (bolsaAuxilio == null || bolsaAuxilio.compareTo(BigDecimal.ZERO) <= 0) {
                        bolsaAuxilio = BigDecimal.ZERO;
                    }

                    resultado.put("referencia", BigDecimal.ONE);
                    resultado.put("vencimentos", bolsaAuxilio);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (Exception e) {
                    throw new DataIntegrityViolationException("Erro ao calcular bolsa auxílio: " +e);
                }*/
            }

            //Calculando Horas Repouso Remunerado (DSR) Noturno
           ✅case 25 -> {
                calcularHorasRepousoRemuneradoNoturnoService.calcularHorasRepousoRemuneradoNoturno();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
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
                }*/
            }

            //Calculando DSR Sobre Hora Extra Diurna 50%
           ✅case 26 -> {
                calcularDsrSobreHoraExtraDiurna50PorcentoService.calcularDsrSobreHoraExtraDiurna50Porcento();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal quantidadeHoraExtra50 = folha.getHorasExtras50();

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
                BigDecimal quantidadeHorasExtrasDiurnas50 = quantidadeHoraExtra50.multiply(BigDecimal.valueOf(workingDaysNaoDiurn50)).divide(BigDecimal.valueOf(workingDaysDiurn50), 2, RoundingMode.HALF_UP);
                BigDecimal dsrSobreHoraExtraDiurna = (quantidadeHoraExtra50.divide(new BigDecimal(workingDaysDiurn50), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(workingDaysNaoDiurn50));

                resultado.put("referencia", quantidadeHorasExtrasDiurnas50);
                resultado.put("vencimentos", dsrSobreHoraExtraDiurna);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;*/
            }

            //Calculando DSR Sobre Hora Extra Diurna 70%
           ✅case 27 -> {
                calcularDsrSobreHoraExtraDiurna70PorcentoService.calcularDsrSobreHoraExtraDiurna70Porcento();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal quantidadeHoraExtra70 = folha.getHorasExtras70();

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
                BigDecimal quantidadeHorasExtrasDiurnas70 = quantidadeHoraExtra70.multiply(BigDecimal.valueOf(workingDaysNaoDiurn70)).divide(BigDecimal.valueOf(workingDaysDiurn70), 2, RoundingMode.HALF_UP);
                BigDecimal dsrSobreHoraExtraDiurna70 = (quantidadeHoraExtra70.divide(new BigDecimal(workingDaysDiurn70), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(workingDaysNaoDiurn70));

                resultado.put("referencia", quantidadeHorasExtrasDiurnas70);
                resultado.put("vencimentos", dsrSobreHoraExtraDiurna70);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;*/
            }

            //Calculando DSR Sobre Hora Extra Diurna 100%
           ✅case 28 -> {
                calcularDsrSobreHoraExtraDiurna100PorcentoService.calcularDsrSobreHoraExtraDiurna100Porcento();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal quantidadeHoraExtra100 = folha.getHorasExtras100();

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
                BigDecimal quantidadeHorasExtrasDiurnas100 = quantidadeHoraExtra100.multiply(BigDecimal.valueOf(workingDaysNaoDiurn100)).divide(BigDecimal.valueOf(workingDaysDiurn100), 2, RoundingMode.HALF_UP);
                BigDecimal dsrSobreHExtraDiurna100 = (quantidadeHoraExtra100.divide(new BigDecimal(workingDaysDiurn100), 2, RoundingMode.HALF_UP)).multiply(new BigDecimal(workingDaysNaoDiurn100));
                resultado.put("referencia", quantidadeHorasExtrasDiurnas100);
                resultado.put("vencimentos", dsrSobreHExtraDiurna100);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;*/
            }

            //Calculando a Insalubridade
           ✅case 46 -> {
                calcularInsalubridadeService.calcularInsalubridadeService();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal valorSalarioMinimo = obtemSalarioMinimo();

                BigDecimal porcentagemInsalubre = folha.getInsalubridade();
                BigDecimal valorInsalubre = (valorSalarioMinimo.multiply(porcentagemInsalubre)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
                resultado.put("referencia", porcentagemInsalubre);
                resultado.put("vencimentos", valorInsalubre);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;*/
            }

            //Calculando a Periculosidade
           ✅case 47 -> {
                calcularPericulosidadeService.calculoPericulosidade();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();
                BigDecimal porcentagemPericuloso = folha.getPericulosidade();
                BigDecimal valorPericuloso = (salarioBase.multiply(porcentagemPericuloso)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
                resultado.put("referencia", porcentagemPericuloso);
                resultado.put("vencimentos", valorPericuloso);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;*/
            }

            //Calculando a Comissão
          ✅case 51 -> {
                calcularComissaoService.calcularComissao();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);

                BigDecimal percentualComissao = folha.getComissao();
                BigDecimal vendasMes = folha.getValorVendaMes();

                BigDecimal valorComissao = BigDecimal.ZERO;

                if (percentualComissao != null && vendasMes != null && percentualComissao.compareTo(BigDecimal.ZERO) > 0 && vendasMes.compareTo(BigDecimal.ZERO) > 0) {
                    valorComissao = percentualComissao.multiply(vendasMes).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                }
                resultado.put("referencia", vendasMes);
                resultado.put("vencimentos", valorComissao);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;*/
            }

            //Calculando a Gratificação
          ✅case 53 -> {
                calculoGratificacaoService.calcularGratificacao();
                /*FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal valorGratifica = folha.getGratificacao();
                resultado.put("referencia", valorGratifica);
                resultado.put("vencimentos", valorGratifica);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;*/
            }

            //Calculando a Quebra Caixa
            case 54 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal horasPorMes = folha.getHorasMes();

                BigDecimal valorQuebraCaixa = folha.getQuebraCaixa();
                BigDecimal quebCaixa = (valorQuebraCaixa.multiply(valorReferenteHoraDiurna)).divide(horasPorMes,2, RoundingMode.HALF_UP);

                resultado.put("referencia", valorQuebraCaixa);
                resultado.put("vencimentos", quebCaixa);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //Calculando horas extras 50% feitas no mês.
            case 98 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal percentualInsalubridade = folha.getInsalubridade();
                BigDecimal totalHoraExtra50 = folha.getHorasExtras50();
                BigDecimal salarioPorHora = folha.getSalarioHora();

                if (percentualInsalubridade.compareTo(BigDecimal.ZERO) == 0) {

                    BigDecimal valorHoraExtra50Mes = (salarioPorHora.multiply(new BigDecimal("1.5"))).multiply(totalHoraExtra50);
                    resultado.put("referencia", totalHoraExtra50);
                    resultado.put("vencimentos", valorHoraExtra50Mes);
                    resultado.put("descontos", BigDecimal.ZERO);

                } else {

                    BigDecimal valorSalarioMinimo = obtemSalarioMinimo();
                    BigDecimal horasTrabNoMes = folha.getHorasMes();
                    BigDecimal valorInsalubre = ((valorSalarioMinimo.multiply(percentualInsalubridade)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP)).divide(horasTrabNoMes,2, RoundingMode.HALF_UP); // Calcula o valor da insalubridade e divide o valor pelas hora strabalhadas no mês

                    BigDecimal valorHoraExtra50Mes = ((salarioPorHora.add(valorInsalubre)).multiply(new BigDecimal("1.5"))).multiply(totalHoraExtra50).setScale(2, RoundingMode.HALF_UP);
                    resultado.put("referencia", totalHoraExtra50);
                    resultado.put("vencimentos", valorHoraExtra50Mes);
                    resultado.put("descontos", BigDecimal.ZERO);
                }

                return resultado;
            }

            //Calculando horas extras 70% feitas no mês
            case 99 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal percentualInsalubridade = folha.getInsalubridade();
                BigDecimal totalHoraExtra70 = folha.getHorasExtras70();
                BigDecimal salarioPorHora = folha.getSalarioHora();

                if (percentualInsalubridade.compareTo(BigDecimal.ZERO) == 0) {

                    BigDecimal valorHoraExtra70Mes = (salarioPorHora.multiply(new BigDecimal("1.7"))).multiply(totalHoraExtra70).setScale(2, RoundingMode.HALF_UP);
                    resultado.put("referencia", totalHoraExtra70);
                    resultado.put("vencimentos", valorHoraExtra70Mes);
                    resultado.put("descontos", BigDecimal.ZERO);

                } else {

                    BigDecimal valorSalarioMinimo = obtemSalarioMinimo();
                    BigDecimal horasTrabNoMes = folha.getHorasMes();
                    BigDecimal valorInsalubre = ((valorSalarioMinimo.multiply(percentualInsalubridade)).divide(new BigDecimal("100"),2, RoundingMode.HALF_UP)).divide(horasTrabNoMes, 2, RoundingMode.HALF_UP); // Calcula o valor da insalubridade e divide o valor pelas hora strabalhadas no mês

                    BigDecimal valorHoraExtra70Mes = ((salarioPorHora.add(valorInsalubre)).multiply(new BigDecimal("1.7"))).multiply(totalHoraExtra70).setScale(2, RoundingMode.HALF_UP);
                    resultado.put("referencia", totalHoraExtra70);
                    resultado.put("vencimentos", valorHoraExtra70Mes);
                    resultado.put("descontos", BigDecimal.ZERO);

                }

                return resultado;
            }

            //Calculando horas extras 100% feitas no mês.
            case 100 -> {

                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal percentualInsalubridade = folha.getInsalubridade();
                BigDecimal totalHoraExtra100 = folha.getHorasExtras100();
                BigDecimal salarioPorHora = folha.getSalarioHora();

                if ((percentualInsalubridade.compareTo(BigDecimal.ZERO) == 0)) {

                    BigDecimal valorHoraExtra100Mes = (salarioPorHora.multiply(new BigDecimal("2"))).multiply(totalHoraExtra100);
                    resultado.put("referencia", totalHoraExtra100);
                    resultado.put("vencimentos", valorHoraExtra100Mes);
                    resultado.put("descontos", BigDecimal.ZERO);

                } else {

                    BigDecimal valorSalarioMinimo = obtemSalarioMinimo();
                    BigDecimal horasTrabNoMes = folha.getHorasMes();
                    BigDecimal valorInsalubre = ((valorSalarioMinimo.multiply(percentualInsalubridade)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)).divide(horasTrabNoMes,2, RoundingMode.HALF_UP); // Calcula o valor da insalubridade e divide o valor pelas hora strabalhadas no mês

                    BigDecimal valorHoraExtra100Mes= ((salarioPorHora.add(valorInsalubre)).multiply(new BigDecimal(2))).multiply(totalHoraExtra100).setScale(2, RoundingMode.HALF_UP);
                    resultado.put("referencia", totalHoraExtra100);
                    resultado.put("vencimentos", valorHoraExtra100Mes);
                    resultado.put("descontos", BigDecimal.ZERO);
                }

                return resultado;
            }

            //Calculando o Salário Maternidade
            case 101 -> {

                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioMaternidade = folha.getSalarioBase();
                resultado.put("referencia", salarioMaternidade);
                resultado.put("vencimentos", salarioMaternidade);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //Calculando Média Horas Extras 50% Sobre Salário Maternidade
            case 102 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioPorHora = folha.getSalarioHora();

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

                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioPorHora = folha.getSalarioHora();

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

                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioPorHora = folha.getSalarioHora();

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

                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioPorHora = folha.getSalarioHora();

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

                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal valorCotaSalarioFamilia = tabelaDeducaoInssService.getSalarioFamilia();
                int numeroDependentes = folha.getDependentesIrrf();

                try {
                    BigDecimal valorSalarioFamilia = calculoBaseService.calcularSalarioFamilia(valorCotaSalarioFamilia, numeroDependentes);

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
                if(tipoSalario.equals("Quinzenal")) {
                    FolhaQuinzenal folha = calculoBaseService.findByMatriculaFuncionario(numeroMatricula);
                    BigDecimal valorReferencia = folha.getEventos().stream()
                            .filter(evento -> evento.getCodigoEvento().equals(130))
                            .findFirst()
                            .map(evento -> {
                                try { return new BigDecimal(evento.getReferencia()); }
                                catch (NumberFormatException e) { return BigDecimal.ZERO; }
                            })
                            .orElse(BigDecimal.ZERO);

                    BigDecimal valorAjudaCusto = valorReferencia.multiply(folha.getTransporteDia());
                    resultado.put("referencia", valorReferencia);
                    resultado.put("vencimentos", valorAjudaCusto);
                } else {
                    FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                    BigDecimal valorVT = folha.getValorValeTransporte();
                    resultado.put("referencia", valorVT);
                    resultado.put("vencimentos", valorVT);
                }
                resultado.put("descontos", BigDecimal.ZERO);
            }

            //Calculando Primeira Parcela 13°
            case 167 -> {
                BigDecimal salarioBase;
                if(tipoSalario.equals("Quinzenal")) {
                    FolhaQuinzenal folha = calculoBaseService.findByMatriculaFuncionario(numeroMatricula);
                    salarioBase = folha.getSalarioBase();
                }else{
                    FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                    salarioBase = folha.getSalarioBase();
                }

                try {
                    LocalDate dataAdmissao = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getDataAdmissao();

                    // Calcular meses trabalhados considerando a regra dos 15 dias
                    int mesesTrabalhados = calculoBaseService.calcularMesesTrabalhados13o(dataAdmissao, LocalDate.now());

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

                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioPorHora = folha.getSalarioHora();

                try {
                      Map<String, BigDecimal> resultadoHE50 = calculoBaseService.calcularMediaHE50PrimeiraParcela13(numeroMatricula, salarioPorHora);

                    // Atualiza o resultado principal
                    resultado.putAll(resultadoHE50);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 50% Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 70% Sobre 1° Parcela do 13°
            case 169 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioPorHora = folha.getSalarioHora();

                try {
                    Map<String, BigDecimal> resultadoHE70 = calculoBaseService.calcularMediaHE70PrimeiraParcela13(numeroMatricula, salarioPorHora);

                    // Atualiza o resultado principal
                    resultado.putAll(resultadoHE70);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 70% Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 100% Sobre 1° Parcela do 13°
            case 170 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioPorHora = folha.getSalarioHora();

                try {
                    Map<String, BigDecimal> resultadoHE100 = calculoBaseService.calcularMediaHE100PrimeiraParcela13(numeroMatricula, salarioPorHora);

                    // Atualiza o resultado principal
                    resultado.putAll(resultadoHE100);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 100% Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Décimo terceiro cheio Adiantado
            case 171 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();

                resultado.put("vencimentos", salarioBase);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //Calculando Média de DSR Noturno Sobre 1° Parcela do 13°
            case 177 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();
                BigDecimal salarioBase = folha.getSalarioBase();

                LocalDate hoje = LocalDate.now();
                int mesesTrabalhados = calculoBaseService.calcularMesesTrabalhados13o(dataAdmissao,hoje);

                try {
                    Map<String, BigDecimal> resultadoDSRNoturno = calculoBaseService.calcularMediaDSRNoturnoPrimeiraParcela13(numeroMatricula, salarioBase, mesesTrabalhados);
                    resultado.putAll(resultadoDSRNoturno);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de DSR Noturno Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Insalubridade sobre Primeira Parcela do 13°
            case 178 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();
                BigDecimal salarioBase = folha.getSalarioBase();

                LocalDate hoje = LocalDate.now();
                int mesesTrabalhados = calculoBaseService.calcularMesesTrabalhados13o(dataAdmissao,hoje);

                try {

                    Map<String, BigDecimal> resultadoInsalubridade = calculoBaseService.calcularInsalubridadePrimeiraParcela13(numeroMatricula, salarioBase, mesesTrabalhados);
                    resultado.putAll(resultadoInsalubridade);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Insalubridade Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Periculosidade sobre Primeira Parcela do 13°
            case 179 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();
                BigDecimal salarioBase = folha.getSalarioBase();

                LocalDate hoje = LocalDate.now();
                int mesesTrabalhados = calculoBaseService.calcularMesesTrabalhados13o(dataAdmissao,hoje);

                try {
                  Map<String, BigDecimal> resultadoPericulosidade = calculoBaseService.calcularPericulosidadePrimeiraParcela13(numeroMatricula, salarioBase, mesesTrabalhados);
                    resultado.putAll(resultadoPericulosidade);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Periculosidade Sobre 1° Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 50% Sobre 2° Parcela do 13°
            case 182 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal horasTrabalhadasPorMes = folha.getHorasMes();
                BigDecimal salarioBase = folha.getSalarioBase();
                try {
                    Map<String, BigDecimal> resultadoMediaHE50 = calculoBaseService.calcularMediaHE50SegundaParcela13(numeroMatricula, salarioBase, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoMediaHE50);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 50% para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 70% Sobre 2° Parcela do 13°
            case 183 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal horasTrabalhadasPorMes = folha.getHorasMes();
                BigDecimal salarioBase = folha.getSalarioBase();

                try {
                    Map<String, BigDecimal> resultadoMediaHE70 = calculoBaseService.calcularMediaHE70SegundaParcela13(numeroMatricula, salarioBase, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoMediaHE70);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 70% para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de Horas Extras 100% Sobre 2° Parcela do 13°
            case 184 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal horasTrabalhadasPorMes = folha.getHorasMes();
                BigDecimal salarioBase = folha.getSalarioBase();

                try {
                    Map<String, BigDecimal> resultadoMediaHE100 = calculoBaseService.calcularMediaHE100SegundaParcela13(numeroMatricula, salarioBase, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoMediaHE100);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de Horas Extras 100% para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de DSR Diurno Sobre 2° Parcela do 13°
            case 185 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();

                try {
                     Map<String, BigDecimal> resultadoMediaDsr13 = calculoBaseService.calcularMediaDSRSegundaParcela13(numeroMatricula, dataAdmissao);
                     resultado.putAll(resultadoMediaDsr13);
                     return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de DSR para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Média de DSR Noturno Sobre 2° Parcela do 13°
            case 186 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();

                try {
                    Map<String, BigDecimal> resultadoMediaDsrNoturno13 = calculoBaseService.calcularMediaDSRNoturnoSegundaParcela13(numeroMatricula, dataAdmissao);
                    resultado.putAll(resultadoMediaDsrNoturno13);
                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Média de DSR Noturno para 2ª Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Insalubridade sobre Segunda Parcela do 13°
            case 187 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();
                BigDecimal salarioBase = folha.getSalarioBase();

                try {
                    Map<String, BigDecimal> resultadoInsalubreSegundaParcela13 = calculoBaseService.calcularInsalubridadeSegundaParcela13(numeroMatricula, salarioBase, dataAdmissao);
                    resultado.putAll(resultadoInsalubreSegundaParcela13);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Insalubridade sobre Segunda Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando Periculosidade sobre Segunda Parcela do 13°
            case 188 ->{
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();
                LocalDate dataAdmissao = folha.getDataAdmissao();
                try {
                    Map<String, BigDecimal> resultadoPericulosoSegundaParcela13 = calculoBaseService.calcularPericulosidadeSegundaParcela13(numeroMatricula, salarioBase, dataAdmissao);
                    resultado.putAll(resultadoPericulosoSegundaParcela13);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Periculosidade sobre Segunda Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando a Segunda Parcela do 13°
            case 189 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();
                LocalDate dataAdmissao = folha.getDataAdmissao();
                BigDecimal horasTrabalhadasPorMes = folha.getHorasMes();
                int numeroDependentes = folha.getDependentesIrrf();

                try {
                    Map<String, BigDecimal> resultadoSegundaParcela13 = calculoBaseService.calcularSegundaParcela13(numeroMatricula, salarioBase, dataAdmissao, numeroDependentes, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoSegundaParcela13);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular Segunda Parcela do 13°: " + e.getMessage());
                }
            }

            //Calculando 13.º Salário Final Média Comissões
            case 195 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();
                BigDecimal salarioBase = folha.getSalarioBase();

                try {
                    Map<String, BigDecimal> resultado13Comissoes = calculoBaseService.calcularDecimoTerceiroComMediaComissoes(numeroMatricula, salarioBase, dataAdmissao);
                    resultado.putAll(resultado13Comissoes);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular 13º com média de comissões: " + e.getMessage());
                }
            }

            //Calculando o Complemento Média Hora Extra 50% do 13°
            case 201 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();
                LocalDate dataAdmissao = folha.getDataAdmissao();
                BigDecimal horasTrabalhadasPorMes = folha.getHorasMes();

                try {
                    Map<String, BigDecimal> resultadoComplementoHE5013 = calculoBaseService.calcularComplementoMediaHE5013(numeroMatricula, salarioBase, dataAdmissao, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoComplementoHE5013);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular complemento média HE 50% do 13º: " + e.getMessage());
                }
            }

            //Calculando o Complemento Média Hora Extra 70% do 13°
            case 202 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();
                LocalDate dataAdmissao = folha.getDataAdmissao();
                BigDecimal horasTrabalhadasPorMes = folha.getHorasMes();

                try {
                    Map<String, BigDecimal> resultadoComplementoHE7013 = calculoBaseService.calcularComplementoMediaHE7013(numeroMatricula, salarioBase, dataAdmissao, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoComplementoHE7013);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular complemento média HE 70% do 13º: " + e.getMessage());
                }
            }

            //Calculando o Complemento Média Hora Extra 100% do 13°
            case 203 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal salarioBase = folha.getSalarioBase();
                LocalDate dataAdmissao = folha.getDataAdmissao();
                BigDecimal horasTrabalhadasPorMes = folha.getHorasMes();

                try {
                    Map<String, BigDecimal> resultadoComplementoHE10013 = calculoBaseService.calcularComplementoMediaHE10013(numeroMatricula, salarioBase, dataAdmissao, horasTrabalhadasPorMes);
                    resultado.putAll(resultadoComplementoHE10013);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular complemento média HE 100% do 13º: " + e.getMessage());
                }
            }

            //Calculando Complemento DSR Diurno Sobre o 13°
            case 204 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();
                try {
                    Map<String, BigDecimal> resultadoDSRDiurno = calculoBaseService.calcularComplementoDSRDiurno13(numeroMatricula, dataAdmissao);
                    resultado.putAll(resultadoDSRDiurno);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular complemento DSR Diurno do 13º: " + e.getMessage());
                }
            }

            //Calculando Complemento DSR Noturno Sobre o 13°
            case 205 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();
                try {
                    Map<String, BigDecimal> resultadoDSRNoturno = calculoBaseService.calcularComplementoDSRNoturno13(numeroMatricula, dataAdmissao);
                    resultado.putAll(resultadoDSRNoturno);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular complemento DSR Noturno do 13º: " + e.getMessage());
                }
            }

            //Média Adicional Noturno do 13° Salário
            case 206 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                LocalDate dataAdmissao = folha.getDataAdmissao();

                try {
                    Map<String, BigDecimal> resultadoAdicionalNoturno = calculoBaseService.calcularMediaAdicionalNoturno13(numeroMatricula, dataAdmissao);
                    resultado.putAll(resultadoAdicionalNoturno);
                    return resultado;
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular média adicional noturno do 13º: " + e.getMessage());
                }
            }

            //Cálculo do Vale Transporte
            case 239 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal valorUnitarioVT = folha.getValorValeTransporte();
                String jornada = folha.getJornada();

                try {

                    BigDecimal diasTrabalhados = BigDecimal.ZERO;
                    BigDecimal valorVT = BigDecimal.ZERO;

                    LocalDate hoje = LocalDate.now();
                    YearMonth anoMesAtual = YearMonth.of(hoje.getYear(), hoje.getMonth());
                    int diasNoMes = anoMesAtual.lengthOfMonth();

                    // Calcular feriados uma única vez
                    Set<LocalDate> feriados = new HashSet<>();
                    feriados.addAll(calendario.getFeriadosFixos(hoje.getYear()));
                    feriados.addAll(calendario.getFeriadosMoveis(hoje.getYear()));

                    switch (jornada) {
                        case "6 x 1" -> {
                            // Trabalha 6 dias, folga 1 - conta todos os dias exceto domingos e feriados
                            int diasUteis = 0;
                            for (int dia = 1; dia <= diasNoMes; dia++) {
                                LocalDate data = anoMesAtual.atDay(dia);
                                if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                    diasUteis++;
                                }
                            }
                            diasTrabalhados = new BigDecimal(diasUteis);
                        }

                        case "4 x 2" -> {
                            // Trabalha 4 dias, folga 2 - aproximadamente 20 dias por mês
                            diasTrabalhados = new BigDecimal("20");
                        }

                        case "5 x 1" -> {
                            // Trabalha 5 dias, folga 1 - depende dos dias do mês
                            if (diasNoMes == 28) {
                                diasTrabalhados = new BigDecimal("25");
                            } else if (diasNoMes == 30) {
                                diasTrabalhados = new BigDecimal("26");
                            } else if (diasNoMes == 31) {
                                diasTrabalhados = new BigDecimal("27");
                            } else {
                                // Cálculo dinâmico para outros meses
                                int diasUteis = 0;
                                for (int dia = 1; dia <= diasNoMes; dia++) {
                                    LocalDate data = anoMesAtual.atDay(dia);
                                    if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                                        diasUteis++;
                                    }
                                }
                                diasTrabalhados = new BigDecimal(diasUteis);
                            }
                        }

                        case "5 x 2" -> {
                            // Trabalha de segunda a sexta, folga sábado e domingo
                            int diasUteis = 0;
                            for (int dia = 1; dia <= diasNoMes; dia++) {
                                LocalDate data = anoMesAtual.atDay(dia);
                                if (data.getDayOfWeek() != DayOfWeek.SATURDAY &&
                                        data.getDayOfWeek() != DayOfWeek.SUNDAY &&
                                        !feriados.contains(data)) {
                                    diasUteis++;
                                }
                            }
                            diasTrabalhados = new BigDecimal(diasUteis);
                        }

                        case "12 x 36" -> {
                            // Escala 12x36 - aproximadamente 15 dias por mês
                            diasTrabalhados = new BigDecimal("15");
                        }

                        case "24 x 48" -> {
                            // Escala 24x48 - depende dos dias do mês
                            if (diasNoMes == 28) {
                                diasTrabalhados = new BigDecimal("10");
                            } else if (diasNoMes == 30) {
                                diasTrabalhados = new BigDecimal("10");
                            } else if (diasNoMes == 31) {
                                diasTrabalhados = new BigDecimal("11");
                            } else {
                                // Cálculo padrão para escala 24x48
                                diasTrabalhados = new BigDecimal("10");
                            }
                        }

                        default -> {
                            diasTrabalhados = new BigDecimal("22"); // Padrão
                        }
                    }

                    // Cálculo final do Vale Transporte
                    valorVT = diasTrabalhados.multiply(valorUnitarioVT).setScale(2, RoundingMode.HALF_UP);

                    // Se quiser usar no sistema de eventos calculados:
                    resultado.put("referencia", diasTrabalhados);
                    resultado.put("vencimentos", valorVT);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (Exception e) {
                    logger.error("Erro ao calcular Vale Transporte: {}", e.getMessage());
                    throw new DataIntegrityViolationException("Erro ao calcular vale transporte " +e);
                }
            }

            //Cálculo Vale Creche
            case 259 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                BigDecimal valorValeCreche = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getValeCreche();

                try {
                    // Validação do valor
                    if (valorValeCreche.compareTo(BigDecimal.ZERO) < 0) {
                        valorValeCreche = BigDecimal.ZERO;
                    }

                    // Limite mensal por dependente (ajuste conforme sua política)
                    BigDecimal limiteMensal = new BigDecimal("500.00");
                    if (valorValeCreche.compareTo(limiteMensal) > 0) {
                        valorValeCreche = limiteMensal;
                    }

                    // Para o sistema de eventos calculados
                    resultado.put("referencia", BigDecimal.ONE);
                    resultado.put("vencimentos", valorValeCreche);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (NumberFormatException e) {
                    logger.error("Valor do Vale Creche inválido: {}", e.getMessage());
                    throw new DataIntegrityViolationException("Erro ao calcular vale-creche " +e);
                } catch (Exception e) {
                    logger.error("Erro ao calcular Vale Creche: {}", e.getMessage());
                    throw new DataIntegrityViolationException("Erro ao calcular vale-creche " +e);
                }
            }

            //Calculando FGTS Sobre o Salário
            case 402 -> {
                FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
                String descricaoDoCargo = folha.getCargoFuncionario();
                BigDecimal salarioBase = folha.getSalarioBase();

                try {
                    BigDecimal taxaFGTS;

                    // Definir a taxa conforme o tipo de contrato
                    if (descricaoDoCargo != null && descricaoDoCargo.toLowerCase().contains("aprendiz")) {
                        taxaFGTS = new BigDecimal("0.02"); // 2% para aprendiz
                    } else {
                        taxaFGTS = new BigDecimal("0.08"); // 8% para demais empregados
                    }

                    // Calcular FGTS
                    BigDecimal valorFGTS = salarioBase.multiply(taxaFGTS).setScale(2, RoundingMode.HALF_UP);

                    // Para sistema de eventos calculados
                    resultado.put("referencia", taxaFGTS.multiply(new BigDecimal("100"))); // Percentual
                    resultado.put("vencimentos", valorFGTS);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (Exception e) {
                    // Em caso de erro
                    resultado.put("referencia", BigDecimal.ZERO);
                    resultado.put("vencimentos", BigDecimal.ZERO);
                    resultado.put("descontos", BigDecimal.ZERO);

                    logger.error("Erro ao calcular FGTS sobre salário: {}", e.getMessage());
                }
            }

            //Calculando FGTS Sobre 13º Salário
            case 403 -> {
                try {
                    // ✅ Busca apenas UMA vez e reutiliza os dados
                    FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);

                    String descricaoDoCargo = folha.getCargoFuncionario();
                    BigDecimal salarioBase = folha.getSalarioBase();
                    LocalDate dataAdmissao = folha.getDataAdmissao();

                    BigDecimal taxaFGTS;

                    // Definir taxa
                    if (descricaoDoCargo != null && descricaoDoCargo.toLowerCase().contains("aprendiz")) {
                        taxaFGTS = new BigDecimal("0.02");
                    } else {
                        taxaFGTS = new BigDecimal("0.08");
                    }

                    // Calcular meses trabalhados
                    int mesesTrabalhados = calculoBaseService.calcularMesesTrabalhados13o(dataAdmissao, LocalDate.now());

                    // Cálculo do 13º (integral ou proporcional)
                    BigDecimal decimoTerceiroIntegral = salarioBase.multiply(new BigDecimal(mesesTrabalhados)).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

                    // Calcular FGTS sobre o 13º
                    BigDecimal valorFGTS13 = decimoTerceiroIntegral.multiply(taxaFGTS)
                            .setScale(2, RoundingMode.HALF_UP);

                    resultado.put("referencia", new BigDecimal(mesesTrabalhados)); // Meses como referência
                    resultado.put("vencimentos", valorFGTS13);
                    resultado.put("descontos", BigDecimal.ZERO);

                    return resultado;

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao calcular FGTS sobre 13º salário: " + e.getMessage());
                }
            }

            //Participação Nos Lucros e Resultados
            case 409 -> {
                BigDecimal participacaoLucrosResultado = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getParticipacaoLucrosResultado();
                resultado.put("referencia", participacaoLucrosResultado);
                resultado.put("vencimentos", participacaoLucrosResultado);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //Abono Salarial
            case 410 -> {
                BigDecimal abonoSalarial = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getAbonoSalarial();
                resultado.put("referencia", abonoSalarial);
                resultado.put("vencimentos", abonoSalarial);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //Cálculo Reembolso Creche
            case 412 -> {
                BigDecimal reembolsoCreche = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getValeCreche();
                resultado.put("referencia", reembolsoCreche);
                resultado.put("vencimentos", reembolsoCreche);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //Cálculo Gratificação Semestral
            case 414 -> {
                BigDecimal gratificacaoSemestral = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getGratificacao();
                resultado.put("referencia", gratificacaoSemestral);
                resultado.put("vencimentos", gratificacaoSemestral);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //Cálculo Reembolso Viagem
            case 416 -> {
                BigDecimal reembolsoViagem = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getReembolsoViagem();
                resultado.put("referencia", reembolsoViagem);
                resultado.put("vencimentos", reembolsoViagem);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //1° Parcela Participação Nos Lucros e Resultados
            case 417 -> {
                BigDecimal plr1 = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getParticipacaoLucrosResultado();
                resultado.put("referencia", plr1);
                resultado.put("vencimentos", plr1);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //2° Parcela Participação Nos Lucros e Resultados
            case 418 -> {
                BigDecimal plr2 = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getParticipacaoLucrosResultado();
                resultado.put("referencia", plr2);
                resultado.put("vencimentos", plr2);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //1° Parcela Abono Salarial
            case 420 -> {
                BigDecimal primeiraParcelAbonoSalarial = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getAbonoSalarial();
                resultado.put("referencia", primeiraParcelAbonoSalarial);
                resultado.put("vencimentos", primeiraParcelAbonoSalarial);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            //2° Parcela Abono Salarial
            case 421 -> {
                BigDecimal segundaParcelAbonoSalarial = calculoBaseService.findByMatriculaColaborador(numeroMatricula).getAbonoSalarial();
                resultado.put("referencia", segundaParcelAbonoSalarial);
                resultado.put("vencimentos", segundaParcelAbonoSalarial);
                resultado.put("descontos", BigDecimal.ZERO);

                return resultado;
            }

            default -> {
                return resultado;
            }
        }
        return resultado;
    }
}