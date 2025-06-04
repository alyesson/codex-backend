package br.com.codex.v1.apinfe.cte300.classes.evento.comprovanteentrega;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "infEntrega")
@Namespace(reference = "http://www.portalfiscal.inf.br/cte")
public class CTeInformacaoComprovanteEntrega extends DFBase {
    private static final long serialVersionUID = -4220281259795679332L;

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
