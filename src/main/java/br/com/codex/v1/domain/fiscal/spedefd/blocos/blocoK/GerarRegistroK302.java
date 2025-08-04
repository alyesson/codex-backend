
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK302;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa, Sidnei Klein
 *
 */
public class GerarRegistroK302 {

    public static StringBuilder gerar(RegistroK302 registroK302, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK302.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK302.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroK302.getQtd()));
        sb.append("|").append('\n');

        return sb;
    }
}
