
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1910;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes
 *
 */
public class GerarRegistro1910 {

    public static StringBuilder gerar(Registro1910 registro1910, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1910.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1910.getDt_ini()));
        sb.append("|").append(Util.preencheRegistro(registro1910.getDt_fin()));
        sb.append("|").append('\n');

        return sb;
    }
}
