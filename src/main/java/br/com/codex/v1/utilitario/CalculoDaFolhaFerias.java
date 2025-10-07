/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilitarios;

import ConexaoBDCodex.ConexaoBD;
import static RecursosHumanos.CalculoFerias.inicioAquisitivoFerias;
import static RecursosHumanos.CalculoFerias.numMatricula;
import static RecursosHumanos.CalculoFerias.numeroDependentes;
import static RecursosHumanos.CalculoFerias.salarioBruto;
import static RecursosHumanos.CalculoFerias.salarioHora;
import static RecursosHumanos.CalculoFerias.tabelaEventosFerias;
import static RecursosHumanos.CalculoFerias.terminoAquisitivoFerias;
import static RecursosHumanos.CalculoFerias.totalDiasFerias;
import static RecursosHumanos.CalculoFerias.totalFaltas;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author alyes
 */
public class CalculoDaFolhaFerias {

    BigDecimal valorFerias, valorUmTercoFerias, diasDeFerias, faixa1Inss, faixa2Inss, faixa3Inss, faixa4Inss, inssTotal, irrfTotalDeduzir,
            salBase, totalHoras50, totalHoras70, totalHoras100, diasDeFeriasAbono, umTercoFerias, umTercoFeriasAbono, numDependente, calculo13, feriasEmDobro, umTercoDobro,
            valorIsencaoTabelaIrrf, salaryHour, Diasferias, aInsFer, aPerFer, horasTrabalhadasMes, mediComissoesFerias, mediAdicionaNoturno, valorMediaAdicional;

    BigDecimal salBruto = new BigDecimal(salarioBruto.getText());
    double valorDoSalarBruto = salBruto.doubleValue();
    
    BigDecimal salarioPorHora = new BigDecimal(salarioHora.getText());

    Date data;
    SimpleDateFormat dataAno = new SimpleDateFormat("yyyy");

    DecimalFormat df = new DecimalFormat("##,##00.##");

    ConexaoBD conectaFerias;

