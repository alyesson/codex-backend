
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD360;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD360 {

    public static StringBuilder gerar(RegistroD360 registroD360, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD360.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD360.getVl_pis()));
        sb.append("|").append(Util.preencheRegistro(registroD360.getVl_cofins()));
        sb.append("|").append('\n');

        return sb;
    }
}
