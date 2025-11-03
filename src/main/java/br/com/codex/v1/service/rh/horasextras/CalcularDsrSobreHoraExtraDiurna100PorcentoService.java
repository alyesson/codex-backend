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
public class CalcularDsrSobreHoraExtraDiurna100PorcentoService {

    @Autowired
    private CalculoBaseService calculoBaseService;

    Calendario calendario = new Calendario();
    Set<LocalDate> feriados = new HashSet<>();

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularDsrSobreHoraExtraDiurna100Porcento(){

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
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

        return resultado;
    }
}
