package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC177;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC177 {

    public static StringBuilder gerar(RegistroC177 registroC177, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC177.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC177.getCod_selo_ipi()));
        sb.append("|").append(Util.preencheRegistro(registroC177.getQt_selo_ipi()));
        sb.append("|").append('\n');

        return sb;
    }
}
