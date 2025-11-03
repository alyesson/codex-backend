package br.com.codex.v1.service.rh.horasextras;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.utilitario.Calendario;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CalcularDsrSobreHoraExtraDiurna50PorcentoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    Calendario calendario = new Calendario();
    Set<LocalDate> feriados = new HashSet<>();

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularDsrSobreHoraExtraDiurna50Porcento(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
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

        return resultado;
    }
}
