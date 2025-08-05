package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1001;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1010;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1100;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1105;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1110;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1200;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1210;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1250;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1255;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1300;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1310;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1320;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1350;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1360;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1370;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1390;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1391;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1400;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1500;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1510;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1600;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1700;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1710;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1800;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1900;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1910;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1920;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1921;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1922;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1923;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1925;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1926;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1960;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1970;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1975;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1980;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1990;

/**
 * @author Alyesson Sousa
 *
 */
public class Bloco1 {

    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Bloco1 preencheBloco1() {
        br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Bloco1 bloco1 = new br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Bloco1();
        bloco1 = preencheRegistro1001(bloco1);
        bloco1 = preencheRegistro1010(bloco1);
        bloco1 = preencheRegistro1100(bloco1);
        bloco1 = preencheRegistro1200(bloco1);
        bloco1 = preencheRegistro1250(bloco1);
        bloco1 = preencheRegistro1300(bloco1);
        bloco1 = preencheRegistro1350(bloco1);
        bloco1 = preencheRegistro1390(bloco1);
        bloco1 = preencheRegistro1400(bloco1);
        bloco1 = preencheRegistro1500(bloco1);
        bloco1 = preencheRegistro1600(bloco1);
        bloco1 = preencheRegistro1700(bloco1);
        bloco1 = preencheRegistro1800(bloco1);
        bloco1 = preencheRegistro1900(bloco1);
        bloco1 = preencheRegistro1960(bloco1);
        bloco1 = preencheRegistro1970(bloco1);
        bloco1 = preencheRegistro1980(bloco1);
        bloco1 = preencheRegistro1990(bloco1);

        return bloco1;
    }

