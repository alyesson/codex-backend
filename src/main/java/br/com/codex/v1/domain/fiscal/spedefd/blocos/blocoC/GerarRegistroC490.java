package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC490;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC490 {

    public static StringBuilder gerar(RegistroC490 registroC490, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC490.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC490.getCst_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC490.getCfop()));
        sb.append("|").append(Util.preencheRegistro(registroC490.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC490.getVl_opr()));
        sb.append("|").append(Util.preencheRegistro(registroC490.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC490.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC490.getCod_obs()));
        sb.append("|").append('\n');

        return sb;
    }
}
