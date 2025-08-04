
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE200;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroE200 {

    public static StringBuilder gerar(RegistroE200 registroE200, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroE200.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroE200.getUf()));
        sb.append("|").append(Util.preencheRegistro(registroE200.getDt_ini()));
        sb.append("|").append(Util.preencheRegistro(registroE200.getDt_fin()));
        sb.append("|").append('\n');

        return sb;
    }
}
