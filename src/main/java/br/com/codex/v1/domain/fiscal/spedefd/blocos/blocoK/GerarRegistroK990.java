
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK990;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 *
 */
public class GerarRegistroK990 {

    public static StringBuilder gerar(RegistroK990 registroK990, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK990.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK990.getQtd_lin_k()));
        sb.append("|").append('\n');

        return sb;
    }

}
