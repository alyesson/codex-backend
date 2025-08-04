
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK301;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa, Sidnei Klein
 *
 */
public class GerarRegistroK301 {

    public static StringBuilder gerar(RegistroK301 registroK301, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK301.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK301.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroK301.getQtd()));
        sb.append("|").append('\n');

        return sb;
    }
}
