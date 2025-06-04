package br.com.codex.v1.apinfe.cte.classes.distribuicao;

import br.com.codex.v1.apinfe.DFBase;
import org.simpleframework.xml.Element;

public class CTDistribuicaoConsultaNSU extends DFBase {

    private static final long serialVersionUID = -582191692175285331L;

    @Element(name = "NSU")
    private String nsu;

    public String getNsu() {
        return this.nsu;
    }

    public CTDistribuicaoConsultaNSU setNsu(final String nsu) {
        this.nsu = nsu;
        return this;
    }

}
