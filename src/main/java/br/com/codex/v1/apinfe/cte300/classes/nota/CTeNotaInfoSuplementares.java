package br.com.codex.v1.apinfe.cte300.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "infCTeSupl")
@Namespace(reference = "http://www.portalfiscal.inf.br/cte")
public class CTeNotaInfoSuplementares extends DFBase {

    @Element(name = "qrCodCTe")
    private String qrCode;

    public String getQrCode() {
        return qrCode;
    }

    public CTeNotaInfoSuplementares setQrCode(String qrCode) {
        this.qrCode = qrCode;
        return this;
    }
}
