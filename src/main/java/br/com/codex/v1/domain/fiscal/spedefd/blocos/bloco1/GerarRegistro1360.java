
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1360;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes - yurilemes2@gmail.com
 *
 */
public class GerarRegistro1360 {

    public static StringBuilder gerar(Registro1360 registro1360, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1360.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1360.getNum_lacre()));
        sb.append("|").append(Util.preencheRegistro(registro1360.getDt_aplicacao()));
        sb.append("|").append('\n');

        return sb;
    }
}
