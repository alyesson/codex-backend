package br.com.codex.v1.utilitario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

public class CalculoDaFolhaProventosQuinzenal {

    private BigDecimal salarioBase;
    private BigDecimal salarioPorHora;
    private Set<LocalDate> feriados = new HashSet<>();

    // Construtor
    public CalculoDaFolhaProventosQuinzenal(BigDecimal salarioBase, BigDecimal salarioPorHora) {
        this.salarioBase = salarioBase;
        this.salarioPorHora = salarioPorHora;
    }

    /**
     * Métudo principal que processa os eventos de proventos baseado no código do evento
     * @param codigoEvento - Código do evento a ser processado
     * @param parametros - Array de parâmetros necessários para o cálculo
     * @return ResultadoCalculoProvento com valor calculado e referência
     */
    public ResultadoCalculoProvento processarProvento(int codigoEvento, Object... parametros) {
        switch (codigoEvento) {
            case 1 -> {
                // Horas Normais Diurnas
                return calcularHorasNormaisDiurnas(
                        (String) parametros[0], // horaEntrada
                        (String) parametros[1]  // horaSaida
                );
            }

            case 2 -> {
                // Adiantamento de Salário (40%)
                return calcularAdiantamentoSalarial();
            }

            case 5 -> {
                // Horas Repouso Remunerado Diurno
                return calcularHorasRepousoDiurno(
                        (String) parametros[0], // horaEntrada
                        (String) parametros[1]  // horaSaida
                );
            }

            case 8 -> {
                // Horas de Atestado Médico
                return calcularHorasAtestadoMedico(
                        (BigDecimal) parametros[0] // horasAtestado
                );
            }

            case 9 -> {
                // Dias de Atestado Médico
                return calcularDiasAtestadoMedico(
                        (BigDecimal) parametros[0] // diasAtestado
                );
            }

            case 12 -> {
                // Horas Normais Noturnas
                return calcularHorasNormaisNoturnas(
                        (String) parametros[0], // horaEntrada
                        (String) parametros[1], // horaSaida
                        (String) parametros[2]  // tipoJornada
                );
            }

            case 14 -> {
                // Adicional Noturno
                return calcularAdicionalNoturno(
                        (BigDecimal) parametros[0], // horasNoturnas
                        (BigDecimal) parametros[1], // dsrNoturno
                        (BigDecimal) parametros[2]  // percentualAdicional
                );
            }

            case 17 -> {
                // Pro-Labore
                return calcularProLabore();
            }

            case 19 -> {
                // Bolsa Auxílio
                return calcularBolsaAuxilio();
            }

            case 25 -> {
                // Horas Repouso Remunerado Noturno
                return calcularHorasRepousoNoturno(
                        (String) parametros[0], // horaEntrada
                        (String) parametros[1], // horaSaida
                        (String) parametros[2]  // tipoJornada
                );
            }

            case 46 -> {
                // Insalubridade
                return calcularInsalubridade(
                        (BigDecimal) parametros[0], // salarioMinimo
                        (BigDecimal) parametros[1]  // percentualInsalubridade
                );
            }

            case 47 -> {
                // Periculosidade
                return calcularPericulosidade(
                        (BigDecimal) parametros[0] // percentualPericulosidade
                );
            }

            case 51 -> {
                // Comissão
                return calcularComissao(
                        (BigDecimal) parametros[0], // percentualComissao
                        (BigDecimal) parametros[1]  // valorVendas
                );
            }

            case 53 -> {
                // Gratificação
                return calcularGratificacao(
                        (BigDecimal) parametros[0] // valorGratificacao
                );
            }

            case 98 -> {
                // Horas Extras 50%
                return calcularHorasExtras50(
                        (BigDecimal) parametros[0], // horasExtras50
                        (BigDecimal) parametros[1]  // valorInsalubridade (opcional)
                );
            }

            case 99 -> {
                // Horas Extras 70%
                return calcularHorasExtras70(
                        (BigDecimal) parametros[0], // horasExtras70
                        (BigDecimal) parametros[1]  // valorInsalubridade (opcional)
                );
            }

            case 100 -> {
                // Horas Extras 100%
                return calcularHorasExtras100(
                        (BigDecimal) parametros[0], // horasExtras100
                        (BigDecimal) parametros[1]  // valorInsalubridade (opcional)
                );
            }

            case 130 -> {
                // Ajuda de Custo
                return calcularAjudaCusto(
                        (BigDecimal) parametros[0] // valorAjudaCusto
                );
            }

            case 133 -> {
                // Salário Família
                return calcularSalarioFamilia(
                        (Integer) parametros[0],   // numeroDependentes
                        (BigDecimal) parametros[1] // salarioMinimo
                );
            }

            case 204 -> {
                // Ajuda de Locomoção (do documento quinzenal)
                return calcularAjudaLocomocao(
                        (BigDecimal) parametros[0] // valorAjudaLocomocao
                );
            }

            case 239 -> {
                // Vale Transporte
                return calcularValeTransporte(
                        (String) parametros[0],    // tipoJornada
                        (BigDecimal) parametros[1] // valorValeTransporte
                );
            }

            case 980 -> {
                // Adiantamento Salarial Quinzenal (do documento)
                return calcularAdiantamentoQuinzenal();
            }

            default -> {
                return new ResultadoCalculoProvento(BigDecimal.ZERO, BigDecimal.ZERO, "Evento não encontrado");
            }
        }
    }

