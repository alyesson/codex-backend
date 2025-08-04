
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1970;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 *
 */
public class GerarRegistro1970 {

    public static StringBuilder gerar(Registro1970 reg, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(reg.getReg()));
        sb.append("|").append(Util.preencheRegistro(reg.getInd_ap()));
        sb.append("|").append(Util.preencheRegistro(reg.getG3_01()));
        sb.append("|").append(Util.preencheRegistro(reg.getG3_02()));
        sb.append("|").append(Util.preencheRegistro(reg.getG3_03()));
        sb.append("|").append(Util.preencheRegistro(reg.getG3_04()));
        sb.append("|").append(Util.preencheRegistro(reg.getG3_05()));
        sb.append("|").append(Util.preencheRegistro(reg.getG3_06()));
        sb.append("|").append(Util.preencheRegistro(reg.getG3_07()));
        sb.append("|").append(Util.preencheRegistro(reg.getG3_08()));
        sb.append("|").append(Util.preencheRegistro(reg.getG3_09()));
        sb.append("|").append('\n');

        return sb;
    }
}
