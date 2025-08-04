package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Registro0500;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro0500 {

    public static StringBuilder gerar(Registro0500 registro0500, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro0500.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro0500.getDt_alt()));
        sb.append("|").append(Util.preencheRegistro(registro0500.getCod_nat_cc()));
        sb.append("|").append(Util.preencheRegistro(registro0500.getInd_cta()));
        sb.append("|").append(Util.preencheRegistro(registro0500.getNivel()));
        sb.append("|").append(Util.preencheRegistro(registro0500.getCod_cta()));
        sb.append("|").append(Util.preencheRegistro(registro0500.getNome_cta()));
        sb.append("|").append('\n');

        return sb;
    }
}
