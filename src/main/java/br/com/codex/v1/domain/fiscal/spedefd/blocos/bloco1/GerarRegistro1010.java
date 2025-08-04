
package br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1;

import br.com.codex.v1.domain.fiscal.spedefd.registros.EfdIcms;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco1.Registro1010;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Yuri Lemes, Sidnei Klein, Alyesson Sousa
 */
public class GerarRegistro1010 {

    public static StringBuilder gerar(EfdIcms efdIcms, Registro1010 registro1010, StringBuilder sb) {

        sb.append("|").append(Util.preencheRegistro(registro1010.getReg()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_exp()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_ccrf()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_comb()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_usina()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_va()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_ee()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_cart()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_form()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_aer()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_giaf1()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_giaf3()));
        sb.append("|").append(Util.preencheRegistro(registro1010.getInd_giaf4()));
        if (Util.versao2020(efdIcms.getBloco0().getRegistro0000().getDt_ini())) {
            sb.append("|").append(Util.preencheRegistro(registro1010.getInd_rest_ressarc_compl_icms()));
        }

        sb.append("|").append('\n');

        return sb;
    }
}
