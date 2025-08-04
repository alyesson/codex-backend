
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK100;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroK100 {

    public static StringBuilder gerar(RegistroK100 registroK100, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK100.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK100.getDt_ini()));
        sb.append("|").append(Util.preencheRegistro(registroK100.getDt_fin()));
        sb.append("|").append('\n');

        return sb;
    }
}
