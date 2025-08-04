package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco9;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Registro9001;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro9001 {

    public static StringBuilder gerar(Registro9001 registro9001, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro9001.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro9001.getInd_mov()));
        sb.append("|").append('\n');

        return sb;
    }
}
