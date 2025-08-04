
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD301;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD301 {

    public static StringBuilder gerar(RegistroD301 registroD301, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD301.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD301.getNum_doc_canc()));
        sb.append("|").append('\n');

        return sb;
    }
}
