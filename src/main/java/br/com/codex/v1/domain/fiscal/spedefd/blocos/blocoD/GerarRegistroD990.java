package br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD;

import br.com.codex.v1.domain.fiscal.spedefd.registros.blocoD.RegistroD990;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarRegistroD990 {

    public static StringBuilder gerar(RegistroD990 registroD990, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registroD990.getReg()));
        sb.append("|").append(Util.preencheRegistro(registroD990.getQtd_lin_d()));
        sb.append("|").append('\n');

        return sb;
    }
}
