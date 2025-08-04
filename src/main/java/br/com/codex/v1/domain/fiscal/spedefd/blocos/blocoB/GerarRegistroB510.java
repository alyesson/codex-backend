package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoB;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoB.RegistroB510;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Sidnei Klein
 */
public class GerarRegistroB510 {

    public static StringBuilder gerar(RegistroB510 reg, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(reg.getReg()));
        sb.append("|").append(Util.preencheRegistro(reg.getInd_prof()));
        sb.append("|").append(Util.preencheRegistro(reg.getInd_esc()));
        sb.append("|").append(Util.preencheRegistro(reg.getInd_soc()));
        sb.append("|").append(Util.preencheRegistro(reg.getCpf()));
        sb.append("|").append(Util.preencheRegistro(reg.getNome()));
        sb.append("|").append('\n');

        return sb;
    }
}
