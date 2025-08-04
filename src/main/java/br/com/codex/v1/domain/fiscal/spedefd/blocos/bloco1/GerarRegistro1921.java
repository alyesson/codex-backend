
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1921;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistro1921 {

    public static StringBuilder gerar(Registro1921 registro1921, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1921.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1921.getCod_aj_apur()));
        sb.append("|").append(Util.preencheRegistro(registro1921.getDescr_compl_aj()));
        sb.append("|").append(Util.preencheRegistro(registro1921.getVl_aj_apur()));
        sb.append("|").append('\n');

        return sb;
    }
}
