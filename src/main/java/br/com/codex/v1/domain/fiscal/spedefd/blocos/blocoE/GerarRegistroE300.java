
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE300;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroE300 {

    public static StringBuilder gerar(RegistroE300 registroE300, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroE300.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroE300.getUf()));
        sb.append("|").append(Util.preencheRegistro(registroE300.getDt_ini()));
        sb.append("|").append(Util.preencheRegistro(registroE300.getDt_fin()));
        sb.append("|").append('\n');

        return sb;
    }
}
