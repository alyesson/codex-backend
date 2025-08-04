
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1350;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes - yurilemes2@gmail.com
 *
 */
public class GerarRegistro1350 {

    public static StringBuilder gerar(Registro1350 registro1350, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1350.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1350.getSerie()));
        sb.append("|").append(Util.preencheRegistro(registro1350.getFabricante()));
        sb.append("|").append(Util.preencheRegistro(registro1350.getModelo()));
        sb.append("|").append(Util.preencheRegistro(registro1350.getTipo_medicao()));
        sb.append("|").append('\n');

        return sb;
    }
}
