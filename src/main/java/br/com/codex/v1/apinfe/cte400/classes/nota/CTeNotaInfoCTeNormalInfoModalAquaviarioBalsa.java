package br.com.codex.v1.apinfe.cte400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Grupo de informações das balsas
 */

@Root(name = "balsa")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeNotaInfoCTeNormalInfoModalAquaviarioBalsa extends DFBase {
    private static final long serialVersionUID = 3791338263882670862L;

    @Element(name = "xBalsa")
    private String descricao;

    public String getDescricao() {
        return this.descricao;
    }

    /**
     * Identificador da Balsa
     */
    public void setDescricao(final String descricao) {
        DFStringValidador.tamanho60(descricao, "Identificador da Balsa");
        this.descricao = descricao;
    }
}
