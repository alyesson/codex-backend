package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC601;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC601 {

    public static StringBuilder gerar(RegistroC601 registroC601, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC601.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC601.getNum_doc_canc()));
        sb.append("|").append('\n');

        return sb;
    }
}
