package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;

import ConexaoBDCodex.ConexaoBD;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.dataAte;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.dataDe;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.naoEmiteBlocoC;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.tarefaGeraSped;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.*;
import static Logon.Principal.numCnpjDaEmpresa;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC171;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC173;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC174;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC175;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC300;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC350;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC400;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC500;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC495;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC600;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC700;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC800;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.emiteBlocoC860;

/**
 * @author Alyesson Sousa
 *
 */
public class BlocoC {

    static String valoRazaoSocial, numCpfCnpj, indicEmit, codigoPart, resultCodPart, notaFisc, indiOper;
    static String numcpf = "", numcpfdestina = "", numcnpj = "", numcnpjdestina = "";

    static LocalDate dataPeriodoDe = Instant.ofEpochMilli(dataDe.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    static LocalDate dataPeriodoAte = Instant.ofEpochMilli(dataAte.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

    SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");

    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheBlocoC() {

        br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC = new br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC();

        if (naoEmiteBlocoC.isSelected()) {

            blocoC = preencheRegistroC001(blocoC);
            blocoC = preencheRegistroC990(blocoC);
        } else {

            blocoC = preencheRegistroC001(blocoC);
            blocoC = preencheRegistroC100(blocoC);
            blocoC = preencheRegistroC300(blocoC);
            blocoC = preencheRegistroC350(blocoC);
            blocoC = preencheRegistroC400(blocoC);
            blocoC = preencheRegistroC495(blocoC);
            blocoC = preencheRegistroC500(blocoC);
            blocoC = preencheRegistroC600(blocoC);
            blocoC = preencheRegistroC700(blocoC);
            blocoC = preencheRegistroC800(blocoC);
            blocoC = preencheRegistroC860(blocoC);
            blocoC = preencheRegistroC990(blocoC);
        }

        return blocoC;
    }

    //Abertura do Bloco C
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC001(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (naoEmiteBlocoC.isSelected()) {
            RegistroC001 registroC001 = new RegistroC001();
            registroC001.setInd_mov("1");

            blocoC.setRegistroC001(registroC001);
        } else {

            RegistroC001 registroC001 = new RegistroC001();
            registroC001.setInd_mov("0");

            blocoC.setRegistroC001(registroC001);
        }

        return blocoC;
    }

    //Documento - Nota Fiscal (código 01), Nota Fiscal Avulsa (código 1B), Nota Fiscal de Produtor (código 04), Nota Fiscal Eletrônica (código 55)
    //e Nota Fiscal Eletrônica para Consumidor Final (código 65)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC100(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");

        numCpfCnpj = numCnpjDaEmpresa.getSelectedItem().toString();

        ConexaoBD conectaC100 = new ConexaoBD();
        conectaC100.conecta();

        try {
            ResultSet rsC100;
            try ( PreparedStatement psC100 = conectaC100.conexao.prepareStatement("Select * from fsc_imnf01 where emissao between ? and ?")) {
                psC100.setDate(1, java.sql.Date.valueOf(dataPeriodoDe));
                psC100.setDate(2, java.sql.Date.valueOf(dataPeriodoAte));
                rsC100 = psC100.executeQuery();

                while (rsC100.next()) {

                    codigoPart = rsC100.getString("documentoEmitente");
                    notaFisc = rsC100.getString("numero");
                    indiOper = rsC100.getString("tipo");

                    //(numCpfCnpj);
                    verificaEmitente(codigoPart);

                    RegistroC100 registroC100 = new RegistroC100();
                    registroC100.setInd_oper(rsC100.getString("tipo"));
                    registroC100.setInd_emit(rsC100.getString("indEmit"));
                    registroC100.setCod_part(resultCodPart);
                    registroC100.setCod_mod(rsC100.getString("modelo"));
                    registroC100.setCod_sit(rsC100.getString("indicadorPresenca"));
                    registroC100.setSer(rsC100.getString("serie"));
                    registroC100.setNum_doc(rsC100.getString("numero"));
                    registroC100.setChv_nfe(rsC100.getString("chave"));
                    registroC100.setDt_doc(df.format(rsC100.getString("emissao")));
                    registroC100.setDt_e_s(df.format(rsC100.getString("dhSaidaEntrada")));
                    registroC100.setVl_doc(rsC100.getString("valorTotal").replace(".", ","));
                    registroC100.setInd_pagto("1");
                    registroC100.setVl_desc(rsC100.getString("valorDesconto").replace(".", ","));
                    registroC100.setVl_abat_nt("0");
                    registroC100.setVl_merc(rsC100.getString("valorProdutos").replace(".", ","));
                    registroC100.setInd_frt(rsC100.getString("modalidadeFrete"));
                    registroC100.setVl_frt(rsC100.getString("valorFrete").replace(".", ","));
                    registroC100.setVl_seg(rsC100.getString("valorSeguro").replace(".", ","));
                    registroC100.setVl_out_da(rsC100.getString("valorOutros").replace(".", ","));
                    registroC100.setVl_bc_icms(rsC100.getString("valorBaseCalculo").replace(".", ","));
                    registroC100.setVl_icms(rsC100.getString("valorIcms").replace(".", ","));
                    registroC100.setVl_bc_icms_st(rsC100.getString("valorBaseCalculoSt").replace(".", ","));
                    registroC100.setVl_icms_st(rsC100.getString("valorSt").replace(".", ","));
                    registroC100.setVl_ipi(rsC100.getString("valorIpi").replace(".", ","));
                    registroC100.setVl_pis(rsC100.getString("valorPis").replace(".", ","));
                    registroC100.setVl_cofins(rsC100.getString("valorCofins").replace(".", ","));
                    registroC100.setVl_pis_st(rsC100.getString("valorPis").replace(".", ","));
                    registroC100.setVl_cofins_st(rsC100.getString("valorCofins").replace(".", ","));

                    //Informação complementar dos documentos fiscais quando das operações interestaduais destinadas a consumidor final não contribuinte EC 87/15 (código 55)
                    ConexaoBD conectaC101 = new ConexaoBD();
                    conectaC101.conecta();
                    try {
                        ResultSet rsC101;
                        try ( PreparedStatement psC101 = conectaC101.conexao.prepareStatement("Select * from fsc_imnf02 where numeroNotaFiscal = ?")) {
                            psC101.setString(1, notaFisc);
                            rsC101 = psC101.executeQuery();

                            while (rsC101.next()) {

                                RegistroC101 registroC101 = new RegistroC101();
                                registroC101.setVl_fcp_uf_dest(rsC101.getString("valorFcp").replace(".", ","));
                                registroC101.setVl_icms_uf_dest(rsC101.getString("valorIcmsStDestino").replace(".", ","));
                                registroC101.setVl_icms_uf_rem(rsC101.getString("valorIcmsEfetivo").replace(".", ","));

                                //Operações com ICMS ST recolhido para UF diversa do destinatário do documento fiscal (Código 55)
                                RegistroC105 registroC105 = new RegistroC105();
                                registroC105.setOper("");
                                registroC105.setUf("");
                            }
                            rsC101.close();
                        }
                    } catch (SQLException erro) {
                        tarefaGeraSped.setOpaque(true);
                        tarefaGeraSped.setText("Erro ao ler informações do bloco C101 - Bloco C: " + erro);
                        tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
                    }
                    conectaC101.desconecta();

                    //Complemento de Documento - Informação Complementar da Nota Fiscal (código 01, 1B, 55)
                    RegistroC110 registroC110 = new RegistroC110();
                    registroC110.setCod_inf("");
                    registroC110.setTxt_compl(rsC100.getString("informacaoAdicionalFisco"));

                    //Complemento de Documento - Processo referenciado
                    RegistroC111 registroC111 = new RegistroC111();
                    registroC111.setNum_proc("");
                    registroC111.setInd_proc("");
                    registroC110.getRegistroC111().add(registroC111);

                    //Complemento de Documento - Processo referenciado
                    RegistroC112 registroC112 = new RegistroC112();
                    registroC112.setCod_da("");
                    registroC112.setUf("");
                    registroC112.setNum_da("");
                    registroC112.setCod_aut("");
                    registroC112.setVl_da("");
                    registroC112.setDt_vcto("");
                    registroC112.setDt_pgto("");
                    registroC110.getRegistroC112().add(registroC112);

                    //Complemento de Documento - Documento Fiscal Referenciado
                    RegistroC113 registroC113 = new RegistroC113();
                    registroC113.setInd_oper("");
                    registroC113.setInd_emit("");
                    registroC113.setCod_part("");
                    registroC113.setCod_mod("");
                    registroC113.setSer("");
                    registroC113.setSub("");
                    registroC113.setNum_doc("");
                    registroC113.setDt_doc("");
                    registroC113.setChv_docE("");
                    registroC110.getRegistroC113().add(registroC113);

                    //Complemento de Documento - Cupom Fiscal Referenciado
                    RegistroC114 registroC114 = new RegistroC114();
                    registroC114.setCod_mod("");
                    registroC114.setEcf_fab("");
                    registroC114.setEcf_cx("");
                    registroC114.setNum_doc("");
                    registroC114.setDt_doc("");
                    registroC110.getRegistroC114().add(registroC114);

                    //Local de coleta e/ou entrega (CÓDIGOS 01, 1B e 04)
                    ConexaoBD conectaC115 = new ConexaoBD();
                    conectaC115.conecta();

                    try {
                        ResultSet rsC115;
                        try ( PreparedStatement psC115 = conectaC115.conexao.prepareStatement("Select tipoServico,documentoTomador,inscricaoEstadualTomador,codigoMunicipioTomador,documentoDestinatario,inscricaoEstadualDestinatario,codigoMunicipioDestinatario, from fsc_imct01 where emissao between ? and ?")) {
                            psC115.setDate(1, java.sql.Date.valueOf(dataPeriodoDe));
                            psC115.setDate(2, java.sql.Date.valueOf(dataPeriodoAte));
                            rsC115 = psC115.executeQuery();

                            while (rsC115.next()) {

                                if (rsC115.getString("documentoTomador").length() == 14) {
                                    numcnpj = rsC115.getString("documentoTomador");
                                } else {
                                    numcpf = rsC115.getString("documentoTomador");
                                }

                                if (rsC115.getString("documentoDestinatario").length() == 114) {
                                    numcnpjdestina = rsC115.getString("documentoDestinatario");
                                } else {
                                    numcpfdestina = rsC115.getString("documentoDestinatario");
                                }

                                RegistroC115 registroC115 = new RegistroC115();
                                registroC115.setInd_carga(rsC115.getString("tipoServico"));
                                registroC115.setCnpj_col(numcnpj);
                                registroC115.setIe_col(rsC115.getString("inscricaoEstadualTomador"));
                                registroC115.setCpf_col(numcpf);
                                registroC115.setCod_mun_col(rsC115.getString("codigoMunicipioTomador"));
                                registroC115.setCnpj_entg(numcnpjdestina);
                                registroC115.setIe_entg(rsC115.getString("inscricaoEstadualDestinatario"));
                                registroC115.setCpf_entg(numcpfdestina);
                                registroC115.setCod_mun_entg(rsC115.getString("codigoMunicipioDestinatario"));
                                registroC110.getRegistroC115().add(registroC115);
                            }
                            rsC115.close();
                        }
                    } catch (SQLException erro) {
                        tarefaGeraSped.setOpaque(true);
                        tarefaGeraSped.setText("Erro ao ler informações do bloco C115 - Bloco C: " + erro);
                        tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
                    }
                    conectaC115.desconecta();

                    //Cupom Fiscal Eletrônico - CF-e referenciado
                    RegistroC116 registroC116 = new RegistroC116();
                    registroC116.setCod_mod("");
                    registroC116.setNr_sat("");
                    registroC116.setChv_cfe("");
                    registroC116.setNum_cfe("");
                    registroC116.setDt_doc("");
                    registroC110.getRegistroC116().add(registroC116);

                    registroC100.getRegistroC110().add(registroC110);

//=================================================================
                    //Complemento de Documento - Operações de Importação (código 01 e 55)
                    RegistroC120 registroC120 = new RegistroC120();
                    registroC120.setCod_doc_imp("");
                    registroC120.setNum_doc_imp("");
                    registroC120.setPis_imp("");
                    registroC120.setCofins_imp("");
                    registroC120.setNum_acdraw("");

                    registroC100.getRegistroC120().add(registroC120);

//=================================================================             
                    //Complemento de Documento - ISSQN, IRRF e Previdência Social
                    RegistroC130 registroC130 = new RegistroC130();
                    registroC130.setVl_serv_nt("");
                    registroC130.setVl_bc_issqn("");
                    registroC130.setVl_issqn("");
                    registroC130.setVl_bc_irrf("");
                    registroC130.setVl_irrf("");
                    registroC130.setVl_bc_prev("");
                    registroC130.setVl_prev("");

//=================================================================
                    //Complemento de Documento - Fatura (código 01)
                    ConexaoBD conectaC140 = new ConexaoBD();
                    conectaC140.conecta();

                    try {
                        ResultSet rsC140;
                        try ( PreparedStatement psC140 = conectaC140.conexao.prepareStatement("Select numeroNotaFiscal,valorOriginalFatura,valorDescontoFatura,valorLiquido,numeroDuplicata,dataVencimentoDuplicata,valordaParcelaDuplicata from fsc_imnf03 where numeroNotaFiscal = ?")) {
                            psC140.setString(1, notaFisc);
                            rsC140 = psC140.executeQuery();

                            while (rsC140.next()) {

                                RegistroC140 registroC140 = new RegistroC140();
                                registroC140.setInd_emit(indicEmit);
                                registroC140.setInd_tit("00");
                                registroC140.setDesc_tit(rsC100.getString(""));
                                registroC140.setNum_tit(rsC140.getString("numeroDuplicata"));
                                registroC140.setQtd_parc(rsC140.getString("count(numeroDuplicata)"));
                                registroC140.setVl_tit(rsC100.getString("valorLiquido").replace(".", ","));

                                //Complemento de Documento - Vencimento da Fatura (código 01)
                                RegistroC141 registroC141 = new RegistroC141();
                                registroC141.setNum_parc(rsC140.getString("numeroDuplicata"));
                                registroC141.setDt_vcto(df.format(rsC140.getString("dataVencimentoDuplicata")));
                                registroC141.setVl_parc(rsC140.getString("valordaParcelaDuplicata").replace(".", ","));
                                registroC140.getRegistroC141().add(registroC141);
                            }
                            rsC140.close();
                        }
                    } catch (SQLException erro) {
                        tarefaGeraSped.setOpaque(true);
                        tarefaGeraSped.setText("Erro ao ler informações do bloco C140 ou C141 - Bloco C: " + erro);
                        tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
                    }

                    conectaC140.desconecta();

//=================================================================
                    //Complemento de Documento - Volumes Transportados (código 01 e 04) Exceto Combustívei
                    RegistroC160 registroC160 = new RegistroC160();
                    registroC160.setCod_part("");
                    registroC160.setVeic_id("");
                    registroC160.setQtd_vol("");
                    registroC160.setPeso_brt("");
                    registroC160.setPeso_liq("");
                    registroC160.setUf_id("");

                    //Complemento de Documento - Operações com combustíveis (código 01)
                    RegistroC165 registroC165 = new RegistroC165();
                    registroC165.setCod_part("");
                    registroC165.setVeic_id("");
                    registroC165.setCod_aut("");
                    registroC165.setNr_passe("");
                    registroC165.setHora("");
                    registroC165.setTemper("");
                    registroC165.setQtd_vol("");
                    registroC165.setPeso_brt("");
                    registroC165.setPeso_liq("");
                    registroC165.setNom_mot("");
                    registroC165.setCpf("");
                    registroC165.setUf_id("");
                    registroC100.getRegistroC165().add(registroC165);

//===============================================================
                    //Complemento de Documento - Itens do Documento (código 01, 1B, 04 e 55)
                    ConexaoBD conectaC170 = new ConexaoBD();
                    conectaC170.conecta();
                    try {
                        ResultSet rsC170;
                        try ( PreparedStatement psC170 = conectaC170.conexao.prepareStatement("Select * from fsc_imnf02 where numeroNotaFiscal = ?")) {
                            psC170.setString(1, notaFisc);
                            rsC170 = psC170.executeQuery();

                            while (rsC170.next()) {

                                RegistroC170 registroC170 = new RegistroC170();
                                registroC170.setNum_item(rsC170.getString("item"));
                                registroC170.setCod_item(rsC170.getString("codigoProduto"));
                                registroC170.setQtd(rsC170.getString("quantidadeComercial"));
                                registroC170.setUnid(rsC170.getString("UnidadeComercial"));
                                registroC170.setVl_item(rsC170.getString("valorUnitarioComercial").replace(".", ","));
                                registroC170.setVl_desc(rsC170.getString("valorDesconto").replace(".", ","));
                                registroC170.setInd_mov("0");
                                registroC170.setCst_icms(rsC170.getString("cstIcms"));
                                registroC170.setCfop(rsC170.getString("cfop"));
                                registroC170.setVl_bc_icms(rsC170.getString("bcIcms").replace(".", ","));
                                registroC170.setAliq_icms(rsC170.getString("aliqIcms").replace(".", ","));
                                registroC170.setVl_icms(rsC170.getString("valorIcms").replace(".", ","));
                                registroC170.setVl_bc_icms_st(rsC170.getString("bcIcmsSt").replace(".", ","));
                                registroC170.setAliq_st(rsC170.getString("aliqIcmsSt").replace(".", ","));
                                registroC170.setVl_icms_st(rsC170.getString("valorIcmsSt").replace(".", ","));
                                registroC170.setInd_apur("0");
                                registroC170.setCst_ipi(rsC170.getString("cstIpi").replace(".", ","));
                                registroC170.setCod_enq(rsC170.getString("enquadramentoIpi").replace(".", ","));
                                registroC170.setVl_bc_ipi(rsC170.getString("bcIpi").replace(".", ","));
                                registroC170.setAliq_ipi(rsC170.getString("aliqIpi").replace(".", ","));
                                registroC170.setVl_ipi(rsC170.getString("valorIpi").replace(".", ","));
                                registroC170.setCst_pis(rsC170.getString("cstPis").replace(".", ","));
                                registroC170.setVl_bc_pis(rsC170.getString("bcPis").replace(".", ","));
                                registroC170.setAliq_pis_percentual(rsC170.getString("aliqPis").replace(".", ","));
                                registroC170.setQuant_bc_pis("0");
                                registroC170.setAliq_pis_reais("0");
                                registroC170.setVl_pis(rsC170.getString("valorPis").replace(".", ","));
                                registroC170.setCst_cofins(rsC170.getString("cstCofins").replace(".", ","));
                                registroC170.setVl_bc_cofins(rsC170.getString("bcCofins").replace(".", ","));
                                registroC170.setAliq_cofins_percentual(rsC170.getString("aliqCofins").replace(".", ","));
                                registroC170.setQuant_bc_cofins(rsC170.getString("quantVendidaCofins"));
                                registroC170.setAliq_cofins_reais(rsC170.getString("valorAliqCofinsRs").replace(".", ","));
                                registroC170.setVl_cofins(rsC170.getString("valorCofins").replace(".", ","));
                                registroC170.setCod_cta("");
                                registroC170.setVl_abat_nt("0");

                                //Complemento de Item - Armazenamento de Combustíveis (código 01,55) 
                                if (emiteBlocoC171.isSelected()) {

                                    ConexaoBD conectaC171 = new ConexaoBD();
                                    conectaC171.conecta();
                                    try {
                                        ResultSet rsC171;
                                        try ( PreparedStatement psC171 = conectaC171.conexao.prepareStatement("Select numeroNotaFiscal,tipoNota,numeroTanque,quantidade,dataInclusao from fsc_imnf04 where numeroNotaFiscal = ? and dataInclusao between ? and ?")) {
                                            psC171.setString(1, notaFisc);
                                            psC171.setDate(2, java.sql.Date.valueOf(dataPeriodoDe));
                                            psC171.setDate(2, java.sql.Date.valueOf(dataPeriodoAte));
                                            rsC171 = psC171.executeQuery();

                                            while (rsC171.next()) {

                                                String tip = rsC171.getString("tipoNota").substring(0, 1);

                                                if (tip.equals("0")) {
                                                    RegistroC171 registroC171 = new RegistroC171();
                                                    registroC171.setNum_tanque(rsC171.getString("numeroTanque"));
                                                    registroC171.setQtde(rsC171.getString("quantidade"));
                                                    registroC170.getRegistroC171().add(registroC171);
                                                }
                                            }
                                            rsC171.close();
                                        }
                                    } catch (SQLException erro) {
                                        tarefaGeraSped.setOpaque(true);
                                        tarefaGeraSped.setText("Erro ao ler informações do bloco C171 - Bloco C: " + erro);
                                        tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
                                    }
                                    conectaC171.desconecta();
                                }

                                //Complemento de Item - Operações com ISSQN (código 01)
                                RegistroC172 registroC172 = new RegistroC172();
                                registroC172.setVl_bc_issqn("");
                                registroC172.setAliq_issqn("");
                                registroC172.setVl_issqn("");

                                //Complemento de Item - Operações com Medicamentos (código 01,55)
                                if (emiteBlocoC173.isSelected()) {
                                    String numLote = "", valiLote = "", fabLote = "";
                                    if (rsC170.getString("numeroLote") != null) {
                                        numLote = rsC170.getString("numeroLote");
                                    }
                                    if (rsC170.getString("fabricacaoLote") != null) {
                                        fabLote = rsC170.getString("fabricacaoLote");
                                    }
                                    if (rsC170.getString("validadeLote") != null) {
                                        fabLote = rsC170.getString("validadeLote");
                                    }

                                    RegistroC173 registroC173 = new RegistroC173();
                                    registroC173.setLote_med(numLote);
                                    registroC173.setQtd_item(rsC170.getString("quantidadeComercial"));
                                    registroC173.setDt_fab(fabLote);
                                    registroC173.setDt_val(valiLote);
                                    registroC173.setInd_med(rsC170.getString("modBc"));
                                    registroC173.setTp_prod(rsC170.getString(""));
                                    registroC173.setVl_tab_max(rsC170.getString("valorUnitarioComercial").replace(".", ","));
                                    registroC170.getRegistroC173().add(registroC173);
                                }
                                //Complemento de Item - Operações com Armas de Fogo (código 01) 
                                if (emiteBlocoC174.isSelected()) {
                                    RegistroC174 registroC174 = new RegistroC174();
                                    registroC174.setInd_arm("");
                                    registroC174.setNum_arm("");
                                    registroC174.setDescr_compl("");
                                    registroC170.getRegistroC174().add(registroC174);
                                }

                                //Complemento de Item - Operações com Veículos Novos (código 01,55)
                                if (emiteBlocoC175.isSelected()) {
                                    RegistroC175 registroC175 = new RegistroC175();
                                    registroC175.setInd_veic_oper("");
                                    registroC175.setCnpj("");
                                    registroC175.setUf("");
                                    registroC175.setChassi_veic("");
                                    registroC170.getRegistroC175().add(registroC175);
                                }

                                //Complemento de Item -Ressarcimento de ICMS em operações com Substituição Tributária (código 01,55)
                                RegistroC176 registroC176 = new RegistroC176();
                                registroC176.setCod_mod_ult_e("");
                                registroC176.setNum_doc_ult_e("");
                                registroC176.setSer_ult_e("");
                                registroC176.setDt_ult_e("");
                                registroC176.setCod_part_ult_e("");
                                registroC176.setQuant_ult_e("");
                                registroC176.setVl_unit_ult_e("");
                                registroC176.setVl_unit_bc_st("");
                                registroC176.setChave_nfe_ult_e("");
                                registroC176.setNum_item_ult_e("");
                                registroC176.setVl_unit_bc_icms_ult_e("");
                                registroC176.setAliq_icms_ult_e("");
                                registroC176.setVl_unit_limite_bc_icms_ult_e("");
                                registroC176.setVl_unit_icms_ult_e("");
                                registroC176.setAliq_st_ult_e("");
                                registroC176.setVl_unit_res("");
                                registroC176.setCod_resp_ret("");
                                registroC176.setCod_mot_res("");
                                registroC176.setChave_nfe_ret("");
                                registroC176.setCod_part_nfe_ret("");
                                registroC176.setSer_nfe_ret("");
                                registroC176.setNum_nfe_ret("");
                                registroC176.setItem_nfe_ret("");
                                registroC176.setCod_da("");
                                registroC176.setNum_da("");
                                registroC176.setVl_unit_res_fcp_st("");
                                registroC170.getRegistroC176().add(registroC176);

                                //Complemento de Item – Outras informações (Cód. 01, 55) – (Válido a partir de 01/01/2019)
                                RegistroC177 registroC177 = new RegistroC177();
                                registroC177.setCod_selo_ipi(rsC170.getString("codigoSeloIpi"));
                                registroC177.setQt_selo_ipi(rsC170.getString("quantidadeSeloIpi"));

                                //Complemento de Item - Operações com Produtos Sujeitos a Tributação de IPI por Unidade ou Quantidade de produto
                                RegistroC178 registroC178 = new RegistroC178();
                                registroC178.setCl_enq(rsC170.getString("enquadramentoIpi"));
                                registroC178.setVl_unid(rsC170.getString("valorUniIpi").replace(".", ","));
                                registroC178.setQuant_pad(rsC170.getString("quantidadeTributacao"));

                                //Complemento de Item - Informações Complementares ST (código 01)
                                String a = "", b = "", c = "", d = "";

                                if (rsC170.getString("bcIcmsStDestino") != null) {
                                    a = rsC170.getString("bcIcmsStDestino");
                                }
                                if (rsC170.getString("valorIcmsStDestino") != null) {
                                    b = rsC170.getString("valorIcmsStDestino");
                                }
                                if (rsC170.getString("bcIcmsStRetido") != null) {
                                    c = rsC170.getString("bcIcmsStRetido");
                                }
                                if (rsC170.getString("valorIcmsStRetido") != null) {
                                    d = rsC170.getString("valorIcmsStRetido");
                                }

                                RegistroC179 registroC179 = new RegistroC179();
                                registroC179.setBc_st_orig_dest(a);
                                registroC179.setIcms_st_rep(b);
                                registroC179.setIcms_st_compl("");
                                registroC179.setBc_ret(c);
                                registroC179.setIcms_ret(d);

                                //Informações complementares das operações de entrada de mercadorias sujeitas à substituição tributária (código 01, 1B, 04 e 55)
                                if (indiOper.equals("0")) {

                                    RegistroC180 registroC180 = new RegistroC180();
                                    registroC180.setCod_resp_ret("");
                                    registroC180.setQuant_conv("");
                                    registroC180.setUnid("");
                                    registroC180.setVl_unit_conv("");
                                    registroC180.setVl_unit_icms_op_conv("");
                                    registroC180.setVl_unit_bc_icms_st_conv("");
                                    registroC180.setVl_unit_icms_st_conv("");
                                    registroC180.setVl_unit_fcp_st_conv("");
                                    registroC180.setCod_da("");
                                    registroC180.setNum_da("");
                                }

                                registroC100.getRegistroC170().add(registroC170);
                            }
                            rsC170.close();
                        }

                    } catch (SQLException erro) {
                        tarefaGeraSped.setOpaque(true);
                        tarefaGeraSped.setText("Erro ao ler informações do bloco C170 - Bloco C: " + erro);
                        tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
                    }

                    conectaC170.desconecta();

//============================================================================
                    //Informações complementares das operações de saída de mercadorias sujeitas à substituição tributária (código 01, 1B, 04 e 55)
                    if (indiOper.equals("1")) {

                        RegistroC185 registroC185 = new RegistroC185();
                        registroC185.setNum_item("");
                        registroC185.setCod_item("");
                        registroC185.setCst_icms("");
                        registroC185.setCfop("");
                        registroC185.setCod_mot_rest_compl("");
                        registroC185.setQuant_mov("");
                        registroC185.setUnid("");
                        registroC185.setVl_unid_conv("");
                        registroC185.setVl_unit_icms_na_operacao_conv("");
                        registroC185.setVl_unit_icms_op_conv("");
                        registroC185.setVl_unit_icms_op_estoque_conv("");
                        registroC185.setVl_unit_icms_st_estoque_conv("");
                        registroC185.setVl_unit_fcp_icms_st_estoque_conv("");
                        registroC185.setVl_unit_icms_st_conv_rest("");
                        registroC185.setVl_unit_fcp_st_conv_rest("");
                        registroC185.setVl_unit_icms_st_conv_compl("");
                        registroC185.setVl_unit_fcp_st_conv_compl("");

                        registroC100.getRegistroC185().add(registroC185);
                    }
//============================================================================

                    //Registro Analítico do Documento (código 01, 1B, 04, 55 e 65)
                    ConexaoBD conectaC190 = new ConexaoBD();
                    conectaC190.conecta();
                    try {
                        ResultSet rsC190;
                        try ( PreparedStatement psC190 = conectaC190.conexao.prepareStatement("Select * from fsc_imnf02 where numeroNotaFiscal = ?")) {
                            psC190.setString(1, notaFisc);
                            rsC190 = psC190.executeQuery();

                            while (rsC190.next()) {
                                RegistroC190 registroC190 = new RegistroC190();
                                registroC190.setCst_icms(rsC190.getString("cstIcms").replace(".", ","));
                                registroC190.setCfop(rsC190.getString("cfop"));
                                registroC190.setAliq_icms(rsC190.getString("aliqIcms").replace(".", ","));
                                registroC190.setVl_opr(rsC190.getString("valorTotalProdutos").replace(".", ","));
                                registroC190.setVl_bc_icms(rsC190.getString("bcIcms").replace(".", ","));
                                registroC190.setVl_icms(rsC190.getString("valorIcms").replace(".", ","));
                                registroC190.setVl_bc_icms_st(rsC190.getString("bcIcmsSt").replace(".", ","));
                                registroC190.setVl_icms_st(rsC190.getString("valorIcmsSt").replace(".", ","));
                                registroC190.setVl_red_bc(rsC190.getString("bcIcmsStRetido").replace(".", ","));
                                registroC190.setVl_ipi(rsC190.getString("valorIpi").replace(".", ","));
                                registroC190.setCod_obs(rsC190.getString(""));
                                registroC100.getRegistroC190().add(registroC190);

                                //Informações do Fundo de Combate à Pobreza – FCP – na NF-e (Código 55)
                                RegistroC191 registroC191 = new RegistroC191();
                                registroC191.setVl_fcp_op(rsC190.getString("valorFcp").replace(".", ","));
                                registroC191.setVl_fcp_st(rsC190.getString("valorFcpSt").replace(".", ","));
                                registroC191.setVl_fcp_ret(rsC190.getString("valorFcpStRetido").replace(".", ","));

                                //Complemento do Registro Analítico - Observações do Lançamento Fiscal (código 01, 1B, 04 e 55)
                                RegistroC195 registroC195 = new RegistroC195();
                                registroC195.setTxt_compl(rsC190.getString("informacaoAdicional"));
                                registroC195.setCod_obs("");
                                registroC100.getRegistroC195().add(registroC195);

                                //Outras Obrigações Tributárias, Ajustes e Informações provenientes de Documento Fiscal
                                RegistroC197 registroC197 = new RegistroC197();
                                registroC197.setCod_aj("");
                                registroC197.setDescr_compl_aj("");
                                registroC197.setCod_item("");
                                registroC197.setVl_bc_icms("");
                                registroC197.setAliq_icms("");
                                registroC197.setVl_icms("");
                                registroC197.setVl_outros("");

                                blocoC.getRegistroC100().add(registroC100);
                            }
                            rsC190.close();
                        }
                    } catch (SQLException erro) {
                        tarefaGeraSped.setOpaque(true);
                        tarefaGeraSped.setText("Erro ao ler informações do bloco C190 - Bloco C: " + erro);
                        tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
                    }
                    conectaC190.desconecta();
                }
                rsC100.close();
            }

        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco C100: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }

        conectaC100.desconecta();

        return blocoC;
    }

    //Outras Obrigações Tributárias, Ajustes e Informações provenientes de Documento Fiscal - NFC-e
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC300(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (emiteBlocoC300.isSelected()) {

            RegistroC300 registroC300 = new RegistroC300();
            registroC300.setCod_mod("");
            registroC300.setSer("");
            registroC300.setSub("");
            registroC300.setNum_doc_ini("");
            registroC300.setNum_doc_fin("");
            registroC300.setDt_doc("");
            registroC300.setVl_doc("");
            registroC300.setVl_pis("");
            registroC300.setVl_cofins("");
            registroC300.setCod_cta("");

            //Outras Obrigações Tributárias, Ajustes e Informações provenientes de Documento Fiscal
            RegistroC310 registroC310 = new RegistroC310();
            registroC310.setNum_doc_canc("");
            registroC300.getRegistroC310().add(registroC310);

            //Registro Analítico das Notas Fiscais de Venda a Consumidor (código 02)
            RegistroC320 registroC320 = new RegistroC320();
            registroC320.setCst_icms("");
            registroC320.setCfop("");
            registroC320.setAliq_icms("");
            registroC320.setVl_opr("");
            registroC320.setVl_bc_icms("");
            registroC320.setVl_icms("");
            registroC320.setVl_red_bc("");
            registroC320.setCod_obs("");

            //Itens dos Resumos Diários dos Documentos (código 02)
            RegistroC321 registroC321 = new RegistroC321();
            registroC321.setCod_item("");
            registroC321.setQtd("");
            registroC321.setUnid("");
            registroC321.setVl_item("");
            registroC321.setVl_desc("");
            registroC321.setVl_bc_icms("");
            registroC321.setVl_icms("");
            registroC321.setVl_pis("");
            registroC321.setVl_cofins("");
            registroC320.getRegistroC321().add(registroC321);

            //Informações complementares das operações de saída de mercadorias sujeitas à substituição tributária (código 02)
            RegistroC330 registroC330 = new RegistroC330();
            registroC330.setCod_mot_rest_compl("");
            registroC330.setQuant_conv("");
            registroC330.setUnid("");
            registroC330.setVl_unit_conv("");
            registroC330.setVl_unit_icms_na_operacao_conv("");
            registroC330.setVl_unit_icms_op_conv("");
            registroC330.setVl_unit_icms_op_estoque_conv("");
            registroC330.setVl_unit_icms_st_estoque_conv("");
            registroC330.setVl_unit_fcp_icms_st_estoque_conv("");
            registroC330.setVl_unit_icms_st_conv_rest("");
            registroC330.setVl_unit_fcp_st_conv_rest("");
            registroC330.setVl_unit_icms_st_conv_compl("");
            registroC330.setVl_unit_fcp_st_conv_compl("");
            registroC300.getRegistroC320().add(registroC320);

            blocoC.getRegistroC300().add(registroC300);
        }
        return blocoC;
    }

    //Nota Fiscal de venda a consumidor (código 02)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC350(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (emiteBlocoC350.isSelected()) {

            RegistroC350 registroC350 = new RegistroC350();
            registroC350.setSer("");
            registroC350.setSub_ser("");
            registroC350.setNum_doc("");
            registroC350.setDt_doc("");
            registroC350.setCnpj_cpf("");
            registroC350.setVl_merc("");
            registroC350.setVl_doc("");
            registroC350.setVl_desc("");
            registroC350.setVl_pis("");
            registroC350.setVl_cofins("");
            registroC350.setCod_cta("");

            //Itens do documento (código 02)
            RegistroC370 registroC370 = new RegistroC370();
            registroC370.setNum_item("");
            registroC370.setCod_item("");
            registroC370.setQtd("");
            registroC370.setUnid("");
            registroC370.setVl_item("");
            registroC370.setVl_desc("");

            //Informações complementares das operações de saída de mercadorias sujeitas à substituição tributária (código 02)
            RegistroC380 registroC380 = new RegistroC380();
            registroC380.setCod_mot_rest_compl("");
            registroC380.setQuant_conv("");
            registroC380.setUnid("");
            registroC380.setVl_unit_conv("");
            registroC380.setVl_unit_icms_na_operacao_conv("");
            registroC380.setVl_unit_icms_op_conv("");
            registroC380.setVl_unit_icms_op_estoque_conv("");
            registroC380.setVl_unit_icms_st_estoque_conv("");
            registroC380.setVl_unit_fcp_icms_st_estoque_conv("");
            registroC380.setVl_unit_icms_st_conv_rest("");
            registroC380.setVl_unit_fcp_st_conv_rest("");
            registroC380.setVl_unit_icms_st_conv_compl("");
            registroC380.setVl_unit_fcp_st_conv_compl("");
            registroC380.setCst_icms("");
            registroC380.setCfop("");
            registroC350.getRegistroC370().add(registroC370);

            //Registro Analítico das Notas Fiscais de Venda a Consumidor (código 02)
            RegistroC390 registroC390 = new RegistroC390();
            registroC390.setCst_icms("");
            registroC390.setCfop("");
            registroC390.setAliq_icms("");
            registroC390.setVl_opr("");
            registroC390.setVl_bc_icms("");
            registroC390.setVl_icms("");
            registroC390.setVl_red_bc("");
            registroC390.setCod_obs("");
            registroC350.getRegistroC390().add(registroC390);

            blocoC.getRegistroC350().add(registroC350);
        }
        return blocoC;
    }

    //Equipamento ECF (código 02, 2D e 60)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC400(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (emiteBlocoC400.isSelected()) {

            RegistroC400 registroC400 = new RegistroC400();
            registroC400.setCod_mod("");
            registroC400.setEcf_mod("");
            registroC400.setEcf_fab("");
            registroC400.setEcf_cx("");

            //Redução Z (código 02, 2D e 60)
            RegistroC405 registroC405 = new RegistroC405();
            registroC405.setDt_doc("");
            registroC405.setCro("");
            registroC405.setCrz("");
            registroC405.setNum_coo_fin("");
            registroC405.setGt_fin("");
            registroC405.setVl_brt("");

            //PIS e COFINS Totalizados no Dia (código 02 e 2D)
            RegistroC410 registroC410 = new RegistroC410();
            registroC410.setVl_pis("");
            registroC410.setVl_cofins("");

            //Registro dos Totalizadores Parciais da Redução Z (código 02, 2D e 60)
            RegistroC420 registroC420 = new RegistroC420();
            registroC420.setCod_tot_par("");
            registroC420.setVlr_acum_tot("");

            //Resumo de itens do movimento diário (código 02 e 2D)
            RegistroC425 registroC425 = new RegistroC425();
            registroC425.setCod_item("");
            registroC425.setQtd("");
            registroC425.setUnid("");
            registroC425.setVl_item("");
            registroC425.setVl_pis("");
            registroC425.setVl_cofins("");

            //Informações complementares das operações de saída de mercadorias sujeitas à substituição tributária (código 02, 2D e 60)
            RegistroC430 registroC430 = new RegistroC430();
            registroC430.setCod_mot_rest_compl("");
            registroC430.setQuant_conv("");
            registroC430.setUnid("");
            registroC430.setVl_unit_conv("");
            registroC430.setVl_unit_icms_na_operacao_conv("");
            registroC430.setVl_unit_icms_op_conv("");
            registroC430.setVl_unit_icms_op_estoque_conv("");
            registroC430.setVl_unit_icms_st_estoque_conv("");
            registroC430.setVl_unit_fcp_icms_st_estoque_conv("");
            registroC430.setVl_unit_icms_st_conv_rest("");
            registroC430.setVl_unit_fcp_st_conv_rest("");
            registroC430.setVl_unit_icms_st_conv_compl("");
            registroC430.setVl_unit_fcp_st_conv_compl("");
            registroC430.setCst_icms("");
            registroC430.setCfop("");

            registroC420.getRegistroC425().add(registroC425);
            registroC405.getRegistroC420().add(registroC420);

