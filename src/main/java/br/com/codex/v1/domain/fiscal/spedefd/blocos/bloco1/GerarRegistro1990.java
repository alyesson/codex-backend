
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1990;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 *
 */
public class GerarRegistro1990 {

    public static StringBuilder gerar(Registro1990 registro1990, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1990.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1990.getQtd_lin_1()));
        sb.append("|").append('\n');

        return sb;
    }

}
