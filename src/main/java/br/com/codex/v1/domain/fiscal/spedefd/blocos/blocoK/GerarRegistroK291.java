
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK291;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa, Sidnei Klein
 *
 */
public class GerarRegistroK291 {

    public static StringBuilder gerar(RegistroK291 registroK291, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK291.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK291.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroK291.getQtd()));
        sb.append("|").append('\n');

        return sb;
    }
}
