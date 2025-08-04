package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC310;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC310 {

    public static StringBuilder gerar(RegistroC310 registroC310, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC310.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC310.getNum_doc_canc()));
        sb.append("|").append('\n');

        return sb;
    }
}
