package br.com.codex.v1.apinfe.cte300.classes.nota.assinatura;

import org.simpleframework.xml.Element;

import br.com.codex.v1.apinfe.DFBase;

public class CTeKeyInfo extends DFBase {
    private static final long serialVersionUID = 2107560216949120375L;

    @Element(name = "X509Data", required = false)
    private CTeX509Data data;

    public CTeX509Data getData() {
        return this.data;
    }

    public void setData(final CTeX509Data data) {
        this.data = data;
    }
}