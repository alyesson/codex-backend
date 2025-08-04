package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;

import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.dataAte;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.dataDe;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.indicadorMovimento;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE001;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE100;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE110;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE111;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE112;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE113;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE115;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE116;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE200;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE210;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE220;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE230;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE240;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE250;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE300;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE310;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE311;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE312;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE313;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE316;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE500;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE510;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE520;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE530;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE531;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE990;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import static br.com.codex.v1.domain.fiscal.spedefd.SpedEfd.naoEmiteBlocoE110;

/**
 * @author Alyesson Sousa
 *
 */
public class BlocoE {

    static LocalDate dataPeriodoDe = Instant.ofEpochMilli(dataDe.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    static LocalDate dataPeriodoAte = Instant.ofEpochMilli(dataAte.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

    static SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");

    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE preencheBlocoE() {
        br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE blocoE = new br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE();
        blocoE = preencheRegistroE001(blocoE);
        blocoE = preencheRegistroE100(blocoE);
        blocoE = preencheRegistroE200(blocoE);
        blocoE = preencheRegistroE300(blocoE);
        blocoE = preencheRegistroE500(blocoE);
        blocoE = preencheRegistroE990(blocoE);

        return blocoE;
    }

    /**
     * @param blocoE
     * @return
     */
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE preencheRegistroE001(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE blocoE) {
        RegistroE001 registroE001 = new RegistroE001();
        registroE001.setInd_mov(indicadorMovimento.getSelectedItem().toString());

        blocoE.setRegistroE001(registroE001);

        return blocoE;
    }

    /**
     * @param blocoE
     * @return
     */
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE preencheRegistroE100(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE blocoE) {
        RegistroE100 registroE100 = new RegistroE100();
        registroE100.setDt_ini(df.format(dataPeriodoDe));
        registroE100.setDt_fin(df.format(dataPeriodoAte));

        if (naoEmiteBlocoE110.isSelected()) {

            RegistroE110 registroE110 = new RegistroE110();
            registroE110.setDeb_esp("0");
            registroE110.setVl_aj_creditos("0");
            registroE110.setVl_aj_debitos("0");
            registroE110.setVl_estornos_cred("0");
            registroE110.setVl_estornos_deb("0");
            registroE110.setVl_icms_recolher("0");
            registroE110.setVl_sld_apurado("0");
            registroE110.setVl_sld_credor_ant("0");
            registroE110.setVl_sld_credor_transportar("0");
            registroE110.setVl_tot_aj_creditos("0");
            registroE110.setVl_tot_aj_debitos("0");
            registroE110.setVl_tot_creditos("0");
            registroE110.setVl_tot_debitos("0");
            registroE110.setVl_tot_ded("0");
            registroE100.setRegistroE110(registroE110);
            
            //fazer conexao aqui
            RegistroE111 registroE111 = new RegistroE111();
            registroE111.setCod_aj_apur("");
            registroE111.setDescr_compl_aj("");
            registroE111.setVl_aj_apur("");
            registroE110.getRegistroE111().add(registroE111);

            RegistroE112 registroE112 = new RegistroE112();
            registroE112.setNum_da("");
            registroE112.setNum_proc("");
            registroE112.setInd_proc("");
            registroE112.setProc("");
            registroE112.setTxt_compl("");
            registroE111.getRegistroE112().add(registroE112);

            RegistroE113 registroE113 = new RegistroE113();
            registroE113.setCod_part("");
            registroE113.setCod_mod("");
            registroE113.setSer("");
            registroE113.setSub("");
            registroE113.setNum_doc("");
            registroE113.setDt_doc("");
            registroE113.setCod_item("");
            registroE113.setVl_aj_item("");
            registroE113.setChv_doc_e("");
            registroE111.getRegistroE113().add(registroE113);

            RegistroE115 registroE115 = new RegistroE115();
            registroE115.setCod_inf_adic("");
            registroE115.setVl_inf_adic("");
            registroE115.setDescr_compl_aj("");
            registroE110.getRegistroE115().add(registroE115);

            RegistroE116 registroE116 = new RegistroE116();
            registroE116.setCod_or("");
            registroE116.setVl_or("");
            registroE116.setDt_vcto("");
            registroE116.setCod_rec("");
            registroE116.setNum_proc("");
            registroE116.setInd_proc("");
            registroE116.setProc("");
            registroE116.setTxt_compl("");
            registroE116.setMes_ref("");
            registroE110.getRegistroE116().add(registroE116);

            registroE100.setRegistroE110(registroE110);

            blocoE.getRegistroE100().add(registroE100);

        } else {
            //fazer conexao aqui
            RegistroE110 registroE110 = new RegistroE110();
            registroE110.setDeb_esp("0");
            registroE110.setVl_aj_creditos("0");
            registroE110.setVl_aj_debitos("0");
            registroE110.setVl_estornos_cred("0");
            registroE110.setVl_estornos_deb("0");
            registroE110.setVl_icms_recolher("0");
            registroE110.setVl_sld_apurado("0");
            registroE110.setVl_sld_credor_ant("0");
            registroE110.setVl_sld_credor_transportar("0");
            registroE110.setVl_tot_aj_creditos("0");
            registroE110.setVl_tot_aj_debitos("0");
            registroE110.setVl_tot_creditos("0");
            registroE110.setVl_tot_debitos("0");
            registroE110.setVl_tot_ded("0");
            registroE100.setRegistroE110(registroE110);

            RegistroE111 registroE111 = new RegistroE111();
            registroE111.setCod_aj_apur("");
            registroE111.setDescr_compl_aj("");
            registroE111.setVl_aj_apur("");
            registroE110.getRegistroE111().add(registroE111);

            RegistroE112 registroE112 = new RegistroE112();
            registroE112.setNum_da("");
            registroE112.setNum_proc("");
            registroE112.setInd_proc("");
            registroE112.setProc("");
            registroE112.setTxt_compl("");
            registroE111.getRegistroE112().add(registroE112);

            RegistroE113 registroE113 = new RegistroE113();
            registroE113.setCod_part("");
            registroE113.setCod_mod("");
            registroE113.setSer("");
            registroE113.setSub("");
            registroE113.setNum_doc("");
            registroE113.setDt_doc("");
            registroE113.setCod_item("");
            registroE113.setVl_aj_item("");
            registroE113.setChv_doc_e("");
            registroE111.getRegistroE113().add(registroE113);

            RegistroE115 registroE115 = new RegistroE115();
            registroE115.setCod_inf_adic("");
            registroE115.setVl_inf_adic("");
            registroE115.setDescr_compl_aj("");
            registroE110.getRegistroE115().add(registroE115);

            RegistroE116 registroE116 = new RegistroE116();
            registroE116.setCod_or("");
            registroE116.setVl_or("");
            registroE116.setDt_vcto("");
            registroE116.setCod_rec("");
            registroE116.setNum_proc("");
            registroE116.setInd_proc("");
            registroE116.setProc("");
            registroE116.setTxt_compl("");
            registroE116.setMes_ref("");
            registroE110.getRegistroE116().add(registroE116);

            registroE100.setRegistroE110(registroE110);

            blocoE.getRegistroE100().add(registroE100);
        }

        return blocoE;
    }

    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE preencheRegistroE200(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE blocoE) {

        RegistroE200 registroE200 = new RegistroE200();
        registroE200.setUf("");
        registroE200.setDt_ini("");
        registroE200.setDt_fin("");

        RegistroE210 registroE210 = new RegistroE210();
        registroE210.setInd_mov_st("");
        registroE210.setVl_sld_cred_ant_st("");
        registroE210.setVl_devol_st("");
        registroE210.setVl_ressarc_st("");
        registroE210.setVl_out_cred_st("");
        registroE210.setVl_aj_creditos_st("");
        registroE210.setVl_retencao_st("");
        registroE210.setVl_out_deb_st("");
        registroE210.setVl_aj_debitos_st("");
        registroE210.setVl_sld_dev_ant_st("");
        registroE210.setVl_deducoes_st("");
        registroE210.setVl_icms_recol_st("");
        registroE210.setVl_sld_cred_st_transportar("");
        registroE210.setDeb_esp_st("");
        registroE200.setRegistroE210(registroE210);

        RegistroE220 registroE220 = new RegistroE220();
        registroE220.setCod_aj_apur("");
        registroE220.setDescr_compl_aj("");
        registroE220.setVl_aj_apur("");

        RegistroE230 registroE230 = new RegistroE230();
        registroE230.setNum_da("");
        registroE230.setNum_proc("");
        registroE230.setInd_proc("");
        registroE230.setProc("");
        registroE230.setTxt_compl("");
        registroE220.getRegistroE230().add(registroE230);

        RegistroE240 registroE240 = new RegistroE240();
        registroE240.setCod_part("");
        registroE240.setCod_mod("");
        registroE240.setSer("");
        registroE240.setSub("");
        registroE240.setNum_doc("");
        registroE240.setDt_doc("");
        registroE240.setCod_item("");
        registroE240.setVl_aj_item("");
        registroE240.setChv_doc_e("");
        registroE220.getRegistroE240().add(registroE240);
        registroE210.getRegistroE220().add(registroE220);

        RegistroE250 registroE250 = new RegistroE250();
        registroE250.setCod_or("");
        registroE250.setVl_or("");
        registroE250.setDt_vcto("");
        registroE250.setCod_rec("");
        registroE250.setNum_proc("");
        registroE250.setInd_proc("");
        registroE250.setProc("");
        registroE250.setTxt_compl("");
        registroE250.setMes_ref("");
        registroE210.getRegistroE250().add(registroE250);

        blocoE.getRegistroE200().add(registroE200);

        return blocoE;
    }

    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE preencheRegistroE300(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE blocoE) {

        RegistroE300 registroE300 = new RegistroE300();
        registroE300.setUf("");
        registroE300.setDt_ini("");
        registroE300.setDt_fin("");

        RegistroE310 registroE310 = new RegistroE310();
        registroE310.setInd_mov_fcp_difal("");
        registroE310.setVl_sld_cred_ant_difal("");
        registroE310.setVl_tot_debitos_difal("");
        registroE310.setVl_out_deb_difal("");
        registroE310.setVl_tot_creditos_difal("");
        registroE310.setVl_out_cred_difal("");
        registroE310.setVl_sld_dev_ant_difal("");
        registroE310.setVl_deducoes_difal("");
        registroE310.setVl_recol_difal("");
        registroE310.setVl_sld_cred_transportar_difal("");
        registroE310.setDeb_esp_difal("");
        registroE310.setVl_sld_cred_ant_fcp("");
        registroE310.setVl_tot_deb_fcp("");
        registroE310.setVl_out_deb_fcp("");
        registroE310.setVl_tot_cred_fcp("");
        registroE310.setVl_out_cred_fcp("");
        registroE310.setVl_sld_dev_ant_fcp("");
        registroE310.setVl_deducoes_fcp("");
        registroE310.setVl_recol_fcp("");
        registroE310.setVl_sld_cred_transportar_fcp("");
        registroE310.setDeb_esp_fcp("");

        RegistroE311 registroE311 = new RegistroE311();
        registroE311.setCod_aj_apur("");
        registroE311.setDescr_compl_aj("");
        registroE311.setVl_aj_apur("");

        RegistroE312 registroE312 = new RegistroE312();
        registroE312.setNum_da("");
        registroE312.setNum_proc("");
        registroE312.setInd_proc("");
        registroE312.setProc("");
        registroE312.setTxt_compl("");
        registroE311.getRegistroE312().add(registroE312);

        RegistroE313 registroE313 = new RegistroE313();
        registroE313.setCod_part("");
        registroE313.setCod_mod("");
        registroE313.setSer("");
        registroE313.setSub("");
        registroE313.setNum_doc("");
        registroE313.setChv_doc_e("");
        registroE313.setDt_doc("");
        registroE313.setCod_item("");
        registroE313.setVl_aj_item("");
        registroE311.getRegistroE313().add(registroE313);
        registroE310.getRegistroE311().add(registroE311);

        RegistroE316 registroE316 = new RegistroE316();
        registroE316.setCod_or("");
        registroE316.setVl_or("");
        registroE316.setDt_vcto("");
        registroE316.setCod_rec("");
        registroE316.setNum_proc("");
        registroE316.setInd_proc("");
        registroE316.setProc("");
        registroE316.setTxt_compl("");
        registroE316.setMes_ref("");
        registroE310.getRegistroE316().add(registroE316);
        registroE300.setRegistroE310(registroE310);

        blocoE.getRegistroE300().add(registroE300);

        return blocoE;
    }

    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE preencheRegistroE500(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE blocoE) {

        RegistroE500 registroE500 = new RegistroE500();
        registroE500.setInd_apur("");
        registroE500.setDt_ini("");
        registroE500.setDt_fin("");

        RegistroE520 registroE520 = new RegistroE520();
        registroE520.setVl_sd_ant_ipi("");
        registroE520.setVl_deb_ipi("");
        registroE520.setVl_cred_ipi("");
        registroE520.setVl_od_ipi("");
        registroE520.setVl_oc_ipi("");
        registroE520.setVl_sc_ipi("");
        registroE520.setVl_sd_ipi("");

        RegistroE530 registroE530 = new RegistroE530();
        registroE530.setInd_aj("");
        registroE530.setVl_aj("");
        registroE530.setCod_aj("");
        registroE530.setInd_doc("");
        registroE530.setNum_doc("");
        registroE530.setDescr_aj("");

        RegistroE531 registroE531 = new RegistroE531();
        registroE531.setCod_part("");
        registroE531.setCod_mod("");
        registroE531.setSer("");
        registroE531.setSub("");
        registroE531.setNum_doc("");
        registroE531.setDt_doc("");
        registroE531.setCod_item("");
        registroE531.setVl_aj_item("");
        registroE531.setChv_nfe("");
        registroE530.getRegistroE531().add(registroE531);
        registroE520.getRegistroE530().add(registroE530);

        registroE500.setRegistroE520(registroE520);

        RegistroE510 registroE510 = new RegistroE510();
        registroE510.setCfop("");
        registroE510.setCst_ipi("");
        registroE510.setVl_cont_ipi("");
        registroE510.setVl_bc_ipi("");
        registroE510.setVl_ipi("");
        registroE500.getRegistroE510().add(registroE510);

        blocoE.getRegistroE500().add(registroE500);

        return blocoE;
    }

    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE preencheRegistroE990(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.BlocoE blocoE) {

        RegistroE990 registroE9900 = new RegistroE990();
        registroE9900.setQtd_lin_e("");

        blocoE.setRegistroE990(registroE9900);

        return blocoE;

    }
}