    LocalDate resultData_PerAqui = Instant.ofEpochMilli(inicioAquisitivoFerias.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate resultData_TermPerAqui = Instant.ofEpochMilli(terminoAquisitivoFerias.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

    Duration mesesDuration = Duration.between(resultData_PerAqui, resultData_TermPerAqui);// Total de meses recebendo a insalubridade

    /*--------------Calculando valor de Desconto do INSS--------------*/
    private void calculaInss() {

        if (valorDoSalarBruto <= 1100.00) {
            faixa1Inss = (new BigDecimal(1100.00)).multiply(new BigDecimal(0.075));
            inssTotal = faixa1Inss;
        } else if (valorDoSalarBruto >= 1100.01 && valorDoSalarBruto <= 2203.48) {
            faixa1Inss = (new BigDecimal(1100.00)).multiply(new BigDecimal(0.075));
            faixa2Inss = (salBruto.subtract(new BigDecimal(1100))).multiply(new BigDecimal(0.09));
            inssTotal = faixa1Inss.add( faixa2Inss);
        } else if (valorDoSalarBruto >= 2203.49 && valorDoSalarBruto <= 3305.22) {
            faixa1Inss = (new BigDecimal(1100.00)).multiply(new BigDecimal(0.075));
            faixa2Inss = ((new BigDecimal(2203.48)).subtract(new BigDecimal(1100.00))).multiply(new BigDecimal(0.09));
            faixa3Inss = (salBruto.subtract(new BigDecimal(2203.48))).multiply(new BigDecimal(0.12));
            inssTotal = faixa1Inss.add(faixa2Inss).add(faixa3Inss);
        } else if (valorDoSalarBruto >= 3305.23 || valorDoSalarBruto >= 6433.57) {
            faixa1Inss = (new BigDecimal(1100.00)).multiply(new BigDecimal(0.075));
            faixa2Inss = ((new BigDecimal(2203.48)).subtract(new BigDecimal(1100.00))).multiply(new BigDecimal(0.09));
            faixa3Inss = ((new BigDecimal(3305.22)).subtract(new BigDecimal(2203.48))).multiply(new BigDecimal(0.12));
            faixa4Inss = (salBruto.subtract(new BigDecimal(3305.22))).multiply(new BigDecimal(0.14));
            inssTotal = faixa1Inss.add(faixa2Inss).add(faixa3Inss).add(faixa4Inss);
        }

        /*A fórmula é realizada de acordo com o salário que o funcionário
        ganha, pega o salário compara com a faixa da tabela do governo e vai 
        fazendo as multiplicaçoes de acordo com essa faixa até chegar o
        valor do salário que o funcionário ganha, depois que encontrar
        o valor de cada faixa salarial, soma-se tudo, chegando assim ao
        valor de desconto do INSS*/
    }

    /*--------------Calculando valor de Desconto do IRRF--------------*/
    private void calculaIrrf() {

        numDependente = new BigDecimal(numeroDependentes.getText());

        /* 1 - Encontradno o salário Base*/
        salBase = salBruto.subtract(inssTotal.subtract((new BigDecimal("189.59")).multiply(numDependente)));

        /* 2 - Agora tem que calcular o valor da isenção obedecendo a tabela do governo*/
        double valorDoSalarBase = salBase.doubleValue();
        
        /*O cálculo do desconto do IRRf é feito assim:
        1 - Encontrar o salário base que é o salário bruto menos o desconto do INSS menos 189,59 vezes o N° de dependentes
        2 - Achar o valor de Isenção com base na tabela do IRRF, que é o salário base vezes a alícota de acordo com sua faixa salarial
        3 - Achar o valor a deduzir, que é o valor da Isenção (passo 2) menos o valor da parcela a deduzir (ver valores na tabela de IRRF)
         */
        
        if (valorDoSalarBase <= 1903.98) {
            valorIsencaoTabelaIrrf = new BigDecimal(0);
            irrfTotalDeduzir = valorIsencaoTabelaIrrf.subtract(new BigDecimal(0));
        } else if (valorDoSalarBase >= 1903.99 && valorDoSalarBase <= 2826.65) {
            valorIsencaoTabelaIrrf = salBase .multiply(new BigDecimal(0.075));
            irrfTotalDeduzir = valorIsencaoTabelaIrrf.subtract(new BigDecimal(142.80));// esse valor de 142.80 é a parcela a deduzir da tabela IRRF
        } else if (valorDoSalarBase >= 2826.66 && valorDoSalarBase <= 3751.05) {
            valorIsencaoTabelaIrrf = salBase .multiply(new BigDecimal(0.15));
            irrfTotalDeduzir = valorIsencaoTabelaIrrf.subtract(new BigDecimal(354.80));// esse valor de 354.80 é a parcela a deduzir da tabela IRRF
        } else if (valorDoSalarBase >= 3751.06 && valorDoSalarBase < 4664.68) {
            valorIsencaoTabelaIrrf = salBase .multiply(new BigDecimal(0.225));
            irrfTotalDeduzir = valorIsencaoTabelaIrrf.subtract(new BigDecimal(636.13));// esse valor de 636.13 é a parcela a deduzir da tabela IRRF
        } else if (valorDoSalarBase >= 4664.68) {
            valorIsencaoTabelaIrrf = salBase.multiply(new BigDecimal(0.275));
            irrfTotalDeduzir = valorIsencaoTabelaIrrf.subtract(new BigDecimal(869.36));// esse valor de 869.36 é a parcela a deduzir da tabela IRRF
        }
    }

    private void calculaMediaHoras50() {
        data = new Date();
        int numerOfMatric = Integer.parseInt(numMatricula.getText());

        String pegaAno = (dataAno.format(data));

        int valorAno = Integer.parseInt(pegaAno) - 1; //Pega o ano atual e subtrai por menos 1.

        String dataDe = String.valueOf(valorAno) + "-01" + "-01";
        String dataAte = String.valueOf(valorAno) + "-12" + "-31";

        conectaFerias = new ConexaoBD();
        conectaFerias.conecta();
        try {
            ResultSet re;
            try ( PreparedStatement ps = conectaFerias.conexao.prepareStatement("Select Avg(horaExtra50) as 'somaHoras50', valorSalarioHora from rhu_calf01 where numeroMatricula=? and dataProcessamento between ? and ?")) {
                ps.setInt(1, numerOfMatric);
                ps.setDate(2, java.sql.Date.valueOf(dataDe));
                ps.setDate(3, java.sql.Date.valueOf(dataAte));
                re = ps.executeQuery();

                if (re.next()) {
                    /*--- Aqui é encontrado o valor da hora extra mais o valor da hora normal----*/
                    BigDecimal hour50 = ((re.getBigDecimal("valorSalarioHora").multiply(new BigDecimal(50))).divide(new BigDecimal(100))).add((re.getBigDecimal("valorSalarioHora")).multiply(re.getBigDecimal("somaHoras50"))); //valor da hora extra vezes a média do período aquisitivo dela                    
                    totalHoras50 = hour50; //somando o total de horas no ano.
                }
            }
            re.close();
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao calcular média de horas " + erro, "", JOptionPane.ERROR_MESSAGE);
        }
        conectaFerias.desconecta();
    }

    private void calculaMediaHoras70() {
        data = new Date();
        int numerOfMatric = Integer.parseInt(numMatricula.getText());

        String pegaAno = (dataAno.format(data));

        int valorAno = Integer.parseInt(pegaAno) - 1; //Pega o ano atual e subtrai por menos 1.

        String dataDe = String.valueOf(valorAno) + "-01" + "-01";
        String dataAte = String.valueOf(valorAno) + "-12" + "-31";

        conectaFerias = new ConexaoBD();
        conectaFerias.conecta();
        try {
            ResultSet re;
            try ( PreparedStatement ps = conectaFerias.conexao.prepareStatement("Select Avg(horaExtra75) as 'somaHoras70', valorSalarioHora from rhu_calf01 where numeroMatricula=? and dataProcessamento between ? and ?")) {
                ps.setInt(1, numerOfMatric);
                ps.setDate(2, java.sql.Date.valueOf(dataDe));
                ps.setDate(3, java.sql.Date.valueOf(dataAte));
                re = ps.executeQuery();

                if (re.next()) {
                    /*--- Aqui é encontrado o valor da hora extra mais o valor da hora normal----*/
                    BigDecimal hour70 = ((re.getBigDecimal("valorSalarioHora").multiply(new BigDecimal(70))).divide(new BigDecimal(100))).add(re.getBigDecimal("valorSalarioHora").multiply(re.getBigDecimal("somaHoras70"))); //valor da hora extra vezes a média do período aquisitivo dela
                    totalHoras70 = hour70; //somando o total de horas no ano.
                }
            }
            re.close();
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao calcular média de horas " + erro, "", JOptionPane.ERROR_MESSAGE);
        }
        conectaFerias.desconecta();
    }

    private void calculaMediaHoras100() {
        data = new Date();
        int numerOfMatric = Integer.parseInt(numMatricula.getText());

        String pegaAno = (dataAno.format(data));

        int valorAno = Integer.parseInt(pegaAno) - 1; //Pega o ano atual e subtrai por menos 1 por conta que o período aquisitivo é referente ao ano anterior.

        String dataDe = String.valueOf(valorAno) + "-01" + "-01";
        String dataAte = String.valueOf(valorAno) + "-12" + "-31";

        conectaFerias = new ConexaoBD();
        conectaFerias.conecta();
        try {
            ResultSet re;
            try ( PreparedStatement ps = conectaFerias.conexao.prepareStatement("Select Avg(horaExtra100) as 'somaHoras100', valorSalarioHora from rhu_calf01 where numeroMatricula=? and dataProcessamento between ? and ?")) {
                ps.setInt(1, numerOfMatric);
                ps.setDate(2, java.sql.Date.valueOf(dataDe));
                ps.setDate(3, java.sql.Date.valueOf(dataAte));
                re = ps.executeQuery();

                if (re.next()) {
                    /*--- Aqui é encontrado o valor da hora extra mais o valor da hora normal----*/
                    BigDecimal hour100 = ((re.getBigDecimal("valorSalarioHora").multiply(new BigDecimal(100))).divide(new BigDecimal("100"))).add(re.getBigDecimal("valorSalarioHora").multiply(re.getBigDecimal("somaHoras100"))); //valor da hora extra vezes a média do período aquisitivo dela
                    totalHoras100 = hour100; //somando o total de horas no ano.
                }
            }
            re.close();
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao calcular média de horas " + erro, "", JOptionPane.ERROR_MESSAGE);
        }
        conectaFerias.desconecta();
    }

    public void escolheEventos() {

        for (int i = 0; i < tabelaEventosFerias.getRowCount(); i++) {

            int leEvento = Integer.parseInt(tabelaEventosFerias.getValueAt(i, 0).toString());

            
            switch (leEvento) {
                case 140 -> {
                    //Calculando 30 Dias de Férias Gozadas
                    diasDeFerias = salBruto; //Valor dos 30 dias de férias
                    tabelaEventosFerias.setValueAt(30, i, 2);
                    tabelaEventosFerias.setValueAt(df.format(diasDeFerias).replace(",", "."), i, 3);
                }
                
                case 141 -> {
                    //Calculando 20 Dias de Férias Gozadas
                    diasDeFerias = (salBruto.divide(new BigDecimal(30))).multiply(new BigDecimal(20));
                    tabelaEventosFerias.setValueAt(20, i, 2);
                    tabelaEventosFerias.setValueAt(df.format(valorFerias).replace(",", "."), i, 3);
                }
                
                case 142 -> {
                    //Calculando 15 Dias de Férias Gozadas
                    diasDeFerias = (salBruto.divide(new BigDecimal(30))).multiply(new BigDecimal(15)); //Valor dos 15 dias de férias
                    tabelaEventosFerias.setValueAt(15, i, 2);
                    tabelaEventosFerias.setValueAt(df.format(diasDeFerias).replace(",", "."), i, 3);
                }
                
                case 143 -> {
                    //Calculando 10 Dias de Férias Gozadas
                    diasDeFerias = (salBruto.divide(new BigDecimal(30))).multiply(new BigDecimal(10)); //Valor dos 10 dias de férias
                    tabelaEventosFerias.setValueAt(10, i, 2);
                    tabelaEventosFerias.setValueAt(df.format(diasDeFerias).replace(",", "."), i, 3);
                }
                
                case 144 -> {
                    //Calculando outros Dias de Férias
                    Diasferias = new BigDecimal(totalDiasFerias.getText());
                    diasDeFerias = (salBruto.divide(new BigDecimal(30))).multiply(Diasferias); //Valor dos dias de férias
                    tabelaEventosFerias.setValueAt(Diasferias, i, 2);
                    tabelaEventosFerias.setValueAt(df.format(diasDeFerias).replace(",", "."), i, 3);
                }
                
                case 145 -> {
                    //Calculando 1/3 de Férias
                    umTercoFerias = diasDeFerias.divide(new BigDecimal(3)); // cálculo de 1/3 de férias
                    tabelaEventosFerias.setValueAt(df.format(umTercoFerias).replace(",", "."), i, 3);
                }
                
                case 146 -> {
                    //Calculando Abono Pecuniário
                    diasDeFeriasAbono = (salBruto.divide(new BigDecimal(30))).multiply(new BigDecimal(10)); //Valor dos 10 dias de férias vendidas
                    tabelaEventosFerias.setValueAt(10, i, 2);
                    tabelaEventosFerias.setValueAt(df.format(diasDeFeriasAbono).replace(",", "."), i, 3);
                }
                
                case 147 -> {
                    //Calculando 1/3 Abono Pecuniário
                    umTercoFeriasAbono = diasDeFeriasAbono.divide(new BigDecimal(3)); // cálculo de 1/3 de férias vendidas
                    tabelaEventosFerias.setValueAt(df.format(umTercoFeriasAbono).replace(",", "."), i, 3);
                }
                
                case 148 -> {
                    //Calculando Média de Horas Extras 50%
                    calculaMediaHoras50();
                    tabelaEventosFerias.setValueAt(df.format(totalHoras50).replace(",", "."), i, 3);
                }
                
                case 149 -> {
                    //Calculando Média de Horas Extras 100%
                    calculaMediaHoras100();
                    tabelaEventosFerias.setValueAt(df.format(totalHoras100).replace(",", "."), i, 3);
                }
                
                case 150 -> {
                    //Calculando Média de Horas Extras 70%
                    calculaMediaHoras70();
                    tabelaEventosFerias.setValueAt(df.format(totalHoras70).replace(",", "."), i, 3);
                }
                
                case 151 -> {
                    //Calculando Insalubridade Sobre Férias
                    int nuMatric = Integer.parseInt(numMatricula.getText());

                    //Este bloco obtém o último valor do Provento da Insalubridade
                    //Aqui é calculado a média dos valores pagos da insalubridade entre o período aquisitivo do funcionário
                    conectaFerias = new ConexaoBD();
                    conectaFerias.conecta();
                    try {
                        ResultSet reSt;
                        try ( PreparedStatement ptt = conectaFerias.conexao.prepareStatement("Select Avg(valorProvento) as 'mediaProporInsalubri' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and dataProcessamento between ? and ?")) {
                            ptt.setInt(1, nuMatric);
                            ptt.setInt(2, 46);
                            ptt.setDate(3, java.sql.Date.valueOf(resultData_PerAqui));
                            ptt.setDate(4, java.sql.Date.valueOf(resultData_TermPerAqui));
                            reSt = ptt.executeQuery();

                            if (reSt.next()) {
                                aInsFer = reSt.getBigDecimal("mediaProporInsalubri");
                            }
                        }
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao obter média da Insalubridade " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conectaFerias.desconecta();

                    tabelaEventosFerias.setValueAt(mesesDuration, i, 2);
                    tabelaEventosFerias.setValueAt("%.2f%" + aInsFer, i, 3);
                }
                
                case 152 -> {
                    //Calculando Periculosidade Sobre as Férias
                    int numMatricu = Integer.parseInt(numMatricula.getText());

                    //Este bloco obtém o último valor do Provento da periculosidade
                    //Aqui é calculado a média dos valores pagos da periculosidade entre o período aquisitivo do funcionário
                    conectaFerias = new ConexaoBD();
                    conectaFerias.conecta();
                    try {
                        ResultSet reSt;
                        try ( PreparedStatement ptt = conectaFerias.conexao.prepareStatement("Select Avg(valorProvento) as 'mediaProporPericulos' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and dataProcessamento between ? and ?")) {
                            ptt.setInt(1, numMatricu);
                            ptt.setInt(2, 47);
                            ptt.setDate(3, java.sql.Date.valueOf(resultData_PerAqui));
                            ptt.setDate(4, java.sql.Date.valueOf(resultData_TermPerAqui));
                            reSt = ptt.executeQuery();

                            if (reSt.next()) {
                                aPerFer = reSt.getBigDecimal("mediaProporPericulos");
                            }
                        }
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao obter média da Periculosidade " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conectaFerias.desconecta();
                    tabelaEventosFerias.setValueAt(mesesDuration, i, 2);
                    tabelaEventosFerias.setValueAt("%.2f%" + aPerFer, i, 3);
                }
                
                case 153 -> {
                    //Calculando Comissões Sobre Férias
                    int numMatricuCf = Integer.parseInt(numMatricula.getText());

                    //Este bloco obtém o último valor do Provento da Comissão
                    //Aqui é calculado a média dos valores pagos da comissão entre o período aquisitivo do funcionário
                    conectaFerias = new ConexaoBD();
                    conectaFerias.conecta();
                    try {
                        ResultSet reSt;
                        try ( PreparedStatement ptt = conectaFerias.conexao.prepareStatement("Select Avg(valorProvento) as 'mediaComissFerias' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and dataProcessamento between=? and dataProcessamento=?")) {
                            ptt.setInt(1, numMatricuCf);
                            ptt.setInt(2, 51);
                            ptt.setDate(3, java.sql.Date.valueOf(resultData_PerAqui));
                            ptt.setDate(4, java.sql.Date.valueOf(resultData_TermPerAqui));
                            reSt = ptt.executeQuery();

                            if (reSt.next()) {
                                mediComissoesFerias = reSt.getBigDecimal("mediaComissFerias");
                            }
                        }
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao obter média da Comissões " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conectaFerias.desconecta();
                    tabelaEventosFerias.setValueAt(mesesDuration, i, 2);
                    tabelaEventosFerias.setValueAt(df.format(mediComissoesFerias).replace(",", "."), i, 3);
                }
                
                case 154 -> {
                    //Calculando Média de Adicional Noturno nas Férias
                    int numMatricuManf = Integer.parseInt(numMatricula.getText());

                    conectaFerias = new ConexaoBD();
                    conectaFerias.conecta();
                    try {
                        ResultSet reSt;
                        try ( PreparedStatement ptt = conectaFerias.conexao.prepareStatement("Select Avg(valorProvento) as 'mediaAdicNoturno' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and dataProcessamento between=? and dataProcessamento=?")) {
                            ptt.setInt(1, numMatricuManf);
                            ptt.setInt(2, 14);
                            ptt.setDate(3, java.sql.Date.valueOf(resultData_PerAqui));
                            ptt.setDate(4, java.sql.Date.valueOf(resultData_TermPerAqui));
                            reSt = ptt.executeQuery();

                            if (reSt.next()) {
                                mediAdicionaNoturno = reSt.getBigDecimal("mediaAdicNoturno");
                            }

                            BigDecimal valorDahoraNoturna = (salarioPorHora.multiply(new BigDecimal(20))).divide(new BigDecimal(100)); //Valor das Horas Noturnas
                            BigDecimal valorHorasNotMaisMediaAdNot = valorDahoraNoturna.multiply(mediAdicionaNoturno); //Valor das Horas Noturnas vezes a média do total das horas noturnos (somam todas as horas noturnas e divide por 12)
                            BigDecimal adicUmTerco = (valorHorasNotMaisMediaAdNot.add(salBruto)).divide(new BigDecimal(3));
                            valorMediaAdicional = valorHorasNotMaisMediaAdNot.add(salBruto).add(adicUmTerco);
                        }
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao obter média dos Adicionais Noturnos " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conectaFerias.desconecta();
                    tabelaEventosFerias.setValueAt(mesesDuration, i, 2);
                    tabelaEventosFerias.setValueAt(df.format(valorMediaAdicional).replace(",", "."), i, 3);
                }
                
                case 158 -> {
                    //Calculando Desconto INSS Sobre Férias
                    calculaInss();
                    tabelaEventosFerias.setValueAt(df.format(inssTotal).replace(",", "."), i, 4);
                }
                
                case 159 -> {
                    //Calculando Imposto de Renda Sobre Férias
                    calculaIrrf();
                    tabelaEventosFerias.setValueAt(df.format(irrfTotalDeduzir).replace(",", "."), i, 4);
                }
                
                case 167 -> {
                    //Calculando Adiantamento 1° Parcela Décimo Terceiro
                    calculo13 = salBruto.divide(new BigDecimal(2));
                    tabelaEventosFerias.setValueAt(df.format(calculo13).replace(",", "."), i, 3);
                }
                
                case 155 -> {
                    //Calculando Desconto Dias Redução Faltas Férias
                    BigDecimal diasDeFaltasFunc = new BigDecimal(totalFaltas.getText());
                    int valoDiasDEFalta = diasDeFaltasFunc.intValue();
                            
                    if (valoDiasDEFalta <= 5) {

                    } else if (valoDiasDEFalta >= 6 && valoDiasDEFalta <= 14) {

                        tabelaEventosFerias.setValueAt("24", i, 2);
                    } else if (valoDiasDEFalta >= 15 && valoDiasDEFalta <= 23) {

                        tabelaEventosFerias.setValueAt("18", i, 2);
                    } else if (valoDiasDEFalta >= 24 && valoDiasDEFalta <= 32) {

                        tabelaEventosFerias.setValueAt("12", i, 2);
                    } else if (valoDiasDEFalta > 32) {
                        JOptionPane.showMessageDialog(null, "Funcionário não têm direito a Férias, faltas são maior ou igual a 32.", "", JOptionPane.WARNING_MESSAGE);
                    }
                }
                
                case 156 -> {
                    // Calculando Férias em Dobro - FÉRIAS
                    
                    if (valorDoSalarBruto >= 1713.58) {

                        BigDecimal valorLiquidFerias = (salBruto.multiply(umTercoFerias)).subtract(inssTotal).subtract(irrfTotalDeduzir);
                        BigDecimal totalFeriasLiqui = valorLiquidFerias.multiply(new BigDecimal(2));
                        tabelaEventosFerias.setValueAt(totalFeriasLiqui, i, 3);

                    } else {

                        BigDecimal valorLiquidFerias = (salBruto.multiply(umTercoFerias)).subtract(inssTotal);
                        BigDecimal totalFeriasLiqui = valorLiquidFerias.multiply(new BigDecimal(2));
                        tabelaEventosFerias.setValueAt(totalFeriasLiqui, i, 3);
                    }
                }
                
                case 157 -> //Calculando 1/3 Constitucional Férias em Dobro - FÉRIAS
                    tabelaEventosFerias.setValueAt(umTercoFerias, i, 3);
                default -> {
                }
            }
        }
    }
}
