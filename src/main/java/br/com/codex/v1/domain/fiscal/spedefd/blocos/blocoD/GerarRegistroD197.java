
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD197;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD197 {

    public static StringBuilder gerar(RegistroD197 registroD197, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD197.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD197.getCod_aj()));
        sb.append("|").append(Util.preencheRegistro(registroD197.getDescr_compl_aj()));
        sb.append("|").append(Util.preencheRegistro(registroD197.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroD197.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD197.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD197.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroD197.getVl_outros()));
        sb.append("|").append('\n');

        return sb;
    }
}
