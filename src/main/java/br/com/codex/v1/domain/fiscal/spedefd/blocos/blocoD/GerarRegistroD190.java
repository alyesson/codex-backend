
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD190;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD190 {

    public static StringBuilder gerar(RegistroD190 registroD190, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD190.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD190.getCst_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD190.getCfop()));
        sb.append("|").append(Util.preencheRegistro(registroD190.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD190.getVl_opr()));
        sb.append("|").append(Util.preencheRegistro(registroD190.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD190.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD190.getVl_red_bc()));
        sb.append("|").append(Util.preencheRegistro(registroD190.getCod_obs()));
        sb.append("|").append('\n');

        return sb;
    }
}
