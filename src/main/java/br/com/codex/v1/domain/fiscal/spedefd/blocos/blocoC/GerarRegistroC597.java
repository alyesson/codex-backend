package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC597;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC597 {

    public static StringBuilder gerar(RegistroC597 registroC597, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC597.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC597.getCod_aj()));
        sb.append("|").append(Util.preencheRegistro(registroC597.getDescr_compl_aj()));
        sb.append("|").append(Util.preencheRegistro(registroC597.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroC597.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC597.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC597.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC597.getVl_outros()));
        sb.append("|").append('\n');

        return sb;
    }
}
