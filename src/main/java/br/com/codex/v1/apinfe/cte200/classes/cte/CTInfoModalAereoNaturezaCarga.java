package br.com.codex.v1.apinfe.cte200.classes.cte;

import org.simpleframework.xml.Element;

import br.com.codex.v1.apinfe.DFBase;

public class CTInfoModalAereoNaturezaCarga extends DFBase {
    private static final long serialVersionUID = 5943065004363585994L;

    @Element(name = "xDime", required = false)
    private String dimensao;

    @Element(name = "cInfManu", required = false)
    private String informacoesManuseio;

    @Element(name = "cIMP")
    private String cargaEspecial;

}
