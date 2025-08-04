package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC791;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC791 {

    public static StringBuilder gerar(RegistroC791 registroC791, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC791.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC791.getUf()));
        sb.append("|").append(Util.preencheRegistro(registroC791.getVl_bc_icms_st()));
        sb.append("|").append(Util.preencheRegistro(registroC791.getVl_icms_st()));
        sb.append("|").append('\n');

        return sb;
    }
}
