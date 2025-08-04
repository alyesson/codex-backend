
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD195;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD195 {

    public static StringBuilder gerar(RegistroD195 registroD195, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD195.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD195.getCod_obs()));
        sb.append("|").append(Util.preencheRegistro(registroD195.getTxt_compl()));
        sb.append("|").append('\n');

        return sb;
    }
}
