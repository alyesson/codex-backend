package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC180;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC180 {

    public static StringBuilder gerar(RegistroC180 registroC180, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC180.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getCod_resp_ret()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getQuant_conv()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getUnid()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getVl_unit_conv()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getVl_unit_icms_op_conv()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getVl_unit_bc_icms_st_conv()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getVl_unit_icms_st_conv()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getVl_unit_fcp_st_conv()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getCod_da()));
        sb.append("|").append(Util.preencheRegistro(registroC180.getNum_da()));
        sb.append("|").append('\n');

        return sb;
    }
}
