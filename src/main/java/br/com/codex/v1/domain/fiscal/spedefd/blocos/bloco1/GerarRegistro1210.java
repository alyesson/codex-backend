
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1210;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistro1210 {

    public static StringBuilder gerar(Registro1210 registro1210, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1210.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1210.getTipo_util()));
        sb.append("|").append(Util.preencheRegistro(registro1210.getNr_doc()));
        sb.append("|").append(Util.preencheRegistro(registro1210.getVl_cred_util()));
        sb.append("|").append(Util.preencheRegistro(registro1210.getChv_doce()));
        sb.append("|").append('\n');

        return sb;
    }
}
