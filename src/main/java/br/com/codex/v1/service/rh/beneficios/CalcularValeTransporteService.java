package br.com.codex.v1.service.rh.beneficios;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.rh.CalculoBaseService;
import br.com.codex.v1.utilitario.Calendario;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class CalcularValeTransporteService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularValeTransporteService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    Calendario calendario = new Calendario();
    Set<LocalDate> feriados = new HashSet<>();

    @Setter
    String numeroMatricula;

    public Map<String, BigDecimal> calcularValeTransporte() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaMensal folha = calculoBaseService.findByMatriculaColaborador(numeroMatricula);
            BigDecimal valorUnitarioVT = folha.getValorValeTransporte();
            String jornada = folha.getJornada();

            // ✅ Validação do valor unitário
            if (valorUnitarioVT == null || valorUnitarioVT.compareTo(BigDecimal.ZERO) <= 0) {
                logger.info("Valor unitário do vale transporte inválido para matrícula {}", numeroMatricula);
                return resultado;
            }

            BigDecimal diasTrabalhados = BigDecimal.ZERO;
            BigDecimal valorVT = BigDecimal.ZERO;

            LocalDate hoje = LocalDate.now();
            YearMonth anoMesAtual = YearMonth.of(hoje.getYear(), hoje.getMonth());
            int diasNoMes = anoMesAtual.lengthOfMonth();

            // ✅ Calcular feriados uma única vez
            feriados.addAll(calendario.getFeriadosFixos(hoje.getYear()));
            feriados.addAll(calendario.getFeriadosMoveis(hoje.getYear()));

            // ✅ Calcular dias trabalhados conforme jornada
            diasTrabalhados = calcularDiasTrabalhados(jornada, anoMesAtual, diasNoMes);

            // ✅ Cálculo final do Vale Transporte
            valorVT = diasTrabalhados.multiply(valorUnitarioVT).setScale(2, RoundingMode.HALF_UP);

            resultado.put("referencia", diasTrabalhados);
            resultado.put("vencimentos", valorVT);

            logger.info("Vale transporte calculado para {}: {} dias × R$ {} = R$ {}", numeroMatricula, diasTrabalhados, valorUnitarioVT, valorVT);

        } catch (Exception e) {
            logger.error("Erro ao calcular vale transporte para matrícula {}: {}",
                    numeroMatricula, e.getMessage());
            throw new DataIntegrityViolationException("Erro ao calcular vale transporte: " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Calcula dias trabalhados conforme o tipo de jornada
     */
    private BigDecimal calcularDiasTrabalhados(String jornada, YearMonth anoMesAtual, int diasNoMes) {
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
                return new BigDecimal(diasUteis);
            }

            case "4 x 2" -> {
                // Trabalha 4 dias, folga 2 - aproximadamente 20 dias por mês
                return new BigDecimal("20");
            }

            case "5 x 1" -> {
                // Trabalha 5 dias, folga 1 - depende dos dias do mês
                if (diasNoMes == 28) {
                    return new BigDecimal("25");
                } else if (diasNoMes == 30) {
                    return new BigDecimal("26");
                } else if (diasNoMes == 31) {
                    return new BigDecimal("27");
                } else {
                    // Cálculo dinâmico para outros meses
                    int diasUteis = 0;
                    for (int dia = 1; dia <= diasNoMes; dia++) {
                        LocalDate data = anoMesAtual.atDay(dia);
                        if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                            diasUteis++;
                        }
                    }
                    return new BigDecimal(diasUteis);
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
                return new BigDecimal(diasUteis);
            }

            case "12 x 36" -> {
                // Escala 12x36 - aproximadamente 15 dias por mês
                return new BigDecimal("15");
            }

            case "24 x 48" -> {
                // Escala 24x48 - depende dos dias do mês
                if (diasNoMes == 28) {
                    return new BigDecimal("10");
                } else if (diasNoMes == 30) {
                    return new BigDecimal("10");
                } else if (diasNoMes == 31) {
                    return new BigDecimal("11");
                } else {
                    // Cálculo padrão para escala 24x48
                    return new BigDecimal("10");
                }
            }

            default -> {
                return new BigDecimal("22"); // Padrão
            }
        }
    }
}
