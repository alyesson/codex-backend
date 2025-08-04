package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD110;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD110 {

    public static StringBuilder gerar(RegistroD110 registroD110, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD110.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD110.getNum_item()));
        sb.append("|").append(Util.preencheRegistro(registroD110.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroD110.getVl_serv()));
        sb.append("|").append(Util.preencheRegistro(registroD110.getVl_out()));
        sb.append("|").append('\n');

        return sb;
    }
}
