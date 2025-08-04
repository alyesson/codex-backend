
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1390;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistro1390 {

    public static StringBuilder gerar(Registro1390 registro1390, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1390.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1390.getCod_prod()));
        sb.append("|").append('\n');

        return sb;
    }
}
