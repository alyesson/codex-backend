
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE500;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroE500 {

    public static StringBuilder gerar(RegistroE500 registroE500, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroE500.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroE500.getInd_apur()));
        sb.append("|").append(Util.preencheRegistro(registroE500.getDt_ini()));
        sb.append("|").append(Util.preencheRegistro(registroE500.getDt_fin()));
        sb.append("|").append('\n');

        return sb;
    }
}
