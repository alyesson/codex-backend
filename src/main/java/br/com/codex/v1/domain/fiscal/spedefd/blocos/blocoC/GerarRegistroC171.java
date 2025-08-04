package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC171;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC171 {

    public static StringBuilder gerar(RegistroC171 registroC171, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC171.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC171.getNum_tanque()));
        sb.append("|").append(Util.preencheRegistro(registroC171.getQtde()));
        sb.append("|").append('\n');

        return sb;
    }
}
