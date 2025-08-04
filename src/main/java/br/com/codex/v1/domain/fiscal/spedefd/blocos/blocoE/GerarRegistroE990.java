
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE990;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 *
 */
public class GerarRegistroE990 {

    public static StringBuilder gerar(RegistroE990 registroE990, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroE990.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroE990.getQtd_lin_e()));
        sb.append("|").append('\n');

        return sb;
    }

}
