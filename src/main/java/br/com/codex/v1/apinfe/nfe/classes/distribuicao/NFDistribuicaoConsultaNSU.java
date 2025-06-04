package br.com.codex.v1.apinfe.nfe.classes.distribuicao;

import org.simpleframework.xml.Element;

import br.com.codex.v1.apinfe.DFBase;

public class NFDistribuicaoConsultaNSU extends DFBase {
    private static final long serialVersionUID = -7083100043003754958L;

    @Element(name = "NSU")
    private String nsu;

    public String getNsu() {
        return this.nsu;
    }

    public NFDistribuicaoConsultaNSU setNsu(final String nsu) {
        this.nsu = nsu;
        return this;
    }

}
