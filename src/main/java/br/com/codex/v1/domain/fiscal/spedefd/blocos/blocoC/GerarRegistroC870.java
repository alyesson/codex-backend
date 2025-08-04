package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC870;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC870 {

    public static StringBuilder gerar(RegistroC870 registroC870, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC870.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC870.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroC870.getQtd()));
        sb.append("|").append(Util.preencheRegistro(registroC870.getUnid()));
        sb.append("|").append(Util.preencheRegistro(registroC870.getCst_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC870.getCfop()));
        sb.append("|").append('\n');

        return sb;
    }
}
