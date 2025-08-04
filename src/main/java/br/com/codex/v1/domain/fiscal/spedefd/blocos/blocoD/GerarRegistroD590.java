
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD590;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD590 {

    public static StringBuilder gerar(RegistroD590 registroD590, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD590.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getCst_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getCfop()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getVl_opr()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getVl_bc_icms_uf()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getVl_icms_uf()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getVl_red_bc()));
        sb.append("|").append(Util.preencheRegistro(registroD590.getCod_obs()));
        sb.append("|").append('\n');

        return sb;
    }
}
