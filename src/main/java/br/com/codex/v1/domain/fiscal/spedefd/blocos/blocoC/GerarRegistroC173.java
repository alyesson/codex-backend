package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC173;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC173 {

    public static StringBuilder gerar(RegistroC173 registroC173, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC173.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC173.getLote_med()));
        sb.append("|").append(Util.preencheRegistro(registroC173.getQtd_item()));
        sb.append("|").append(Util.preencheRegistro(registroC173.getDt_fab()));
        sb.append("|").append(Util.preencheRegistro(registroC173.getDt_val()));
        sb.append("|").append(Util.preencheRegistro(registroC173.getInd_med()));
        sb.append("|").append(Util.preencheRegistro(registroC173.getTp_prod()));
        sb.append("|").append(Util.preencheRegistro(registroC173.getVl_tab_max()));
        sb.append("|").append('\n');

        return sb;
    }
}
