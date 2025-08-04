
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoH;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH030;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroH030 {

    public static StringBuilder gerar(RegistroH030 registroH030, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroH030.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroH030.getVl_icms_op()));
        sb.append("|").append(Util.preencheRegistro(registroH030.getVl_bc_icms_st()));
        sb.append("|").append(Util.preencheRegistro(registroH030.getVl_icms_st()));
        sb.append("|").append(Util.preencheRegistro(registroH030.getVl_fcp()));
        sb.append("|").append('\n');

        return sb;
    }
}
