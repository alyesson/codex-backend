
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE311;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroE311 {

    public static StringBuilder gerar(RegistroE311 registroE311, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroE311.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroE311.getCod_aj_apur()));
        sb.append("|").append(Util.preencheRegistro(registroE311.getDescr_compl_aj()));
        sb.append("|").append(Util.preencheRegistro(registroE311.getVl_aj_apur()));
        sb.append("|").append('\n');

        return sb;
    }
}
