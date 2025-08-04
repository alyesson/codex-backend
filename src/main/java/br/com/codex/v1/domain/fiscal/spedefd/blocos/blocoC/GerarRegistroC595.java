package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC595;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC595 {

    public static StringBuilder gerar(RegistroC595 registroC595, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC595.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC595.getCod_obs()));
        sb.append("|").append(Util.preencheRegistro(registroC595.getTxt_compl()));
        sb.append("|").append('\n');

        return sb;
    }
}
