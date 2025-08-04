
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoK.RegistroK290;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa, Sidnei Klein
 *
 */
public class GerarRegistroK290 {

    public static StringBuilder gerar(RegistroK290 registroK290, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroK290.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroK290.getDt_ini_op()));
        sb.append("|").append(Util.preencheRegistro(registroK290.getDt_fin_op()));
        sb.append("|").append(Util.preencheRegistro(registroK290.getCod_doc_op()));
        sb.append("|").append('\n');

        return sb;
    }
}
