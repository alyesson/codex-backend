package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC470;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC470 {

    public static StringBuilder gerar(RegistroC470 registroC470, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC470.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getQtd()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getQtd_canc()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getUnid()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getVl_item()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getCst_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getCfop()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getAliq_icms()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getVl_pis()));
        sb.append("|").append(Util.preencheRegistro(registroC470.getVl_cofins()));
        sb.append("|").append('\n');

        return sb;
    }
}
