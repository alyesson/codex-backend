package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoE.RegistroE001;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroE001 {

    public static StringBuilder gerar(RegistroE001 registroE001, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroE001.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroE001.getInd_mov()));
        sb.append("|").append('\n');

        return sb;
    }
}
