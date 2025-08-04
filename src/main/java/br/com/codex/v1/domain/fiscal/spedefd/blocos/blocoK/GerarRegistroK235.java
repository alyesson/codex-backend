
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK235;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroK235 {

    public static StringBuilder gerar(RegistroK235 registroK235, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK235.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK235.getDt_saida()));
        sb.append("|").append(Util.preencheRegistro(registroK235.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroK235.getQtd()));
        sb.append("|").append(Util.preencheRegistro(registroK235.getCod_ins_subst()));
        sb.append("|").append('\n');

        return sb;
    }
}
