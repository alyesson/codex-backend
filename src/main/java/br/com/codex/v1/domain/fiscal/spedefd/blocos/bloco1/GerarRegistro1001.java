package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1001;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro1001 {

    public static StringBuilder gerar(Registro1001 registro1001, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1001.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1001.getInd_mov()));
        sb.append("|").append('\n');

        return sb;
    }
}
