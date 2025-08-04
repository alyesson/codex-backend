package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;

import ConexaoBDCodex.ConexaoBD;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.dataAte;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.dataDe;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoD300;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoD350;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoD400;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoD500;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoD600;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoD695;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.indicadorMovimento;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.tarefaGeraSped;
import static br.com.codex.v1.domain.fiscal.spedefd.geraBlocos.BlocoC.numCpfCnpj;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD001;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD100;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD101;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD110;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD120;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD130;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD140;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD150;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD160;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD161;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD162;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD170;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD180;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD190;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD195;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD197;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD300;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD301;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD310;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD350;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD355;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD360;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD365;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD370;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD390;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD400;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD410;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD411;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD420;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD500;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD510;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD530;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD590;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD600;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD610;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD690;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD695;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD696;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD697;
import static Logon.Principal.numCnpjDaEmpresa;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Alyesson Sousa
 *
 */
public class BlocoD {

    static LocalDate dataPeriodoDe = Instant.ofEpochMilli(dataDe.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    static LocalDate dataPeriodoAte = Instant.ofEpochMilli(dataAte.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

    SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");

    static String indiOper, modeloCte, entraSai, resultCodPart, codigoPart, numeroDoCte, codCfop;

    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD preencheBlocoD() {
        br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD blocoD = new br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD();
        blocoD = preencheRegistroD001(blocoD);
        blocoD = preencheRegistroD100(blocoD);
        blocoD = preencheRegistroD300(blocoD);
        blocoD = preencheRegistroD350(blocoD);
        blocoD = preencheRegistroD400(blocoD);
        blocoD = preencheRegistroD500(blocoD);
        blocoD = preencheRegistroD600(blocoD);
        blocoD = preencheRegistroD695(blocoD);

        return blocoD;
    }

    //Abertura do Bloco D 
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD preencheRegistroD001(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD blocoD) {
        RegistroD001 registroD001 = new RegistroD001();
        registroD001.setInd_mov(indicadorMovimento.getSelectedItem().toString());

        blocoD.setRegistroD001(registroD001);

        return blocoD;
    }

    //Nota Fiscal de Serviço de Transporte (código 07), Conhecimentos de Transporte
    //Rodoviário De Cargas (código 08), Conhecimentos de Transporte de Cargas
    //Avulso (código 8b), Aquaviário de Cargas (código 09), Aéreo (código 10),
    //Ferroviário de Cargas (código 11), Multimodal de Cargas (código 26), Nota
    //Fiscal de Transporte Ferroviário de Carga (código 27), Conhecimento de
    //Transporte Eletrônico – CT-e (código 57), Conhecimento de Transporte
    //Eletrônico para Outros Serviços - CT-e OS (código 67) e Bilhete de Passagem
    //Eletrônico (código 63);
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD preencheRegistroD100(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD blocoD) {

        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");

        numCpfCnpj = numCnpjDaEmpresa.getSelectedItem().toString();

        ConexaoBD conectaD100 = new ConexaoBD();
        conectaD100.conecta();
        try {
            ResultSet rsD100;
            try ( PreparedStatement psD100 = conectaD100.conexao.prepareStatement("Select * from fsc_imct01 where emissao between ? and ?")) {
                psD100.setDate(1, java.sql.Date.valueOf(dataPeriodoDe));
                psD100.setDate(1, java.sql.Date.valueOf(dataPeriodoAte));
                rsD100 = psD100.executeQuery();

                while (rsD100.next()) {

                    indiOper = rsD100.getString("tipo");
                    modeloCte = rsD100.getString("modelo");
                    numeroDoCte = rsD100.getString("numero");
                    codCfop = rsD100.getString("cfop");

                    if (indiOper.equals("0")) {
                        codigoPart = rsD100.getString("documentoEmitente");
                        verificaEmitente(codigoPart);
                    }

                    if (indiOper.equals("1")) {
                        codigoPart = rsD100.getString("documentoTomador");
                        verificaEmitente(codigoPart);
                    }

                    RegistroD100 registroD100 = new RegistroD100();
                    registroD100.setInd_oper(rsD100.getString("tipo"));
                    registroD100.setInd_emit(rsD100.getString("indEmit"));
                    registroD100.setCod_part(resultCodPart);
                    registroD100.setCod_mod(rsD100.getString("modelo"));
                    registroD100.setCod_sit("");
                    registroD100.setSer(rsD100.getString("serie"));
                    registroD100.setSub("");
                    registroD100.setNum_doc(rsD100.getString("codigoCte"));
                    registroD100.setChv_cte(rsD100.getString("chave"));
                    registroD100.setDt_doc(df.format(rsD100.getString("emissao")));
                    registroD100.setDt_a_p(df.format(rsD100.getString("emissao")));
                    registroD100.setTp_cte(rsD100.getString("tipo"));
                    registroD100.setChv_cte_ref("");
                    registroD100.setVl_doc(rsD100.getString("valorTotalPrestacao"));
                    registroD100.setVl_desc(rsD100.getString("0,00"));
                    registroD100.setInd_frt(rsD100.getString(""));
                    registroD100.setVl_serv(rsD100.getString("valorTotalPrestacao"));
                    registroD100.setVl_bc_icms(rsD100.getString("bcIcms").replace(".", ","));
                    registroD100.setVl_icms(rsD100.getString("valorIcms").replace(".", ","));
                    registroD100.setVl_nt("");
                    registroD100.setCod_inf("");
                    registroD100.setCod_cta("");
                    registroD100.setCod_mun_orig(rsD100.getString("codigoMunicipioEmitente"));
                    registroD100.setCod_mun_dest(rsD100.getString("codigoMunicipioDestinatario"));

                    //Informação complementar dos documentos fiscais quando das prestações interestaduais destinadas a consumidor final não contribuinte EC 87/15 (código 57, 63 e 67)
                    if (modeloCte.equals("57") || modeloCte.equals("63") || modeloCte.equals("67")) {

                        RegistroD101 registroD101 = new RegistroD101();
                        registroD101.setVl_fcp_uf_dest(rsD100.getString("valorFcpUfFim").replace(".", ","));
                        registroD101.setVl_icms_uf_dest(rsD100.getString("valorIcmsUfFim").replace(".", ","));
                        registroD101.setVl_icms_uf_rem(rsD100.getString("valorIcmsUfIni").replace(".", ","));
                    }

                    //Itens do documento - Nota Fiscal de Serviços de Transporte (código 07) - aqui é para CTe de Saída
                    if (modeloCte.equals("07") && indiOper.equals("1")) {

                        RegistroD110 registroD110 = new RegistroD110();
                        registroD110.setCod_item("");
                        registroD110.setNum_item("");
                        registroD110.setVl_serv(rsD100.getString("valorCarga").replace(".", ","));
                        registroD110.setVl_out("");
                        registroD100.getRegistroD110().add(registroD110);

                        //Complemento da Nota Fiscal de Serviços de Transporte (código 07)
                        RegistroD120 registroD120 = new RegistroD120();
                        registroD120.setCod_mun_orig(rsD100.getString("codigoMunicipioRemetente"));
                        registroD120.setCod_mun_dest(rsD100.getString("codigoMunicipioRecebedor"));
                        registroD120.setVeic_id("");
                        registroD120.setUf_id("");
                        registroD110.getRegistroD120().add(registroD120);
                    }

                    //Complemento do Conhecimento Rodoviário de Cargas (código 08) e Conhecimento de Transporte de Cargas Avulso (Código 8B)
                    if (modeloCte.equals("08") || modeloCte.equals("8B")) {

                        ConexaoBD conectaD130 = new ConexaoBD();
                        conectaD130.conecta();

                        try {
                            ResultSet rsD130;
                            try ( PreparedStatement psD130 = conectaD130.conexao.prepareStatement("Select * from fsc_imct03 where numeroDoCte = ?")) {
                                psD130.setString(1, numeroDoCte);
                                rsD130 = psD130.executeQuery();

                                while (rsD130.next()) {

                                    RegistroD130 registroD130 = new RegistroD130();
                                    registroD130.setCod_part_consg("");
                                    registroD130.setCod_part_red("");
                                    registroD130.setInd_frt_red("9");
                                    registroD130.setCod_mun_orig(rsD100.getString("codigoMunicipioRemetente"));
                                    registroD130.setCod_mun_dest(rsD100.getString("codigoMunicipioRecebedor"));
                                    registroD130.setVeic_id("");
                                    registroD130.setVl_liq_frt(rsD130.getString("valor").replace(".", ","));
                                    registroD130.setVl_sec_cat("");
                                    registroD130.setVl_desp("");
                                    registroD130.setVl_pedg("");
                                    registroD130.setVl_out("");
                                    registroD130.setVl_frt(rsD130.getString("sum(valor)").replace(".", ","));
                                    registroD130.setUf_id("");
                                    registroD100.getRegistroD130().add(registroD130);
                                }
                                rsD130.close();
                            }
                        } catch (SQLException erro) {
                            tarefaGeraSped.setOpaque(true);
                            tarefaGeraSped.setText("Erro ao ler informações do bloco D130 - Bloco D: " + erro);
                            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
                        }
                        conectaD130.desconecta();
                    }

                    //Complemento do Conhecimento Aquaviário de Cargas (código 09)
                    if (modeloCte.equals("09")) {

                        RegistroD140 registroD140 = new RegistroD140();
                        registroD140.setCod_part_consg("");
                        registroD140.setCod_mun_orig(rsD100.getString("codigoMunicipioRemetente"));
                        registroD140.setCod_mun_dest(rsD100.getString("codigoMunicipioRecebedor"));
                        registroD140.setInd_veic("0");
                        registroD140.setVeic_id("");
                        registroD140.setInd_nav("0");
                        registroD140.setViagem("");
                        registroD140.setVl_frt_liq("");
                        registroD140.setVl_desp_port("");
                        registroD140.setVl_desp_car_desc("");
                        registroD140.setVl_out("");
                        registroD140.setVl_frt_brt("");
                        registroD140.setVl_frt_mm("");
                    }

                    //Complemento do Conhecimento Aéreo de Cargas (código 10)
                    if (modeloCte.equals("10")) {

                        RegistroD150 registroD150 = new RegistroD150();
                        registroD150.setCod_mun_orig("");
                        registroD150.setCod_mun_dest("");
                        registroD150.setVeic_id("");
                        registroD150.setViagem("");
                        registroD150.setInd_tfa("");
                        registroD150.setVl_peso_tx("");
                        registroD150.setVl_tx_terr("");
                        registroD150.setVl_tx_red("");
                        registroD150.setVl_out("");
                        registroD150.setVl_tx_adv("");
                    }

                    //Carga Transportada (CÓDIGO 08, 8B, 09, 10, 11, 26 E 27)
                    if (modeloCte.equals("08") || modeloCte.equals("8B") || modeloCte.equals("09") || modeloCte.equals("10") || modeloCte.equals("11") || modeloCte.equals("26") || modeloCte.equals("27") && indiOper.equals("1")) {

                        RegistroD160 registroD160 = new RegistroD160();
                        registroD160.setDespacho("");
                        registroD160.setCnpj_cpf_rem(rsD100.getString("documentoRemetente"));
                        registroD160.setIe_rem(rsD100.getString("inscricaoEstadualRemetente"));
                        registroD160.setCod_mun_ori(rsD100.getString("codigoMunicipioRemetente"));
                        registroD160.setCnpj_cpf_dest(rsD100.getString("documentoDestinatario"));
                        registroD160.setIe_dest(rsD100.getString("inscricaoEstadualDestinatario"));
                        registroD160.setCod_mun_dest(rsD100.getString("codigoMunicipioDestinatario"));

                        //Local de Coleta e Entrega (códigos 08, 8B, 09, 10, 11 e 26)
                        RegistroD161 registroD161 = new RegistroD161();
                        registroD161.setInd_carga(rsD100.getString("modal"));
                        registroD161.setCnpj_cpf_col(rsD100.getString("documentoRemetente"));
                        registroD161.setIe_col(rsD100.getString("inscricaoEstadualRemetente"));
                        registroD161.setCod_mun_col(rsD100.getString("codigoMunicipioRemetente"));
                        registroD161.setCnpj_cpf_entg(rsD100.getString("documentoDestinatario"));
                        registroD161.setIe_entg(rsD100.getString("inscricaoEstadualDestinatario"));
                        registroD161.setCod_mun_entg(rsD100.getString("codigoMunicipioDestinatario"));

                        //Identificação dos documentos fiscais (código 08,8B, 09,10,11,26 e 27)
                        if (!codCfop.equals("5359") || !codCfop.equals("6359") && indiOper.equals("1")) {

                            ConexaoBD conectaD162 = new ConexaoBD();
                            conectaD162.conecta();

                            try {
                                ResultSet rsD162;
                                try ( PreparedStatement psD162 = conectaD162.conexao.prepareStatement("Select numeroCte, tipoMedida, quantidadeCarga from fsc_imct02 where numeroCte =?")) {
                                    psD162.setString(1, numeroDoCte);
                                    rsD162 = psD162.executeQuery();

                                    while (rsD162.next()) {

                                        RegistroD162 registroD162 = new RegistroD162();
                                        registroD162.setCod_mod(rsD100.getString("modelo"));
                                        registroD162.setSer(rsD100.getString("serie"));
                                        registroD162.setNum_doc(rsD100.getString("numero"));
                                        registroD162.setDt_doc(rsD100.getString("emissao"));
                                        registroD162.setVl_doc(rsD100.getString("valorTotalPrestacao"));
                                        registroD162.setVl_merc(rsD100.getString("valorReceber"));
                                        registroD162.setQtd_vol(rsD162.getString("quantidadeCarga"));
                                        registroD162.setPeso_brt(rsD162.getString(""));
                                        registroD162.setPeso_liq(rsD162.getString("quantidadeCarga"));
                                        registroD160.getRegistroD162().add(registroD162);
                                    }
                                    rsD162.close();
                                }
                            } catch (SQLException erro) {
                                tarefaGeraSped.setOpaque(true);
                                tarefaGeraSped.setText("Erro ao ler informações do bloco D162 - Bloco D: " + erro);
                                tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
                            }
                            conectaD162.desconecta();
                        }
                        registroD100.getRegistroD160().add(registroD160);
                    }

                    //Complemento do Conhecimento Multimodal de Cargas (código 26)
                    if (modeloCte.equals("26") && indiOper.equals("1")) {

                        RegistroD170 registroD170 = new RegistroD170();
                        registroD170.setCod_part_consg("");
                        registroD170.setCod_part_red("");
                        registroD170.setCod_mun_orig("");
                        registroD170.setCod_mun_dest("");
                        registroD170.setOtm("");
                        registroD170.setInd_nat_frt("");
                        registroD170.setVl_liq_frt("");
                        registroD170.setVl_gris("");
                        registroD170.setVl_pdg("");
                        registroD170.setVl_out("");
                        registroD170.setVl_frt("");
                        registroD170.setVeic_id("");
                        registroD170.setUf_id("");

                        //Modais (código 26)
                        RegistroD180 registroD180 = new RegistroD180();
                        registroD180.setNum_seq("");
                        registroD180.setInd_emit("");
                        registroD180.setCnpj_cpf_emit("");
                        registroD180.setUf_emit("");
                        registroD180.setIe_emit("");
                        registroD180.setCod_mun_orig("");
                        registroD180.setCnpj_cpf_tom("");
                        registroD180.setUf_tom("");
                        registroD180.setIe_tom("");
                        registroD180.setCod_mun_dest("");
                        registroD180.setCod_mod("");
                        registroD180.setSer("");
                        registroD180.setSub("");
                        registroD180.setNum_doc("");
                        registroD180.setDt_doc("");
                        registroD180.setVl_doc("");
                        registroD100.getRegistroD180().add(registroD180);
                    }

                    //Registro Analítico dos Documentos (CÓDIGO 07, 08, 8B, 09, 10, 11, 26, 27, 57 e 67)
                    String resultcfop = "", valorCfop, vlredbc = "", vlcsticms;

                    valorCfop = rsD100.getString("cfop").substring(2, 2);
                    vlcsticms = rsD100.getString("cstIcms");

                    if (!valorCfop.equals("00") || !valorCfop.equals("50")) {

                        if (indiOper.equals("0") && valorCfop.startsWith("1") || valorCfop.startsWith("2") || valorCfop.startsWith("3")) {
                            resultcfop = rsD100.getString("cfop");
                        }
                        if (indiOper.equals("1") && valorCfop.startsWith("5") || valorCfop.startsWith("6") || valorCfop.startsWith("7")) {
                            resultcfop = rsD100.getString("cfop");
                        }
                    }

                    if (vlcsticms.equals("20") || vlcsticms.equals("70")) {
                        vlredbc = rsD100.getString("cstIcms").replace(".", ",");
                    }

                    RegistroD190 registroD190 = new RegistroD190();
                    registroD190.setCst_icms(rsD100.getString("cstIcms").replace(".", ","));
                    registroD190.setCfop(resultcfop);
                    registroD190.setAliq_icms(rsD100.getString("aliqIcms").replace(".", ","));
                    registroD190.setVl_opr(rsD100.getString("valorTotalPrestacao").replace(".", ","));
                    registroD190.setVl_bc_icms(rsD100.getString("bcIcms").replace(".", ","));
                    registroD190.setVl_icms(rsD100.getString("valorIcms").replace(".", ","));
                    registroD190.setVl_red_bc(vlredbc);
                    registroD190.setCod_obs(rsD100.getString(""));
                    registroD100.getRegistroD190().add(registroD190);

                    //Observações do lançamento (CÓDIGO 07, 08, 8B, 09, 10, 11, 26, 27, 57 e 67) 
                    RegistroD195 registroD195 = new RegistroD195();
                    registroD195.setCod_obs("");
                    registroD195.setTxt_compl(rsD100.getString("obs"));

                    //Outras obrigações tributárias, ajustes e informações de valores provenientes do documento fiscal.
                    RegistroD197 registroD197 = new RegistroD197();
                    registroD197.setCod_aj("");
                    registroD197.setDescr_compl_aj("");
                    registroD197.setCod_item("");
                    registroD197.setVl_bc_icms("");
                    registroD197.setAliq_icms("");
                    registroD197.setVl_icms("");
                    registroD197.setVl_outros("");
                    registroD195.getRegistroD197().add(registroD197);

                    registroD100.getRegistroD195().add(registroD195);

                    blocoD.getRegistroD100().add(registroD100);
                }
                rsD100.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco D100 ou bloco D101 - Bloco D: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conectaD100.desconecta();

        return blocoD;
    }

    //Registro Analítico dos bilhetes consolidados de Passagem Rodoviário (código 13), de Passagem Aquaviário (código 14), de Passagem e Nota de Bagagem (código 15) e de Passagem Ferroviário (código 16)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD preencheRegistroD300(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD blocoD) {

        if (emiteBlocoD300.isSelected()) {

            RegistroD300 registroD300 = new RegistroD300();
            registroD300.setCod_mod("");
            registroD300.setSer("");
            registroD300.setSub("");
            registroD300.setNum_doc_ini("");
            registroD300.setNum_doc_fin("");
            registroD300.setCst_icms("");
            registroD300.setCfop("");
            registroD300.setAliq_icms("");
            registroD300.setDt_doc("");
            registroD300.setVl_opr("");
            registroD300.setVl_desc("");
            registroD300.setVl_serv("");
            registroD300.setVl_seg("");
            registroD300.setVl_out_desp("");
            registroD300.setVl_bc_icms("");
            registroD300.setVl_icms("");
            registroD300.setVl_red_bc("");
            registroD300.setCod_obs("");
            registroD300.setCod_cta("");

            //Documentos cancelados dos Bilhetes de Passagem Rodoviário (código 13), de Passagem Aquaviário (código 14), de Passagem e Nota de Bagagem (código 15) e de Passagem Ferroviário (código 16)
            RegistroD301 registroD301 = new RegistroD301();
            registroD301.setNum_doc_canc("");
            registroD300.getRegistroD301().add(registroD301);

            //Complemento dos Bilhetes (código 13, código 14, código 15 e código 16) 
            RegistroD310 registroD310 = new RegistroD310();
            registroD310.setCod_mun_orig("");
            registroD310.setVl_serv("");
            registroD310.setVl_bc_icms("");
            registroD310.setVl_icms("");
            registroD300.getRegistroD310().add(registroD310);

            blocoD.getRegistroD300().add(registroD300);
        }
        return blocoD;
    }

    //Equipamento ECF (Códigos 2E, 13, 14, 15 e 16)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD preencheRegistroD350(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD blocoD) {

        if (emiteBlocoD350.isSelected()) {

            RegistroD350 registroD350 = new RegistroD350();
            registroD350.setCod_mod("");
            registroD350.setEcf_mod("");
            registroD350.setEcf_fab("");
            registroD350.setEcf_cx("");

            //Redução Z (Códigos 2E, 13, 14, 15 e 16)
            RegistroD355 registroD355 = new RegistroD355();
            registroD355.setDt_doc("");
            registroD355.setCro("");
            registroD355.setCrz("");
            registroD355.setNum_coo_fin("");
            registroD355.setGt_fin("");
            registroD355.setVl_brt("");

            //Redução Z (Códigos 2E, 13, 14, 15 e 16)
            RegistroD360 registroD360 = new RegistroD360();
            registroD360.setVl_pis("");
            registroD360.setVl_cofins("");

            //Registro dos Totalizadores Parciais da Redução Z (Códigos 2E, 13, 14, 15 e 16) 
            RegistroD365 registroD365 = new RegistroD365();
            registroD365.setCod_tot_par("");
            registroD365.setVlr_acum_tot("");
            registroD365.setNr_tot("");
            registroD365.setDescr_nr_tot("");

            //Complemento dos documentos informados (Códigos 13, 14, 15, 16 E 2E) 
            RegistroD370 registroD370 = new RegistroD370();
            registroD370.setCod_mun_orig("");
            registroD370.setVl_serv("");
            registroD370.setQtd_bilh("");
            registroD370.setVl_bc_icms("");
            registroD370.setVl_icms("");
            registroD365.getRegistroD370().add(registroD370);
            registroD355.getRegistroD365().add(registroD365);

            //Registro analítico do movimento diário (Códigos 13, 14, 15, 16 E 2E)
            RegistroD390 registroD390 = new RegistroD390();
            registroD390.setCst_icms("");
            registroD390.setCfop("");
            registroD390.setAliq_icms("");
            registroD390.setVl_opr("");
            registroD390.setVl_bc_issqn("");
            registroD390.setAliq_issqn("");
            registroD390.setVl_issqn("");
            registroD390.setVl_bc_icms("");
            registroD390.setVl_icms("");
            registroD390.setCod_obs("");
            registroD355.getRegistroD390().add(registroD390);

            registroD350.getRegistroD355().add(registroD355);

            blocoD.getRegistroD350().add(registroD350);

        }
        return blocoD;
    }

    //Resumo do Movimento Diário (código 18)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD preencheRegistroD400(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD blocoD) {

        if (emiteBlocoD400.isSelected()) {

            RegistroD400 registroD400 = new RegistroD400();
            registroD400.setCod_part("");
            registroD400.setCod_mod("");
            registroD400.setCod_sit("");
            registroD400.setSer("");
            registroD400.setSub("");
            registroD400.setNum_doc("");
            registroD400.setDt_doc("");
            registroD400.setVl_doc("");
            registroD400.setVl_desc("");
            registroD400.setVl_serv("");
            registroD400.setVl_bc_icms("");
            registroD400.setVl_icms("");
            registroD400.setVl_pis("");
            registroD400.setVl_cofins("");
            registroD400.setCod_cta("");

            //Documentos Informados (Códigos 13, 14, 15 e 16)
            RegistroD410 registroD410 = new RegistroD410();
            registroD410.setCod_mod("");
            registroD410.setSer("");
            registroD410.setSub("");
            registroD410.setNum_doc_ini("");
            registroD410.setNum_doc_fin("");
            registroD410.setDt_doc("");
            registroD410.setCst_icms("");
            registroD410.setCfop("");
            registroD410.setAliq_icms("");
            registroD410.setVl_opr("");
            registroD410.setVl_desc("");
            registroD410.setVl_serv("");
            registroD410.setVl_bc_icms("");
            registroD410.setVl_icms("");

            //Documentos Cancelados dos Documentos Informados (Códigos 13, 14, 15 e 16)
            RegistroD411 registroD411 = new RegistroD411();
            registroD411.setNum_doc_canc("");
            registroD410.getRegistroD411().add(registroD411);
            registroD400.getRegistroD410().add(registroD410);

            //Complemento dos Documentos Informados (Códigos 13, 14, 15 e 16)
            RegistroD420 registroD420 = new RegistroD420();
            registroD420.setCod_mun_orig("");
            registroD420.setVl_serv("");
            registroD420.setVl_bc_icms("");
            registroD420.setVl_icms("");
            registroD400.getRegistroD420().add(registroD420);

            blocoD.getRegistroD400().add(registroD400);

        }
        return blocoD;
    }

    //Nota Fiscal de Serviço de Comunicação (código 21) e Serviço de Telecomunicação (código 22)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD preencheRegistroD500(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD blocoD) {

        if (emiteBlocoD500.isSelected()) {

            RegistroD500 registroD500 = new RegistroD500();
            registroD500.setInd_oper("");
            registroD500.setInd_emit("");
            registroD500.setCod_part("");
            registroD500.setCod_mod("");
            registroD500.setCod_sit("");
            registroD500.setSer("");
            registroD500.setSub("");
            registroD500.setNum_doc("");
            registroD500.setDt_doc("");
            registroD500.setDt_a_p("");
            registroD500.setVl_doc("");
            registroD500.setVl_desc("");
            registroD500.setVl_serv("");
            registroD500.setVl_serv_nt("");
            registroD500.setVl_terc("");
            registroD500.setVl_da("");
            registroD500.setVl_bc_icms("");
            registroD500.setVl_icms("");
            registroD500.setCod_inf("");
            registroD500.setVl_pis("");
            registroD500.setVl_cofins("");
            registroD500.setCod_cta("");
            registroD500.setTp_assiante("");

            //Itens do Documento - Nota Fiscal de Serviço de Comunicação (código 21) e Serviço de Telecomunicação (código 22)
            RegistroD510 registroD510 = new RegistroD510();
            registroD510.setNum_item("");
            registroD510.setCod_item("");
            registroD510.setCod_class("");
            registroD510.setQtd("");
            registroD510.setUnid("");
            registroD510.setVl_item("");
            registroD510.setVl_desc("");
            registroD510.setCst_icms("");
            registroD510.setCfop("");
            registroD510.setVl_bc_icms("");
            registroD510.setAliq_icms("");
            registroD510.setVl_icms("");
            registroD510.setVl_bc_icms_uf("");
            registroD510.setVl_icms_uf("");
            registroD510.setInd_rec("");
            registroD510.setCod_part("");
            registroD510.setVl_pis("");
            registroD510.setVl_cofins("");
            registroD510.setCod_cta("");
            registroD500.getRegistroD510().add(registroD510);

            //Terminal FaturadO
            RegistroD530 registroD530 = new RegistroD530();
            registroD530.setInd_serv("");
            registroD530.setDt_ini_serv("");
            registroD530.setDt_fin_serv("");
            registroD530.setPer_fiscal("");
            registroD530.setCod_area("");
            registroD530.setTerminal("");
            registroD500.getRegistroD530().add(registroD530);

            //Registro Analítico do Documento (códigos 21 e 22)
            RegistroD590 registroD590 = new RegistroD590();
            registroD590.setCst_icms("");
            registroD590.setCfop("");
            registroD590.setAliq_icms("");
            registroD590.setVl_opr("");
            registroD590.setVl_bc_icms("");
            registroD590.setVl_icms("");
            registroD590.setVl_bc_icms_uf("");
            registroD590.setVl_icms_uf("");
            registroD590.setVl_red_bc("");
            registroD590.setCod_obs("");
            registroD500.getRegistroD590().add(registroD590);

            blocoD.getRegistroD500().add(registroD500);
        }
        return blocoD;
    }

