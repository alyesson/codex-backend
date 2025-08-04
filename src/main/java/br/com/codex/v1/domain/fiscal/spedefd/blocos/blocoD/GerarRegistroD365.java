
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD365;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes - yurilemes2@gmail.com
 *
 */
public class GerarRegistroD365 {

    public static StringBuilder gerar(RegistroD365 registroD365, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD365.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD365.getCod_tot_par()));
        sb.append("|").append(Util.preencheRegistro(registroD365.getVlr_acum_tot()));
        sb.append("|").append(Util.preencheRegistro(registroD365.getNr_tot()));
        sb.append("|").append(Util.preencheRegistro(registroD365.getDescr_nr_tot()));
        sb.append("|").append('\n');

        return sb;
    }
}
