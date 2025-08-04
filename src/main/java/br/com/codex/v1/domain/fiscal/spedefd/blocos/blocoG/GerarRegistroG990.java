
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoG;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG990;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 *
 */
public class GerarRegistroG990 {

    public static StringBuilder gerar(RegistroG990 registroG990, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroG990.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroG990.getQtd_lin_g()));
        sb.append("|").append('\n');

        return sb;
    }

}
