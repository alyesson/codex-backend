
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK255;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroK255 {

    public static StringBuilder gerar(RegistroK255 registroK255, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK255.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK255.getDt_cons()));
        sb.append("|").append(Util.preencheRegistro(registroK255.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroK255.getQtd()));
        sb.append("|").append(Util.preencheRegistro(registroK255.getCod_ins_subst()));
        sb.append("|").append('\n');

        return sb;
    }
}