    private static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Bloco1 preencheRegistro1001(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Bloco1 bloco1) {

        Registro1001 registro1001 = new Registro1001();
        registro1001.setInd_mov("0");

        bloco1.setRegistro1001(registro1001);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1010(Bloco1 bloco1) {

        Registro1010 registro1010 = new Registro1010();
        registro1010.setInd_aer("N");
        registro1010.setInd_cart("N");
        registro1010.setInd_ccrf("N");
        registro1010.setInd_comb("N");
        registro1010.setInd_ee("N");
        registro1010.setInd_exp("N");
        registro1010.setInd_form("N");
        registro1010.setInd_giaf1("N");
        registro1010.setInd_giaf3("N");
        registro1010.setInd_giaf4("N");
        registro1010.setInd_usina("N");
        registro1010.setInd_rest_ressarc_compl_icms("N");
        registro1010.setInd_va("N");
        //bloco1.setRegistro1010(registro1010);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1100(Bloco1 bloco1) {

        Registro1100 registro1100 = new Registro1100();
        registro1100.setInd_doc("");
        registro1100.setNro_de("");
        registro1100.setDt_de("");
        registro1100.setNat_exp("");
        registro1100.setNro_re("");
        registro1100.setDt_re("");
        registro1100.setChc_emb("");
        registro1100.setDt_chc("");
        registro1100.setDt_avb("");
        registro1100.setInd_doc("");
        registro1100.setTp_chc("");
        registro1100.setPais("Brasil");

        Registro1105 registro1105 = new Registro1105();
        registro1105.setCod_mod("");
        registro1105.setSerie("");
        registro1105.setNum_doc("");
        registro1105.setChv_nfe("");
        registro1105.setDt_doc("");
        registro1105.setCod_item("");

        Registro1110 registro1110 = new Registro1110();
        registro1110.setCod_mod("");
        registro1110.setCod_part("");
        registro1110.setSer("");
        registro1110.setNum_doc("");
        registro1110.setDt_doc("");
        registro1110.setChv_nfe("");
        registro1110.setNr_memo("");
        registro1110.setQtd("");
        registro1110.setUnid("");
        registro1105.getRegistro1110().add(registro1110);

        registro1100.getRegistro1105().add(registro1105);

        bloco1.getRegistro1100().add(registro1100);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1200(Bloco1 bloco1) {

        Registro1200 registro1200 = new Registro1200();
        registro1200.setCod_aj_apur("");
        registro1200.setSld_cred("");
        registro1200.setCred_apr("");
        registro1200.setCred_receb("");
        registro1200.setCred_util("");
        registro1200.setSld_cred_fim("");
        
        Registro1210 registro1210 = new Registro1210();
        registro1210.setTipo_util("");
        registro1210.setNr_doc("");
        registro1210.setVl_cred_util("");
        registro1210.setChv_doce("");
        registro1200.getRegistro1210().add(registro1210);
        
        bloco1.getRegistro1200().add(registro1200);
        
        return bloco1;
    }

    private static Bloco1 preencheRegistro1250(Bloco1 bloco1) {

        Registro1250 registro1250 = new Registro1250();
        registro1250.setVl_credito_icms_op("");
        registro1250.setVl_icms_st_rest("");
        registro1250.setVl_fcp_st_rest("");
        registro1250.setVl_icms_st_compl("");
        registro1250.setVl_fcp_st_compl("");
        
        Registro1255 registro1255 = new Registro1255();
        registro1255.setCod_mot_rest_compl("");
        registro1255.setVl_credito_icms_op_mot("");
        registro1255.setVl_icms_st_rest_mot("");
        registro1255.setVl_fcp_st_rest_mot("");
        registro1255.setVl_icms_st_compl_mot("");
        registro1255.setVl_fcp_st_compl_mot("");
        registro1250.getRegistro1255().add(registro1255);
        
        bloco1.setRegistro1250(registro1250);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1300(Bloco1 bloco1) {

        Registro1300 registro1300 = new Registro1300();
        registro1300.setCod_item("");
        registro1300.setDt_fech("");
        registro1300.setEstq_abert("");
        registro1300.setVol_entr("");
        registro1300.setVol_disp("");
        registro1300.setVol_saidas("");
        registro1300.setEstq_escr("");
        registro1300.setVal_aj_perda("");
        registro1300.setVal_aj_ganho("");
        registro1300.setFech_fisico("");
        
        Registro1310 registro1310 = new Registro1310();
        registro1310.setNum_tanque("");
        registro1310.setEstq_abert("");
        registro1310.setVol_entr("");
        registro1310.setVol_disp("");
        registro1310.setVol_saidas("");
        registro1310.setEstq_escr("");
        registro1310.setVal_aj_perda("");
        registro1310.setVal_aj_ganho("");
        registro1310.setFech_fisico("");
        
        Registro1320 registro1320 = new Registro1320();
        registro1320.setNum_bico("");
        registro1320.setNr_interv("");
        registro1320.setMot_interv("");
        registro1320.setNom_interv("");
        registro1320.setCnpj_interv("");
        registro1320.setCpf_interv("");
        registro1320.setVal_fecha("");
        registro1320.setVal_abert("");
        registro1320.setVol_aferi("");
        registro1320.setVol_vendas("");
        registro1310.getRegistro1320().add(registro1320);
        
        registro1300.getRegistro1310().add(registro1310);
        
        bloco1.getRegistro1300().add(registro1300);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1350(Bloco1 bloco1) {

        Registro1350 registro1350 = new Registro1350();
        registro1350.setSerie("");
        registro1350.setFabricante("");
        registro1350.setModelo("");
        registro1350.setTipo_medicao("");
        
        Registro1360 registro1360 = new Registro1360();
        registro1360.setNum_lacre("");
        registro1360.setDt_aplicacao("");
        registro1350.getRegistro1360().add(registro1360);
        
        Registro1370 registro1370 = new Registro1370();
        registro1370.setNum_bico("");
        registro1370.setCod_item("");
        registro1370.setNum_tanque("");
        registro1350.getRegistro1370().add(registro1370);
        
        bloco1.getRegistro1350().add(registro1350);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1390(Bloco1 bloco1) {

        Registro1390 registro1390 = new Registro1390();
        registro1390.setCod_prod("");
        
        Registro1391 registro1391 = new Registro1391();
        registro1391.setDt_registro("");
        registro1391.setQtd_moid("");
        registro1391.setEstq_ini("");
        registro1391.setQtd_produz("");
        registro1391.setEnt_anid_hid("");
        registro1391.setOutr_entr("");
        registro1391.setPerda("");
        registro1391.setCons("");
        registro1391.setSai_ani_hid("");
        registro1391.setSaidas("");
        registro1391.setEstq_fin("");
        registro1391.setEstq_ini_mel("");
        registro1391.setProd_dia_mel("");
        registro1391.setUtil_mel("");
        registro1391.setProd_alc_mel("");
        registro1391.setObs("");
        registro1391.setCod_item("");
        registro1391.setTp_residuo("");
        registro1391.setQtd_residuo("");
        registro1390.getRegistro1391().add(registro1391);
        
        bloco1.getRegistro1390().add(registro1390);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1400(Bloco1 bloco1) {

        Registro1400 registro1400 = new Registro1400();
        registro1400.setCod_item_ipm("");
        registro1400.setMun("");
        registro1400.setValor("");
        
        bloco1.getRegistro1400().add(registro1400);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1500(Bloco1 bloco1) {

        Registro1500 registro1500 = new Registro1500();
        registro1500.setInd_oper("");
        registro1500.setInd_emit("");
        registro1500.setCod_part("");
        registro1500.setCod_mod("");
        registro1500.setCod_sit("");
        registro1500.setSer("");
        registro1500.setSub("");
        registro1500.setCod_cons("");
        registro1500.setNum_doc("");
        registro1500.setDt_doc("");
        registro1500.setDt_e_s("");
        registro1500.setVl_doc("");
        registro1500.setVl_desc("");
        registro1500.setVl_forn("");
        registro1500.setVl_serv_nt("");
        registro1500.setVl_terc("");
        registro1500.setVl_da("");
        registro1500.setVl_bc_icms("");
        registro1500.setVl_icms("");
        registro1500.setVl_bc_icms_st("");
        registro1500.setVl_icms_st("");
        registro1500.setCod_inf("");
        registro1500.setVl_pis("");
        registro1500.setVl_cofins("");
        registro1500.setTp_ligacao("");
        registro1500.setCod_grupo_tensao("");
        
        Registro1510 registro1510 = new Registro1510();
        registro1510.setNum_item("");
        registro1510.setCod_item("");
        registro1510.setCod_class("");
        registro1510.setQtd("");
        registro1510.setUnid("");
        registro1510.setVl_item("");
        registro1510.setCst_icms("");
        registro1510.setCfop("");
        registro1510.setVl_bc_icms("");
        registro1510.setAliq_icms("");
        registro1510.setVl_icms("");
        registro1510.setVl_bc_icms_st("");
        registro1510.setAliq_st("");
        registro1510.setVl_icms_st("");
        registro1510.setInd_rec("");
        registro1510.setCod_part("");
        registro1510.setVl_pis("");
        registro1510.setVl_cofins("");
        registro1510.setCod_cta("");
        registro1500.getRegistro1510().add(registro1510);
        
        bloco1.getRegistro1500().add(registro1500);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1600(Bloco1 bloco1) {

        Registro1600 registro1600 = new Registro1600();
        registro1600.setCod_part("");
        registro1600.setTot_credito("");
        registro1600.setTot_debito("");
        
        bloco1.getRegistro1600().add(registro1600);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1700(Bloco1 bloco1) {

        Registro1700 registro1700 = new Registro1700();
        registro1700.setCod_disp("");
        registro1700.setCod_mod("");
        registro1700.setSer("");
        registro1700.setSub("");
        registro1700.setNum_doc_ini("");
        registro1700.setNum_doc_fin("");
        registro1700.setNum_aut("");
        
        Registro1710 registro1710 = new Registro1710();
        registro1710.setNum_doc_ini("");
        registro1710.setNum_doc_fin("");
        registro1700.getRegistro1710().add(registro1710);
        
        bloco1.getRegistro1700().add(registro1700);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1800(Bloco1 bloco1) {

        Registro1800 registro1800 = new Registro1800();
        registro1800.setVl_carga("");
        registro1800.setVl_fat("");
        registro1800.setVl_pass("");
        registro1800.setInd_rat("");
        registro1800.setVl_icms_ant("");
        registro1800.setVl_bc_icms("");
        registro1800.setVl_icms_apur("");
        registro1800.setVl_bc_icms_apur("");
        registro1800.setVl_dif("");
        
        bloco1.setRegistro1800(registro1800);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1900(Bloco1 bloco1) {

        Registro1900 registro1900 = new Registro1900();
        registro1900.setInd_apur_icms("");
        registro1900.setDescr_compl_out_apur("");
        
        Registro1910 registro1910 = new Registro1910();
        registro1910.setDt_ini("");
        registro1910.setDt_fin("");
        
        Registro1920 registro1920 = new Registro1920();
        registro1920.setVl_tot_transf_debitos_oa("");
        registro1920.setVl_tot_aj_debitos_oa("");
        registro1920.setVl_estornos_cred_oa("");
        registro1920.setVl_tot_transf_creditos_oa("");
        registro1920.setVl_tot_aj_creditos_oa("");
        registro1920.setVl_estornos_deb_oa("");
        registro1920.setVl_sld_credor_ant_oa("");
        registro1920.setVl_sld_apurado_oa("");
        registro1920.setVl_tot_ded("");
        registro1920.setVl_icms_recolher_oa("");
        registro1920.setVl_sld_credor_transp_oa("");
        registro1920.setDeb_esp_oa("");
        
        Registro1921 registro1921 = new Registro1921();
        registro1921.setCod_aj_apur("");
        registro1921.setDescr_compl_aj("");
        registro1921.setVl_aj_apur("");
        
        Registro1922 registro1922 = new Registro1922();
        registro1922.setNum_da("");
        registro1922.setNum_proc("");
        registro1922.setInd_proc("");
        registro1922.setProc("");
        registro1922.setTxt_compl("");
        registro1921.getRegistro1922().add(registro1922);
        
        Registro1923 registro1923 = new Registro1923();
        registro1923.setCod_part("");
        registro1923.setCod_mod("");
        registro1923.setSer("");
        registro1923.setSub("");
        registro1923.setNum_doc("");
        registro1923.setDt_doc("");
        registro1923.setCod_item("");
        registro1923.setVl_aj_item("");
        registro1923.setChv_doce("");
        registro1921.getRegistro1923().add(registro1923);
        registro1920.getRegistro1921().add(registro1921);
        
        Registro1925 registro1925 = new Registro1925();
        registro1925.setCod_inf_adic("");
        registro1925.setVl_inf_adic("");
        registro1925.setDescr_compl_aj("");
        registro1920.getRegistro1925().add(registro1925);
                
        Registro1926 registro1926 = new Registro1926();
        registro1926.setCod_or("");
        registro1926.setVl_or("");
        registro1926.setDt_vcto("");
        registro1926.setCod_rec("");
        registro1926.setNum_proc("");
        registro1926.setInd_proc("");
        registro1926.setProc("");
        registro1926.setTxt_compl("");
        registro1926.setMes_ref("");
        registro1920.getRegistro1926().add(registro1926);
        
        registro1910.setRegistro1920(registro1920);
        registro1900.getRegistro1910().add(registro1910);
        
        bloco1.getRegistro1900().add(registro1900);
        
        return bloco1;
    }

    private static Bloco1 preencheRegistro1960(Bloco1 bloco1) {

        Registro1960 registro1960 = new Registro1960();
        registro1960.setInd_ap("111");
        registro1960.setG1_01("222");
        registro1960.setG1_02("3333");
        registro1960.setG1_03("444");
        registro1960.setG1_04("5555");
        registro1960.setG1_05("68954");
        registro1960.setG1_06("002");
        registro1960.setG1_07("5454");
        registro1960.setG1_08("4222");
        registro1960.setG1_09("4444");
        registro1960.setG1_10("335");
        registro1960.setG1_11("3020");
        
        bloco1.getRegistro1960().add(registro1960);
       
        return bloco1;
    }

    private static Bloco1 preencheRegistro1970(Bloco1 bloco1) {

        Registro1970 registro1970 = new Registro1970();
        registro1970.setInd_ap("111");
        registro1970.setG3_01("2002");
        registro1970.setG3_02("0222");
        registro1970.setG3_03("10111");
        registro1970.setG3_04("2544");
        registro1970.setG3_05("0");
        registro1970.setG3_06("0");
        registro1970.setG3_07("2555");
        registro1970.setG3_08("1110");
        registro1970.setG3_09("2232");
        
        Registro1975 registro1975 = new Registro1975();
        registro1975.setAliq_imp_base("33");
        registro1975.setG3_10("445");
        registro1975.setG3_11("3325");
        registro1975.setG3_11("8444");

        bloco1.getRegistro1970().add(registro1970);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1980(Bloco1 bloco1) {

        Registro1980 registro1980 = new Registro1980();
        registro1980.setInd_ap("");
        registro1980.setG4_01("");
        registro1980.setG4_02("");
        registro1980.setG4_03("");
        registro1980.setG4_04("");
        registro1980.setG4_05("");
        registro1980.setG4_06("");
        registro1980.setG4_07("");
        registro1980.setG4_08("");
        registro1980.setG4_09("");
        registro1980.setG4_10("");
        registro1980.setG4_11("");
        registro1980.setG4_12("");
        
        bloco1.setRegistro1980(registro1980);

        return bloco1;
    }

    private static Bloco1 preencheRegistro1990(Bloco1 bloco1) {

        Registro1990 registro1990 = new Registro1990();
        registro1990.setQtd_lin_1("");
        
        bloco1.setRegistro1990(registro1990);

        return bloco1;
    }
}
