
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1925;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistro1925 {

    public static StringBuilder gerar(Registro1925 registro1925, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1925.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1925.getCod_inf_adic()));
        sb.append("|").append(Util.preencheRegistro(registro1925.getVl_inf_adic()));
        sb.append("|").append(Util.preencheRegistro(registro1925.getDescr_compl_aj()));
        sb.append("|").append('\n');

        return sb;
    }
}
