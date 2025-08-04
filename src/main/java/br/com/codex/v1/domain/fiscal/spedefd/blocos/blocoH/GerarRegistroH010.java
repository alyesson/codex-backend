
package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoH;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoH.RegistroH010;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistroH010 {

    public static StringBuilder gerar(RegistroH010 registroH010, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroH010.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getUnid()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getQtd()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getVl_unit()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getVl_item()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getInd_prop()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getCod_part()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getTxt_compl()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getCod_cta()));
        sb.append("|").append(Util.preencheRegistro(registroH010.getVl_item_ir()));
        sb.append("|").append('\n');

        return sb;
    }
}
