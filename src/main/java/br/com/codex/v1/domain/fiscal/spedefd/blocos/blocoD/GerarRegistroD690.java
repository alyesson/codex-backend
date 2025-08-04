
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD690;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD690 {

    public static StringBuilder gerar(RegistroD690 registroD690, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD690.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getCst_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getCfop()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getVl_opr()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getVl_bc_icms_uf()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getVl_icms_uf()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getVl_red_bc()));
        sb.append("|").append(Util.preencheRegistro(registroD690.getCod_obs()));
        sb.append("|").append('\n');

        return sb;
    }
}