    //Consolidação da Prestação de Serviços - Notas de Serviço de Comunicação (código 21) e de Serviço de Telecomunicação (código 22)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD preencheRegistroD600(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD blocoD) {

        if (emiteBlocoD600.isSelected()) {

            RegistroD600 registroD600 = new RegistroD600();
            registroD600.setCod_mod("");
            registroD600.setCod_mun("");
            registroD600.setSer("");
            registroD600.setSub("");
            registroD600.setCod_cons("");
            registroD600.setQtd_cons("");
            registroD600.setDt_doc("");
            registroD600.setVl_doc("");
            registroD600.setVl_desc("");
            registroD600.setVl_serv("");
            registroD600.setVl_serv_nt("");
            registroD600.setVl_terc("");
            registroD600.setVl_da("");
            registroD600.setVl_bc_icms("");
            registroD600.setVl_icms("");
            registroD600.setVl_pis("");
            registroD600.setVl_cofins("");

            //Itens do Documento Consolidado (códigos 21 e 22)
            RegistroD610 registroD610 = new RegistroD610();
            registroD610.setCod_class("");
            registroD610.setCod_item("");
            registroD610.setQtd("");
            registroD610.setUnid("");
            registroD610.setVl_item("");
            registroD610.setVl_desc("");
            registroD610.setCst_icms("");
            registroD610.setCfop("");
            registroD610.setAliq_icms("");
            registroD610.setVl_bc_icms("");
            registroD610.setVl_icms("");
            registroD610.setVl_bc_icms_uf("");
            registroD610.setVl_icms_uf("");
            registroD610.setVl_red_bc("");
            registroD610.setVl_pis("");
            registroD610.setVl_cofins("");
            registroD610.setCod_cta("");
            registroD600.getRegistroD610().add(registroD610);

            //Registro Analítico dos Documentos (códigos 21 e 22)
            RegistroD690 registroD690 = new RegistroD690();
            registroD690.setCst_icms("");
            registroD690.setCfop("");
            registroD690.setAliq_icms("");
            registroD690.setVl_opr("");
            registroD690.setVl_bc_icms("");
            registroD690.setVl_icms("");
            registroD690.setVl_bc_icms_uf("");
            registroD690.setVl_icms_uf("");
            registroD690.setVl_red_bc("");
            registroD690.setCod_obs("");
            registroD600.getRegistroD690().add(registroD690);

            blocoD.getRegistroD600().add(registroD600);
        }
        return blocoD;
    }

    //Consolidação da Prestação de Serviços - Notas de Serviço de Comunicação (código 21) e de Serviço de Telecomunicação (código 22)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD preencheRegistroD695(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.BlocoD blocoD) {

        if (emiteBlocoD695.isSelected()) {

            RegistroD695 registroD695 = new RegistroD695();
            registroD695.setCod_mod("");
            registroD695.setSer("");
            registroD695.setNro_ord_ini("");
            registroD695.setNro_ord_fin("");
            registroD695.setDt_doc_ini("");
            registroD695.setDt_doc_fin("");
            registroD695.setNom_mest("");
            registroD695.setChv_cod_dig("");

            //Registro Analítico dos Documentos (códigos 21 e 22)
            RegistroD696 registroD696 = new RegistroD696();
            registroD696.setCst_icms("");
            registroD696.setCfop("");
            registroD696.setAliq_icms("");
            registroD696.setVl_opr("");
            registroD696.setVl_bc_icms("");
            registroD696.setVl_icms("");
            registroD696.setVl_bc_icms_uf("");
            registroD696.setVl_icms_uf("");
            registroD696.setVl_red_bc("");
            registroD696.setCod_obs("");
            registroD695.getRegistroD696().add(registroD696);

            //Registro de informações de outras UFs, relativamente aos serviços “nãomedidos” de televisão por assinatura via satélite
            RegistroD697 registroD697 = new RegistroD697();
            registroD697.setUf("");
            registroD697.setVl_bc_icms("");
            registroD697.setVl_icms("");
            registroD696.getRegistroD697().add(registroD697);

            blocoD.getRegistroD695().add(registroD695);
        }

        return blocoD;
    }

    private static void verificaEmitente(String emit) {

        ConexaoBD conectaEmpresa1 = new ConexaoBD();
        conectaEmpresa1.conecta();

        try {
            ResultSet rsEmp1;
            try ( PreparedStatement ps1 = conectaEmpresa1.conexao.prepareStatement("Select codEmpresa,cpfCnpj,razaoSocial from con_for01 where cpfCnpj = ?")) {
                ps1.setString(1, emit);
                rsEmp1 = ps1.executeQuery();
                if (rsEmp1.next()) {
                    resultCodPart = rsEmp1.getString("codEmpresa");
                }
                rsEmp1.close();
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao procurar empresa pelo cpf/ cnpj " + erro);
        }
        conectaEmpresa1.desconecta();
    }
}