//====================================================================
            //Documento Fiscal Emitido por ECF (código 02, 2D e 60)
            RegistroC460 registroC460 = new RegistroC460();
            registroC460.setCod_mod("");
            registroC460.setCod_sit("");
            registroC460.setNum_doc("");
            registroC460.setDt_doc("");
            registroC460.setVl_doc("");

            //Complemento do Cupom Fiscal Eletrônico Emitido por ECF - CF-e-ECF (código 60)
            RegistroC465 registroC465 = new RegistroC465();
            registroC465.setChv_cfe("");
            registroC465.setNum_ccf("");

            //Itens do Documento Fiscal Emitido por ECF (código 02 e 2D)
            RegistroC470 registroC470 = new RegistroC470();
            registroC470.setCod_item("");
            registroC470.setQtd("");
            registroC470.setUnid("");
            registroC470.setVl_item("");
            registroC470.setCst_icms("");
            registroC470.setCfop("");
            registroC470.setAliq_icms("");
            registroC470.setVl_pis("");
            registroC470.setVl_cofins("");

            //Informações complementares das operações de saída de mercadorias sujeitas à substituição tributária (código 02, 2D e 60)
            RegistroC480 registroC480 = new RegistroC480();
            registroC480.setCod_mot_rest_compl("");
            registroC480.setQuant_conv("");
            registroC480.setUnid("");
            registroC480.setVl_unit_conv("");
            registroC480.setVl_unit_icms_na_operacao_conv("");
            registroC480.setVl_unit_icms_op_conv("");
            registroC480.setVl_unit_icms_op_estoque_conv("");
            registroC480.setVl_unit_icms_st_estoque_conv("");
            registroC480.setVl_unit_fcp_icms_st_estoque_conv("");
            registroC480.setVl_unit_icms_st_conv_rest("");
            registroC480.setVl_unit_fcp_st_conv_rest("");
            registroC480.setVl_unit_icms_st_conv_compl("");
            registroC480.setVl_unit_fcp_st_conv_compl("");
            registroC480.setCst_icms("0");
            registroC480.setCfop("0");

            registroC460.getRegistroC470().add(registroC470);
            registroC405.getRegistroC460().add(registroC460);

