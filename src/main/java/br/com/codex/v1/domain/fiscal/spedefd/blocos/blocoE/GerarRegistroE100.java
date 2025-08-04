
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE100;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroE100 {

    public static StringBuilder gerar(RegistroE100 registroE100, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroE100.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroE100.getDt_ini()));
        sb.append("|").append(Util.preencheRegistro(registroE100.getDt_fin()));
        sb.append("|").append('\n');

        return sb;
    }
}
