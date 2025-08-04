package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC113;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC113 {

    public static StringBuilder gerar(RegistroC113 registroC113, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC113.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC113.getInd_oper()));
        sb.append("|").append(Util.preencheRegistro(registroC113.getInd_emit()));
        sb.append("|").append(Util.preencheRegistro(registroC113.getCod_part()));
        sb.append("|").append(Util.preencheRegistro(registroC113.getCod_mod()));
        sb.append("|").append(Util.preencheRegistro(registroC113.getSer()));
        sb.append("|").append(Util.preencheRegistro(registroC113.getSub()));
        sb.append("|").append(Util.preencheRegistro(registroC113.getNum_doc()));
        sb.append("|").append(Util.preencheRegistro(registroC113.getDt_doc()));
        sb.append("|").append(Util.preencheRegistro(registroC113.getChv_docE()));
        sb.append("|").append('\n');

        return sb;
    }
}
