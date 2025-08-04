package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoB;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB500;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Sidnei Klein
 */
public class GerarRegistroB500 {

    public static StringBuilder gerar(RegistroB500 reg, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(reg.getReg()));
        sb.append("|").append(Util.preencheRegistro(reg.getVl_rec()));
        sb.append("|").append(Util.preencheRegistro(reg.getQtde_prof()));
        sb.append("|").append(Util.preencheRegistro(reg.getVl_or()));
        sb.append("|").append('\n');

        return sb;
    }
}
