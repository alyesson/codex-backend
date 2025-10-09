package br.com.codex.v1.utilitario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

public class CalculoDaFolhaDescontos {

    //Faixas de cálculo do INSS, os valores abaixo são refrentes ao salário mínimo e precisam ser alterados todo ano.
    private static final BigDecimal FAIXA1_LIMITE = new BigDecimal("1918.38");
    private static final BigDecimal FAIXA2_LIMITE = new BigDecimal("2865.00");
    private static final BigDecimal FAIXA3_LIMITE = new BigDecimal("3580.00");
    private static final BigDecimal TETO_INSS = new BigDecimal("7507.49");

    private static final BigDecimal ALIQUOTA_FAIXA1 = new BigDecimal("0.075");
    private static final BigDecimal ALIQUOTA_FAIXA2 = new BigDecimal("0.09");
    private static final BigDecimal ALIQUOTA_FAIXA3 = new BigDecimal("0.12");
    private static final BigDecimal ALIQUOTA_FAIXA4 = new BigDecimal("0.14");

    //Faixa de Cálculo do Imposto de renda:
    private static final BigDecimal FAIXA1_LIMITE_IRRF = new BigDecimal("2259.20");
    private static final BigDecimal FAIXA2_LIMITE_IRRF = new BigDecimal("2826.65");
    private static final BigDecimal FAIXA3_LIMITE_IRRF = new BigDecimal("3751.05");
    private static final BigDecimal FAIXA4_LIMITE_IRRF = new BigDecimal("4664.68");

    private static final BigDecimal FAIXA2_ALIQUOTA = new BigDecimal("0.075");
    private static final BigDecimal FAIXA3_ALIQUOTA = new BigDecimal("0.15");
    private static final BigDecimal FAIXA4_ALIQUOTA = new BigDecimal("0.225");
    private static final BigDecimal FAIXA5_ALIQUOTA = new BigDecimal("0.275");

    private static final BigDecimal FAIXA2_DEDUCAO = new BigDecimal("169.44");
    private static final BigDecimal FAIXA3_DEDUCAO = new BigDecimal("381.44");
    private static final BigDecimal FAIXA4_DEDUCAO = new BigDecimal("662.77");
    private static final BigDecimal FAIXA5_DEDUCAO = new BigDecimal("896.00");

    private static final BigDecimal DESCONTO_VALE_TRANSPORTE = new BigDecimal("0.06");
    private static final BigDecimal DEDUCAO_DEPENDENTE = new BigDecimal("189.59");

    BigDecimal salBase, inssTotal;
    BigDecimal salaryBase = new BigDecimal(salarBase.getText());
    BigDecimal valorSalarHora = new BigDecimal(salarioHora.getText());

