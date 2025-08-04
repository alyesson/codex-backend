package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoB;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB990;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Sidnei Klein
 */
public class GerarRegistroB990 {

    public static StringBuilder gerar(RegistroB990 reg, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(reg.getReg()));
        sb.append("|").append(Util.preencheRegistro(reg.getQtd_lin_b()));
        sb.append("|").append('\n');

        return sb;
    }
}
