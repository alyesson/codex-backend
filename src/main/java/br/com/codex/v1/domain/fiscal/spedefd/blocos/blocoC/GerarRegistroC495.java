package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC495;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC495 {

    public static StringBuilder gerar(RegistroC495 registroC495, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC495.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getQtd()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getQtd_canc()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getUnid()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getVl_item()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getVl_desc()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getVl_canc()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getVl_acmo()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getVl_isen()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getVl_nt()));
        sb.append("|").append(Util.preencheRegistro(registroC495.getVl_icms_st()));
        sb.append("|").append('\n');

        return sb;
    }
}
