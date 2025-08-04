
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD530;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroD530 {

    public static StringBuilder gerar(RegistroD530 registroD530, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD530.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD530.getInd_serv()));
        sb.append("|").append(Util.preencheRegistro(registroD530.getDt_ini_serv()));
        sb.append("|").append(Util.preencheRegistro(registroD530.getDt_fin_serv()));
        sb.append("|").append(Util.preencheRegistro(registroD530.getPer_fiscal()));
        sb.append("|").append(Util.preencheRegistro(registroD530.getCod_area()));
        sb.append("|").append(Util.preencheRegistro(registroD530.getTerminal()));
        sb.append("|").append('\n');

        return sb;
    }
}
