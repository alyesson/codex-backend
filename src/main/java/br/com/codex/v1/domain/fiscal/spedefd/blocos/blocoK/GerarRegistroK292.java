
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK292;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa, Sidnei Klein
 *
 */
public class GerarRegistroK292 {

    public static StringBuilder gerar(RegistroK292 registroK292, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK292.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK292.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroK292.getQtd()));
        sb.append("|").append('\n');

        return sb;
    }
}
