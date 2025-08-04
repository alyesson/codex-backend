package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;

import br.com.codex.v1.domain.fiscal.spedefd.blocos.GerarEfdIcms;
import br.com.codex.v1.domain.fiscal.spedefd.exception.EfdException;
import br.com.codex.v1.domain.fiscal.spedefd.registros.EfdIcms;
import br.com.codex.v1.utilitario.Util;

/**
 * @author Alyesson Sousa
 *
 */

//Versão da API v3.03.1 - 21-01-2020
//Bloco 0 - Abertura, Identificação e Referências
//Bloco 1 - Outras infomrações
//Bloco 9 - Controle e Encerramento do Arquivo Digital
//Bloco B - Escrituração e Apuração do ISS (só utilizado no DF)
//Bloco C - Documentos Fiscais I – Mercadorias (ICMS/IPI)
//Bloco D - Documentos Fiscais II – Serviços (ICMS)
//Bloco E - Apuração do ICMS e do IPI
//Bloco G - Controle do Crédito de ICMS do Ativo Permanente – CIAP
//Bloco H - Inventário Físico
//Bloco K - Controle da Produção e do Estoque
public class GeradorEfdIcms {

    public static void geraBlocos() throws EfdException {
        StringBuilder sb = new StringBuilder();
        EfdIcms efdIcms = new EfdIcms();

        efdIcms.setBloco0(Bloco0.preencheBloco0());
        efdIcms.setBlocoB(BlocoB.preencheBlocoB());
        efdIcms.setBlocoC(BlocoC.preencheBlocoC());
        efdIcms.setBlocoD(BlocoD.preencheBlocoD());
        efdIcms.setBlocoE(BlocoE.preencheBlocoE());
        efdIcms.setBlocoG(BlocoG.preencheBlocoG());
        efdIcms.setBlocoH(BlocoH.preencheBlocoH());
        efdIcms.setBlocoK(BlocoK.preencheBlocoK());
        efdIcms.setBloco1(Bloco1.preencheBloco1());
        efdIcms.setBloco9(Bloco9.preencheBloco9());
        
        sb = GerarEfdIcms.gerar(efdIcms, sb);
        System.out.println(sb.toString());
        Util.criarPastaArquivo("C:/Program Files/Codex/configuracao/export/Sped/EFD", "efd.txt", sb.toString());
    }
}
