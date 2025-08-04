package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC105;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC105 {

    public static StringBuilder gerar(RegistroC105 registroC105, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC105.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC105.getOper()));
        sb.append("|").append(Util.preencheRegistro(registroC105.getUf()));
        sb.append("|").append('\n');

        return sb;
    }
}
