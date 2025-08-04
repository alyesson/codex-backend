package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC195;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC195 {

    public static StringBuilder gerar(RegistroC195 registroC195, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC195.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC195.getCod_obs()));
        sb.append("|").append(Util.preencheRegistro(registroC195.getTxt_compl()));
        sb.append("|").append('\n');

        return sb;
    }
}
