package br.com.codex.v1.utilitario;

import java.math.BigDecimal;

public class CalculoDaFolhaRescisao {


    BigDecimal salarioMinimo, mediaHorasExtras, calculo50MedSob13, mediaAdicNoturn13, mediAdicNotFer, mediaComissoesAno, mesesTrabalhados1, anosTrabalhados1, diasTrabalhados1, valrMediaAdicional, mediComissoesFerias, mediaHorasExtrasFer, inssTotal;
    BigDecimal salarioBaseFunc = new BigDecimal(salarioBase.getText());
    BigDecimal valorDoSalarioHora = new BigDecimal(salarioHora.getText());

    int diasCorridos;
    int numerOfMatric = Integer.parseInt(numeroDaMatricula.getText());

    DecimalFormat df = new DecimalFormat("##,##00.##");
    SimpleDateFormat dataFormato = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat pegaAno = new SimpleDateFormat("yyyy");

    Date dataAtual;

    private void obtemSalarioMin() {
        ConexaoBD conectaSalMin = new ConexaoBD();
        conectaSalMin.conecta();
        try {
            ResultSet rs;
            try ( PreparedStatement ps = conectaSalMin.conexao.prepareStatement("Select isentoAte from rhu_irrf01")) {
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
    }

    private void contaPeriodoAquisitivo() {

        /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/
        dataAtual = new Date();
        String dataPresente = dataFormato.format(dataAtual);
        String[] data_Presente = dataPresente.split("/");

        String dataDeInsal = dataFormato.format(dataDeAdmissao.getDateFormatString());
        String[] data_DeInsal = dataDeInsal.split("/");
        LocalDate resultDataDeInsal = LocalDate.of(Integer.parseInt(data_Presente[2]), Integer.parseInt(data_DeInsal[1]), Integer.parseInt(data_DeInsal[0]));

        String dataAteInsal = dataFormato.format(dataDeDemissao.getDateFormatString());
        String[] data_AteInsal = dataAteInsal.split("/");
        LocalDate resultDataAteInsal = LocalDate.of(Integer.parseInt(data_AteInsal[2]), Integer.parseInt(data_AteInsal[1]), Integer.parseInt(data_AteInsal[0]));

        Period periodo = Period.between(resultDataDeInsal, resultDataAteInsal);
        mesesTrabalhados1 = new BigDecimal(periodo.getMonths());
        anosTrabalhados1 = new BigDecimal(periodo.getYears());
    }

    public void escolheEventos() {

        obtemSalarioMin();

        for (int e = 0; e < tabelaEventosRescisao.getRowCount(); e++) {

            int leEvento = Integer.parseInt(tabelaEventosRescisao.getValueAt(e, 0).toString());

            switch (leEvento) {

                case 302 -> {
                    //Calculando Saldo de Salário
                    BigDecimal valorSalarioHora = new BigDecimal(salarioHora.getText());
                    BigDecimal valorDaHoraTrabaSemana = (new BigDecimal(horasSemanais.getText())).divide(new BigDecimal("7"));
                    BigDecimal quantDiasTrabMes = new BigDecimal(diasTrabalhadosNoMes.getText());

                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {

                        BigDecimal valorSaldoHoras = (salarioBaseFunc.divide(new BigDecimal(30)).multiply(quantDiasTrabMes));
                        tabelaEventosRescisao.setValueAt(quantDiasTrabMes, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras).replace(",", "."), e, 3);

                    } else {
                        BigDecimal valorSaldoHoras = valorSalarioHora.multiply(valorDaHoraTrabaSemana).multiply(quantDiasTrabMes);
                        tabelaEventosRescisao.setValueAt(valorDaHoraTrabaSemana, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras).replace(",", "."), e, 3);
                    }
                }

                case 303 -> {
                    //Calculando o Aviso Prévio Trabalhado
                    BigDecimal quantDiasTrabaNoMes = new BigDecimal(diasTrabalhadosNoMes.getText());
                    tabelaEventosRescisao.setValueAt(quantDiasTrabaNoMes, e, 2);
                }

                case 304 -> {
                    //Calculando o Aviso Prévio Indenizado

                    /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/
                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {
                        String dataAdmiss = dataFormato.format(dataDeAdmissao.getDate());
                        String[] data_Admiss = dataAdmiss.split("/");
                        LocalDate resultDataAdmiss = LocalDate.of(Integer.parseInt(data_Admiss[2]), Integer.parseInt(data_Admiss[1]), Integer.parseInt(data_Admiss[0]));

                        String dataDesmiss = dataFormato.format(dataDeAdmissao.getDate());
                        String[] data_Dedmiss = dataDesmiss.split("/");
                        LocalDate resultDataDedmiss = LocalDate.of(Integer.parseInt(data_Dedmiss[2]), Integer.parseInt(data_Dedmiss[1]), Integer.parseInt(data_Dedmiss[0]));

                        Period anos = Period.between(resultDataAdmiss, resultDataDedmiss); //obtendo a duração de anos trabalhados

                        int duracaoAnos = anos.getYears(); //obtem a duração em anos

                        int totalDiasTempoServ = 30 + (3 * duracaoAnos); // Cálculo do aviso prévio proporcional ao tempo de serviços (n° dias)

                        BigDecimal avisPrevio = salarioBaseFunc.multiply(new BigDecimal(totalDiasTempoServ));
                        tabelaEventosRescisao.setValueAt(totalDiasTempoServ, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(avisPrevio).replace(",", "."), e, 3);

                    } else {

                        String dataAdmiss = dataFormato.format(dataDeAdmissao.getDate());
                        String[] data_Admiss = dataAdmiss.split("/");
                        LocalDate resultDataAdmiss = LocalDate.of(Integer.parseInt(data_Admiss[2]), Integer.parseInt(data_Admiss[1]), Integer.parseInt(data_Admiss[0]));

                        String dataDesmiss = dataFormato.format(dataDeAdmissao.getDate());
                        String[] data_Dedmiss = dataDesmiss.split("/");
                        LocalDate resultDataDedmiss = LocalDate.of(Integer.parseInt(data_Dedmiss[2]), Integer.parseInt(data_Dedmiss[1]), Integer.parseInt(data_Dedmiss[0]));

                        Period anos = Period.between(resultDataAdmiss, resultDataDedmiss); //obtendo a duração de anos trabalhados

                        BigDecimal duracaoAnos = new BigDecimal(anos.getYears()); //obtem a duração em anos
                        BigDecimal valorDasHoraTrabaNoMes = new BigDecimal(valorValeRefeicao.getText());

                        BigDecimal totalDiasTempoServ = (valorDoSalarioHora.multiply(valorDasHoraTrabaNoMes)).add(new BigDecimal(3).multiply(duracaoAnos)); // Cálculo do aviso prévio proporcional ao tempo de serviços (n° dias)

                        BigDecimal avisPrevio = salarioBaseFunc.multiply(totalDiasTempoServ);
                        tabelaEventosRescisao.setValueAt(totalDiasTempoServ, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(avisPrevio).replace(",", "."), e, 3);

                    }
                }

                case 305 -> {
                    //Calculando a multa do FGTS devido

                    /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/
                    /*Obtendo o valor da data atual*/
                    String valorDatAtual = dataFormato.format(dataAtual);
                    String[] valor_DatAtual = valorDatAtual.split("/");

                    /*Obtendo o valor da data de Admissão*/
                    String datAdmissao = dataFormato.format(dataDeAdmissao.getDate());
                    String[] dat_Admissao = datAdmissao.split("/");

                    /*Obtendo o valor da data de Demissão*/
                    String datDemissao = dataFormato.format(dataDeDemissao.getDate());
                    String[] dat_Demissao = datDemissao.split("/");

                    /*Obtendo o período aquisitivo*/ /*-------Valor do ano atual-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periodoContratou = LocalDate.of(Integer.parseInt(valor_DatAtual[2]), Integer.parseInt(dat_Admissao[1]), Integer.parseInt(dat_Admissao[0]));
                    /*-------Valor do ano seguinte-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periodoDemitiu = LocalDate.of(Integer.parseInt(dat_Demissao[2]), Integer.parseInt(dat_Demissao[1]), Integer.parseInt(dat_Demissao[0]));

                    /*Obtendo quantos meses a pessoa trabalhou antes de tirar as férias*/
                    Period diferenEntreMeses = Period.between(periodoContratou, periodoDemitiu);

                    int totalAnosTrabalhados = diferenEntreMeses.getYears(); //Diferença de anos
                    int totalMesTrabalhados = diferenEntreMeses.getMonths(); //Diferença de meses

                    int valorTotasMeses = (12 * totalAnosTrabalhados) + totalMesTrabalhados;

                    BigDecimal fgts = salarioBaseFunc.multiply(new BigDecimal(0.08)); // valor de fgts para ser depositado na conta da caixa
                    BigDecimal totalDepositoFgts = fgts.multiply(new BigDecimal(valorTotasMeses)); //Quantidade de FGTS depositado durante o tempo em que o funcionário trabalhou
                    BigDecimal multaFgts = totalDepositoFgts.multiply(new BigDecimal(0.4)); // calculando o valor da multa

                    tabelaEventosRescisao.setValueAt(df.format(multaFgts).replace(".", "").replace(",", "."), e, e);
                }

                case 306 -> {
                    //Calculando Férias Proporcionais

                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {

                        /*Calculando faltas caso o funcionário tenha*/
                        int valorDasFaltas = Integer.parseInt(faltasNoMes.getText());

                        if (valorDasFaltas <= 5) {
                            diasCorridos = 30;
                        } else if (valorDasFaltas >= 6 && valorDasFaltas <= 14) {
                            diasCorridos = 24;
                        } else if (valorDasFaltas >= 15 && valorDasFaltas <= 23) {
                            diasCorridos = 18;
                        } else if (valorDasFaltas >= 24 && valorDasFaltas <= 32) {
                            diasCorridos = 12;
                        } else if (valorDasFaltas >= 24 && valorDasFaltas <= 32) {
                            diasCorridos = 12;
                        } else if (valorDasFaltas > 32) {
                            diasCorridos = 0;
                        }

                        BigDecimal valorDasFeriasVencidas = (salarioBaseFunc.multiply(new BigDecimal(diasCorridos))).divide(new BigDecimal(30));

                        /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/

                        /*Aqui são as férias proporcionais, que no caso é quando o funcionário é demitido antes de completar 1 ano do período aquisitivo*/
                        //Calculando o período aquisitivo

                        /*Obtendo o valor da data atual*/
                        String valorDataAtual = dataFormato.format(dataAtual);
                        String[] valor_DataAtual = valorDataAtual.split("/");

                        /*Obtendo o valor da data de Admissão*/
                        String datAdmiss = dataFormato.format(dataDeAdmissao.getDate());
                        String[] dat_Admiss = datAdmiss.split("/");

                        /*Obtendo o valor da data de Demissão*/
                        String datDemiss = dataFormato.format(dataDeDemissao.getDate());
                        String[] dat_Demiss = datDemiss.split("/");

                        /*Obtendo o período aquisitivo*/ /*-------Valor do ano atual-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                        LocalDate periodoAquisitivoDe = LocalDate.of(Integer.parseInt(valor_DataAtual[2]), Integer.parseInt(dat_Admiss[1]), Integer.parseInt(dat_Admiss[0]));

                        /*-------Valor do ano seguinte-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                        LocalDate periodoDemitido = LocalDate.of(Integer.parseInt(dat_Demiss[2]), Integer.parseInt(dat_Demiss[1]), Integer.parseInt(dat_Demiss[0]));

                        /*Obtendo quantos meses a pessoa trabalhou antes de tirar as férias*/
                        Period diferencaEntreMeses = Period.between(periodoAquisitivoDe, periodoDemitido);

                        int mesesTrabalhados = diferencaEntreMeses.getMonths(); //Diferença de meses
                        int valorDias = diferencaEntreMeses.getDays(); //Difereçna de dias

                        if (valorDias > 15) {

                            BigDecimal valorFeriasProporcional = (valorDasFeriasVencidas.divide(new BigDecimal(12))).multiply(new BigDecimal(mesesTrabalhados));
                            tabelaEventosRescisao.setValueAt(mesesTrabalhados, e, 2);
                            tabelaEventosRescisao.setValueAt(df.format(valorFeriasProporcional).replace(".", "").replace(",", "."), e, 3);

                        } else {

                            BigDecimal valorFeriasProporcional = (valorDasFeriasVencidas.divide(new BigDecimal(12))).multiply((new BigDecimal(mesesTrabalhados)).subtract(new BigDecimal(1)));
                            tabelaEventosRescisao.setValueAt(mesesTrabalhados - 1, e, 2);
                            tabelaEventosRescisao.setValueAt(df.format(valorFeriasProporcional).replace(".", "").replace(",", "."), e, 3);
                        }
                    } else { // Este else é para funcionários horista ou outros

                        /*Calculando faltas caso o funcionário tenha*/
                        int valorDasFaltas = Integer.parseInt(faltasNoMes.getText());

                        if (valorDasFaltas <= 5) {
                            diasCorridos = 30;
                        } else if (valorDasFaltas >= 6 && valorDasFaltas <= 14) {
                            diasCorridos = 24;
                        } else if (valorDasFaltas >= 15 && valorDasFaltas <= 23) {
                            diasCorridos = 18;
                        } else if (valorDasFaltas >= 24 && valorDasFaltas <= 32) {
                            diasCorridos = 12;
                        } else if (valorDasFaltas >= 24 && valorDasFaltas <= 32) {
                            diasCorridos = 12;
                        } else if (valorDasFaltas > 32) {
                            diasCorridos = 0;
                        }

                        /*Valor salário dia*/
                        BigDecimal valorDasFeriasVencidas = ((salarioBaseFunc.divide(new BigDecimal(30)))).multiply(new BigDecimal(diasCorridos));

                        /*Aqui são as férias proporcionais, que no caso é quando o funcionário é demitido antes de completar 1 ano do período aquisitivo*/
                        //Calculando o período aquisitivo

                        /*Obtendo o valor da data atual*/
                        String valorDataAtual = dataFormato.format(dataAtual);
                        String[] valor_DataAtual = valorDataAtual.split("/");

                        /*Obtendo o valor da data de Admissão*/
                        String datAdmiss = dataFormato.format(dataDeAdmissao.getDate());
                        String[] dat_Admiss = datAdmiss.split("/");

                        /*Obtendo o valor da data de Demissão*/
                        String datDemiss = dataFormato.format(dataDeDemissao.getDate());
                        String[] dat_Demiss = datDemiss.split("/");

                        /*Obtendo o período aquisitivo*/ /*-------Valor do ano atual-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                        LocalDate periodoAquisitivoDe = LocalDate.of(Integer.parseInt(valor_DataAtual[2]), Integer.parseInt(dat_Admiss[1]), Integer.parseInt(dat_Admiss[0]));
                        /*-------Valor do ano seguinte-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                        LocalDate periodoDemitido = LocalDate.of(Integer.parseInt(dat_Demiss[2]), Integer.parseInt(dat_Demiss[1]), Integer.parseInt(dat_Demiss[0]));

                        /*Obtendo quantos meses a pessoa trabalhou antes de tirar as férias*/
                        Period diferencaEntreMeses = Period.between(periodoAquisitivoDe, periodoDemitido);

                        int mesesTrabalhados = diferencaEntreMeses.getMonths();
                        int valorDias = diferencaEntreMeses.getDays(); //Difereçna de dias

                        if (valorDias > 15) {

                            BigDecimal valorFeriasProporcional = (valorDasFeriasVencidas.divide(new BigDecimal(12))).multiply(new BigDecimal(mesesTrabalhados));
                            tabelaEventosRescisao.setValueAt(mesesTrabalhados, e, 2);
                            tabelaEventosRescisao.setValueAt(df.format(valorFeriasProporcional).replace(".", "").replace(",", "."), e, 3);

                        } else {

                            BigDecimal valorFeriasProporcional = (valorDasFeriasVencidas.divide(new BigDecimal(12))).multiply((new BigDecimal(mesesTrabalhados)).subtract(new BigDecimal(1)));
                            tabelaEventosRescisao.setValueAt(mesesTrabalhados - 1, e, 2);
                            tabelaEventosRescisao.setValueAt(df.format(valorFeriasProporcional).replace(".", "").replace(",", "."), e, 3);
                        }
                    }
                }

                case 307 -> {
                    //Calculando Férias Vencidas

                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {

                        /*Calculando faltas caso o funcionário tenha*/
                        int valorDasFaltas = Integer.parseInt(faltasNoMes.getText());

                        if (valorDasFaltas <= 5) {
                            diasCorridos = 30;
                        } else if (valorDasFaltas >= 6 && valorDasFaltas <= 14) {
                            diasCorridos = 24;
                        } else if (valorDasFaltas >= 15 && valorDasFaltas <= 23) {
                            diasCorridos = 18;
                        } else if (valorDasFaltas >= 24 && valorDasFaltas <= 32) {
                            diasCorridos = 12;
                        } else if (valorDasFaltas >= 24 && valorDasFaltas <= 32) {
                            diasCorridos = 12;
                        } else if (valorDasFaltas > 32) {
                            diasCorridos = 0;
                        }

                        BigDecimal valorDasFeriasVencidas = (salarioBaseFunc.multiply(new BigDecimal(diasCorridos))).divide(new BigDecimal(30));

                        /*A fórmula é o valor do salário + ( salário/3  - que no caso é 1/3)*/
                        BigDecimal valorFeriasVencidas = valorDasFeriasVencidas.add(salarioBaseFunc.divide(new BigDecimal(3)));
                        tabelaEventosRescisao.setValueAt(diasCorridos, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorFeriasVencidas).replace(".", "").replace(",", "."), e, 3);

                    } else { // Este else é para funcionários horista ou outros

                        /*Calculando faltas caso o funcionário tenha*/
                        int valorDasFaltas = Integer.parseInt(faltasNoMes.getText());

                        if (valorDasFaltas <= 5) {
                            diasCorridos = 30;
                        } else if (valorDasFaltas >= 6 && valorDasFaltas <= 14) {
                            diasCorridos = 24;
                        } else if (valorDasFaltas >= 15 && valorDasFaltas <= 23) {
                            diasCorridos = 18;
                        } else if (valorDasFaltas >= 24 && valorDasFaltas <= 32) {
                            diasCorridos = 12;
                        } else if (valorDasFaltas >= 24 && valorDasFaltas <= 32) {
                            diasCorridos = 12;
                        } else if (valorDasFaltas > 32) {
                            diasCorridos = 0;
                        }
                        /*Valor salário dia*/
                        BigDecimal valorDasFeriasVencidas = (salarioBaseFunc.divide(new BigDecimal(30))).multiply(new BigDecimal(diasCorridos));

                        /*A fórmula é o valor do salário + ( salário/3  - que no caso é 1/3)*/
                        BigDecimal valorFeriasVencidas = valorDasFeriasVencidas.add(salarioBaseFunc.divide(new BigDecimal(3)));
                        tabelaEventosRescisao.setValueAt(diasCorridos, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorFeriasVencidas).replace(".", "").replace(",", "."), e, 3);
                    }
                }

                case 308 -> {
                    //Calculando Média Horas Extras Férias Proporcionais

                    /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/
                    /*Aqui são as férias proporcionais, que no caso é quando o funcionário é demitido antes de completar 1 ano do período aquisitivo*/
                    //Calculando o período aquisitivo

                    /*Obtendo o valor da data atual*/
                    String valDataAtual = dataFormato.format(dataAtual);
                    String[] val_DataAtual = valDataAtual.split("/");

                    /*Obtendo o valor da data de Admissão*/
                    String datDAdmiss = dataFormato.format(dataDeAdmissao.getDate());
                    String[] dat_DAdmiss = datDAdmiss.split("/");

                    /*Obtendo o valor da data de Demissão*/
                    String datDeDemiss = dataFormato.format(dataDeDemissao.getDate());
                    String[] dat_DeDemiss = datDeDemiss.split("/");

                    /*Obtendo o período aquisitivo*/ /*-------Valor do ano atual-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate peryodoAquisitivoDe = LocalDate.of(Integer.parseInt(val_DataAtual[2]), Integer.parseInt(dat_DAdmiss[1]), Integer.parseInt(dat_DAdmiss[0]));
                    /*Valor do ano de demissão-*/ /*-------Mês e dia oriundos a partir da data de demissão------*/
                    LocalDate peryodoDemitido = LocalDate.of(Integer.parseInt(dat_DeDemiss[2]), Integer.parseInt(dat_DeDemiss[1]), Integer.parseInt(dat_DeDemiss[0]));

                    /*Obtendo quantos meses a pessoa trabalhou antes de tirar as férias*/
                    Period diferEntreMeses = Period.between(peryodoAquisitivoDe, peryodoDemitido);

                    int monthTrabalhados = diferEntreMeses.getMonths(); //Diferença de meses
                    int valDias = diferEntreMeses.getDays(); //Difereçna de dias

                    /*Calculando Média de Horas Extras do Período Aquisitivo*/
                    BigDecimal somaTodasHorasEx = new BigDecimal(0);
                    ConexaoBD conecHextPerAqui = new ConexaoBD();
                    conecHextPerAqui.conecta();
                    try {
                        ResultSet rsHePerAq;
                        try ( PreparedStatement pstHePerAq = conecHextPerAqui.conexao.prepareStatement("Select Sum(horaExtra50) as 'mediaHorEx50', Sum(horaExtra75) as 'mediaHorEx75', Sum(horaExtra100) as 'mediaHorEx100' from rhu_calf01 where numeroMatricula=? and dataProcessamento between ? and ?")) {
                            pstHePerAq.setInt(1, numerOfMatric);
                            pstHePerAq.setDate(2, java.sql.Date.valueOf(peryodoAquisitivoDe));
                            pstHePerAq.setDate(3, java.sql.Date.valueOf(peryodoDemitido));

                            rsHePerAq = pstHePerAq.executeQuery();

                            if (rsHePerAq.next()) {
                                /*Apuara-se as médias de horas extras do período, ou seja, soma-se as horas extras de todo período aquisitivo e divide-se por 12 meses (que seria o período aquisitivo).*/
                                somaTodasHorasEx = (rsHePerAq.getBigDecimal("mediaHorEx50").add(rsHePerAq.getBigDecimal("mediaHorEx75")).add(rsHePerAq.getBigDecimal("mediaHorEx100"))).divide(new BigDecimal(12));
                            }
                        }
                        rsHePerAq.close();
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular média de horas extras sobre férias: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conecHextPerAqui.desconecta();

                    tabelaEventosRescisao.setValueAt(monthTrabalhados, e, 2);
                    /*calculando valor da hora extra*/
                    BigDecimal horaExtraDe50 = ((salarioBaseFunc.add((new BigDecimal(0.3)).multiply(salarioBaseFunc))).divide(new BigDecimal(220))).multiply(new BigDecimal(1.5));

                    /*Calculando 1/3 de férias*/
                    BigDecimal diasDeFerias = ((salarioBaseFunc.divide(new BigDecimal(30))).multiply(new BigDecimal(valDias))).divide(new BigDecimal(3));
                    /*Calculando valor da Médida de Horas Extras sobre as Férias*/
                    BigDecimal valorMediaHorasFerias = (somaTodasHorasEx.multiply(horaExtraDe50)).add(diasDeFerias);
                    tabelaEventosRescisao.setValueAt(df.format(valorMediaHorasFerias).replace(".", "").replace(",", "."), e, 3);
                }

                case 309 -> {
                    //Calculando Insalubridade Sobre Férias Proporcionais
                    obtemSalarioMin();
                    contaPeriodoAquisitivo();
                    BigDecimal porcentInsalubreFer = new BigDecimal(valorInsalubridade.getText());
                    BigDecimal diasTrabInsalu = ((salarioMinimo.multiply(porcentInsalubreFer.divide(new BigDecimal(100)))).divide(new BigDecimal(12))).multiply(mesesTrabalhados1);
                    tabelaEventosRescisao.setValueAt(df.format(diasTrabInsalu).replace(",", "."), e, 3);
                }

                case 310 -> {
                    //Calculando Periculosidade Sobre Férias FORMULA = (Valor Férias Proporcionais + 1/3 Férias ) * 30%
                    contaPeriodoAquisitivo();
                    BigDecimal saldoHorasPericFer;
                    BigDecimal periculosidadeFer = new BigDecimal(valorPericulosidade.getText());
                    BigDecimal salaHoraPeric = new BigDecimal(salarioHora.getText());
                    BigDecimal horaTrabMesPeric = new BigDecimal(valorValeRefeicao.getText());
                    BigDecimal diasTrabMesPeric = new BigDecimal(diasTrabalhadosNoMes.getText());

                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {

                        saldoHorasPericFer = (salarioBaseFunc.divide(new BigDecimal(30))).multiply(diasTrabMesPeric);
                        tabelaEventosRescisao.setValueAt(diasTrabMesPeric, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(saldoHorasPericFer).replace(".", "").replace(",", "."), e, 3);

                    } else {
                        saldoHorasPericFer = salaHoraPeric.multiply(horaTrabMesPeric);
                        tabelaEventosRescisao.setValueAt(diasTrabMesPeric, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(saldoHorasPericFer).replace(",", "."), e, 3);
                    }
                    //Calculando Periculosida sobre Férias  /* calcula as férias proporcionais ou vencidas*/         /* calculando 1/3 de férias*/
                    BigDecimal periculSobreFer = (((saldoHorasPericFer.divide(new BigDecimal(12))).multiply(mesesTrabalhados1)).add(((saldoHorasPericFer.divide(new BigDecimal(12))).multiply(mesesTrabalhados1)).divide(new BigDecimal(3)))).multiply(periculosidadeFer.divide(new BigDecimal(100)));
                    tabelaEventosRescisao.setValueAt(df.format(periculSobreFer).replace(".", "").replace(",", "."), e, 3);
                }

                case 311 -> {
                    //Calculando Média Adicional Noturno Férias
                    int numMatric = Integer.parseInt(numeroDaMatricula.getText());

                    /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/
                    /*Obtendo o valor da data atual*/
                    String datAtualizada = dataFormato.format(dataAtual);
                    String[] dat_Atualizada = datAtualizada.split("/");

                    /*Obtendo o valor da data de Admissão*/
                    String valorDataAdmissao = dataFormato.format(dataDeAdmissao.getDate());
                    String[] valorData_Admissao = valorDataAdmissao.split("/");

                    /*Obtendo o valor da data de Demissão*/
                    String dataDemissao = dataFormato.format(dataDeDemissao.getDate());
                    String[] valorData_Demissao = dataDemissao.split("/");

                    /*Obtendo o período aquisitivo*/ /*-------Valor do ano atual-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periAquisitivoDe = LocalDate.of(Integer.parseInt(dat_Atualizada[2]), Integer.parseInt(valorData_Admissao[1]), Integer.parseInt(valorData_Admissao[0]));
                    /*-------Valor do ano seguinte-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periodoDemitido = LocalDate.of(Integer.parseInt(valorData_Demissao[2]), Integer.parseInt(valorData_Demissao[1]), Integer.parseInt(valorData_Demissao[0]));

                    Period duracaoMeses = Period.between(periAquisitivoDe, periodoDemitido);

                    ConexaoBD conectaFerias = new ConexaoBD();
                    conectaFerias.conecta();
                    try {
                        ResultSet reSt;
                        try ( PreparedStatement ptt = conectaFerias.conexao.prepareStatement("Select Avg(valorProvento) as 'mediaAdicNotuFer' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and dataProcessamento between ? and ?")) {
                            ptt.setInt(1, numMatric);
                            ptt.setInt(2, 14);
                            ptt.setDate(3, java.sql.Date.valueOf(periAquisitivoDe));
                            ptt.setDate(4, java.sql.Date.valueOf(periodoDemitido));
                            reSt = ptt.executeQuery();

                            if (reSt.next()) {
                                mediAdicNotFer = reSt.getBigDecimal("mediaAdicNotuFer");
                            }

                            BigDecimal valrDahoraNoturna = (valorDoSalarioHora.multiply(new BigDecimal(20))).divide(new BigDecimal(100)); //Valor das Horas Noturnas
                            BigDecimal valrHorasNotMaisMediaAdNot = valrDahoraNoturna.multiply(mediAdicNotFer); //Valor das Horas Noturnas vezes a média do total das horas noturnos (somam todas as horas noturnas e divide por 12)
                            BigDecimal adicUmTerco = (valrHorasNotMaisMediaAdNot.add(salarioBaseFunc)).divide(new BigDecimal(3));
                            valrMediaAdicional = valrHorasNotMaisMediaAdNot.add(salarioBaseFunc).add(adicUmTerco);
                        }
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao obter média dos Adicionais Noturnos " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conectaFerias.desconecta();
                    tabelaEventosRescisao.setValueAt(duracaoMeses, e, 2);
                    tabelaEventosRescisao.setValueAt(df.format(valrMediaAdicional).replace(".", "").replace(",", "."), e, 3);
                }

                case 312 -> {
                    //Calculando Salário Família na Rescisão

                    /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/
                    /*Obtendo o valor da data atual*/
                    String datAtualiz = dataFormato.format(dataAtual);
                    String[] dat_Atualiz = datAtualiz.split("/");
                    /*Obtendo o valor da data de Admissão*/
                    String valDaDataAdmissao = dataFormato.format(dataDeAdmissao.getDate());
                    String[] valDaData_Admissao = valDaDataAdmissao.split("/");
                    /*Obtendo o valor da data de Demissão*/
                    String dataDeDemiss = dataFormato.format(dataDeDemissao.getDate());
                    String[] valDaData_Demissao = dataDeDemiss.split("/");

                    /*Obtendo o período aquisitivo*/ /*-------Valor do ano atual-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periAquisDe = LocalDate.of(Integer.parseInt(dat_Atualiz[2]), Integer.parseInt(valDaData_Admissao[1]), Integer.parseInt(valDaData_Admissao[0]));

                    /*-------Valor do ano seguinte-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periodoDemit = LocalDate.of(Integer.parseInt(valDaData_Demissao[2]), Integer.parseInt(valDaData_Demissao[1]), Integer.parseInt(valDaData_Demissao[0]));

                    Period duracaoDosMeses = Period.between(periAquisDe, periodoDemit);

                    int valDifDias = duracaoDosMeses.getDays(); //Difereçna de dias
                    BigDecimal filhos = new BigDecimal(numDependenteIrrf.getText());
                    double cota1, cota2, val1, val2;
                    BigDecimal valorTotal = new BigDecimal(0);
                    BigDecimal valorSalFam = new BigDecimal(0);

                    double valorDoSalavrioBasFunc = salarioBaseFunc.doubleValue();
                    ConexaoBD conSalFam = new ConexaoBD();
                    conSalFam.conecta();
                    try {
                        ResultSet rSalFam;
                        try ( PreparedStatement pStm = conSalFam.conexao.prepareStatement("Select * from rhu_sfml01")) {
                            rSalFam = pStm.executeQuery();

                            if (rSalFam.next()) {

                                val1 = rSalFam.getDouble("faixa1");
                                val2 = rSalFam.getDouble("faixa2");
                                cota1 = rSalFam.getDouble("cota1");
                                cota2 = rSalFam.getDouble("cota2");

                                if (valorDoSalavrioBasFunc > val2 && valorDoSalavrioBasFunc <= val1) {
                                    valorTotal = new BigDecimal(cota1);
                                } else if (valorDoSalavrioBasFunc <= val2) {
                                    valorTotal = new BigDecimal(cota2);
                                }
                                valorSalFam = valorTotal.multiply(filhos);
                            } else {
                            }
                        }
                        rSalFam.close();
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular salário família: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conSalFam.desconecta();
                    //Calculando o Salário Família
                    BigDecimal totalSalFamResc = (valorSalFam.multiply(new BigDecimal(30))).multiply(new BigDecimal(valDifDias));
                    tabelaEventosRescisao.setValueAt(valDifDias, e, 2);
                    tabelaEventosRescisao.setValueAt(df.format(totalSalFamResc).replace(".", "").replace(",", "."), e, 3);
                }

                case 313 -> {
                    //Calculando o INSS Sobre a Rescisão
                    double valorDoSalavrioBasFunc2 = salarioBaseFunc.doubleValue();

                    if (valorDoSalavrioBasFunc2 <= 1100.00) {
                        BigDecimal faixa1Inss = (new BigDecimal(1100.00)).multiply(new BigDecimal(0.075));
                        inssTotal = faixa1Inss;
                        tabelaEventosRescisao.setValueAt(df.format(inssTotal).replace(",", "."), e, 4);

                    } else if (valorDoSalavrioBasFunc2 >= 1100.01 && valorDoSalavrioBasFunc2 <= 2203.48) {
                        BigDecimal faixa1Inss = (new BigDecimal(1100.00)).multiply(new BigDecimal(0.075));
                        BigDecimal faixa2Inss = (salarioBaseFunc.subtract(new BigDecimal(1100))).multiply(new BigDecimal(0.09));
                        inssTotal = faixa1Inss.add(faixa2Inss);
                        tabelaEventosRescisao.setValueAt(df.format(inssTotal).replace(",", "."), e, 4);

                    } else if (valorDoSalavrioBasFunc2 >= 2203.49 && valorDoSalavrioBasFunc2 <= 3305.22) {
                        BigDecimal faixa1Inss = (new BigDecimal(1100.00)).multiply(new BigDecimal(0.075));
                        BigDecimal faixa2Inss = (new BigDecimal(2203.48).subtract(new BigDecimal(1100.00))).multiply(new BigDecimal(0.09));
                        BigDecimal faixa3Inss = (salarioBaseFunc.subtract(new BigDecimal(2203.48))).multiply(new BigDecimal(0.12));
                        inssTotal = faixa1Inss.add(faixa2Inss).add(faixa3Inss);
                        tabelaEventosRescisao.setValueAt(df.format(inssTotal).replace(",", "."), e, 4);

                    } else if (valorDoSalavrioBasFunc2 >= 3305.23 || valorDoSalavrioBasFunc2 >= 6433.57) {
                        BigDecimal faixa1Inss = (new BigDecimal(1100.00)).multiply(new BigDecimal(0.075));
                        BigDecimal faixa2Inss = ((new BigDecimal(2203.48)).subtract(new BigDecimal(1100.00))).multiply(new BigDecimal(0.09));
                        BigDecimal faixa3Inss = ((new BigDecimal(3305.22)).subtract(new BigDecimal(2203.48))).multiply(new BigDecimal(0.12));
                        BigDecimal faixa4Inss = (salarioBaseFunc.subtract(new BigDecimal(3305.22))).multiply(new BigDecimal(0.14));
                        inssTotal = faixa1Inss.add(faixa2Inss).add(faixa3Inss).add(faixa4Inss);
                        tabelaEventosRescisao.setValueAt(df.format(inssTotal).replace(",", "."), e, 4);
                    }
                }

                case 315 -> {
                    //Calculando Média de Comissões nas Férias:
                    int numMatricuCf = Integer.parseInt(numeroDaMatricula.getText());
                    /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/

                    /*Obtendo o valor da data atual*/
                    String actualDate = dataFormato.format(dataAtual);
                    String[] actual_Date = actualDate.split("/");
                    /*Obtendo o valor da data de Admissão*/
                    String vlrDataAdmissao = dataFormato.format(dataDeAdmissao.getDate());
                    String[] vlrData_Admissao = vlrDataAdmissao.split("/");
                    /*Obtendo o valor da data de Demissão*/
                    String vlrDataDemissao = dataFormato.format(dataDeDemissao.getDate());
                    String[] vlrData_Demissao = vlrDataDemissao.split("/");
                    /*Obtendo o período aquisitivo*/ /*-------Valor do ano atual-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periAquisiDe = LocalDate.of(Integer.parseInt(actual_Date[2]), Integer.parseInt(vlrData_Admissao[1]), Integer.parseInt(vlrData_Admissao[0]));
                    /*-------Valor do ano seguinte-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periDemitido = LocalDate.of(Integer.parseInt(vlrData_Demissao[2]), Integer.parseInt(vlrData_Demissao[1]), Integer.parseInt(vlrData_Demissao[0]));
                    Period duracaoMesesFer = Period.between(periAquisiDe, periDemitido);
                    ConexaoBD conectaFerias = new ConexaoBD();
                    conectaFerias.conecta();
                    try {
                        ResultSet reSt;
                        try ( PreparedStatement ptt = conectaFerias.conexao.prepareStatement("Select Avg(valorProvento) as 'mediaComissFerias' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and dataProcessamento between ? and ?")) {
                            ptt.setInt(1, numMatricuCf);
                            ptt.setInt(2, 51);
                            ptt.setDate(3, java.sql.Date.valueOf(periAquisiDe));
                            ptt.setDate(4, java.sql.Date.valueOf(periDemitido));
                            reSt = ptt.executeQuery();

                            if (reSt.next()) {
                                mediComissoesFerias = reSt.getBigDecimal("mediaComissFerias");
                            }
                        }
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao obter média da Comissões: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conectaFerias.desconecta();
                    tabelaEventosRescisao.setValueAt(duracaoMesesFer, e, 2);
                    tabelaEventosRescisao.setValueAt(df.format(mediComissoesFerias).replace(".", "").replace(",", "."), e, 3);
                }

                case 316 -> {
                    //Calculando Média de Horas Extras Sobre Férias
                    //Calculando Horas 50%
                    int numMatriMHEx = Integer.parseInt(numeroDaMatricula.getText());

                    /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/
                    dataAtual = new Date();
                    String dtAnoAtualMHEx = dataFormato.format(dataAtual);
                    String[] dtAno_AtualMHEx = dtAnoAtualMHEx.split("/");

                    /*Obtendo o valor da data de Admissão*/
                    String dateAdmissao = dataFormato.format(dataDeAdmissao.getDate());
                    String[] date_Admissao = dateAdmissao.split("/");

                    /*Obtendo o valor da data de Demissão*/
                    String dateDemissao = dataFormato.format(dataDeDemissao.getDate());
                    String[] date_Demissao = dateDemissao.split("/");

                    /*Obtendo o período aquisitivo*/ /*-------Valor do ano atual-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate prdoAquisiDe = LocalDate.of(Integer.parseInt(dtAno_AtualMHEx[2]), Integer.parseInt(date_Admissao[1]), Integer.parseInt(date_Admissao[0]));
                    /*-------Valor do ano seguinte-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate prdoDemitido = LocalDate.of(Integer.parseInt(date_Demissao[2]), Integer.parseInt(date_Demissao[1]), Integer.parseInt(date_Demissao[0]));

                    ConexaoBD connectMHEx = new ConexaoBD();
                    connectMHEx.conecta();
                    try {
                        ResultSet rtMHEx;
                        try ( PreparedStatement pstMHEx = connectMHEx.conexao.prepareStatement("Select Avg(valorProvento) as 'media50Ano', Avg(valorProvento) as 'media70Ano', Avg(valorProvento) as 'media100Ano' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and numeroDoEvento=? and numeroDoEvento=? and dataProcessamento between ? and ?")) {
                            pstMHEx.setInt(1, numMatriMHEx);
                            pstMHEx.setInt(2, 98);
                            pstMHEx.setInt(3, 99);
                            pstMHEx.setInt(4, 100);
                            pstMHEx.setDate(5, java.sql.Date.valueOf(prdoAquisiDe));
                            pstMHEx.setDate(6, java.sql.Date.valueOf(prdoDemitido));
                            rtMHEx = pstMHEx.executeQuery();
                            if (rtMHEx.next()) {
                                mediaHorasExtrasFer = rtMHEx.getBigDecimal("media50Ano").add(rtMHEx.getBigDecimal("media70Ano")).add(rtMHEx.getBigDecimal("media100Ano"));
                            }
                        }
                        rtMHEx.close();
                        BigDecimal calc50MedSobFer = mediaHorasExtrasFer.multiply(valorDoSalarioHora);
                        BigDecimal calcMedSobFer = calc50MedSobFer.add(calc50MedSobFer);
                        tabelaEventosRescisao.setValueAt(df.format(calcMedSobFer).replace(".", "").replace(",", "."), e, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Horas Exttras Sobre Férias: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectMHEx.desconecta();
                }

                case 317 -> //Calculando Desonto Adiantamento Décimo Terceiro

                        tabelaEventosRescisao.setValueAt(salarioBaseFunc, e, 4);

                case 318 -> {
                    //Calculando Triênio Férias Proporcionais
                    double valorAnosTrabalhados1 = anosTrabalhados1.doubleValue();

                    if (valorAnosTrabalhados1 >= 3 && valorAnosTrabalhados1 < 6) {
                        BigDecimal resultadoSalarioBaseTrienio = salarioBaseFunc.add((new BigDecimal(5).divide(new BigDecimal(100))).multiply(salarioBaseFunc));
                        tabelaEventosRescisao.setValueAt(df.format(resultadoSalarioBaseTrienio).replace(".", "").replace(",", "."), e, 3);
                    }
                    if (valorAnosTrabalhados1 >= 6 && valorAnosTrabalhados1 < 9) {
                        BigDecimal resultadoSalarioBaseTrienio = salarioBaseFunc.add((new BigDecimal(10).divide(new BigDecimal(100))).multiply(salarioBaseFunc));
                        tabelaEventosRescisao.setValueAt(df.format(resultadoSalarioBaseTrienio).replace(".", "").replace(",", "."), e, 3);
                    }
                    if (valorAnosTrabalhados1 >= 9 && valorAnosTrabalhados1 < 12) {
                        BigDecimal resultadoSalarioBaseTrienio = salarioBaseFunc.add((new BigDecimal(15).divide(new BigDecimal(100))).multiply(salarioBaseFunc));
                        tabelaEventosRescisao.setValueAt(df.format(resultadoSalarioBaseTrienio).replace(".", "").replace(",", "."), e, 3);
                    }
                    if (valorAnosTrabalhados1 >= 12 && valorAnosTrabalhados1 < 15) {
                        BigDecimal resultadoSalarioBaseTrienio = salarioBaseFunc.add((new BigDecimal(25).divide(new BigDecimal(100))).multiply(salarioBaseFunc));
                        tabelaEventosRescisao.setValueAt(df.format(resultadoSalarioBaseTrienio).replace(".", "").replace(",", "."), e, 3);
                    }
                    if (valorAnosTrabalhados1 >= 15 && valorAnosTrabalhados1 < 18) {
                        BigDecimal resultadoSalarioBaseTrienio = salarioBaseFunc.add((new BigDecimal(30).divide(new BigDecimal(100))).multiply(salarioBaseFunc));
                        tabelaEventosRescisao.setValueAt(df.format(resultadoSalarioBaseTrienio).replace(".", "").replace(",", "."), e, 3);
                    }
                    if (valorAnosTrabalhados1 >= 18 && valorAnosTrabalhados1 < 21) {
                        BigDecimal resultadoSalarioBaseTrienio = salarioBaseFunc.add((new BigDecimal(35).divide(new BigDecimal(100))).multiply(salarioBaseFunc));
                        tabelaEventosRescisao.setValueAt(df.format(resultadoSalarioBaseTrienio).replace(".", "").replace(",", "."), e, 3);
                    }
                    if (valorAnosTrabalhados1 >= 21 && valorAnosTrabalhados1 < 24) {
                        BigDecimal resultadoSalarioBaseTrienio = salarioBaseFunc.add((new BigDecimal(40).divide(new BigDecimal(100))).multiply(salarioBaseFunc));
                        tabelaEventosRescisao.setValueAt(df.format(resultadoSalarioBaseTrienio).replace(".", "").replace(",", "."), e, 3);
                    }
                    if (valorAnosTrabalhados1 >= 24 && valorAnosTrabalhados1 < 27) {
                        BigDecimal resultadoSalarioBaseTrienio = salarioBaseFunc.add((new BigDecimal(45).divide(new BigDecimal(100))).multiply(salarioBaseFunc));
                        tabelaEventosRescisao.setValueAt(df.format(resultadoSalarioBaseTrienio).replace(".", "").replace(",", "."), e, 3);
                    }
                    if (valorAnosTrabalhados1 >= 27 && valorAnosTrabalhados1 <= 30) {
                        BigDecimal resultadoSalarioBaseTrienio = salarioBaseFunc.add((new BigDecimal(50).divide(new BigDecimal(100))).multiply(salarioBaseFunc));
                        tabelaEventosRescisao.setValueAt(df.format(resultadoSalarioBaseTrienio).replace(".", "").replace(",", "."), e, 3);
                    }
                }

                case 319 -> {
                    //Calculando Quinquênio Férias Proporcionais

                    BigDecimal resultadoFinal;
                    BigDecimal porcentagemQuinquenio = new BigDecimal(0);
                    BigDecimal cemPorcento = new BigDecimal(100);

                    ConexaoBD conectaQuin = new ConexaoBD();
                    conectaQuin.conecta();

                    try {
                        ResultSet rsQuin;
                        try ( PreparedStatement psQuin = conectaQuin.conexao.prepareStatement("Select Max(valorPercentual) as 'ultimoValor' from rhu_tqnq01")) {
                            rsQuin = psQuin.executeQuery();

                            if (rsQuin.getBigDecimal("ultimoValor") == null) {
                                porcentagemQuinquenio = new BigDecimal("0");
                            } else {
                                porcentagemQuinquenio = rsQuin.getBigDecimal("ultimoValor");
                            }
                        }
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular quinquênio " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    conectaQuin.desconecta();

                    resultadoFinal = salarioBaseFunc.add((porcentagemQuinquenio.divide(cemPorcento)).multiply(salarioBaseFunc));
                    tabelaEventosRescisao.setValueAt(df.format(resultadoFinal).replace(".", "").replace(",", "."), e, 3);
                }

                case 324 -> {
                    //Calculando 1/3 das férias

                    BigDecimal umTercoFerias = salarioBaseFunc.divide(new BigDecimal(3));
                    tabelaEventosRescisao.setValueAt(df.format(umTercoFerias).replace(".", "").replace(",", "."), e, 3);
                }

                case 325 -> {
                    //Calculando 13 Proporcional

                    /*Calculando faltas caso o funcionário tenha*/
                    int valorDasFaltas13 = Integer.parseInt(faltasNoMes.getText());

                    if (valorDasFaltas13 <= 5) {
                        diasCorridos = 30;
                    } else if (valorDasFaltas13 >= 6 && valorDasFaltas13 <= 14) {
                        diasCorridos = 24;
                    } else if (valorDasFaltas13 >= 15 && valorDasFaltas13 <= 23) {
                        diasCorridos = 18;
                    } else if (valorDasFaltas13 >= 24 && valorDasFaltas13 <= 32) {
                        diasCorridos = 12;
                    } else if (valorDasFaltas13 >= 24 && valorDasFaltas13 <= 32) {
                        diasCorridos = 12;
                    } else if (valorDasFaltas13 > 32) {
                        diasCorridos = 0;
                    }

                    /*Valor salário dia*/
                    BigDecimal valorDo13Propor = (salarioBaseFunc.divide(new BigDecimal(30))).multiply(new BigDecimal(diasCorridos));

                    /*Não é para mexer nestas datas, pois se alterado poderá afetar o cálculo das férias, o código desta forma está correto e funcional*/
                    /*Obtendo o valor da data atual*/
                    String valorDataAtual = dataFormato.format(dataAtual);
                    String[] valor_DataAtual = valorDataAtual.split("/");

                    /*Obtendo o valor da data de Admissão*/
                    String datAdmiss = dataFormato.format(dataDeAdmissao.getDate());
                    String[] dat_Admiss = datAdmiss.split("/");

                    /*Obtendo o valor da data de Demissão*/
                    String datDemiss = dataFormato.format(dataDeDemissao.getDate());
                    String[] dat_Demiss = datDemiss.split("/");

                    /*Obtendo o período aquisitivo*/ /*-------Valor do ano atual-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periodoAquisitivoDe = LocalDate.of(Integer.parseInt(valor_DataAtual[2]), Integer.parseInt(dat_Admiss[1]), Integer.parseInt(dat_Admiss[0]));
                    /*-------Valor do ano seguinte-------*/ /*-------Mês e dia oriundos a partir da data de admissão------*/
                    LocalDate periodDemitido = LocalDate.of(Integer.parseInt(dat_Demiss[2]), Integer.parseInt(dat_Demiss[1]), Integer.parseInt(dat_Demiss[0]));

                    /*Obtendo quantos meses a pessoa trabalhou antes de tirar as férias*/
                    Period diferencaEntreMeses = Period.between(periodoAquisitivoDe, periodDemitido);

                    int mesesTrabalhados = diferencaEntreMeses.getMonths(); //Diferença de meses
                    int valorDias = diferencaEntreMeses.getDays(); //Difereçna de dias

                    if (valorDias >= 15) {

                        BigDecimal valor13Proporcional = (valorDo13Propor.divide(new BigDecimal(12))).multiply(new BigDecimal(mesesTrabalhados));
                        /*13º Salário = (salário/12) x meses trabalhados*/
                        tabelaEventosRescisao.setValueAt(mesesTrabalhados, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valor13Proporcional).replace(".", "").replace(",", "."), e, 3);

                    } else {

                        BigDecimal valor13Proporcional = (valorDo13Propor.divide(new BigDecimal(12))).multiply(new BigDecimal(mesesTrabalhados).subtract(new BigDecimal(1)));/*13º Salário = (salário/12) x meses trabalhados*/
                        tabelaEventosRescisao.setValueAt(mesesTrabalhados - 1, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valor13Proporcional).replace(".", "").replace(",", "."), e, 3);
                    }
                }

