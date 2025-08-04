package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC591;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC591 {

    public static StringBuilder gerar(RegistroC591 registroC591, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC591.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC591.getVl_fcp_op()));
        sb.append("|").append(Util.preencheRegistro(registroC591.getVl_fcp_st()));
        sb.append("|").append('\n');

        return sb;
    }
}
