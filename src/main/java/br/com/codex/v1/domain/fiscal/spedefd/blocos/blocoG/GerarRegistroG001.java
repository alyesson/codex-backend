package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoG;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoG.RegistroG001;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroG001 {

    public static StringBuilder gerar(RegistroG001 registroG001, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroG001.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroG001.getInd_mov()));
        sb.append("|").append('\n');

        return sb;
    }
}
