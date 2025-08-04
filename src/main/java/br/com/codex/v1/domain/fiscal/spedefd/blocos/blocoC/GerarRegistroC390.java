package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC390;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC390 {

    public static StringBuilder gerar(RegistroC390 registroC390, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC390.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC390.getCst_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC390.getCfop()));
        sb.append("|").append(Util.preencheRegistro(registroC390.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC390.getVl_opr()));
        sb.append("|").append(Util.preencheRegistro(registroC390.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC390.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC390.getVl_red_bc()));
        sb.append("|").append(Util.preencheRegistro(registroC390.getCod_obs()));
        sb.append("|").append('\n');

        return sb;
    }
}
