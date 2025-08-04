package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC810;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC810 {

    public static StringBuilder gerar(RegistroC810 registroC810, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC810.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC810.getNum_item()));
        sb.append("|").append(Util.preencheRegistro(registroC810.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroC810.getQtd()));
        sb.append("|").append(Util.preencheRegistro(registroC810.getUnid()));
        sb.append("|").append(Util.preencheRegistro(registroC810.getVl_item()));
        sb.append("|").append(Util.preencheRegistro(registroC810.getCst_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC810.getCfop()));
        sb.append("|").append('\n');

        return sb;
    }
}
