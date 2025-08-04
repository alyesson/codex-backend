package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;


import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG001;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG110;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG125;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG126;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG130;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG140;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG990;

/**
 * @author Alyesson Sousa
 *
 */
public class BlocoG {

    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.BlocoG preencheBlocoG() {
        br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.BlocoG blocoG = new br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.BlocoG();
        blocoG = preencheRegistroG001(blocoG);
        blocoG = preencheRegistroG110(blocoG);
        blocoG = preencheRegistroG990(blocoG);

        return blocoG;
    }

    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.BlocoG preencheRegistroG001(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.BlocoG blocoG) {
        RegistroG001 registroG001 = new RegistroG001();
        registroG001.setInd_mov("1");
        
        blocoG.setRegistroG001(registroG001);
        
        return blocoG;
    }
    
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.BlocoG preencheRegistroG110(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.BlocoG blocoG) {
        
        RegistroG110 registroG110 = new RegistroG110();
        registroG110.setDt_ini("");
        registroG110.setDt_fin("");
        registroG110.setSaldo_in_icms("");
        registroG110.setSom_parc("");
        registroG110.setVl_trib_exp("");
        registroG110.setVl_total("");
        registroG110.setInd_per_sai("");
        registroG110.setIcms_aprop("");
        registroG110.setSom_icms_oc("");
        
        RegistroG125 registroG125 = new RegistroG125();
        registroG125.setCod_ind_bem("");
        registroG125.setDt_mov("");
        registroG125.setTipo_mov("");
        registroG125.setVl_imob_icms_op("");
        registroG125.setVl_imob_icms_st("");
        registroG125.setVl_imob_icms_frt("");
        registroG125.setVl_imob_icms_dif("");
        registroG125.setNum_parc("");
        registroG125.setVl_parc_pass("");
        
        RegistroG126 registroG126 = new RegistroG126();
        registroG126.setDt_ini("");
        registroG126.setDt_fin("");
        registroG126.setNum_parc("");
        registroG126.setVl_parc_pass("");
        registroG126.setVl_trib_oc("");
        registroG126.setVl_total("");
        registroG126.setInd_per_sai("");
        registroG126.setVl_parc_aprop("");
        registroG125.getRegistroG126().add(registroG126);
        
        RegistroG130 registroG130= new RegistroG130();
        registroG130.setInd_emit("");
        registroG130.setCod_part("");
        registroG130.setCod_mod("");
        registroG130.setSerie("");
        registroG130.setNum_doc("");
        registroG130.setChv_nfe_cte("");
        registroG130.setDt_doc("");
        registroG130.setNum_da("");
        
        RegistroG140 registroG140 = new RegistroG140();
        registroG140.setNum_item("");
        registroG140.setCod_item("");
        registroG140.setQtde("");
        registroG140.setUnid("");
        registroG140.setVl_icms_op_aplicado("");
        registroG140.setVl_icms_st_aplicado("");
        registroG140.setVl_icms_frt_aplicado("");
        registroG140.setVl_icms_dif_aplicado("");
        registroG130.getRegistroG140().add(registroG140);
        registroG125.getRegistroG130().add(registroG130);
        
        registroG110.getRegistroG125().add(registroG125);
        
        blocoG.getRegistroG110().add(registroG110);
        
        return blocoG;
    }
    
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.BlocoG preencheRegistroG990(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.BlocoG blocoG) {
        
        RegistroG990 registroG990= new RegistroG990();
        registroG990.setQtd_lin_g("");
        
        blocoG.setRegistroG990(registroG990);
        
        return blocoG;
    }  
}
