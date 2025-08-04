package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC465;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC465 {

    public static StringBuilder gerar(RegistroC465 registroC465, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC465.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC465.getChv_cfe()));
        sb.append("|").append(Util.preencheRegistro(registroC465.getNum_ccf()));
        sb.append("|").append('\n');

        return sb;
    }
}
