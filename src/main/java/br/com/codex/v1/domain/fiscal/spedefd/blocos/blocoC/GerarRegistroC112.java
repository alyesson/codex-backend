package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoC.RegistroC112;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroC112 {

    public static StringBuilder gerar(RegistroC112 registroC112, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroC112.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroC112.getCod_da()));
        sb.append("|").append(Util.preencheRegistro(registroC112.getUf()));
        sb.append("|").append(Util.preencheRegistro(registroC112.getNum_da()));
        sb.append("|").append(Util.preencheRegistro(registroC112.getCod_aut()));
        sb.append("|").append(Util.preencheRegistro(registroC112.getVl_da()));
        sb.append("|").append(Util.preencheRegistro(registroC112.getDt_vcto()));
        sb.append("|").append(Util.preencheRegistro(registroC112.getDt_pgto()));
        sb.append("|").append('\n');

        return sb;
    }
}
