
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1370;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistro1370 {

    public static StringBuilder gerar(Registro1370 registro1370, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1370.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1370.getNum_bico()));
        sb.append("|").append(Util.preencheRegistro(registro1370.getCod_item()));
        sb.append("|").append(Util.preencheRegistro(registro1370.getNum_tanque()));
        sb.append("|").append('\n');

        return sb;
    }
}