//====================================================================
            //Registro Analítico do movimento diário (código 02, 2D e 60)
            RegistroC490 registroC490 = new RegistroC490();
            registroC490.setCst_icms("");
            registroC490.setCfop("");
            registroC490.setAliq_icms("");
            registroC490.setVl_opr("");
            registroC490.setVl_bc_icms("");
            registroC490.setVl_icms("");
            registroC405.getRegistroC490().add(registroC490);

            registroC400.getRegistroC405().add(registroC405);

            blocoC.getRegistroC400().add(registroC400);
        }
        return blocoC;
    }

    //Resumo Mensal de Itens do ECF por Estabelecimento (código 02 e 2D e 2E)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC495(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (emiteBlocoC495.isSelected()) {

            RegistroC495 registroC495 = new RegistroC495();
            registroC495.setAliq_icms("");
            registroC495.setCod_item("");
            registroC495.setQtd("");
            registroC495.setQtd_canc("");
            registroC495.setUnid("");
            registroC495.setVl_item("");
            registroC495.setVl_desc("");
            registroC495.setVl_canc("");
            registroC495.setVl_acmo("");
            registroC495.setVl_bc_icms("");
            registroC495.setVl_icms("");
            registroC495.setVl_isen("");
            registroC495.setVl_nt("");
            registroC495.setVl_icms_st("");

            blocoC.getRegistroC495().add(registroC495);
        }
        return blocoC;
    }

    //Nota Fiscal/Conta de Energia Elétrica (código 06), Nota Fiscal de Energia Elétrica Eletrônica (código 66) , Nota Fiscal/Conta de fornecimento dágua canalizada (código 29) e Nota Fiscal/Consumo Fornecimento de Gás (Código 28)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC500(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (emiteBlocoC500.isSelected()) {

            RegistroC500 registroC500 = new RegistroC500();
            registroC500.setInd_oper("");
            registroC500.setInd_emit("");
            registroC500.setCod_part("");
            registroC500.setCod_mod("");
            registroC500.setCod_sit("");
            registroC500.setSer("");
            registroC500.setSub("");
            registroC500.setCod_cons("");
            registroC500.setNum_doc("");
            registroC500.setDt_doc("");
            registroC500.setDt_e_s("");
            registroC500.setVl_doc("");
            registroC500.setVl_desc("");
            registroC500.setVl_forn("");
            registroC500.setVl_serv_nt("");
            registroC500.setVl_terc("");
            registroC500.setVl_da("");
            registroC500.setVl_bc_icms("");
            registroC500.setVl_icms("");
            registroC500.setVl_bc_icms_st("");
            registroC500.setVl_icms_st("");
            registroC500.setCod_inf("");
            registroC500.setVl_pis("");
            registroC500.setVl_cofins("");
            registroC500.setTp_ligacao("");
            registroC500.setCod_grupo_tensao("");
            registroC500.setChv_doc_e("");
            registroC500.setFin_doc_e("");
            registroC500.setChv_doc_e_ref("");
            registroC500.setInd_dest("");
            registroC500.setCod_mun_dest("");
            registroC500.setCod_cta("");

            //Itens do Documento - Nota Fiscal/Conta de Energia Elétrica (código 06), Nota Fiscal/Conta de fornecimento d'água canalizada (código 29) e Nota Fiscal/Conta Fornecimento de Gás (Código 28)
            RegistroC510 registroC510 = new RegistroC510();
            registroC510.setNum_item("");
            registroC510.setCod_item("");
            registroC510.setCod_class("");
            registroC510.setQtd("");
            registroC510.setUnid("");
            registroC510.setVl_item("");
            registroC510.setVl_desc("");
            registroC510.setCst_icms("");
            registroC510.setCfop("");
            registroC510.setVl_bc_icms("");
            registroC510.setAliq_icms("");
            registroC510.setVl_icms("");
            registroC510.setVl_bc_icms_st("");
            registroC510.setAliq_st("");
            registroC510.setVl_icms_st("");
            registroC510.setInd_rec("");
            registroC510.setCod_part("");
            registroC510.setVl_pis("");
            registroC510.setVl_cofins("");
            registroC510.setCod_cta("");
            registroC500.getRegistroC510().add(registroC510);

            //Registro Analítico do Documento - Nota Fiscal/Conta de Energia Elétrica (código 06), Nota Fiscal de Energia Elétrica Eletrônica (código 66), Nota Fiscal/Conta de fornecimento d'água canalizada (código 29) e Nota Fiscal/Conta Fornecimento de Gás (Código 28)
            RegistroC590 registroC590 = new RegistroC590();
            registroC590.setCst_icms("");
            registroC590.setCfop("");
            registroC590.setAliq_icms("");
            registroC590.setVl_opr("");
            registroC590.setVl_bc_icms("");
            registroC590.setVl_icms("");
            registroC590.setVl_bc_icms_st("");
            registroC590.setVl_icms_st("");
            registroC590.setVl_red_bc("");
            registroC590.setCod_obs("");

            //Informações do Fundo de Combate à Pobreza – FCP na NF3e (código 66)
            RegistroC591 registroC591 = new RegistroC591();
            registroC591.setVl_fcp_op("");
            registroC591.setVl_fcp_st("");
            registroC500.getRegistroC590().add(registroC590);

            //Observações do Lançamento Fiscal (códigos 06, 28, 29 e 66)
            RegistroC595 registroC595 = new RegistroC595();
            registroC595.setTxt_compl("");
            registroC595.setCod_obs("");

            //Outras obrigações tributárias, ajustes e informações de valores provenientes de documento fiscal
            RegistroC597 registroC597 = new RegistroC597();
            registroC597.setAliq_icms("");
            registroC597.setCod_aj("");
            registroC597.setCod_item("");
            registroC597.setDescr_compl_aj("");
            registroC597.setVl_bc_icms("");
            registroC597.setVl_icms("");
            registroC597.setVl_outros("");
            registroC595.getRegistroC597().add(registroC597);

            registroC500.getRegistroC595().add(registroC595);

            blocoC.getRegistroC500().add(registroC500);
        }
        return blocoC;
    }

    //Consolidação Diária de Notas Fiscais/Contas de Energia Elétrica (Código 06), Nota Fiscal/Conta de Fornecimento d´água (código 29) e Nota Fiscal/Conta de Fornecimento de Gás (Código 28) - (Empresas não obrigadas ao Convênio ICMS 115/03)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC600(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (emiteBlocoC600.isSelected()) {

            RegistroC600 registroC600 = new RegistroC600();
            registroC600.setCod_mod("");
            registroC600.setCod_mun("");
            registroC600.setSer("");
            registroC600.setSub("");
            registroC600.setCod_cons("");
            registroC600.setQtd_cons("");
            registroC600.setQtd_canc("");
            registroC600.setDt_doc("");
            registroC600.setVl_doc("");
            registroC600.setVl_desc("");
            registroC600.setCons("");
            registroC600.setVl_forn("");
            registroC600.setVl_serv_nt("");
            registroC600.setVl_terc("");
            registroC600.setVl_da("");
            registroC600.setVl_bc_icms("");
            registroC600.setVl_icms("");
            registroC600.setVl_bc_icms_st("");
            registroC600.setVl_icms_st("");
            registroC600.setVl_pis("");
            registroC600.setVl_cofins("");

            //Documentos cancelados - Consolidação diária de notas fiscais/conta de energia elétrica (Código 06), nota fiscal/conta de fornecimento de água (código 29) e nota fiscal/conta de fornecimento de gás (código 28)
            RegistroC601 registroC601 = new RegistroC601();
            registroC601.setNum_doc_canc("");
            registroC600.getRegistroC601().add(registroC601);

            //Itens do Documento Consolidado - Notas Fiscais/Contas de Energia Elétrica (Código 06), Nota Fiscal/Conta de Fornecimento d´água (código 29) e Nota Fiscal/Conta de Fornecimento de Gás (Código 28) - (Empresas não obrigadas ao Convênio ICMS 115/03)
            RegistroC610 registroC610 = new RegistroC610();
            registroC610.setCod_class("");
            registroC610.setCod_item("");
            registroC610.setQtd("");
            registroC610.setUnid("");
            registroC610.setVl_item("");
            registroC610.setVl_desc("");
            registroC610.setCst_icms("");
            registroC610.setCfop("");
            registroC610.setAliq_icms("");
            registroC610.setVl_bc_icms("");
            registroC610.setVl_icms("");
            registroC610.setVl_bc_icms_st("");
            registroC610.setVl_icms_st("");
            registroC610.setVl_pis("");
            registroC610.setVl_cofins("");
            registroC610.setCod_cta("");
            registroC600.getRegistroC610().add(registroC610);

            //Registro Analítico dos Documentos - Notas Fiscais/Contas de Energia Elétrica (Código 06), Nota Fiscal/Conta de Fornecimento d´água (código 29) e Nota Fiscal/Conta de Fornecimento de Gás (Código 28)
            RegistroC690 registroC690 = new RegistroC690();
            registroC690.setCst_icms("");
            registroC690.setCfop("");
            registroC690.setAliq_icms("");
            registroC690.setVl_opr("");
            registroC690.setVl_bc_icms("");
            registroC690.setVl_icms("");
            registroC690.setVl_red_bc("");
            registroC690.setVl_bc_icms_st("");
            registroC690.setVl_icms_st("");
            registroC690.setCod_obs("");
            registroC600.getRegistroC690().add(registroC690);

            blocoC.getRegistroC600().add(registroC600);
        }
        return blocoC;
    }

    //Consolidação dos Documentos Nota Fiscal/Conta Energia Elétrica (código 06) emitidas em via única - (Empresas obrigadas à entrega do arquivo previsto no Convênio ICMS 115/03), Nota Fiscal/Conta de Fornecimento de Gás Canalizado (Código 28) e Nota Fiscal de Energia Elétrica Eletrônica (código 66)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC700(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (emiteBlocoC700.isSelected()) {

            RegistroC700 registroC700 = new RegistroC700();
            registroC700.setCod_mod("");
            registroC700.setSer("");
            registroC700.setNro_ord_ini("");
            registroC700.setNro_ord_fin("");
            registroC700.setDt_doc_ini("");
            registroC700.setDt_doc_fin("");
            registroC700.setNom_mest("");
            registroC700.setChv_cod_dig("");

            //Registro Analítico dos Documentos (Códigos 06, 28 e 66)
            RegistroC790 registroC790 = new RegistroC790();
            registroC790.setCst_icms("");
            registroC790.setCfop("");
            registroC790.setAliq_icms("");
            registroC790.setVl_opr("");
            registroC790.setVl_bc_icms("");
            registroC790.setVl_icms("");
            registroC790.setVl_bc_icms_st("");
            registroC790.setVl_icms_st("");
            registroC790.setVl_red_bc("");
            registroC790.setCod_obs("");

            //Registro de Informações de ICMS ST por UF (Código 06 e 66)
            RegistroC791 registroC791 = new RegistroC791();
            registroC791.setUf("");
            registroC791.setVl_bc_icms_st("");
            registroC791.setVl_icms_st("");
            registroC790.getRegistroC791().add(registroC791);

            registroC700.getRegistroC790().add(registroC790);

            blocoC.getRegistroC700().add(registroC700);
        }
        return blocoC;
    }

    //Registro Cupom Fiscal Eletrônico - CF-e (Código 59)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC800(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (emiteBlocoC800.isSelected()) {

            RegistroC800 registroC800 = new RegistroC800();
            registroC800.setCod_mod("");
            registroC800.setCod_sit("");
            registroC800.setNum_cfe("");
            registroC800.setDt_doc("");
            registroC800.setVl_cfe("");
            registroC800.setVl_pis("");
            registroC800.setVl_cofins("");
            registroC800.setCnpj_cpf("");
            registroC800.setNr_sat("");
            registroC800.setChv_cfe("");
            registroC800.setVl_desc("");
            registroC800.setVl_merc("");
            registroC800.setVl_out_da("");
            registroC800.setVl_icms("");
            registroC800.setVl_pis_st("");
            registroC800.setVl_cofins_st("");

            //Itens do documento do cupom fiscal eletrônico – SAT (CF-E-SAT) (código 59)
            RegistroC810 registroC810 = new RegistroC810();
            registroC810.setNum_item("");
            registroC810.setCod_item("");
            registroC810.setQtd("");
            registroC810.setUnid("");
            registroC810.setVl_item("");
            registroC810.setCst_icms("");
            registroC810.setCfop("");

            //Informações complementares das operações de saída de mercadorias sujeitas à substituição tributária (CF-E-SAT) (código 59)
            RegistroC815 registroC815 = new RegistroC815();
            registroC815.setCod_mot_rest_compl("");
            registroC815.setQuant_conv("");
            registroC815.setUnid("");
            registroC815.setVl_unit_conv("");
            registroC815.setVl_unit_icms_na_operacao_conv("");
            registroC815.setVl_unit_icms_op_conv("");
            registroC815.setVl_unit_icms_op_estoque_conv("");
            registroC815.setVl_unit_icms_st_estoque_conv("");
            registroC815.setVl_unit_fcp_icms_st_estoque_conv("");
            registroC815.setVl_unit_icms_st_conv_rest("");
            registroC815.setVl_unit_fcp_st_conv_rest("");
            registroC815.setVl_unit_icms_st_conv_compl("");
            registroC815.setVl_unit_fcp_st_conv_compl("");
            registroC800.getRegistroC810().add(registroC810);

            //Registro Analítico do CF-e (Código 59)
            RegistroC850 registroC50 = new RegistroC850();
            registroC50.setCst_icms("");
            registroC50.setCfop("");
            registroC50.setAliq_icms("");
            registroC50.setVl_opr("");
            registroC50.setVl_bc_icms("");
            registroC50.setVl_icms("");
            registroC50.setCod_obs("");
            registroC800.getRegistroC850().add(registroC50);

            blocoC.getRegistroC800().add(registroC800);
        }
        return blocoC;
    }

    //Identificação do equipamento SAT-CF-e (Código 59)
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC860(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        if (emiteBlocoC860.isSelected()) {

            RegistroC860 registroC860 = new RegistroC860();
            registroC860.setCod_mod("");
            registroC860.setNr_sat("");
            registroC860.setDt_doc("");
            registroC860.setDoc_ini("");
            registroC860.setDoc_fim("");

            //Itens do documento do cupom fiscal eletrônico – SAT (CF-E-SAT) (código 59)
            RegistroC870 registroC870 = new RegistroC870();
            registroC870.setCod_item("");
            registroC870.setQtd("");
            registroC870.setUnid("");
            registroC870.setCst_icms("");
            registroC870.setCfop("");

            //Informações complementares das operações de saída de mercadorias sujeitas à substituição tributária (CF-E-SAT) (código 59)
            RegistroC880 registroC880 = new RegistroC880();
            registroC880.setCod_mot_rest_compl("");
            registroC880.setQuant_conv("");
            registroC880.setUnid("");
            registroC880.setVl_unit_conv("");
            registroC880.setVl_unit_icms_na_operacao_conv("");
            registroC880.setVl_unit_icms_op_conv("");
            registroC880.setVl_unit_icms_op_estoque_conv("");
            registroC880.setVl_unit_icms_st_estoque_conv("");
            registroC880.setVl_unit_fcp_icms_st_estoque_conv("");
            registroC880.setVl_unit_icms_st_conv_rest("");
            registroC880.setVl_unit_fcp_st_conv_rest("");
            registroC880.setVl_unit_icms_st_conv_compl("");
            registroC880.setVl_unit_fcp_st_conv_compl("");
            registroC860.getRegistroC870().add(registroC870);

            //Resumo diário de CF-e (Código 59) por equipamento SAT-CF-e
            RegistroC890 registroC890 = new RegistroC890();
            registroC890.setCst_icms("");
            registroC890.setCfop("");
            registroC890.setAliq_icms("");
            registroC890.setVl_opr("");
            registroC890.setVl_bc_icms("");
            registroC890.setVl_icms("");
            registroC890.setCod_obs("");
            registroC860.getRegistroC890().add(registroC890);

            blocoC.getRegistroC860().add(registroC860);
        }
        return blocoC;
    }

    //Encerramento do Bloco C
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC preencheRegistroC990(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.BlocoC blocoC) {

        RegistroC990 registroC990 = new RegistroC990();
        registroC990.setQtd_lin_c("");

        return blocoC;
    }

    private static void verificaEmitente(String emit) {

        ConexaoBD conectaEmpresa1 = new ConexaoBD();
        conectaEmpresa1.conecta();

        try {
            ResultSet rsEmp1;
            try ( PreparedStatement ps1 = conectaEmpresa1.conexao.prepareStatement("Select codEmpresa,cpfCnpj from con_for01 where cpfCnpj = ?")) {
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
