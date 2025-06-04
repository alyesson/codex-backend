package br.com.codex.v1.apinfe.mdfe3;

import br.com.codex.v1.apinfe.DFConfig;
import br.com.codex.v1.apinfe.DFModelo;
import br.com.codex.v1.apinfe.mdfe3.classes.def.MDFTipoEmissao;

/**
 * Created by Eldevan Nery Junior on 10/11/17.
 * Configuracao padrao do MDF-e.
 */
public abstract class MDFeConfig extends DFConfig {

    public static final String VERSAO = "3.00";
    public static final String NAMESPACE = "http://www.portalfiscal.inf.br/mdfe";

    public MDFTipoEmissao getTipoEmissao() {
        return MDFTipoEmissao.NORMAL;
    }

    @Override
    public DFModelo getModelo() {
        return DFModelo.MDFE;
    }
}