    // ========== MÉTODOS DE CÁLCULO ESPECÍFICOS ==========

    private ResultadoCalculoProvento calcularHorasNormaisDiurnas(String horaEntrada, String horaSaida) {
        try {
            LocalTime entrada = LocalTime.parse(horaEntrada);
            LocalTime saida = LocalTime.parse(horaSaida);
            LocalTime limiteNoturno = LocalTime.parse("22:00");

            Duration duracao;
            if (saida.isBefore(limiteNoturno)) {
                duracao = Duration.between(entrada, saida);
            } else {
                duracao = Duration.between(entrada, limiteNoturno);
            }

            // Subtrai 1 hora para almoço
            duracao = duracao.minusHours(1);

            BigDecimal horasTrabalhadas = BigDecimal.valueOf(duracao.toHours())
                    .add(BigDecimal.valueOf(duracao.toMinutesPart()).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));

            int diasUteis = calcularDiasUteisNoMes();
            BigDecimal horasMes = horasTrabalhadas.multiply(BigDecimal.valueOf(diasUteis));
            BigDecimal valorTotal = horasMes.multiply(salarioPorHora);

            return new ResultadoCalculoProvento(horasMes, valorTotal, "Horas Diurnas");

        } catch (Exception e) {
            return new ResultadoCalculoProvento(BigDecimal.ZERO, BigDecimal.ZERO, "Erro no cálculo");
        }
    }

    private ResultadoCalculoProvento calcularAdiantamentoSalarial() {
        BigDecimal valorAdiantamento = salarioBase.multiply(new BigDecimal("0.40"))
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(BigDecimal.ONE, valorAdiantamento, "Adiantamento 40%");
    }

    private ResultadoCalculoProvento calcularAdiantamentoQuinzenal() {
        // Para folha quinzenal, calcula 40% do salário base
        BigDecimal valorAdiantamento = salarioBase.multiply(new BigDecimal("0.40"))
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(BigDecimal.ONE, valorAdiantamento, "Adiantamento Quinzenal");
    }

    private ResultadoCalculoProvento calcularAjudaLocomocao(BigDecimal valorAjudaLocomocao) {
        // Valor fixo de ajuda de locomoção (baseado no documento)
        return new ResultadoCalculoProvento(
                BigDecimal.ONE,
                valorAjudaLocomocao,
                "Ajuda Locomoção"
        );
    }

    private ResultadoCalculoProvento calcularHorasRepousoDiurno(String horaEntrada, String horaSaida) {
        try {
            LocalTime entrada = LocalTime.parse(horaEntrada);
            LocalTime saida = LocalTime.parse(horaSaida);
            LocalTime limiteNoturno = LocalTime.parse("22:00");

            Duration duracao;
            if (saida.isBefore(limiteNoturno)) {
                duracao = Duration.between(entrada, saida);
            } else {
                duracao = Duration.between(entrada, limiteNoturno);
            }

            // Subtrai 1 hora para almoço
            duracao = duracao.minusHours(1);

            BigDecimal horasPorDia = BigDecimal.valueOf(duracao.toHours())
                    .add(BigDecimal.valueOf(duracao.toMinutesPart()).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));

            int diasNaoUteis = calcularDiasNaoUteisNoMes();
            BigDecimal horasMes = horasPorDia.multiply(BigDecimal.valueOf(diasNaoUteis));
            BigDecimal valorTotal = horasMes.multiply(salarioPorHora);

            return new ResultadoCalculoProvento(horasMes, valorTotal, "DSR Diurno");

        } catch (Exception e) {
            return new ResultadoCalculoProvento(BigDecimal.ZERO, BigDecimal.ZERO, "Erro no cálculo DSR");
        }
    }

    private ResultadoCalculoProvento calcularHorasAtestadoMedico(BigDecimal horasAtestado) {
        BigDecimal valorTotal = horasAtestado.multiply(salarioPorHora)
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(horasAtestado, valorTotal, "Horas Atestado");
    }

    private ResultadoCalculoProvento calcularDiasAtestadoMedico(BigDecimal diasAtestado) {
        BigDecimal horasPorDia = new BigDecimal("8"); // Assume 8 horas por dia
        BigDecimal horasTotais = diasAtestado.multiply(horasPorDia);
        BigDecimal valorTotal = horasTotais.multiply(salarioPorHora)
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(diasAtestado, valorTotal, "Dias Atestado");
    }

    private ResultadoCalculoProvento calcularHorasNormaisNoturnas(String horaEntrada, String horaSaida, String tipoJornada) {
        try {
            LocalTime inicioNoturno = LocalTime.parse("22:00");
            LocalTime saida = LocalTime.parse(horaSaida);

            Duration duracaoNoturna;
            if (saida.isBefore(LocalTime.parse("05:00"))) {
                // Trabalhou após meia-noite
                LocalTime meiaNoite = LocalTime.parse("00:00");
                Duration ateMeiaNoite = Duration.between(inicioNoturno, meiaNoite);
                Duration depoisMeiaNoite = Duration.between(meiaNoite, saida);
                duracaoNoturna = ateMeiaNoite.plus(depoisMeiaNoite);
            } else {
                duracaoNoturna = Duration.between(inicioNoturno, saida);
            }

            // Aplica fator de redução da hora noturna (52:30 para 60:00)
            BigDecimal horasNoturnas = BigDecimal.valueOf(duracaoNoturna.toMinutes())
                    .divide(BigDecimal.valueOf(52.5), 2, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

            int diasUteis = calcularDiasUteisNoMes();
            BigDecimal horasMes = horasNoturnas.multiply(BigDecimal.valueOf(diasUteis));
            BigDecimal valorTotal = horasMes.multiply(salarioPorHora);

            return new ResultadoCalculoProvento(horasMes, valorTotal, "Horas Noturnas");

        } catch (Exception e) {
            return new ResultadoCalculoProvento(BigDecimal.ZERO, BigDecimal.ZERO, "Erro cálculo horas noturnas");
        }
    }

    private ResultadoCalculoProvento calcularAdicionalNoturno(BigDecimal horasNoturnas, BigDecimal dsrNoturno, BigDecimal percentualAdicional) {
        BigDecimal horasTotais = horasNoturnas.add(dsrNoturnos);
        BigDecimal valorAdicional = horasTotais.multiply(salarioPorHora)
                .multiply(percentualAdicional.divide(new BigDecimal("100")))
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(horasTotais, valorAdicional, "Adicional Noturno");
    }

    private ResultadoCalculoProvento calcularProLabore() {
        return new ResultadoCalculoProvento(BigDecimal.ONE, salarioBase, "Pro-Labore");
    }

    private ResultadoCalculoProvento calcularBolsaAuxilio() {
        return new ResultadoCalculoProvento(BigDecimal.ONE, salarioBase, "Bolsa Auxílio");
    }

    private ResultadoCalculoProvento calcularInsalubridade(BigDecimal salarioMinimo, BigDecimal percentualInsalubridade) {
        BigDecimal valorInsalubridade = salarioMinimo.multiply(percentualInsalubridade)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(BigDecimal.ONE, valorInsalubridade, "Insalubridade");
    }

    private ResultadoCalculoProvento calcularPericulosidade(BigDecimal percentualPericulosidade) {
        BigDecimal valorPericulosidade = salarioBase.multiply(percentualPericulosidade)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(BigDecimal.ONE, valorPericulosidade, "Periculosidade");
    }

    private ResultadoCalculoProvento calcularComissao(BigDecimal percentualComissao, BigDecimal valorVendas) {
        BigDecimal valorComissao = valorVendas.multiply(percentualComissao)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(valorVendas, valorComissao, "Comissão");
    }

    private ResultadoCalculoProvento calcularGratificacao(BigDecimal valorGratificacao) {
        return new ResultadoCalculoProvento(BigDecimal.ONE, valorGratificacao, "Gratificação");
    }

    private ResultadoCalculoProvento calcularHorasExtras50(BigDecimal horasExtras50, BigDecimal valorInsalubridade) {
        BigDecimal valorHoraExtra = salarioPorHora.multiply(new BigDecimal("1.5"));
        if (valorInsalubridade != null && valorInsalubridade.compareTo(BigDecimal.ZERO) > 0) {
            valorHoraExtra = valorHoraExtra.add(valorInsalubridade);
        }
        BigDecimal valorTotal = horasExtras50.multiply(valorHoraExtra)
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(horasExtras50, valorTotal, "Hora Extra 50%");
    }

    private ResultadoCalculoProvento calcularHorasExtras70(BigDecimal horasExtras70, BigDecimal valorInsalubridade) {
        BigDecimal valorHoraExtra = salarioPorHora.multiply(new BigDecimal("1.7"));
        if (valorInsalubridade != null && valorInsalubridade.compareTo(BigDecimal.ZERO) > 0) {
            valorHoraExtra = valorHoraExtra.add(valorInsalubridade);
        }
        BigDecimal valorTotal = horasExtras70.multiply(valorHoraExtra)
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(horasExtras70, valorTotal, "Hora Extra 70%");
    }

    private ResultadoCalculoProvento calcularHorasExtras100(BigDecimal horasExtras100, BigDecimal valorInsalubridade) {
        BigDecimal valorHoraExtra = salarioPorHora.multiply(new BigDecimal("2.0"));
        if (valorInsalubridade != null && valorInsalubridade.compareTo(BigDecimal.ZERO) > 0) {
            valorHoraExtra = valorHoraExtra.add(valorInsalubridade);
        }
        BigDecimal valorTotal = horasExtras100.multiply(valorHoraExtra)
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(horasExtras100, valorTotal, "Hora Extra 100%");
    }

    private ResultadoCalculoProvento calcularAjudaCusto(BigDecimal valorAjudaCusto) {
        return new ResultadoCalculoProvento(BigDecimal.ONE, valorAjudaCusto, "Ajuda de Custo");
    }

    private ResultadoCalculoProvento calcularSalarioFamilia(Integer numeroDependentes, BigDecimal salarioMinimo) {
        // Lógica simplificada - ajustar conforme tabela oficial
        BigDecimal valorPorDependente = new BigDecimal("50.00"); // Valor exemplo
        BigDecimal valorTotal = valorPorDependente.multiply(BigDecimal.valueOf(numeroDependentes))
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(
                BigDecimal.valueOf(numeroDependentes),
                valorTotal,
                "Salário Família"
        );
    }

    private ResultadoCalculoProvento calcularValeTransporte(String tipoJornada, BigDecimal valorValeTransporte) {
        int diasTrabalhados = calcularDiasUteisPorJornada(tipoJornada);
        BigDecimal valorTotal = valorValeTransporte.multiply(BigDecimal.valueOf(diasTrabalhados))
                .setScale(2, RoundingMode.HALF_UP);
        return new ResultadoCalculoProvento(
                BigDecimal.valueOf(diasTrabalhados),
                valorTotal,
                "Vale Transporte"
        );
    }

    // ========== MÉTODOS AUXILIARES ==========

    private int calcularDiasUteisNoMes() {
        YearMonth anoMes = YearMonth.now();
        int diasUteis = 0;

        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
            LocalDate data = anoMes.atDay(dia);
            if (data.getDayOfWeek() != DayOfWeek.SUNDAY && !feriados.contains(data)) {
                diasUteis++;
            }
        }
        return diasUteis;
    }

    private int calcularDiasNaoUteisNoMes() {
        YearMonth anoMes = YearMonth.now();
        int diasNaoUteis = 0;

        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
            LocalDate data = anoMes.atDay(dia);
            if (data.getDayOfWeek() == DayOfWeek.SUNDAY || feriados.contains(data)) {
                diasNaoUteis++;
            }
        }
        return diasNaoUteis;
    }

    private int calcularDiasUteisPorJornada(String tipoJornada) {
        switch (tipoJornada) {
            case "6 x 1":
                return calcularDiasUteisNoMes();
            case "12 x 36":
                return 15; // Valor fixo para 12x36
            case "5 x 2":
                return calcularDiasUteisNoMes() - 4; // Aproximação
            default:
                return 20; // Valor padrão
        }
    }

    // Classe para retornar o resultado do cálculo
    public static class ResultadoCalculoProvento {
        private BigDecimal referencia;
        private BigDecimal valor;
        private String descricao;

        public ResultadoCalculoProvento(BigDecimal referencia, BigDecimal valor, String descricao) {
            this.referencia = referencia;
            this.valor = valor;
            this.descricao = descricao;
        }

        // Getters
        public BigDecimal getReferencia() { return referencia; }
        public BigDecimal getValor() { return valor; }
        public String getDescricao() { return descricao; }
    }
}