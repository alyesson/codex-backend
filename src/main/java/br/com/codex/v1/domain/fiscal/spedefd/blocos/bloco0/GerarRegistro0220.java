package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Registro0220;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro0220 {

    public static StringBuilder gerar(Registro0220 registro0220, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro0220.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro0220.getUnid_conv()));
        sb.append("|").append(Util.preencheRegistro(registro0220.getFat_conv()));
        sb.append("|").append('\n');

        return sb;
    }
}
