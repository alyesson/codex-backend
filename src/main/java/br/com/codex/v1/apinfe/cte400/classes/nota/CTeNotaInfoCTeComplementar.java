package br.com.codex.v1.apinfe.cte400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Detalhamento do CT-e complementado
 */

@Root(name = "infCteComp")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeNotaInfoCTeComplementar extends DFBase {
    private static final long serialVersionUID = 2354656887092410693L;

    @Element(name = "chCTe")
    private String chave;

    public String getChave() {
        return this.chave;
    }

    /**
     * Chave do CT-e complementado
     */
    public void setChave(final String chave) {
        DFStringValidador.exatamente44N(chave, "Chave do CT-e complementado");
        this.chave = chave;
    }
}
