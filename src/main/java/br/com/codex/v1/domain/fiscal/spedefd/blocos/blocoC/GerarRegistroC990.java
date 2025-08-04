
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC990;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 *
 */
public class GerarRegistroC990 {

    public static StringBuilder gerar(RegistroC990 registroC990, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC990.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC990.getQtd_lin_c()));
        sb.append("|").append('\n');

        return sb;
    }

}
