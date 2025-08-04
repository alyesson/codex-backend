package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Registro0221;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro0221 {

    public static StringBuilder gerar(Registro0221 registro0221, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro0221.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro0221.getCod_item_atomico()));
        sb.append("|").append(Util.preencheRegistro(registro0221.getQtd_contida()));
        sb.append("|").append('\n');

        return sb;
    }
}
