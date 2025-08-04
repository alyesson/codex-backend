
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoH;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH990;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 *
 */
public class GerarRegistroH990 {

    public static StringBuilder gerar(RegistroH990 registroH990, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroH990.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroH990.getQtd_lin_h()));
        sb.append("|").append('\n');

        return sb;
    }

}
