package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;

import ConexaoBDCodex.ConexaoBD;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.dataAte;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.dataDe;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.naoEmiteBlocoB;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.tarefaGeraSped;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB001;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB020;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB025;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB030;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB035;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB350;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB420;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB440;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB460;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB470;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB500;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB510;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB990;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 *
 * @author alyes
 */
public class BlocoB {

    static LocalDate dataPeriodoDe = Instant.ofEpochMilli(dataDe.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    static LocalDate dataPeriodoAte = Instant.ofEpochMilli(dataAte.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

    static SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");

    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheBlocoB() {

        br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB = new br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB();

        if (naoEmiteBlocoB.isSelected()) {
            blocoB = preencheRegistroB001(blocoB);
            blocoB = preencheRegistroB990(blocoB);
        } else {

            blocoB = preencheRegistroB001(blocoB);
            blocoB = preencheRegistroB020(blocoB);
            blocoB = preencheRegistroB030(blocoB);
            blocoB = preencheRegistroB350(blocoB);
            blocoB = preencheRegistroB420(blocoB);
            blocoB = preencheRegistroB440(blocoB);
            blocoB = preencheRegistroB460(blocoB);
            blocoB = preencheRegistroB470(blocoB);
            blocoB = preencheRegistroB500(blocoB);
            blocoB = preencheRegistroB990(blocoB);
        }

        return blocoB;
    }

    //Abertura do Bloco B
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB001(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        if (naoEmiteBlocoB.isSelected()) {
            RegistroB001 registroB001 = new RegistroB001();
            registroB001.setInd_dad("1");

        } else {
            RegistroB001 registroB001 = new RegistroB001();
            registroB001.setInd_dad("0");
        }

        return blocoB;
    }

    /*Nota Fiscal (código 01), Nota Fiscal de Serviços (código 03), Nota Fiscal de Serviços Avulsa (código 3B),
    Nota Fiscal de Produtor (código 04), Conhecimento de Transporte Rodoviário de Cargas (código 08), NF-e (código 55) e NFC-e (código 65)*/
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB020(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        //Obtendo informações da notas de entrada (aquisição)
        ConexaoBD conectaB020 = new ConexaoBD();
        conectaB020.conecta();

        String modeloDaNota;

        try {
            ResultSet rsB20;
            try ( PreparedStatement psB020 = conectaB020.conexao.prepareStatement("Select codigoEmpresa,modelo,codigoCidade,situacao,aliquota,serie,numero,codigoVerificacao,dataEmissao,valorNota,valorIss,baseIss,issRetido from con_enfs01 where dataEmissao between ? and ?")) {
                psB020.setDate(1, java.sql.Date.valueOf(dataPeriodoDe));
                psB020.setDate(2, java.sql.Date.valueOf(dataPeriodoAte));
                rsB20 = psB020.executeQuery();

                while (rsB20.next()) {

                    modeloDaNota = rsB20.getString("modelo");

                    if (!modeloDaNota.equals("3A - Nota Fiscal Simplificada")) {

                        RegistroB020 registroB20 = new RegistroB020();
                        registroB20.setInd_oper("0");
                        registroB20.setInd_emit("1");
                        registroB20.setCod_part(rsB20.getString("codigoEmpresa"));
                        registroB20.setCod_mod(rsB20.getString("modelo"));
                        registroB20.setCod_sit(rsB20.getString("situacao"));
                        registroB20.setSer(rsB20.getString("serie"));
                        registroB20.setNum_doc(rsB20.getString("numero"));
                        registroB20.setChv_nfe(rsB20.getString("codigoVerificacao"));
                        registroB20.setDt_doc(df.format(rsB20.getDate("dataEmissao")));
                        registroB20.setCod_mun_serv(rsB20.getString("codigoCidade"));
                        registroB20.setVl_cont(rsB20.getString("valorNota"));
                        registroB20.setVl_mat_terc(rsB20.getString("valorNota"));
                        registroB20.setVl_sub("0,00");
                        registroB20.setVl_isnt_iss("0,00");
                        registroB20.setVl_ded_bc(rsB20.getString("valorIss"));
                        registroB20.setVl_bc_iss(rsB20.getString("baseIss"));
                        registroB20.setVl_bc_iss_rt(rsB20.getString("issRetido"));
                        registroB20.setVl_iss_rt("0,00");
                        registroB20.setVl_iss(rsB20.getString("valorIss"));
                        registroB20.setCod_inf_obs("");

                        BigDecimal vlbcissp = new BigDecimal(rsB20.getString("baseIss"));
                        BigDecimal aliqiss = new BigDecimal(rsB20.getString("aliquota"));
                        BigDecimal vlissp = vlbcissp.multiply(aliqiss);

                        RegistroB025 registroB025 = new RegistroB025();
                        registroB025.setVl_cont_p(rsB20.getString("valorNota"));
                        registroB025.setVl_bc_iss_p(rsB20.getString("baseIss"));
                        registroB025.setAliq_iss(rsB20.getString("aliquota"));
                        registroB025.setVl_iss_p(vlissp.toString());
                        registroB025.setVl_isnt_iss_p(rsB20.getString("issRetido"));
                        registroB025.setCod_serv(rsB20.getString("discriminacaoServicos"));
                        registroB20.getRegistroB025().add(registroB025);

                        blocoB.getRegistroB001().getRegistroB020().add(registroB20);
                    }
                }
                rsB20.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco B020 ou bloco B025 - Bloco B: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conectaB020.desconecta();

        return blocoB;
    }

    //Nota Fiscal de Serviços Simplificada (código 3A)
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB030(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        String modeloDaNota;
        
        ConexaoBD conectaB030 = new ConexaoBD();
        conectaB030.conecta();

        try {
            ResultSet rsB30;
            try ( PreparedStatement psB030 = conectaB030.conexao.prepareStatement("Select modelo,count(situacao) as 'cancelados',aliquota,serie,numero,baseTributacao,dataEmissao,count(valorNota) as 'somaVlrNota',valorIss,baseIss,issretido,natureza from con_snfs01 where dataEmissao between ? and ?")) {
                psB030.setDate(1, java.sql.Date.valueOf(dataPeriodoDe));
                psB030.setDate(2, java.sql.Date.valueOf(dataPeriodoAte));
                rsB30 = psB030.executeQuery();

                while (rsB30.next()) {

                    modeloDaNota = rsB30.getString("modelo");

                    if (modeloDaNota.equals("3A - Nota Fiscal Simplificada")) {

                        RegistroB030 registroB030 = new RegistroB030();
                        registroB030.setCod_mod("3A");
                        registroB030.setSer(rsB30.getString("serie"));
                        registroB030.setNum_doc_ini(rsB30.getString("numero"));
                        registroB030.setNum_doc_fin(rsB30.getString("Last(numero)"));
                        registroB030.setDt_doc(rsB30.getString("dataEmissao"));
                        registroB030.setQtde_canc(rsB30.getString("cancelados"));
                        registroB030.setVl_cont(rsB30.getString("somaVlrNota"));
                        registroB030.setVl_isnt_iss(rsB30.getString("issretido"));
                        registroB030.setVl_bc_iss(rsB30.getString("baseIss"));
                        registroB030.setVl_iss(rsB30.getString("valorIss"));
                        registroB030.setCod_inf_obs(rsB30.getString("complementoLivro57"));
                        
                        // Detalhamento por combinação de alíquota e item da lista de serviços da Lei Complementar nº 116/2003
                        RegistroB035 registroB035 = new RegistroB035();
                        registroB035.setVl_cont_p(rsB30.getString("valorNota"));
                        registroB035.setVl_bc_iss_p(rsB30.getString("baseTributacao"));
                        registroB035.setAliq_iss(rsB30.getString("aliquota"));
                        registroB035.setVl_iss_p(rsB30.getString("valorIss"));
                        registroB035.setVl_isnt_iss_p(rsB30.getString("issretido"));
                        registroB035.setCod_serv(rsB30.getString("natureza").substring(0, 2));
                        registroB030.getRegistroB035().add(registroB035);
                        
                        blocoB.getRegistroB001().getRegistroB030().add(registroB030);
                    }
                }
                rsB30.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco B030 ou bloco B035 - Bloco B: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conectaB030.desconecta();

        return blocoB;
    }

    //Serviços prestados por instituições financeiras
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB350(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        RegistroB350 resgistroB350 = new RegistroB350();
        resgistroB350.setCod_ctd("");
        resgistroB350.setCta_iss("");
        resgistroB350.setCta_cosif("");
        resgistroB350.setQtde_ocor("");
        resgistroB350.setCod_serv("");
        resgistroB350.setVl_cont("");
        resgistroB350.setVl_bc_iss("");
        resgistroB350.setAliq_iss("");
        resgistroB350.setVl_iss("");
        resgistroB350.setCod_inf_obs("");
        
        blocoB.getRegistroB001().getRegistroB350().add(resgistroB350);
        
        return blocoB;
    }

    //Totalização dos valores de serviços prestados por combinação de alíquota e item da lista de serviços da Lei Complementar nº 116/2003
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB420(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        ConexaoBD conectaB420 = new ConexaoBD();
        conectaB420.conecta();

        try {
            ResultSet rsB420;
            try ( PreparedStatement psB420 = conectaB420.conexao.prepareStatement("Select codigoEmpresa,modelo,codigoCidade,situacao,aliquota,serie,numero,codigoVerificacao,dataEmissao,valorNota,valorIss,baseIss,issRetido from con_enfs01 where dataEmissao between ? and ?")) {
                psB420.setDate(1, java.sql.Date.valueOf(dataPeriodoDe));
                psB420.setDate(2, java.sql.Date.valueOf(dataPeriodoAte));
                rsB420 = psB420.executeQuery();

                while (rsB420.next()) {

                    String modeloDaNota = rsB420.getString("modelo");
                    
                    if (!modeloDaNota.equals("3A - Nota Fiscal Simplificada")) {

                        BigDecimal vlbcissp = new BigDecimal(rsB420.getString("baseIss"));
                        BigDecimal aliqiss = new BigDecimal(rsB420.getString("aliquota"));
                        BigDecimal vlissp = vlbcissp.multiply(aliqiss);

                        RegistroB420 registroB420 = new RegistroB420();
                        registroB420.setVl_cont(rsB420.getString("valorNota"));
                        registroB420.setVl_bc_iss(rsB420.getString("baseIss"));
                        registroB420.setAliq_iss(rsB420.getString("aliquota"));
                        registroB420.setVl_iss(vlissp.toString());
                        registroB420.setVl_isnt_iss(rsB420.getString("issRetido"));
                        registroB420.setCod_serv(rsB420.getString("discriminacaoServicos"));
  
                        blocoB.getRegistroB001().getRegistroB420().add(registroB420);
                    }
                }
                rsB420.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco B020 ou bloco B025 - Bloco B: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conectaB420.desconecta();
        
        return blocoB;
    }

    //Totalização dos valores retidos
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB440(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        ConexaoBD conectaB440 = new ConexaoBD();
        conectaB440.conecta();

        String modeloDaNota;

        try {
            ResultSet rsB440;
            try ( PreparedStatement psB440 = conectaB440.conexao.prepareStatement("Select modelo,aliquota,dataEmissao,sum(valorNota) as 'soma',valorIss,sum(baseIss) as 'somaiss',sum(issRetido) as 'somaretido' from con_enfs01 where dataEmissao between ? and ?")) {
                psB440.setDate(1, java.sql.Date.valueOf(dataPeriodoDe));
                psB440.setDate(2, java.sql.Date.valueOf(dataPeriodoAte));
                rsB440 = psB440.executeQuery();

                while (rsB440.next()) {

                    modeloDaNota = rsB440.getString("modelo");

                    if (!modeloDaNota.equals("3A - Nota Fiscal Simplificada")) {

                        RegistroB440 registroB440 = new RegistroB440();
                        registroB440.setInd_oper("0");
                        registroB440.setCod_part("1");
                        registroB440.setVl_cont_rt(rsB440.getString("soma"));
                        registroB440.setVl_bc_iss_rt(rsB440.getString("somaiss"));
                        registroB440.setVl_iss_rt("somaretido");

                        blocoB.getRegistroB001().getRegistroB440().add(registroB440);
                    }
                }
                rsB440.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco B020 ou bloco B025 - Bloco B: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        
        return blocoB;
    }

    //Deduções do ISS
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB460(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        RegistroB460 registroB460 = new RegistroB460();
        registroB460.setInd_ded("9");
        registroB460.setVl_ded("0,00");
        registroB460.setNum_proc("");
        registroB460.setInd_proc("9");
        registroB460.setProc("");
        registroB460.setCod_inf_obs("");
        registroB460.setInd_obr("0");
        
        blocoB.getRegistroB001().getRegistroB460().add(registroB460);
        
        return blocoB;
    }

    //Apuração do ISS
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB470(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        RegistroB470 registroB470 = new RegistroB470();
        registroB470.setVl_cont("");
        registroB470.setVl_mat_terc("");
        registroB470.setVl_mat_prop("");
        registroB470.setVl_sub("");
        registroB470.setVl_isnt("");
        registroB470.setVl_ded_bc("");
        registroB470.setVl_bc_iss("");
        registroB470.setVl_bc_iss_rt("");
        registroB470.setVl_iss("");
        registroB470.setVl_iss_rt("");
        registroB470.setVl_ded("");
        registroB470.setVl_iss_rec("");
        registroB470.setVl_iss_st("");
        registroB470.setVl_iss_rec_uni("");
        
        blocoB.getRegistroB001().getRegistroB470();
        
        return blocoB;
    }

    //Apuração do ISS sociedade uniprofissional
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB500(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        RegistroB500 registroB500 = new RegistroB500();
        registroB500.setVl_rec("");
        registroB500.setQtde_prof("");
        registroB500.setVl_or("");
        
        RegistroB510 registroB510 = new RegistroB510();
        registroB510.setInd_prof("0");
        registroB510.setInd_esc("3");
        registroB510.setInd_soc("1");
        registroB510.setCpf("");
        registroB510.setNome("");
        registroB500.getRegistroB510().add(registroB510);
        
        blocoB.getRegistroB001().getRegistroB500();
        
        return blocoB;
    }

    //Encerramento do Bloco B 
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB preencheRegistroB990(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.BlocoB blocoB) {

        RegistroB990 registroB990 = new RegistroB990();
        registroB990.setQtd_lin_b("");
        
        blocoB.setRegistroB990(registroB990);
        
        return blocoB;
    }
}
