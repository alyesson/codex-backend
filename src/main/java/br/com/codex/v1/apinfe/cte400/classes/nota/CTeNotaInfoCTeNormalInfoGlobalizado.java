package br.com.codex.v1.apinfe.cte400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Informações do CT-e Globalizado
 */

@Root(name = "infGlobalizado")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeNotaInfoCTeNormalInfoGlobalizado extends DFBase {
    private static final long serialVersionUID = 2273459701568571788L;

    @Element(name = "xObs")
    private String observacao;

    public String getObservacao() {
        return this.observacao;
    }

    /**
     * Preencher com informações adicionais, legislação do regime especial, etc
     */
    public void setObservacao(final String observacao) {
        DFStringValidador.tamanho15a256(observacao, "Preencher com informações adicionais, legislação do regime especial, etc");
        this.observacao = observacao;
    }
}
