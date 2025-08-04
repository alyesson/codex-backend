
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1800;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistro1800 {

    public static StringBuilder gerar(Registro1800 registro1800, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1800.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1800.getVl_carga()));
        sb.append("|").append(Util.preencheRegistro(registro1800.getVl_pass()));
        sb.append("|").append(Util.preencheRegistro(registro1800.getVl_fat()));
        sb.append("|").append(Util.preencheRegistro(registro1800.getInd_rat()));
        sb.append("|").append(Util.preencheRegistro(registro1800.getVl_icms_ant()));
        sb.append("|").append(Util.preencheRegistro(registro1800.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registro1800.getVl_icms_apur()));
        sb.append("|").append(Util.preencheRegistro(registro1800.getVl_bc_icms_apur()));
        sb.append("|").append(Util.preencheRegistro(registro1800.getVl_dif()));
        sb.append("|").append('\n');

        return sb;
    }
}
