package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD001;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroD001 {

    public static StringBuilder gerar(RegistroD001 registroD001, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD001.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD001.getInd_mov()));
        sb.append("|").append('\n');

        return sb;
    }
}