    private BigDecimal calcularINSS(BigDecimal salarioBase) {
        inssTotal = BigDecimal.ZERO;

        if (salarioBase.compareTo(FAIXA1_LIMITE) <= 0) {
            inssTotal = salarioBase.multiply(ALIQUOTA_FAIXA1);
        } else if (salarioBase.compareTo(FAIXA2_LIMITE) <= 0) {
            inssTotal = FAIXA1_LIMITE.multiply(ALIQUOTA_FAIXA1)
                    .add(salarioBase.subtract(FAIXA1_LIMITE).multiply(ALIQUOTA_FAIXA2));
        } else if (salarioBase.compareTo(FAIXA3_LIMITE) <= 0) {
            inssTotal = FAIXA1_LIMITE.multiply(ALIQUOTA_FAIXA1).add(FAIXA2_LIMITE.subtract(FAIXA1_LIMITE).multiply(ALIQUOTA_FAIXA2))
                    .add(salarioBase.subtract(FAIXA2_LIMITE).multiply(ALIQUOTA_FAIXA3));
        } else if (salarioBase.compareTo(TETO_INSS) <= 0) {
            inssTotal = FAIXA1_LIMITE.multiply(ALIQUOTA_FAIXA1).add(FAIXA2_LIMITE.subtract(FAIXA1_LIMITE).multiply(ALIQUOTA_FAIXA2))
                    .add(FAIXA3_LIMITE.subtract(FAIXA2_LIMITE).multiply(ALIQUOTA_FAIXA3)).add(salarioBase.subtract(FAIXA3_LIMITE).multiply(ALIQUOTA_FAIXA4));
        } else {
            inssTotal = FAIXA1_LIMITE.multiply(ALIQUOTA_FAIXA1).add(FAIXA2_LIMITE.subtract(FAIXA1_LIMITE).multiply(ALIQUOTA_FAIXA2))
                    .add(FAIXA3_LIMITE.subtract(FAIXA2_LIMITE).multiply(ALIQUOTA_FAIXA3)).add(TETO_INSS.subtract(FAIXA3_LIMITE).multiply(ALIQUOTA_FAIXA4));
        }

        return inssTotal.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularIRRF(BigDecimal salarioBase, int numeroDependentes) {
        BigDecimal deducaoDependentes = DEDUCAO_DEPENDENTE.multiply(new BigDecimal(numeroDependentes)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal baseCalculo = salarioBase.subtract(deducaoDependentes);

        BigDecimal irrf;

        if (baseCalculo.compareTo(FAIXA1_LIMITE_IRRF) <= 0) {
            irrf = BigDecimal.ZERO;
        } else if (baseCalculo.compareTo(FAIXA2_LIMITE_IRRF) <= 0) {
            irrf = baseCalculo.multiply(FAIXA2_ALIQUOTA).subtract(FAIXA2_DEDUCAO);
        } else if (baseCalculo.compareTo(FAIXA3_LIMITE_IRRF) <= 0) {
            irrf = baseCalculo.multiply(FAIXA3_ALIQUOTA).subtract(FAIXA3_DEDUCAO);
        } else if (baseCalculo.compareTo(FAIXA4_LIMITE_IRRF) <= 0) {
            irrf = baseCalculo.multiply(FAIXA4_ALIQUOTA).subtract(FAIXA4_DEDUCAO);
        } else {
            irrf = baseCalculo.multiply(FAIXA5_ALIQUOTA).subtract(FAIXA5_DEDUCAO);
        }
        return irrf.setScale(2, RoundingMode.HALF_UP);
    }

    /*private void obtemSalarioMin() {
        ConexaoBD conectaSalMin = new ConexaoBD();
        conectaSalMin.conecta();
        try {
            ResultSet rs;
            try (PreparedStatement ps = conectaSalMin.conexao.prepareStatement("Select isentoAte from rhu_irrf01")) {
                rs = ps.executeQuery();
                if (rs.next()) {
                    salarioMinimo = rs.getBigDecimal("isentoAte");
                }
            }
            rs.close();
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao obter salário mínimo: " + erro, "", JOptionPane.ERROR_MESSAGE);
        }
        conectaSalMin.desconecta();
    }*/

    public void escolheEventos() {
        // Obtém salário mínimo
        // obtemSalarioMin();

        for (int i = 0; i < tabelaCalculoFolha.getRowCount(); i++) {
            int leEvento = Integer.parseInt(tabelaCalculoFolha.getValueAt(i, 0).toString());

            switch (leEvento) {
                case 6 -> {
                    // Desconto Dias de Faltas
                    BigDecimal faltaMes = new BigDecimal(faltasNoMes.getText());
                    BigDecimal valorFalta = calcularDescontoPorHoras(valorSalarHora, faltaMes);
                    tabelaCalculoFolha.setValueAt(faltaMes, i, 2);
                    tabelaCalculoFolha.setValueAt(valorFalta, i, 4);
                }

                case 7 -> {
                    // Desconto DSR (Descanso Semanal Remunerado)
                    BigDecimal faltaDsr = new BigDecimal(faltasDeDsr.getText());
                    BigDecimal valorFalta = calcularDescontoPorHoras(valorSalarHora, faltaDsr);
                    tabelaCalculoFolha.setValueAt(faltaDsr, i, 2);
                    tabelaCalculoFolha.setValueAt(valorFalta, i, 4);
                }

                case 15 -> {
                    // Desconto por Atraso
                    BigDecimal diasDeFaltaDha = new BigDecimal(faltasHorasMes.getText());
                    BigDecimal valorFaltaDha = calcularDescontoPorHoras(valorSalarHora, diasDeFaltaDha);
                    tabelaCalculoFolha.setValueAt(diasDeFaltaDha, i, 2);
                    tabelaCalculoFolha.setValueAt(valorFaltaDha, i, 4);
                }

                case 18 -> {
                    // Desconto por Faltas em Feriado
                    BigDecimal faltaFeriados = new BigDecimal(faltasFeriado.getText());
                    BigDecimal valorFaltaF = calcularDescontoPorHoras(valorSalarHora, faltaFeriados);
                    tabelaCalculoFolha.setValueAt(faltaFeriados, i, 2);
                    tabelaCalculoFolha.setValueAt(valorFaltaF, i, 4);
                }

                case 23 -> {
                    // Desconto por Suspensão Disciplinar
                    BigDecimal faltaMesSusp = new BigDecimal(faltasNoMes.getText());
                    BigDecimal valorFaltaSusp = calcularDescontoPorHoras(valorSalarHora, faltaMesSusp);
                    tabelaCalculoFolha.setValueAt(faltaMesSusp, i, 2);
                    tabelaCalculoFolha.setValueAt(valorFaltaSusp, i, 4);
                }

                case 44 -> {
                    // Desconto Adiantamento de Salário
                    BigDecimal vale = salaryBase.multiply(new BigDecimal("0.40")).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt(vale, i, 4);
                }

                case 131 -> {
                    // Desconto Salário Maternidade
                    BigDecimal salarioMaternidade = salaryBase;
                    tabelaCalculoFolha.setValueAt(salarioMaternidade, i, 3);
                }

                case 172 ->
                    // Adiantamento Décimo Terceiro
                        tabelaCalculoFolha.setValueAt(salaryBase, i, 4);

                case 190 -> {
                    // Segunda Parcela do 13°
                    BigDecimal decimoTerc1Parcel2 = salaryBase.divide(new BigDecimal("2")).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt(decimoTerc1Parcel2, i, 4);
                }

                case 193 -> {
                    // Desconto Vale
                    BigDecimal decimoTerc1Parcel = salaryBase.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt(decimoTerc1Parcel, i, 4);
                }

                case 229 -> {
                    // Empréstimo Consignado
                    BigDecimal emprestConsig = new BigDecimal(emprestimoConsignado.getText());
                    tabelaCalculoFolha.setValueAt(emprestConsig, i, 4);
                }

                case 231 -> {
                    BigDecimal vAliment = new BigDecimal(valeAlimentacao.getText());
                    tabelaCalculoFolha.setValueAt(vAliment, i, 4);
                }
                case 232 -> {
                    BigDecimal valFarmac = new BigDecimal(valeFarmacia.getText());
                    tabelaCalculoFolha.setValueAt(valFarmac, i, 4);
                }
                case 233 -> {
                    BigDecimal vRefeicao = new BigDecimal(valeRefeicao.getText());
                    tabelaCalculoFolha.setValueAt(vRefeicao, i, 4);
                }
                case 235 -> {
                    BigDecimal planoMedic = new BigDecimal(planoMedico.getText());
                    tabelaCalculoFolha.setValueAt(planoMedic, i, 4);
                }
                case 236 -> {
                    BigDecimal descSegVida = new BigDecimal(seguroDeVida.getText());
                    tabelaCalculoFolha.setValueAt(descSegVida, i, 4);
                }
                case 241 -> {
                    BigDecimal descontaValeTrans = salaryBase.multiply(DESCONTO_VALE_TRANSPORTE).setScale(2, RoundingMode.HALF_UP);
                    tabelaCalculoFolha.setValueAt(descontaValeTrans, i, 4);
                }
                case 242 -> {
                    BigDecimal planoOdont = new BigDecimal(planoOdonto.getText());
                    tabelaCalculoFolha.setValueAt(planoOdont, i, 4);
                }
                case 243 -> {
                    BigDecimal planoMedicD = new BigDecimal(planoMedico.getText());
                    tabelaCalculoFolha.setValueAt(planoMedicD, i, 4);
                }
                case 244 -> {
                    BigDecimal inssTotal1 = calcularINSS(salaryBase);
                    tabelaCalculoFolha.setValueAt(inssTotal1, i, 4);
                }
                case 245 -> {
                    BigDecimal inssTotal2 = calcularINSS(salaryBase);
                    tabelaCalculoFolha.setValueAt(inssTotal2, i, 4);
                }
                case 246 -> {
                    int numDependente = Integer.parseInt(dependentesIrrf.getText());
                    salBase = (salaryBase.subtract(inssTotal)).subtract(DEDUCAO_DEPENDENTE.multiply(new BigDecimal(numDependente))).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal irrfTotalDeduzir1 = calcularIRRF(salBase, numDependente);
                    tabelaCalculoFolha.setValueAt(irrfTotalDeduzir1, i, 4);
                }
                case 247 -> {
                    int numDependenteIr = Integer.parseInt(dependentesIrrf.getText());
                    salBase = (salaryBase.subtract(inssTotal)).subtract(DEDUCAO_DEPENDENTE.multiply(new BigDecimal(numDependenteIr))).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal irrfTotalDeduzir2 = calcularIRRF(salBase, numDependenteIr);
                    tabelaCalculoFolha.setValueAt(irrfTotalDeduzir2, i, 4);
                }
            }
        }
    }

    // Métudo auxiliar para calcular o valor do desconto com base nas horas trabalhadas e faltas
    private BigDecimal calcularDescontoPorHoras(BigDecimal valorHora, BigDecimal faltas) {
        LocalTime horaIni = LocalTime.parse(horaEntrada.getText());
        LocalTime horaFim = LocalTime.parse(horaSaida.getText());
        Duration duracao = Duration.between(horaIni, horaFim);

        int horas = duracao.toHoursPart();
        BigDecimal minutos = BigDecimal.valueOf(duracao.toMinutesPart()).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal horasTotais = BigDecimal.valueOf(horas).add(minutos);

        return valorHora.multiply(horasTotais).multiply(faltas).setScale(1, RoundingMode.HALF_UP);
    }
}