                case 326 -> {
                    //Calculando Insalubridade sobre 13° Salário
                    dataAtual = new Date();
                    String dataAnoAtualInsSegParc13 = pegaAno.format(dataAtual); //yyyy
                    int numMatriInsalubre13 = Integer.parseInt(numeroDaMatricula.getText());
                    ConexaoBD connectFolhaInsalubre13 = new ConexaoBD();
                    connectFolhaInsalubre13.conecta();
                    try {
                        ResultSet rstInsalubre13;
                        PreparedStatement pstmentInsalubre13 = connectFolhaInsalubre13.conexao.prepareStatement("Select valorProvento from rhu_calf02 where where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=?");
                        pstmentInsalubre13.setInt(1, numMatriInsalubre13);
                        pstmentInsalubre13.setInt(2, 46);
                        pstmentInsalubre13.setDate(3, java.sql.Date.valueOf(dataAnoAtualInsSegParc13));
                        rstInsalubre13 = pstmentInsalubre13.executeQuery();

                        if (rstInsalubre13.last()) {
                            BigDecimal ultValorInsalubre = rstInsalubre13.getBigDecimal("valorProvento");
                            tabelaEventosRescisao.setValueAt(df.format(ultValorInsalubre).replace(".", "").replace(",", "."), e, 3);
                        }
                        rstInsalubre13.close();
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Insalubridade do 13° da Rescisão: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaInsalubre13.desconecta();
                }

                case 327 -> {
                    //Calculando Periculosidade sobre 13° Salário
                    dataAtual = new Date();
                    String dataAnoAtualPer13 = pegaAno.format(dataAtual); //yyyy
                    int numMatriPericulosi13 = Integer.parseInt(numeroDaMatricula.getText());
                    ConexaoBD connectFolhaPericulosi13 = new ConexaoBD();
                    connectFolhaPericulosi13.conecta();
                    try {
                        ResultSet rstPeric13;
                        PreparedStatement pstmentPericulos13 = connectFolhaPericulosi13.conexao.prepareStatement("Select valorProvento from rhu_calf02 where where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=?");
                        pstmentPericulos13.setInt(1, numMatriPericulosi13);
                        pstmentPericulos13.setInt(2, 47);
                        pstmentPericulos13.setDate(3, java.sql.Date.valueOf(dataAnoAtualPer13));
                        rstPeric13 = pstmentPericulos13.executeQuery();

                        if (rstPeric13.last()) {
                            BigDecimal ultValorPericulosi = rstPeric13.getBigDecimal("valorProvento");
                            tabelaEventosRescisao.setValueAt(df.format(ultValorPericulosi).replace(".", "").replace(",", "."), e, 3);
                        }
                        rstPeric13.close();
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular periculosidade do 13° da Rescisão: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaPericulosi13.desconecta();
                }

                case 328 -> {
                    //Calculando Média de Horas Extras Sobre 13° salário
                    int numMatriMHEx50 = Integer.parseInt(numeroDaMatricula.getText());
                    dataAtual = new Date();
                    String dataAnoAtualMHEx = pegaAno.format(dataAtual);
                    ConexaoBD connectFolhaMHEx = new ConexaoBD();
                    connectFolhaMHEx.conecta();
                    try {
                        ResultSet rstMHEx;
                        try ( PreparedStatement pstmentMHEx = connectFolhaMHEx.conexao.prepareStatement("Select Avg(valorProvento) as 'media50Ano', Avg(valorProvento) as 'media70Ano', Avg(valorProvento) as 'media100Ano' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and numeroDoEvento=? and numeroDoEvento=? and YEAR(dataProcessamento)=?")) {
                            pstmentMHEx.setInt(1, numMatriMHEx50);
                            pstmentMHEx.setInt(2, 98);
                            pstmentMHEx.setInt(3, 99);
                            pstmentMHEx.setInt(4, 100);
                            pstmentMHEx.setDate(5, java.sql.Date.valueOf(dataAnoAtualMHEx));
                            rstMHEx = pstmentMHEx.executeQuery();
                            if (rstMHEx.next()) {
                                mediaHorasExtras = rstMHEx.getBigDecimal("media50Ano").add(rstMHEx.getBigDecimal("media70Ano")).add(rstMHEx.getBigDecimal("media100Ano"));
                            }
                        }
                        rstMHEx.close();
                        BigDecimal calcMedSob13 = mediaHorasExtras.multiply(valorDoSalarioHora);
                        calculo50MedSob13 = calcMedSob13.add(calcMedSob13);
                        tabelaEventosRescisao.setValueAt(df.format(calculo50MedSob13).replace(".", "").replace(",", "."), e, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Horas Extras 13°: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMHEx.desconecta();
                }

                case 329 -> {
                    //Calculando Média Adicional Noturno Sobre 13° salário
                    int numMatriMedAdNot13 = Integer.parseInt(numeroDaMatricula.getText());
                    dataAtual = new Date();
                    String dataAnoAtualMedAdNot13 = pegaAno.format(dataAtual);
                    ConexaoBD connectFolhaMedAdNot13 = new ConexaoBD();
                    connectFolhaMedAdNot13.conecta();
                    try {
                        ResultSet rstMedAdNot13;
                        try ( PreparedStatement pstmentMedAdNot13 = connectFolhaMedAdNot13.conexao.prepareStatement("Select Avg(valorProvento) as 'mediaAdicNoturn13' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=?")) {
                            pstmentMedAdNot13.setInt(1, numMatriMedAdNot13);
                            pstmentMedAdNot13.setInt(2, 14);// Mudar aqui quando a lista for atualizada
                            pstmentMedAdNot13.setDate(3, java.sql.Date.valueOf(dataAnoAtualMedAdNot13));
                            rstMedAdNot13 = pstmentMedAdNot13.executeQuery();
                            if (rstMedAdNot13.next()) {
                                mediaAdicNoturn13 = rstMedAdNot13.getBigDecimal("mediaAdicNoturn13");
                            }
                        }
                        rstMedAdNot13.close();
                        BigDecimal calcAdiciNotur = mediaAdicNoturn13.multiply(valorDoSalarioHora).multiply(new BigDecimal(0.2));
                        tabelaEventosRescisao.setValueAt(df.format(calcAdiciNotur).replace(".", "").replace(",", "."), e, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Adicional Noturno Sobre Rescisão: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMedAdNot13.desconecta();
                }

                case 330 -> {
                    //Calculando 13º Salário Final Média Comissões
                    dataAtual = new Date();
                    int numMatriMedComiss = Integer.parseInt(numeroDaMatricula.getText());
                    String pegaValorDoAnoMedComiss = (pegaAno.format(dataAtual));
                    ConexaoBD connectFolhaMedComiss = new ConexaoBD();
                    connectFolhaMedComiss.conecta();
                    try {
                        ResultSet rstMedCom;
                        try ( PreparedStatement psMedCom = connectFolhaMedComiss.conexao.prepareStatement("Select AVG(valorProvento) as 'mediaComissoes' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento=? and YEAR(dataProcessamento)=?")) {
                            psMedCom.setInt(1, numMatriMedComiss);
                            psMedCom.setInt(2, 51);
                            psMedCom.setDate(3, java.sql.Date.valueOf(pegaValorDoAnoMedComiss));
                            rstMedCom = psMedCom.executeQuery();
                            if (rstMedCom.next()) {
                                mediaComissoesAno = rstMedCom.getBigDecimal("mediaComissoes");
                            }
                        }
                        rstMedCom.close();
                        tabelaEventosRescisao.setValueAt(df.format(mediaComissoesAno).replace(".", "").replace(",", "."), e, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Comissões sobre o 13° da Rescisão: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMedComiss.desconecta();
                }

                case 331 -> {
                    //Calculando a Multa Art. 476-A § 5º CLT

                    BigDecimal valorMulta = salarioBaseFunc;
                    tabelaEventosRescisao.setValueAt(valorMulta, e, 3);
                }

                case 332 -> {
                    //Calculando Adicional Noturno

                    int numMatriAdcicNot = Integer.parseInt(numeroDaMatricula.getText());
                    dataAtual = new Date();
                    String dataAnoAtualAdicNot = pegaAno.format(dataAtual);
                    ConexaoBD connectFolhaMedAdNot = new ConexaoBD();
                    connectFolhaMedAdNot.conecta();
                    try {
                        ResultSet rstMedAdNot;
                        try ( PreparedStatement pstmentMedAdNot = connectFolhaMedAdNot.conexao.prepareStatement("Select Avg(valorProvento) as 'mediaAdicNoturn' from rhu_calf02 where numeroDaMatricula=? and numeroDoEvento =? and YEAR(dataProcessamento)=?")) {
                            pstmentMedAdNot.setInt(1, numMatriAdcicNot);
                            pstmentMedAdNot.setInt(2, 14);// Mudar aqui quando a lista for atualizada
                            pstmentMedAdNot.setDate(3, java.sql.Date.valueOf(dataAnoAtualAdicNot));
                            rstMedAdNot = pstmentMedAdNot.executeQuery();
                            if (rstMedAdNot.next()) {
                                mediaAdicNoturn13 = rstMedAdNot.getBigDecimal("mediaAdicNoturn");
                            }
                        }
                        rstMedAdNot.close();
                        BigDecimal calcAdiciNotur = mediaAdicNoturn13.multiply(valorDoSalarioHora).multiply(new BigDecimal(0.2));
                        tabelaEventosRescisao.setValueAt(df.format(calcAdiciNotur).replace(".", "").replace(",", "."), e, 3);
                    } catch (SQLException erro) {
                        JOptionPane.showMessageDialog(null, "Erro ao calcular Média de Adicional Noturno Sobre Rescisão: " + erro, "", JOptionPane.ERROR_MESSAGE);
                    }
                    connectFolhaMedAdNot.desconecta();
                }

                case 333 -> {
                    //Calculando a Insalubridade no mês da Demissão FORMULA = ((salario min * % Insalubre)÷ 30) * número de dias trabalhados no mes da rescisão
                    obtemSalarioMin();
                    BigDecimal diasTrabaInsalubre = new BigDecimal(diasTrabalhadosNoMes.getText());
                    BigDecimal porcentagemInsalubre = new BigDecimal(valorInsalubridade.getText());
                    BigDecimal adicionalInsalubre = ((salarioMinimo.multiply(porcentagemInsalubre.divide(new BigDecimal(100)))).divide(new BigDecimal(30))).multiply(diasTrabaInsalubre);
                    tabelaEventosRescisao.setValueAt(diasTrabaInsalubre, e, 2);
                    tabelaEventosRescisao.setValueAt(df.format(adicionalInsalubre).replace(".", "").replace(",", "."), e, 3);
                }

                case 335 -> {
                    //Calculando Insalubridade Sobre o 13° FORMULA = ((Salario Minimo * % insalubre ) ÷ 12) * número de meses trabalhado no período aquisitivo
                    obtemSalarioMin();
                    contaPeriodoAquisitivo();
                    BigDecimal porcentInsalubre13 = new BigDecimal(valorInsalubridade.getText());
                    BigDecimal diasTrabInsalu13 = (salarioMinimo.multiply((porcentInsalubre13.divide(new BigDecimal(100))).divide(new BigDecimal(12)))).multiply(mesesTrabalhados1);
                    tabelaEventosRescisao.setValueAt(df.format(diasTrabInsalu13).replace(".", "").replace(",", "."), e, 3);
                }

                case 336 -> {
                    //Caluclando a Periculosidade no mês da Demissão
                    //Calculando o saldo de Salário
                    BigDecimal valorSaldoHoras;
                    BigDecimal periculosidade = new BigDecimal(valorPericulosidade.getText());
                    BigDecimal valorSalaHoraperic = new BigDecimal(salarioHora.getText());
                    BigDecimal HoraTrabalhNoMesPeric = new BigDecimal(valorValeRefeicao.getText());
                    BigDecimal quantDiasTrabMesPeric = new BigDecimal(diasTrabalhadosNoMes.getText());

                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {

                        valorSaldoHoras = (salarioBaseFunc.divide(new BigDecimal(30))).multiply(quantDiasTrabMesPeric);
                        tabelaEventosRescisao.setValueAt(quantDiasTrabMesPeric, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras).replace(".", "").replace(",", "."), e, 3);

                    } else {
                        valorSaldoHoras = valorSalaHoraperic.multiply(HoraTrabalhNoMesPeric);
                        tabelaEventosRescisao.setValueAt(HoraTrabalhNoMesPeric, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras).replace(".", "").replace(",", "."), e, 3);
                    }

                    //Calculando a periculosidade
                    BigDecimal valorPericulo = valorSaldoHoras.multiply(periculosidade.divide(new BigDecimal(100)));
                    tabelaEventosRescisao.setValueAt(df.format(valorPericulo).replace(".", "").replace(",", "."), e, 3);
                }

                case 338 -> {
                    BigDecimal valorSaldoHoras50, valorHoraExt50;
                    BigDecimal quntHExt50 = new BigDecimal(valorHoraExtra50.getText());
                    BigDecimal salaHora50 = new BigDecimal(salarioHora.getText());
                    BigDecimal HoraTrabalhNoMes50 = new BigDecimal(valorValeRefeicao.getText());
                    BigDecimal quantDiasTrabMes50 = new BigDecimal(diasTrabalhadosNoMes.getText());
                    BigDecimal insalubre50Porc = new BigDecimal(valorInsalubridade.getText());

                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {

                        valorSaldoHoras50 = (salarioBaseFunc.divide(new BigDecimal(30))).multiply(quantDiasTrabMes50);
                        tabelaEventosRescisao.setValueAt(quantDiasTrabMes50, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras50).replace(".", "").replace(",", "."), e, 3);

                    } else {
                        valorSaldoHoras50 = salaHora50.multiply(HoraTrabalhNoMes50);
                        tabelaEventosRescisao.setValueAt(HoraTrabalhNoMes50, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras50).replace(".", "").replace(",", "."), e, 3);
                    }
                    //Aqui valida se é para calcular com periculosidade ou insalubridade
                    if (!"0".equals(valorPericulosidade.getText())) {

                        obtemSalarioMin();
                        BigDecimal horaExtra50 = (salarioBaseFunc.add((new BigDecimal(0.3).multiply(salarioBaseFunc)).divide(new BigDecimal(220)))).multiply(new BigDecimal(1.5)); //aqui é calculado o valor da hora extra
                        valorHoraExt50 = horaExtra50.multiply(quntHExt50); // aqui é encontrado o total de horas extras
                        tabelaEventosRescisao.setValueAt(df.format(valorHoraExt50).replace(".", "").replace(",", "."), e, 3);

                    } else {
                    }
                    if (!"0".equals(valorInsalubridade.getText())) {
                        BigDecimal horaExtra50 = ((salarioMinimo.multiply(insalubre50Porc.divide(new BigDecimal(100)))).add(salarioBaseFunc.divide(new BigDecimal(220)))).multiply(new BigDecimal(1.5));
                        valorHoraExt50 = horaExtra50.multiply(quntHExt50);
                        tabelaEventosRescisao.setValueAt(df.format(valorHoraExt50).replace(".", "").replace(",", "."), e, 3);
                    } else {
                    }
                }

                case 339 -> {

                    BigDecimal valorSaldoHoras70, valorHoraExt70;
                    BigDecimal quntHExt70 = new BigDecimal(valorHoraExtra50.getText());
                    BigDecimal salaHora70 = new BigDecimal(salarioHora.getText());
                    BigDecimal HoraTrabalhNoMes70 = new BigDecimal(valorValeRefeicao.getText());
                    BigDecimal quantDiasTrabMes70 = new BigDecimal(diasTrabalhadosNoMes.getText());
                    BigDecimal insalubre70Porc = new BigDecimal(valorInsalubridade.getText());

                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {

                        valorSaldoHoras70 = (salarioBaseFunc.divide(new BigDecimal(30))).multiply(quantDiasTrabMes70);
                        tabelaEventosRescisao.setValueAt(quantDiasTrabMes70, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras70).replace(".", "").replace(",", "."), e, 3);

                    } else {
                        valorSaldoHoras70 = salaHora70.multiply(HoraTrabalhNoMes70);
                        tabelaEventosRescisao.setValueAt(HoraTrabalhNoMes70, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras70).replace(".", "").replace(",", "."), e, 3);
                    }
                    //Aqui valida se é para calcular com periculosidade ou insalubridade
                    if (!"0".equals(valorPericulosidade.getText())) {

                        obtemSalarioMin();
                        BigDecimal horaExtra70 = ((salarioBaseFunc.add(new BigDecimal(0.3).multiply(salarioBaseFunc))).divide(new BigDecimal(220))).multiply(new BigDecimal(1.7)); //aqui é calculado o valor da hora extra
                        valorHoraExt70 = horaExtra70.multiply(quntHExt70); // aqui é encontrado o total de horas extras
                        tabelaEventosRescisao.setValueAt(df.format(valorHoraExt70).replace(".", "").replace(",", "."), e, 3);
                    } else {
                    }
                    if (!"0".equals(valorInsalubridade.getText())) {
                        BigDecimal horaExtra70 = (((salarioMinimo.multiply(insalubre70Porc.divide(new BigDecimal(100)))).add(salarioBaseFunc)).divide(new BigDecimal(220))).multiply(new BigDecimal(1.7));
                        valorHoraExt70 = horaExtra70.multiply(quntHExt70);
                        tabelaEventosRescisao.setValueAt(df.format(valorHoraExt70).replace(".", "").replace(",", "."), e, 3);
                    } else {
                    }
                }

                case 340 -> {
                    BigDecimal valorSaldoHoras100, valorHoraExt100;
                    BigDecimal quntHExt100 = new BigDecimal(valorHoraExtra50.getText());
                    BigDecimal salaHora100 = new BigDecimal(salarioHora.getText());
                    BigDecimal HoraTrabalhNoMes100 = new BigDecimal(valorValeRefeicao.getText());
                    BigDecimal quantDiasTrabMes100 = new BigDecimal(diasTrabalhadosNoMes.getText());
                    BigDecimal insalubre100Porc = new BigDecimal(valorInsalubridade.getText());
                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {

                        valorSaldoHoras100 = (salarioBaseFunc.divide(new BigDecimal(30))).multiply(quantDiasTrabMes100);
                        tabelaEventosRescisao.setValueAt(quantDiasTrabMes100, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras100).replace(".", "").replace(",", "."), e, 3);

                    } else {
                        valorSaldoHoras100 = salaHora100.multiply(HoraTrabalhNoMes100);
                        tabelaEventosRescisao.setValueAt(HoraTrabalhNoMes100, e, 2);
                        tabelaEventosRescisao.setValueAt(df.format(valorSaldoHoras100).replace(".", "").replace(",", "."), e, 3);
                    }
                    //Aqui valida se é para calcular com periculosidade ou insalubridade
                    if (!"0".equals(valorPericulosidade.getText())) {

                        obtemSalarioMin();
                        BigDecimal horaExtra100 = ((salarioBaseFunc.add(new BigDecimal(0.3).multiply(salarioBaseFunc))).divide(new BigDecimal(220))).multiply(new BigDecimal(2)); //aqui é calculado o valor da hora extra
                        valorHoraExt100 = horaExtra100.multiply(quntHExt100); // aqui é encontrado o total de horas extras
                        tabelaEventosRescisao.setValueAt(df.format(valorHoraExt100).replace(".", "").replace(",", "."), e, 3);
                    } else {
                    }
                    if (!"0".equals(valorInsalubridade.getText())) {

                        BigDecimal horaExtra100 = (((salarioMinimo.multiply(insalubre100Porc.divide(new BigDecimal(100)))).add(salarioBaseFunc)).divide(new BigDecimal(220))).multiply(new BigDecimal(2));
                        valorHoraExt100 = horaExtra100.multiply(quntHExt100);
                        tabelaEventosRescisao.setValueAt(df.format(valorHoraExt100).replace(".", "").replace(",", "."), e, 3);
                    } else {
                    }
                }

                case 342 -> {
                    //Desconto vale transporte

                    BigDecimal vlrSaldoHoras;

                    //Calculando saldo de Salário
                    BigDecimal vlrSalarioHora = new BigDecimal(salarioHora.getText());
                    BigDecimal vlrDaHorSemana = (new BigDecimal(horasSemanais.getText())).divide(new BigDecimal(7));
                    BigDecimal qntDiasTrabMes = new BigDecimal(diasTrabalhadosNoMes.getText());

                    if (tipoDeSalario.getSelectedItem().equals("Mensal")) {

                        vlrSaldoHoras = (salarioBaseFunc.divide(new BigDecimal(30))).multiply(qntDiasTrabMes);

                    } else {
                        vlrSaldoHoras = vlrSalarioHora.multiply(vlrDaHorSemana).multiply(qntDiasTrabMes);
                    }
                    BigDecimal descontaValeTrans = (vlrSaldoHoras.multiply(new BigDecimal(6))).divide(new BigDecimal(100));
                    tabelaEventosRescisao.setValueAt(df.format(descontaValeTrans).replace(".", "").replace(",", "."), e, 4);
                }

                case 343 -> {
                    //Calculando Desconto de Vale Refeição
                    BigDecimal vRefeicao = new BigDecimal(valorValeRefeicao.getText());
                    tabelaEventosRescisao.setValueAt(df.format(vRefeicao).replace(".", "").replace(",", "."), e, 4);
                }

                case 347 -> {
                    //Calculando falta nas férias

                    /*Calculando faltas caso o funcionário tenha*/
                    int valorDasFaltasFerias = Integer.parseInt(faltasNoMes.getText());

                    if (valorDasFaltasFerias <= 5) {
                        diasCorridos = 30;
                    } else if (valorDasFaltasFerias >= 6 && valorDasFaltasFerias <= 14) {
                        diasCorridos = 24;
                    } else if (valorDasFaltasFerias >= 15 && valorDasFaltasFerias <= 23) {
                        diasCorridos = 18;
                    } else if (valorDasFaltasFerias >= 24 && valorDasFaltasFerias <= 32) {
                        diasCorridos = 12;
                    } else if (valorDasFaltasFerias >= 24 && valorDasFaltasFerias <= 32) {
                        diasCorridos = 12;
                    } else if (valorDasFaltasFerias > 32) {
                        diasCorridos = 0;
                    }
                    tabelaEventosRescisao.setValueAt(valorDasFaltasFerias, e, 2);
                }

                case 349 -> {
                    //Calculando Faltas na rescisão
                    /*Calculando faltas caso o funcionário tenha*/
                    int valorDasFaltasGreal = Integer.parseInt(faltasNoMes.getText());

                    if (valorDasFaltasGreal <= 5) {
                        diasCorridos = 30;
                    } else if (valorDasFaltasGreal >= 6 && valorDasFaltasGreal <= 14) {
                        diasCorridos = 24;
                    } else if (valorDasFaltasGreal >= 15 && valorDasFaltasGreal <= 23) {
                        diasCorridos = 18;
                    } else if (valorDasFaltasGreal >= 24 && valorDasFaltasGreal <= 32) {
                        diasCorridos = 12;
                    } else if (valorDasFaltasGreal >= 24 && valorDasFaltasGreal <= 32) {
                        diasCorridos = 12;
                    } else if (valorDasFaltasGreal > 32) {
                        diasCorridos = 0;
                    }
                    tabelaEventosRescisao.setValueAt(valorDasFaltasGreal, e, 2);
                }

                case 350 -> {
                    //Calculando 1/12 avos 13° Salário S/ aviso prévio

                    BigDecimal umDozeAvos = salarioBaseFunc.divide(new BigDecimal(12));
                    tabelaEventosRescisao.setValueAt(df.format(umDozeAvos).replace(".", "").replace(",", "."), e, 3);
                }

                case 351 -> {
                    //Calculando 1/12 Anos Férias S/ Aviso Indenizado
                    BigDecimal umDozeAvosFerias = salarioBaseFunc.divide(new BigDecimal(12));
                    tabelaEventosRescisao.setValueAt(df.format(umDozeAvosFerias).replace(".", "").replace(",", "."), e, 3);
                }

                case 352 -> {
                    //Calculando 1/12 avos 13° Salário S/ aviso prévio

                    BigDecimal umDozeAvosComAv = salarioBaseFunc.divide(new BigDecimal(12));
                    tabelaEventosRescisao.setValueAt(df.format(umDozeAvosComAv).replace(".", "").replace(",", "."), e, 3);
                }

                case 353 -> {
                    //Calculando 1/12 Anos Férias S/ Aviso Indenizado
                    BigDecimal umDozeAvosFeriasComAv = salarioBaseFunc.divide(new BigDecimal(12));
                    tabelaEventosRescisao.setValueAt(df.format(umDozeAvosFeriasComAv).replace(".", "").replace(",", "."), e, 3);
                }

                case 230 -> {
                    //Calculando Empréstimo que foi pago caso o funcionário tenha feito empréstimo

                    BigDecimal emprestimo = new BigDecimal(valorEmprestimo.getText().replace(".", "").replace(",", "."));
                    tabelaEventosRescisao.setValueAt(emprestimo, e, 4);
                }
            }
        }
    }
}
