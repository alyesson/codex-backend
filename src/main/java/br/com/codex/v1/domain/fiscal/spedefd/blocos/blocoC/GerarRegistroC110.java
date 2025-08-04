package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC110;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC110 {

    public static StringBuilder gerar(RegistroC110 registroC110, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC110.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC110.getCod_inf()));
        sb.append("|").append(Util.preencheRegistro(registroC110.getTxt_compl()));
        sb.append("|").append('\n');

        return sb;
    }
}
