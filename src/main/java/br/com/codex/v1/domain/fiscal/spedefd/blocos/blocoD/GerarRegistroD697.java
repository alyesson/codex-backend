
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD697;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD697 {

    public static StringBuilder gerar(RegistroD697 registroD697, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD697.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD697.getUf()));
        sb.append("|").append(Util.preencheRegistro(registroD697.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD697.getVl_icms()));
        sb.append("|").append('\n');

        return sb;
    }
}
