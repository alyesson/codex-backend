package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;


import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH001;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH005;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH010;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH020;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH030;
import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH990;

/**
 * @author Alyesson Sousa
 *
 */
public class BlocoH {

    public static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.BlocoH preencheBlocoH() {
        br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.BlocoH blocoH = new br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.BlocoH();
        blocoH = preencheRegistroH001(blocoH);
        blocoH = preencheRegistroH005(blocoH);
        blocoH = preencheRegistroH990(blocoH);
        
        return blocoH;
    }

    /**
     * @param blocoH
     * @return
     */
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.BlocoH preencheRegistroH001(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.BlocoH blocoH) {
        RegistroH001 registroH001 = new RegistroH001();
        
        registroH001.setInd_mov("0");
        blocoH.setRegistroH001(registroH001);
        
        return blocoH;
    }

    /**
     * @param blocoH
     * @return
     */
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.BlocoH preencheRegistroH005(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.BlocoH blocoH) {
        
        RegistroH005 registroH005 = new RegistroH005();
        registroH005.setDt_inv("31122016");
        registroH005.setVl_inv("29383,47");
        registroH005.setMot_inv("01");

        RegistroH010 registroH010 = new RegistroH010();
        registroH010.setCod_item("");
        registroH010.setUnid("");
        registroH010.setQtd("");
        registroH010.setVl_unit("");
        registroH010.setVl_item("");
        registroH010.setInd_prop("");
        registroH010.setCod_part("");
        registroH010.setTxt_compl("");
        registroH010.setCod_cta("");
        registroH010.setVl_item_ir("");
        
        RegistroH020 registroH020 = new RegistroH020();
        registroH020.setCst_icms("");
        registroH020.setBc_icms("");
        registroH020.setVl_icms("");
        registroH010.getRegistroH020().add(registroH020);
        
        RegistroH030 registroH030 = new RegistroH030();
        registroH030.setVl_icms_op("");
        registroH030.setVl_bc_icms_st("");
        registroH030.setVl_icms_st("");
        registroH030.setVl_fcp("");
        registroH010.setRegistroH030(registroH030);

        registroH005.getRegistroH010().add(registroH010);

        blocoH.getRegistroH005().add(registroH005);

        return blocoH;
    }
    
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.BlocoH preencheRegistroH990(br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.BlocoH blocoH) {
        
        RegistroH990 registroH990 = new RegistroH990();
        registroH990.setQtd_lin_h("");
        
        blocoH.setRegistroH990(registroH990);
        return blocoH;
    }
}
