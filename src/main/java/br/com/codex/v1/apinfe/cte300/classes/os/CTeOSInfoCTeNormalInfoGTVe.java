package br.com.codex.v1.apinfe.cte300.classes.os;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "infGTVe")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeOSInfoCTeNormalInfoGTVe extends DFBase {
    private static final long serialVersionUID = -7819895569994558919L;

    @Element(name = "chCTe")
    private String chaveGTVe;

    @ElementList(name = "Comp", inline = true)
    private List<CTeOSInfoCTeNormalInfoGTVeComp> comp;

    public String getChaveGTVe() {
        return chaveGTVe;
    }

    public void setChaveGTVe(final String chaveGTVe) {
        DFStringValidador.exatamente44N(chaveGTVe, "Chave de acesso da Guia de Transporte de Valores Eletrônica (GTVe)");
        this.chaveGTVe = chaveGTVe;
    }

    public List<CTeOSInfoCTeNormalInfoGTVeComp> getComp() {
        return comp;
    }

    public void setComp(final List<CTeOSInfoCTeNormalInfoGTVeComp> comp) {
        this.comp = comp;
    }
}
