package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Registro0206;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro0206 {

    public static StringBuilder gerar(Registro0206 registro0206, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro0206.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro0206.getCod_comb()));
        sb.append("|").append('\n');

        return sb;
    }
}
