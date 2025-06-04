package br.com.codex.v1.apinfe.cte300.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * @author Caio
 * @info Detalhamento do CT-e complementado
 */

@Root(name = "infCteComp")
@Namespace(reference = "http://www.portalfiscal.inf.br/cte")
public class CTeNotaInfoCTeComplementar extends DFBase {
    private static final long serialVersionUID = -5820790322163765078L;
    @Element(name = "chCTe")
    private String chave;

    public CTeNotaInfoCTeComplementar() {
        this.chave = null;
    }

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
