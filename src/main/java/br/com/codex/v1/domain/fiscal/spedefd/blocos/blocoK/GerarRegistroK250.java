
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK250;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes, Sidnei Klein
 *
 */
public class GerarRegistroK250 {

    public static StringBuilder gerar(RegistroK250 registroK250, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK250.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK250.getDt_prod()));
        sb.append("|").append(Util.preencheRegistro(registroK250.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroK250.getQtd()));
        sb.append("|").append('\n');

        return sb;
    }
}
