
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK280;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroK280 {

    public static StringBuilder gerar(RegistroK280 registroK280, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK280.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK280.getDt_est()));
        sb.append("|").append(Util.preencheRegistro(registroK280.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroK280.getQtd_cor_pos()));
        sb.append("|").append(Util.preencheRegistro(registroK280.getQtd_cor_neg()));
        sb.append("|").append(Util.preencheRegistro(registroK280.getInd_est()));
        sb.append("|").append(Util.preencheRegistro(registroK280.getCod_part()));
        sb.append("|").append('\n');

        return sb;
    }
}
