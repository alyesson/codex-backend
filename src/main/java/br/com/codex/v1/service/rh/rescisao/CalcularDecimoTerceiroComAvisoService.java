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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalcularDecimoTerceiroComAvisoService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularDecimoTerceiroComAvisoService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    /**
     * ✅ Calcula 13º salário proporcional COM aviso prévio
     */
    public Map<String, BigDecimal> calcularDecimoTerceiroComAviso() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal salarioBase = rescisao.getSalarioBase();
            LocalDate dataAdmissao = rescisao.getDataDeAdmissao();
            LocalDate dataDemissao = rescisao.getDataDeDemissao();
            String tipoAviso = rescisao.getTipoDeAvisoPrevio(); // "INDENIZADO", "TRABALHADO", "NENHUM"

            // ✅ 1. Calcular meses trabalhados no ano (até a demissão)
            int mesesTrabalhados = calcularMesesTrabalhadosAno(dataAdmissao, dataDemissao);

            // ✅ 2. Calcular meses de aviso prévio
            int mesesAvisoPrevio = calcularMesesAvisoPrevio(dataDemissao, tipoAviso, rescisao);

            // ✅ 3. Total de meses considerados (trabalhados + aviso)
            int totalMeses = mesesTrabalhados + mesesAvisoPrevio;

            // ✅ 4. Calcular valor de 1/12 avos: Salário ÷ 12
            BigDecimal umDozeAvos = salarioBase.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

            // ✅ 5. Calcular 13º proporcional: 1/12 × total meses
            BigDecimal decimoTerceiroProporcional = umDozeAvos.multiply(new BigDecimal(totalMeses));

            resultado.put("referencia", new BigDecimal(totalMeses));
            resultado.put("vencimentos", decimoTerceiroProporcional);
            resultado.put("descontos", BigDecimal.ZERO);

            logger.info("13º COM aviso para {}: Trabalhados: {}, Aviso: {}, Total: {} meses, Valor: R$ {}",
                    numeroMatricula, mesesTrabalhados, mesesAvisoPrevio, totalMeses, decimoTerceiroProporcional);

        } catch (Exception e) {
            logger.error("Erro ao calcular 13º COM aviso para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular 13º Salário com Aviso Prévio: " + e.getMessage());
        }

        return resultado;
    }

    /**
     * ✅ Calcula meses trabalhados no ano (até a data de demissão)
     */
    private int calcularMesesTrabalhadosAno(LocalDate dataAdmissao, LocalDate dataDemissao) {
        int anoDemissao = dataDemissao.getYear();
        int mesesTrabalhados = 0;

        // ✅ Se admitido em ano anterior
        if (dataAdmissao.getYear() < anoDemissao) {
            mesesTrabalhados = dataDemissao.getMonthValue() - 1; // Janeiro até mês anterior

            // ✅ Verifica mês da demissão (regra dos 15 dias)
            if (dataDemissao.getDayOfMonth() >= 15) {
                mesesTrabalhados++; // Conta o mês da demissão
            }
        }
        // ✅ Se admitido no mesmo ano
        else if (dataAdmissao.getYear() == anoDemissao) {
            int mesAdmissao = dataAdmissao.getMonthValue();
            int mesDemissao = dataDemissao.getMonthValue();

            for (int mes = mesAdmissao; mes <= mesDemissao; mes++) {
                if (deveContarMes(dataAdmissao, dataDemissao, mes)) {
                    mesesTrabalhados++;
                }
            }
        }

        return mesesTrabalhados;
    }

    /**
     * ✅ Calcula meses de aviso prévio a serem considerados
     */
    private int calcularMesesAvisoPrevio(LocalDate dataDemissao, String tipoAviso, FolhaRescisao rescisao) {
        if ("NENHUM".equalsIgnoreCase(tipoAviso)) {
            return 0; // Sem aviso prévio
        }

        // ✅ Calcula dias totais de aviso prévio
        int diasAviso = calcularDiasAvisoPrevio(rescisao);

        // ✅ Calcula data final do aviso prévio
        LocalDate dataFinalAviso = dataDemissao.plusDays(diasAviso);

        // ✅ Calcula quantos meses completos o aviso prévio cobre
        return calcularMesesCobertosPorAviso(dataDemissao, dataFinalAviso);
    }

    /**
     * ✅ Calcula dias de aviso prévio (CLT)
     */
    private int calcularDiasAvisoPrevio(FolhaRescisao rescisao) {
        LocalDate dataAdmissao = rescisao.getDataDeAdmissao();
        LocalDate dataDemissao = rescisao.getDataDeDemissao();

        // ✅ Calcula anos trabalhados
        long anosTrabalhados = ChronoUnit.YEARS.between(dataAdmissao, dataDemissao);

        // ✅ Regra CLT:
        // - Até 1 ano: 30 dias
        // - Acima de 1 ano: 30 dias + 3 dias por ano completo
        int diasBase = 30;
        int diasAdicionais = (int) Math.max(0, anosTrabalhados - 1) * 3;

        return diasBase + diasAdicionais;
    }

    /**
     * ✅ Calcula quantos meses completos o aviso prévio cobre
     */
    private int calcularMesesCobertosPorAviso(LocalDate dataDemissao, LocalDate dataFinalAviso) {
        int mesesCobertos = 0;

        // ✅ Começa do mês seguinte à demissão
        LocalDate data = dataDemissao.plusMonths(1).withDayOfMonth(1);

        while (!data.isAfter(dataFinalAviso)) {
            LocalDate inicioMes = data.withDayOfMonth(1);
            LocalDate fimMes = data.withDayOfMonth(data.lengthOfMonth());

            // ✅ Verifica quantos dias do mês estão cobertos pelo aviso
            LocalDate inicioCoberto = data.isAfter(inicioMes) ? data : inicioMes;
            LocalDate fimCoberto = dataFinalAviso.isBefore(fimMes) ? dataFinalAviso : fimMes;

            long diasCobertos = ChronoUnit.DAYS.between(inicioCoberto, fimCoberto) + 1;

            // ✅ Se cobre 15+ dias, conta como mês completo
            if (diasCobertos >= 15) {
                mesesCobertos++;
            }

            data = fimMes.plusDays(1); // Próximo mês
        }
        logger.debug("Meses cobertos por aviso: {} meses (de {} a {})", mesesCobertos, dataDemissao.plusDays(1), dataFinalAviso);

        return mesesCobertos;
    }

    /**
     * ✅ Verifica se o mês deve ser contado (regra dos 15 dias)
     */
    private boolean deveContarMes(LocalDate dataAdmissao, LocalDate dataDemissao, int mes) {
        int ano = dataDemissao.getYear();

        // ✅ Mês da ADMISSÃO
        if (mes == dataAdmissao.getMonthValue()) {
            LocalDate fimMesAdmissao = dataAdmissao.withDayOfMonth(dataAdmissao.lengthOfMonth());
            long diasTrabalhados = ChronoUnit.DAYS.between(dataAdmissao, fimMesAdmissao) + 1;
            return diasTrabalhados >= 15;
        }

        // ✅ Mês da DEMISSÃO
        if (mes == dataDemissao.getMonthValue()) {
            LocalDate inicioMesDemissao = dataDemissao.withDayOfMonth(1);
            long diasTrabalhados = ChronoUnit.DAYS.between(inicioMesDemissao, dataDemissao) + 1;
            return diasTrabalhados >= 15;
        }

        // ✅ Meses INTERMEDIÁRIOS (sempre contam)
        if (mes > dataAdmissao.getMonthValue() && mes < dataDemissao.getMonthValue()) {
            return true;
        }

        return false;
    }

    /**
     * ✅ Calcula meses de aviso de forma simplificada
     */
    private int calcularMesesAvisoSimplificado(String tipoAviso, FolhaRescisao rescisao) {
        if ("Nenhum".equalsIgnoreCase(tipoAviso)) {
            return 0;
        }

        // ✅ Para aviso prévio, considera 1 mês adicional (simplificado)
        // Em casos complexos, pode ser mais de 1 mês
        int diasAviso = calcularDiasAvisoPrevio(rescisao);

        // ✅ Se aviso cobre mais de 15 dias em um novo mês, conta como 1 mês
        if (diasAviso > 15) {
            return 1;
        }

        return 0;
    }
}
