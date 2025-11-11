package br.com.codex.v1.service.rh.ferias;

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
public class CalcularFeriasProporcionaisService {
    private static final Logger logger = LoggerFactory.getLogger(CalcularFeriasProporcionaisService.class);

    @Autowired
    private CalculoBaseService calculoBaseService;

    @Setter
    private String numeroMatricula;

    /**
     * ✅ Métudo PRINCIPAL que verifica automaticamente o tipo de aviso prévio
     */
    public Map<String, BigDecimal> calcularFeriasProporcionais() {
        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("referencia", BigDecimal.ZERO);
        resultado.put("vencimentos", BigDecimal.ZERO);
        resultado.put("descontos", BigDecimal.ZERO);

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);
            BigDecimal salarioBase = rescisao.getSalarioBase();
            LocalDate dataAdmissao = rescisao.getDataDeAdmissao();
            LocalDate dataDemissao = rescisao.getDataDeDemissao();

            // ✅ 1. Verificar automaticamente o tipo de aviso prévio
            String tipoAviso = detectarTipoAvisoPrevio(rescisao);

            // ✅ 2. Calcular data final baseada no tipo de aviso
            LocalDate dataFinalCalculo = calcularDataFinalPorTipoAviso(dataDemissao, tipoAviso, rescisao);

            // ✅ 3. Calcular meses trabalhados
            int mesesTrabalhados = calcularMesesTrabalhados(dataAdmissao, dataFinalCalculo);

