package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;


import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK001;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK100;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK200;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK210;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK215;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK220;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK230;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK235;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK250;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK255;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK260;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK265;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK270;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK275;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK280;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK290;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK291;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK292;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK300;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK301;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK302;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK990;

/**
 * @author Alyesson Sousa
 *
 */
public class BlocoK {

    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.BlocoK preencheBlocoK() {
        br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.BlocoK blocoK = new br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.BlocoK();
        blocoK = preencheRegistroK001(blocoK);
        blocoK = preencheRegistroK100(blocoK);
        blocoK = preencheRegistroK990(blocoK);
        return blocoK;
    }

    /**
     * @param blocoK
     * @return
     */
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.BlocoK preencheRegistroK001(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.BlocoK blocoK) {
        
        RegistroK001 registroK001 = new RegistroK001();
        registroK001.setInd_mov("0");
        
        blocoK.setRegistroK001(registroK001);
        
        return blocoK;
    }

    /**
     * @param blocoK
     * @return
     */
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.BlocoK preencheRegistroK100(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.BlocoK blocoK) {
       
        RegistroK100 registroK100 = new RegistroK100();
        registroK100.setDt_ini("01032017");
        registroK100.setDt_fin("31032017");

        RegistroK200 registroK200 = new RegistroK200();
        registroK200.setDt_est("02022017");
        registroK200.setCod_item("810101001");
        registroK200.setQtd("11041,5");
        registroK200.setInd_est("0");
        registroK100.getRegistroK200().add(registroK200);
        
        RegistroK210 registroK210 = new RegistroK210();
        registroK210.setDt_ini_os("");
        registroK210.setDt_fin_os("");
        registroK210.setCod_doc_os("");
        registroK210.setCod_item_ori("");
        registroK210.setQtd_ori("");
        
        RegistroK215 registroK215 = new RegistroK215();
        registroK215.setCod_item_des("");
        registroK215.setQtd_des("");
        registroK210.getRegistroK215().add(registroK215);
        registroK100.getRegistroK210().add(registroK210);

        RegistroK220 registroK220 = new RegistroK220();
        registroK220.setDt_mov("");
        registroK220.setCod_item_ori("");
        registroK220.setCod_item_dest("");
        registroK220.setQtd_ori("");
        registroK220.setQtd_dest("");
        registroK100.getRegistroK220().add(registroK220);
        
        RegistroK230 registroK230 = new RegistroK230();
        registroK230.setDt_ini_op("");
        registroK230.setDt_fin_op("");
        registroK230.setCod_doc_op("");
        registroK230.setCod_item("");
        registroK230.setQtd_enc("");
        
        RegistroK235 registroK235 = new RegistroK235();
        registroK235.setDt_saida("");
        registroK235.setCod_item("");
        registroK235.setQtd("");
        registroK235.setCod_ins_subst("");
        registroK230.getRegistroK235().add(registroK235);
        registroK100.getRegistroK230().add(registroK230);
        
        RegistroK250 registroK250 = new RegistroK250();
        registroK250.setDt_prod("");
        registroK250.setCod_item("");
        registroK250.setQtd("");
        
        RegistroK255 registroK255 = new RegistroK255();
        registroK255.setDt_cons("");
        registroK255.setCod_item("");
        registroK255.setQtd("");
        registroK255.setCod_ins_subst("");
        registroK250.getRegistroK255().add(registroK255);
        registroK100.getRegistroK250().add(registroK250);
        
        RegistroK260 registroK260 = new RegistroK260();
        registroK260.setCod_op_os("");
        registroK260.setCod_item("");
        registroK260.setDt_saida("");
        registroK260.setQtd_saida("");
        registroK260.setDt_ret("");
        registroK260.setQtd_ret("");
        
        RegistroK265 registroK265 = new RegistroK265();
        registroK265.setCod_item("");
        registroK265.setQtd_cons("");
        registroK265.setQtd_ret("");
        registroK260.getRegistroK265().add(registroK265);
        registroK100.getRegistroK260().add(registroK260);
        
        RegistroK270 registroK270 = new RegistroK270();
        registroK270.setDt_ini_ap("");
        registroK270.setDt_fin_ap("");
        registroK270.setCod_op_os("");
        registroK270.setCod_item("");
        registroK270.setQtd_cor_pos("");
        registroK270.setQtd_cor_neg("");
        registroK270.setOrigem("");
        
        RegistroK275 registroK275 = new RegistroK275();
        registroK275.setCod_item("");
        registroK275.setQtd_cor_pos("");
        registroK275.setQtd_cor_neg("");
        registroK275.setCod_ins_subst("");
        registroK270.getRegistroK275().add(registroK275);
        registroK100.getRegistroK270().add(registroK270);
        
        RegistroK280 registroK280 = new RegistroK280();
        registroK280.setDt_est("");
        registroK280.setCod_item("");
        registroK280.setQtd_cor_pos("");
        registroK280.setQtd_cor_neg("");
        registroK280.setInd_est("");
        registroK280.setCod_part("");
        registroK100.getRegistroK280().add(registroK280);
        
        RegistroK290 registroK290 = new RegistroK290();
        registroK290.setDt_ini_op("");
        registroK290.setDt_fin_op("");
        registroK290.setCod_doc_op("");
        
        RegistroK291 registroK291 = new RegistroK291();
        registroK291.setCod_item("");
        registroK291.setQtd("");
        registroK290.getRegistroK291().add(registroK291);
                
        RegistroK292 registroK292 = new RegistroK292();  
        registroK292.setCod_item("");
        registroK292.setQtd("");
        registroK290.getRegistroK292().add(registroK292);
        registroK100.getRegistroK290().add(registroK290);
        
        RegistroK300 registroK300 = new RegistroK300();
        registroK300.setDt_prod("");
        
        RegistroK301 registroK301 = new RegistroK301();
        registroK301.setCod_item("");
        registroK301.setQtd("");
        registroK300.getRegistroK301().add(registroK301);
        
        RegistroK302 registroK302 = new RegistroK302();
        registroK302.setCod_item("");
        registroK302.setQtd("");
        registroK300.getRegistroK302().add(registroK302);
        registroK100.getRegistroK300().add(registroK300);
        
        blocoK.getRegistroK100().add(registroK100);
        
        return blocoK;
    }
    
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.BlocoK preencheRegistroK990(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.BlocoK blocoK) {
        
        RegistroK990 registroK990 = new RegistroK990();
        registroK990.setQtd_lin_k("");
        
        blocoK.setRegistroK990(registroK990);
        
        return blocoK;
    }
    
}
