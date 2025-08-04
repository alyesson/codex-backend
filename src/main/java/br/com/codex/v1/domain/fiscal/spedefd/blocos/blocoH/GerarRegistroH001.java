package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoH;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH001;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroH001 {

    public static StringBuilder gerar(RegistroH001 registroH001, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroH001.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroH001.getInd_mov()));
        sb.append("|").append('\n');

        return sb;
    }
}
