package br.com.codex.v1.apinfe.cte400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import org.simpleframework.xml.Element;

public class CTeInformacoesPAA extends DFBase {
    private static final long serialVersionUID = -7075108362803190611L;

    @Element(name = "CNPJPAA")
    private String CNPJPAA;

    @Element(name = "PAASignature")
    private CTePAASignature PAASignature;

    public String getCNPJPAA() {
        return CNPJPAA;
    }

    public void setCNPJPAA(String CNPJPAA) {
        this.CNPJPAA = CNPJPAA;
    }

    public CTePAASignature getPAASignature() {
        return PAASignature;
    }

    public void setPAASignature(CTePAASignature PAASignature) {
        this.PAASignature = PAASignature;
    }
}
