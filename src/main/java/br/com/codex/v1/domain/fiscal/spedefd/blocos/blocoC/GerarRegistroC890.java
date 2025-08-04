package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC890;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC890 {

    public static StringBuilder gerar(RegistroC890 registroC890, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC890.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC890.getCst_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC890.getCfop()));
        sb.append("|").append(Util.preencheRegistro(registroC890.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC890.getVl_opr()));
        sb.append("|").append(Util.preencheRegistro(registroC890.getVl_bc_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC890.getVl_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC890.getCod_obs()));
        sb.append("|").append('\n');

        return sb;
    }
}
