
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD411;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD411 {

    public static StringBuilder gerar(RegistroD411 registroD411, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD411.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD411.getNum_doc_canc()));
        sb.append("|").append('\n');

        return sb;
    }
}
