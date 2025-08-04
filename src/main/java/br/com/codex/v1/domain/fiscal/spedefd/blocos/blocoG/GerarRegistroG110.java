
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoG;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG110;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroG110 {

    public static StringBuilder gerar(RegistroG110 registroG110, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroG110.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroG110.getDt_ini()));
        sb.append("|").append(Util.preencheRegistro(registroG110.getDt_fin()));
        sb.append("|").append(Util.preencheRegistro(registroG110.getSaldo_in_icms()));
        sb.append("|").append(Util.preencheRegistro(registroG110.getSom_parc()));
        sb.append("|").append(Util.preencheRegistro(registroG110.getVl_trib_exp()));
        sb.append("|").append(Util.preencheRegistro(registroG110.getVl_total()));
        sb.append("|").append(Util.preencheRegistro(registroG110.getInd_per_sai()));
        sb.append("|").append(Util.preencheRegistro(registroG110.getIcms_aprop()));
        sb.append("|").append(Util.preencheRegistro(registroG110.getSom_icms_oc()));
        sb.append("|").append('\n');

        return sb;
    }
}
