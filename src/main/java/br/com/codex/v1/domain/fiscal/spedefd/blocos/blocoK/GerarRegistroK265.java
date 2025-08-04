
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK265;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroK265 {

    public static StringBuilder gerar(RegistroK265 registroK265, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK265.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK265.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroK265.getQtd_cons()));
        sb.append("|").append(Util.preencheRegistro(registroK265.getQtd_ret()));
        sb.append("|").append('\n');

        return sb;
    }
}
