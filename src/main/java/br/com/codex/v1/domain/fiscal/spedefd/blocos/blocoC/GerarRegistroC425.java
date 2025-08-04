package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC425;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC425 {

    public static StringBuilder gerar(RegistroC425 registroC425, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC425.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC425.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroC425.getQtd()));
        sb.append("|").append(Util.preencheRegistro(registroC425.getUnid()));
        sb.append("|").append(Util.preencheRegistro(registroC425.getVl_item()));
        sb.append("|").append(Util.preencheRegistro(registroC425.getVl_pis()));
        sb.append("|").append(Util.preencheRegistro(registroC425.getVl_cofins()));
        sb.append("|").append('\n');

        return sb;
    }
}
