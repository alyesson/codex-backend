package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC001;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC001 {

    public static StringBuilder gerar(RegistroC001 registroC001, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC001.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC001.getInd_mov()));
        sb.append("|").append('\n');

        return sb;
    }
}
