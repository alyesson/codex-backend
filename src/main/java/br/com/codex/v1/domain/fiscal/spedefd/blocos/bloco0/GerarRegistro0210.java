package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Registro0210;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro0210 {

    public static StringBuilder gerar(Registro0210 registro0210, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro0210.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro0210.getCod_item_comp()));
        sb.append("|").append(Util.preencheRegistro(registro0210.getQtd_comp()));
        sb.append("|").append(Util.preencheRegistro(registro0210.getPerda()));
        sb.append("|").append('\n');

        return sb;
    }
}
