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
public class CalcularDsrSobreHoraExtraDiurna70PorcentoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    Calendario calendario = new Calendario();
    Set<LocalDate> feriados = new HashSet<>();

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularDsrSobreHoraExtraDiurna70Porcento(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
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

        return resultado;
    }
}