            // ✅ 4. Calcular férias proporcionais
            BigDecimal feriasProporcionais = salarioBase.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(mesesTrabalhados));

            // ✅ 5. Calcular terço constitucional
            BigDecimal tercoConstitucional = feriasProporcionais.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);

            // ✅ 6. Calcular total
            BigDecimal totalFerias = feriasProporcionais.add(tercoConstitucional);

            resultado.put("referencia", new BigDecimal(mesesTrabalhados));
            resultado.put("vencimentos", totalFerias);
            resultado.put("descontos", BigDecimal.ZERO);

            logger.info("Férias proporcionais AUTO para {}: Aviso: {}, Meses: {}, Data final: {}, Total: R$ {}",
                    numeroMatricula, tipoAviso, mesesTrabalhados, dataFinalCalculo, totalFerias);

        } catch (Exception e) {
            logger.error("Erro ao calcular férias proporcionais AUTO para {}: {}", numeroMatricula, e.getMessage());
            throw new RuntimeException("Erro ao calcular Férias Proporcionais: " + e.getMessage());
        }

        return resultado;
    }

    /**
     * ✅ Detecta automaticamente o tipo de aviso prévio
     */
    private String detectarTipoAvisoPrevio(FolhaRescisao rescisao) {
        // ✅ Opção 1: Se já tem campo específico
        if (rescisao.getTipoDeAvisoPrevio() != null && !rescisao.getTipoDeAvisoPrevio().isEmpty()) {
            String tipo = rescisao.getTipoDeAvisoPrevio().toUpperCase();
            if (tipo.contains("Indenizado") || tipo.contains("Indenização")) {
                return "Indenizado";
            } else if (tipo.contains("Trabalhado") || tipo.contains("Trabalho")) {
                return "Trabalhado";
            } else if (tipo.contains("Nenhum") || tipo.contains("Sem") || tipo.contains("sem")) {
                return "Nenhum";
            }
        }

        // ✅ Opção 2: Verificar por campos de valores
        if (temAvisoPrevioIndenizado(rescisao)) {
            return "Indenizado";
        } else if (temAvisoPrevioTrabalhado(rescisao)) {
            return "Trabalhado";
        }

        // ✅ Opção 3: Verificar por dias trabalhados após demissão
        if (temDiasAvisoTrabalhado(rescisao)) {
            return "Trabalhado";
        }

        // ✅ Padrão: Sem aviso prévio
        return "Nenhum";
    }

    /**
     * ✅ Verifica se tem aviso prévio indenizado (por valores)
     */
    private boolean temAvisoPrevioIndenizado(FolhaRescisao rescisao) {
        // Verifica se existe valor de aviso prévio indenizado
        // Ajuste conforme os campos da sua entidade FolhaRescisao

        // Exemplos de verificação:
        return rescisao.getTipoDeAvisoPrevio() != null && rescisao.getTipoDeAvisoPrevio().compareTo("Indenizado") > 0;
    }

    /**
     * ✅ Verifica se tem aviso prévio trabalhado (por valores)
     */
    private boolean temAvisoPrevioTrabalhado(FolhaRescisao rescisao) {
        // Verifica se existe valor de aviso prévio trabalhado
        // Ou se tem dias trabalhados após a data de demissão

        // Exemplos de verificação:
        return rescisao.getTipoDeAvisoPrevio() != null &&
                rescisao.getTipoDeAvisoPrevio().compareTo("Trabalhado") > 0;
    }

    /**
     * ✅ Verifica se tem dias de aviso trabalhado
     */
    private boolean temDiasAvisoTrabalhado(FolhaRescisao rescisao) {
        // Verifica se a data final é maior que a data de demissão
        // indicando que trabalhou durante o aviso prévio

        if (rescisao.getDataAVisoPrevio() != null && rescisao.getDataDeDemissao() != null) {
            return rescisao.getDataAVisoPrevio().isAfter(rescisao.getDataDeDemissao());
        }
        return false;
    }

    /**
     * ✅ Calcula data final baseada no tipo de aviso
     */
    private LocalDate calcularDataFinalPorTipoAviso(LocalDate dataDemissao, String tipoAviso, FolhaRescisao rescisao) {
        switch (tipoAviso) {
            case "Indenizado":
                // ✅ AVISO Indenizado: data demissão + dias de aviso
                int diasAvisoIndenizado = calcularDiasAvisoPrevio(rescisao);
                return dataDemissao.plusDays(diasAvisoIndenizado);

            case "Trabalhado":
                // ✅ AVISO Trabalhado: usa a data final real do trabalho
                if (rescisao.getDataAVisoPrevio() != null && rescisao.getDataAVisoPrevio().isAfter(dataDemissao)) {
                    return rescisao.getDataAVisoPrevio(); // Data real que trabalhou
                } else {
                    // Se não tem data final, calcula data demissão + dias aviso
                    int diasAvisoTrabalhado = calcularDiasAvisoPrevio(rescisao);
                    return dataDemissao.plusDays(diasAvisoTrabalhado);
                }

            case "Nenhum":
            default:
                // ✅ SEM AVISO: usa apenas a data de demissão
                return dataDemissao;
        }
    }

    /**
     * ✅ Calcula dias de aviso prévio (CLT + tempo de empresa)
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

        int totalDias = diasBase + diasAdicionais;

        logger.debug("Dias aviso prévio para {}: {} anos, {} dias base + {} adicionais = {} dias total",
                numeroMatricula, anosTrabalhados, diasBase, diasAdicionais, totalDias);

        return totalDias;
    }

    /**
     * ✅ Calcula meses trabalhados considerando a regra dos 15 dias
     */
    private int calcularMesesTrabalhados(LocalDate dataAdmissao, LocalDate dataFinal) {
        long meses = ChronoUnit.MONTHS.between(dataAdmissao, dataFinal);

        // ✅ Verifica se trabalhou 15+ dias no último mês
        LocalDate inicioUltimoMes = dataFinal.withDayOfMonth(1);
        long diasTrabalhadosUltimoMes = ChronoUnit.DAYS.between(inicioUltimoMes, dataFinal);

        if (diasTrabalhadosUltimoMes >= 15) {
            meses++; // Conta o mês completo
        }

        return (int) Math.max(0, meses);
    }

    /**
     * ✅ Métudo para debug: mostra como foi detectado o aviso prévio
     */
    public Map<String, Object> debugDetectarAvisoPrevio() {
        Map<String, Object> debug = new HashMap<>();

        try {
            FolhaRescisao rescisao = calculoBaseService.findByMatriculaDoFuncionario(numeroMatricula);

            String tipoDetectado = detectarTipoAvisoPrevio(rescisao);
            LocalDate dataFinal = calcularDataFinalPorTipoAviso(rescisao.getDataDeDemissao(), tipoDetectado, rescisao);
            int meses = calcularMesesTrabalhados(rescisao.getDataDeAdmissao(), dataFinal);

            debug.put("tipoAvisoDetectado", tipoDetectado);
            debug.put("dataDemissao", rescisao.getDataDeDemissao());
            debug.put("dataFinalCalculo", dataFinal);
            debug.put("mesesTrabalhados", meses);
            debug.put("diasAviso", calcularDiasAvisoPrevio(rescisao));
            debug.put("temAvisoIndenizado", temAvisoPrevioIndenizado(rescisao));
            debug.put("temAvisoTrabalhado", temAvisoPrevioTrabalhado(rescisao));

        } catch (Exception e) {
            debug.put("erro", e.getMessage());
        }

        return debug;
    }
}