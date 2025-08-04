
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE220;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroE220 {

    public static StringBuilder gerar(RegistroE220 registroE220, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroE220.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroE220.getCod_aj_apur()));
        sb.append("|").append(Util.preencheRegistro(registroE220.getDescr_compl_aj()));
        sb.append("|").append(Util.preencheRegistro(registroE220.getVl_aj_apur()));
        sb.append("|").append('\n');

        return sb;
    }
}
