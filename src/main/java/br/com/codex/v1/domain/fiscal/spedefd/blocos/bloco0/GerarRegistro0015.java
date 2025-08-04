package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Registro0015;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro0015 {

    public static StringBuilder gerar(Registro0015 registro0015, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro0015.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro0015.getUf_st()));
        sb.append("|").append(Util.preencheRegistro(registro0015.getIe_st()));
        sb.append("|").append('\n');

        return sb;
    }
}
