package br.com.codex.v1.apinfe.cte400.classes.evento.gtv;

import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.cte400.classes.evento.CTeTipoEvento;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "evGTV")
@Namespace(reference = CTeConfig.NAMESPACE)
public class CTeEnviaEventoGtv extends CTeTipoEvento {
    private static final long serialVersionUID = -1779665170091598663L;

    @Element(name = "infGTV")
    private List<CTeInformacaoGtv> gtvs;

    public List<CTeInformacaoGtv> getGtvs() {
        return gtvs;
    }

    public void setGtvs(List<CTeInformacaoGtv> gtvs) {
        this.gtvs = gtvs;
    }
}