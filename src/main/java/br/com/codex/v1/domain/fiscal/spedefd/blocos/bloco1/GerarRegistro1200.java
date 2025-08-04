
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1200;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistro1200 {

    public static StringBuilder gerar(Registro1200 registro1200, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1200.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1200.getCod_aj_apur()));
        sb.append("|").append(Util.preencheRegistro(registro1200.getSld_cred()));
        sb.append("|").append(Util.preencheRegistro(registro1200.getCred_apr()));
        sb.append("|").append(Util.preencheRegistro(registro1200.getCred_receb()));
        sb.append("|").append(Util.preencheRegistro(registro1200.getCred_util()));
        sb.append("|").append(Util.preencheRegistro(registro1200.getSld_cred_fim()));
        sb.append("|").append('\n');

        return sb;
    }
}
