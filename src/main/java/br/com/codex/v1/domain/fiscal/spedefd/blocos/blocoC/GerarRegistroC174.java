package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC174;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC174 {

    public static StringBuilder gerar(RegistroC174 registroC174, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC174.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC174.getInd_arm()));
        sb.append("|").append(Util.preencheRegistro(registroC174.getNum_arm()));
        sb.append("|").append(Util.preencheRegistro(registroC174.getDescr_compl()));
        sb.append("|").append('\n');

        return sb;
    }
}
