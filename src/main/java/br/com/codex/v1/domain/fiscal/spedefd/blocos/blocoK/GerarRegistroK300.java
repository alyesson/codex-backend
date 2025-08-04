
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK300;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa, Sidnei Klein
 *
 */
public class GerarRegistroK300 {

    public static StringBuilder gerar(RegistroK300 registroK300, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK300.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK300.getDt_prod()));
        sb.append("|").append('\n');

        return sb;
    }
}
