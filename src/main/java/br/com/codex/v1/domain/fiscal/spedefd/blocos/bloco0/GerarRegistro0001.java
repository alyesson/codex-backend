
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Registro0001;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 *
 */
public class GerarRegistro0001 {

    public static StringBuilder gerar(Registro0001 registro0001, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro0001.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro0001.getInd_mov()));
        sb.append("|").append('\n');

        return sb;
    }
}
