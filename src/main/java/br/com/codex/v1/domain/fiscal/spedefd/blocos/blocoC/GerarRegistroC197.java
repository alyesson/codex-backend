package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC197;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC197 {

    public static StringBuilder gerar(RegistroC197 registroC197, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC197.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC197.getCod_aj()));
        sb.append("|").append(Util.preencheRegistro(registroC197.getDescr_compl_aj()));
        sb.append("|").append(Util.preencheRegistro(registroC197.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroC197.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC197.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC197.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC197.getVl_outros()));
        sb.append("|").append('\n');

        return sb;
    }
}
