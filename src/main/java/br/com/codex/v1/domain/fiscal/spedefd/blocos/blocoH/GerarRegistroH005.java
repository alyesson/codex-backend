
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoH;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH005;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroH005 {

    public static StringBuilder gerar(RegistroH005 registroH005, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroH005.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroH005.getDt_inv()));
        sb.append("|").append(Util.preencheRegistro(registroH005.getVl_inv()));
        sb.append("|").append(Util.preencheRegistro(registroH005.getMot_inv()));
        sb.append("|").append('\n');

        return sb;
    }
}
