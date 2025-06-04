package br.com.codex.v1.apinfe.cte400.classes.evento.comprovanteentrega;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "infEntrega")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeInformacaoComprovanteEntrega extends DFBase {
    private static final long serialVersionUID = -1932963884481526638L;

    @Element(name = "chNFe")
    private String chaveNFe;

    public String getChaveNFe() {
        return chaveNFe;
    }

    public void setChaveNFe(String chaveNFe) {
        DFStringValidador.exatamente44N(chaveNFe, "Chave de Acesso");
        this.chaveNFe = chaveNFe;
    }
}
