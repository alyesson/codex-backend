package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC191;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Sidnei Klein
 */
public class GerarRegistroC191 {

    public static StringBuilder gerar(RegistroC191 registroC190, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC190.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC190.getVl_fcp_op()));
        sb.append("|").append(Util.preencheRegistro(registroC190.getVl_fcp_st()));
        sb.append("|").append(Util.preencheRegistro(registroC190.getVl_fcp_ret()));
        sb.append("|").append('\n');

        return sb;
    }
}
