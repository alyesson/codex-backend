
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoG;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG125;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroG125 {

    public static StringBuilder gerar(RegistroG125 registroG125, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroG125.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroG125.getCod_ind_bem()));
        sb.append("|").append(Util.preencheRegistro(registroG125.getDt_mov()));
        sb.append("|").append(Util.preencheRegistro(registroG125.getTipo_mov()));
        sb.append("|").append(Util.preencheRegistro(registroG125.getVl_imob_icms_op()));
        sb.append("|").append(Util.preencheRegistro(registroG125.getVl_imob_icms_st()));
        sb.append("|").append(Util.preencheRegistro(registroG125.getVl_imob_icms_frt()));
        sb.append("|").append(Util.preencheRegistro(registroG125.getVl_imob_icms_dif()));
        sb.append("|").append(Util.preencheRegistro(registroG125.getNum_parc()));
        sb.append("|").append(Util.preencheRegistro(registroG125.getVl_parc_pass()));
        sb.append("|").append('\n');

        return sb;
    }
}
