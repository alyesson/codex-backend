package br.com.codex.v1.service.rh.rescisao;

import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.service.rh.CalculoBaseService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularDecimoTerceiroProporcionalService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularDecimoTerceiroProporcionalService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    public Map<String, BigDecimal> calcularDecimoTerceiroProporcional() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal salarioBase = rescisao.getSalarioBase();
            LocalDate dataAdmissao = rescisao.getDataDeAdmissao();
            LocalDate dataDemissao = rescisao.getDataDeDemissao();
            String tipoDemissao = rescisao.getTipoDeDemissao();

            // ✅ Calcular meses trabalhados no ano (considerando regra dos 15 dias)
            int mesesTrabalhados = calcularMesesTrabalhadosAno(dataAdmissao, dataDemissao);

            // ✅ Calcular 13º proporcional: (Salário ÷ 12) × meses trabalhados
            BigDecimal decimoTerceiroProporcional = salarioBase
                    .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados));

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", decimoTerceiroProporcional);
            resultado.put("descontos", BigDecimal.ZERO);
            resultado.put("salarioBase", salarioBase);

            logger.info("13º proporcional calculado para {}: {} meses, Salário: R$ {}, Valor: R$ {}",
                    numeroMatricula, mesesTrabalhados, salarioBase, decimoTerceiroProporcional);

        } catch (Exception e) {
            logger.error("Erro ao calcular 13º proporcional para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular 13º Salário Proporcional: " + e.getMessage());
        }

        return resultado;
    }

    /**
     * Calcula meses trabalhados no ano considerando a regra dos 15 dias
     */
    private int calcularMesesTrabalhadosAno(LocalDate dataAdmissao, LocalDate dataDemissao) {
        int anoDemissao = dataDemissao.getYear();
        int mesesTrabalhados = 0;

        // Se admitido em ano anterior, conta todos os meses de janeiro até o mês da demissão
        if (dataAdmissao.getYear() < anoDemissao) {
            mesesTrabalhados = dataDemissao.getMonthValue(); // Janeiro até mês da demissão
        }
        // Se admitido no mesmo ano
        else if (dataAdmissao.getYear() == anoDemissao) {
            int mesAdmissao = dataAdmissao.getMonthValue();
            int mesDemissao = dataDemissao.getMonthValue();

            // Conta cada mês de trabalho
            for (int mes = mesAdmissao; mes <= mesDemissao; mes++) {
                if (deveContarMes(dataAdmissao, dataDemissao, mes)) {
                    mesesTrabalhados++;
                }
            }
        }

        logger.debug("Meses trabalhados no ano para {}: {} meses", numeroMatricula, mesesTrabalhados);
        return mesesTrabalhados;
    }

    /**
     * Verifica se o mês deve ser contado (regra dos 15 dias)
     */
    private boolean deveContarMes(LocalDate dataAdmissao, LocalDate dataDemissao, int mes) {
        // Para meses entre admissão e demissão (exceto mês da admissão e demissão)
        if (mes > dataAdmissao.getMonthValue() && mes < dataDemissao.getMonthValue()) {
            return true; // Meses intermediários sempre contam
        }

        // Mês da ADMISSÃO: conta se trabalhou pelo menos 15 dias
        if (mes == dataAdmissao.getMonthValue()) {
            int diasTrabalhadosMesAdmissao = calcularDiasTrabalhadosMes(dataAdmissao, dataAdmissao.withDayOfMonth(dataAdmissao.lengthOfMonth()));
            return diasTrabalhadosMesAdmissao >= 15;
        }

        // Mês da DEMISSÃO: conta se trabalhou pelo menos 15 dias
        if (mes == dataDemissao.getMonthValue()) {
            int diasTrabalhadosMesDemissao = calcularDiasTrabalhadosMes(dataDemissao.withDayOfMonth(1), dataDemissao);
            return diasTrabalhadosMesDemissao >= 15;
        }

        return false;
    }

    /**
     * Calcula dias trabalhados em um mês
     */
    private int calcularDiasTrabalhadosMes(LocalDate dataInicio, LocalDate dataFim) {
        // Considera dias úteis (segunda a sexta) - você pode ajustar conforme necessidade
        int diasTrabalhados = 0;
        LocalDate data = dataInicio;

        while (!data.isAfter(dataFim)) {
            DayOfWeek diaSemana = data.getDayOfWeek();
            if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY) {
                diasTrabalhados++;
            }
            data = data.plusDays(1);
        }

        return diasTrabalhados;
    }

    /**
     * Métudo simplificado alternativo (mais comum)
     */
    private int calcularMesesTrabalhadosSimplificado(LocalDate dataAdmissao, LocalDate dataDemissao) {
        int anoDemissao = dataDemissao.getYear();

        // Se admitido em ano anterior, conta todos os meses até a demissão
        if (dataAdmissao.getYear() < anoDemissao) {
            return dataDemissao.getMonthValue(); // Janeiro até mês da demissão
        }

        // Se admitido no mesmo ano
        if (dataAdmissao.getYear() == anoDemissao) {
            int mesAdmissao = dataAdmissao.getMonthValue();
            int mesDemissao = dataDemissao.getMonthValue();

            // Verifica mês da admissão (15+ dias conta)
            boolean contaMesAdmissao = dataAdmissao.getDayOfMonth() <= 15;
            // Verifica mês da demissão (15+ dias conta)
            boolean contaMesDemissao = dataDemissao.getDayOfMonth() >= 15;

            if (mesAdmissao == mesDemissao) {
                // Mesmo mês: conta se trabalhou 15+ dias no total
                long diasTrabalhados = ChronoUnit.DAYS.between(dataAdmissao, dataDemissao) + 1;
                return diasTrabalhados >= 15 ? 1 : 0;
            } else {
                // Meses diferentes
                int meses = mesDemissao - mesAdmissao;
                if (contaMesAdmissao) meses++;
                if (contaMesDemissao) meses++;
                return Math.max(0, meses);
            }
        }

        return 0; // Admitido no futuro
    }
}
