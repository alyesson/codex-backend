package br.com.codex.v1.domain.fiscal.spedefd.blocos;

import br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0.GerarBloco0;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco0.GerarContadoresBloco0;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1.GerarBloco1;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco1.GerarContadoresBloco1;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.bloco9.GerarBloco9;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoB.GerarBlocoB;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoB.GerarContadoresBlocoB;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC.GerarBlocoC;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoC.GerarContadoresBlocoC;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD.GerarBlocoD;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoD.GerarContadoresBlocoD;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE.GerarBlocoE;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoE.GerarContadoresBlocoE;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoG.GerarBlocoG;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoG.GerarContadoresBlocoG;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoH.GerarBlocoH;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoH.GerarContadoresBlocoH;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK.GerarBlocoK;
import br.com.codex.v1.domain.fiscal.spedefd.blocos.blocoK.GerarContadoresBlocoK;
import br.com.codex.v1.domain.fiscal.spedefd.registros.EfdIcms;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco9.Registro9001;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 */
public class GerarEfdIcms {

    public static StringBuilder gerar(EfdIcms efdIcms, StringBuilder sb) {

        //BLOCO0
        if (!Util.isEmpty(efdIcms.getBloco0())) {
            GerarBloco0.gerar(efdIcms, sb);
            GerarContadoresBloco0.gerar(efdIcms);
        }

        //BLOCOB
        if (!Util.isEmpty(efdIcms.getBlocoB())) {
            GerarBlocoB.gerar(efdIcms, sb);
            GerarContadoresBlocoB.gerar(efdIcms);
        }

        //BLOCOC
        if (!Util.isEmpty(efdIcms.getBlocoC())) {
            GerarBlocoC.gerar(efdIcms, sb);
            GerarContadoresBlocoC.gerar(efdIcms);
        }

        //BLOCOD
        if (!Util.isEmpty(efdIcms.getBlocoD())) {
            GerarBlocoD.gerar(efdIcms, sb);
            GerarContadoresBlocoD.gerar(efdIcms);
        }

        //BLOCOE
        if (!Util.isEmpty(efdIcms.getBlocoE())) {
            GerarBlocoE.gerar(efdIcms, sb);
            GerarContadoresBlocoE.gerar(efdIcms);
        }

        //BLOCOG
        if (!Util.isEmpty(efdIcms.getBlocoG())) {
            GerarBlocoG.gerar(efdIcms, sb);
            GerarContadoresBlocoG.gerar(efdIcms);
        }

        //BLOCOH
        if (!Util.isEmpty(efdIcms.getBlocoH())) {
            GerarBlocoH.gerar(efdIcms, sb);
            GerarContadoresBlocoH.gerar(efdIcms);
        }

        //BLOCOK
        if (!Util.isEmpty(efdIcms.getBlocoK())) {
            GerarBlocoK.gerar(efdIcms, sb);
            GerarContadoresBlocoK.gerar(efdIcms);
        }

        //BLOCO1
        if (!Util.isEmpty(efdIcms.getBloco1())) {
            GerarBloco1.gerar(efdIcms, sb);
            GerarContadoresBloco1.gerar(efdIcms);
        }

        //BLOCO9
        Registro9001 registro9001 = new Registro9001();
        registro9001.setInd_mov("0");
        efdIcms.getBloco9().setRegistro9001(registro9001);
        GerarBloco9.gerar(efdIcms.getBloco9(), sb);

        //Nova Linha ao Final do Arquivo:
        sb.append(System.lineSeparator());
        //
        return sb;
    }
}
