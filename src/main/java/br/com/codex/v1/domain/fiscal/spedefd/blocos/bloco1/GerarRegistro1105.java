
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1105;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistro1105 {

    public static StringBuilder gerar(Registro1105 registro1105, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1105.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1105.getCod_mod()));
        sb.append("|").append(Util.preencheRegistro(registro1105.getSerie()));
        sb.append("|").append(Util.preencheRegistro(registro1105.getNum_doc()));
        sb.append("|").append(Util.preencheRegistro(registro1105.getChv_nfe()));
        sb.append("|").append(Util.preencheRegistro(registro1105.getDt_doc()));
        sb.append("|").append(Util.preencheRegistro(registro1105.getCod_item()));
        sb.append("|").append('\n');

        return sb;
    }
}
