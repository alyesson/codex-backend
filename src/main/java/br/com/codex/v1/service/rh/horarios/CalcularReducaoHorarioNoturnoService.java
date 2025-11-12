package br.com.codex.v1.service.rh.horarios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.service.rh.CalculosAuxiliaresFolha;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularReducaoHorarioNoturnoService {

    @Autowired
    private CalculosAuxiliaresFolha calculosAuxiliaresFolha;

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularReducaoHorarioNoturno() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal salarioPorHora = folha.getSalarioHora();
            LocalTime horaIni = folha.getHoraEntrada();
            LocalTime horaFim = folha.getHoraSaida();

            // ✅ 1. CALCULAR A REDUÇÃO HORÁRIA (DIFERENÇA ENTRE HORAS TRABALHADAS E HORAS PAGAS)
            Map<String, BigDecimal> reducao = calcularReducaoHoraria(horaIni, horaFim);

            BigDecimal horasTrabalhadas = reducao.get("horasTrabalhadas");
            BigDecimal horasPagas = reducao.get("horasPagas");
            BigDecimal diferencaReducao = reducao.get("diferenca");

            // ✅ 2. CALCULAR DIAS ÚTEIS NO MÊS
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();
            int diasUteis = calculosAuxiliaresFolha.calcularDiasUteisNoMes(year, month);

            // ✅ 3. CÁLCULO FINAL DA REDUÇÃO MENSAL
            BigDecimal reducaoMensal = diferencaReducao.multiply(new BigDecimal(diasUteis)).setScale(2, RoundingMode.HALF_UP);

            // ✅ 4. VALOR MONETÁRIO DA REDUÇÃO
            BigDecimal valorReducao = reducaoMensal.multiply(salarioPorHora).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", reducaoMensal);        // Horas de redução no mês
            resultado.put("vencimentos", BigDecimal.ZERO);
            resultado.put("descontos", valorReducao);          // Valor da redução (desconto)

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular redução horário noturno: " + e.getMessage());
        }
        return resultado;
    }

    /**
     * ✅ CALCULA A REDUÇÃO HORÁRIA NOTURNA (DIFERENÇA ENTRE HORAS TRABALHADAS E HORAS PAGAS)
     */
    private Map<String, BigDecimal> calcularReducaoHoraria(LocalTime horaIni, LocalTime horaFim) {
        Map<String, BigDecimal> resultado = new HashMap<>();

        LocalTime hora22 = LocalTime.of(22, 0);
        LocalTime hora05 = LocalTime.of(5, 0);

        // ✅ 1. CALCULAR MINUTOS NOTURNOS TRABALHADOS
        long minutosNoturnosTrabalhados = calcularMinutosNoturnosTrabalhados(horaIni, horaFim, hora22, hora05);

        // ✅ 2. CALCULAR HORAS TRABALHADAS (sem redução)
        BigDecimal horasTrabalhadas = BigDecimal.valueOf(minutosNoturnosTrabalhados).divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP); // Minutos → Horas normais

        // ✅ 3. CALCULAR HORAS PAGAS (com redução - 52,5min = 1h)
        BigDecimal horasPagas = BigDecimal.valueOf(minutosNoturnosTrabalhados).divide(BigDecimal.valueOf(52.5), 4, RoundingMode.HALF_UP); // Minutos → Horas noturnas

        // ✅ 4. CALCULAR DIFERENÇA (REDUÇÃO)
        BigDecimal diferenca = horasPagas.subtract(horasTrabalhadas);

        resultado.put("horasTrabalhadas", horasTrabalhadas.setScale(2, RoundingMode.HALF_UP));
        resultado.put("horasPagas", horasPagas.setScale(2, RoundingMode.HALF_UP));
        resultado.put("diferenca", diferenca.setScale(2, RoundingMode.HALF_UP));

        return resultado;
    }

    /**
     * ✅ CALCULA MINUTOS NOTURNOS TRABALHADOS
     */
    private long calcularMinutosNoturnosTrabalhados(LocalTime horaIni, LocalTime horaFim, LocalTime hora22, LocalTime hora05) {
        long minutosNoturnos = 0;

        // ✅ CASO 1: Jornada normal (não cruza meia-noite)
        if (!horaFim.isBefore(horaIni)) {
            minutosNoturnos = calcularMinutosNoturnosJornadaNormal(horaIni, horaFim, hora22, hora05);
        }
        // ✅ CASO 2: Jornada que cruza meia-noite
        else {
            minutosNoturnos = calcularMinutosNoturnosJornadaMadrugada(horaIni, horaFim, hora22, hora05);
        }

        return minutosNoturnos;
    }

    /**
     * ✅ CALCULA MINUTOS NOTURNOS - JORNADA NORMAL
     */
    private long calcularMinutosNoturnosJornadaNormal(LocalTime horaIni, LocalTime horaFim,
                                                      LocalTime hora22, LocalTime hora05) {
        long minutosNoturnos = 0;

        // Verifica se trabalhou no período noturno
        boolean trabalhouNoturno = (horaIni.isBefore(hora05) || horaIni.isAfter(hora22) ||
                horaIni.equals(hora22)) && (horaFim.isBefore(hora05) || horaFim.isAfter(hora22));

        if (trabalhouNoturno) {
            LocalTime inicioNoturno = horaIni.isBefore(hora05) ? LocalTime.MIDNIGHT : (horaIni.isAfter(hora22) ? horaIni : hora22);

            LocalTime fimNoturno = horaFim.isAfter(hora05) ? hora05 : (horaFim.isAfter(hora22) ? horaFim : horaFim);

            if (fimNoturno.isAfter(inicioNoturno)) {
                minutosNoturnos = Duration.between(inicioNoturno, fimNoturno).toMinutes();
            }
        }

        return minutosNoturnos;
    }

    /**
     * ✅ CALCULA MINUTOS NOTURNOS - JORNADA MADRUGADA
     */
    private long calcularMinutosNoturnosJornadaMadrugada(LocalTime horaIni, LocalTime horaFim,
                                                         LocalTime hora22, LocalTime hora05) {
        long minutosNoturnos = 0;

        // Período noturno antes da meia-noite (22h-23:59)
        if (horaIni.isBefore(hora05) || horaIni.isAfter(hora22) || horaIni.equals(hora22)) {
            LocalTime inicioNoturno = horaIni.isAfter(hora22) ? horaIni : hora22;
            minutosNoturnos += Duration.between(inicioNoturno, LocalTime.MAX).toMinutes();
            // Adiciona 1 minuto para incluir o último minuto do dia
            minutosNoturnos += 1;
        }

        // Período noturno após meia-noite (00h-05h)
        if (horaFim.isBefore(hora05)) {
            LocalTime fimNoturno = horaFim;
            minutosNoturnos += Duration.between(LocalTime.MIDNIGHT, fimNoturno).toMinutes();
        }

        return minutosNoturnos;
    }
}