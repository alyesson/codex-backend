package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco9;

import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Registro9999;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistro9999 {

    public static StringBuilder gerar(Registro9999 registro9999, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro9999.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro9999.getQtd_lin()));
        sb.append("|");

        return sb;
    }
}
