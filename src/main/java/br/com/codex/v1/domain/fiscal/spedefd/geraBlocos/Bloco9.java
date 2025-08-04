package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Registro9001;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Registro9900;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Registro9990;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Registro9999;

/**
 *
 * @author alyes
 */
public class Bloco9 {
    
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 preencheBloco9() {
        br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 bloco9 = new br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9();
        bloco9 = preencheRegistro9001(bloco9);
        bloco9 = preencheRegistro9900(bloco9);
        bloco9 = preencheRegistro9990(bloco9);
        bloco9 = preencheRegistro9999(bloco9);

        return bloco9;
    }

    private static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 preencheRegistro9001(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 bloco9) {
        Registro9001 registro1001 = new Registro9001();
        registro1001.setInd_mov("0");
        
        return bloco9;
    }
    
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 preencheRegistro9900 (br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 bloco9){
        
        Registro9900 registro9900 = new Registro9900();
        registro9900.setReg_blc("0");
        registro9900.setQtd_reg_blc("0");
        
        return bloco9;
    }
    
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 preencheRegistro9990 (br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 bloco9){
        
        Registro9990 registro9990 = new Registro9990();
        registro9990.setQtd_lin_9("0");
        
        return bloco9;
    }
    
    private static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 preencheRegistro9999 (br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Bloco9 bloco9){
        
        Registro9999 registro9999 = new Registro9999();
        registro9999.setQtd_lin("0");
        
        return bloco9;
    }
}
