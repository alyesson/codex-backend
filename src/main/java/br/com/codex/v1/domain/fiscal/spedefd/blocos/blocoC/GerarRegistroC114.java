package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC114;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC114 {

    public static StringBuilder gerar(RegistroC114 registroC114, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC114.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC114.getCod_mod()));
        sb.append("|").append(Util.preencheRegistro(registroC114.getEcf_fab()));
        sb.append("|").append(Util.preencheRegistro(registroC114.getEcf_cx()));
        sb.append("|").append(Util.preencheRegistro(registroC114.getNum_doc()));
        sb.append("|").append(Util.preencheRegistro(registroC114.getDt_doc()));
        sb.append("|").append('\n');

        return sb;
    }
}
