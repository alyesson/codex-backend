package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Registro0190;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro0190 {

    public static StringBuilder gerar(Registro0190 registro0190, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro0190.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro0190.getUnid()));
        sb.append("|").append(Util.preencheRegistro(registro0190.getDescr()));
        sb.append("|").append('\n');

        return sb;
    }
}
