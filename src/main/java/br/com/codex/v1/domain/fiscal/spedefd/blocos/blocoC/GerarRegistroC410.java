package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC410;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC410 {

    public static StringBuilder gerar(RegistroC410 registroC410, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC410.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC410.getVl_pis()));
        sb.append("|").append(Util.preencheRegistro(registroC410.getVl_cofins()));
        sb.append("|").append('\n');

        return sb;
    }
}
