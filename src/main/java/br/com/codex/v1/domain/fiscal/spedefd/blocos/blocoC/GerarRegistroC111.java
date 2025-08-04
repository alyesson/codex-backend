package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC111;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC111 {

    public static StringBuilder gerar(RegistroC111 registroC111, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC111.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC111.getNum_proc()));
        sb.append("|").append(Util.preencheRegistro(registroC111.getInd_proc()));
        sb.append("|").append('\n');

        return sb;
    }
}
