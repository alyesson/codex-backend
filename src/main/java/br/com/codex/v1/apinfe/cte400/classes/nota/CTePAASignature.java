package br.com.codex.v1.apinfe.cte400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import org.simpleframework.xml.Element;

public class CTePAASignature extends DFBase {
    private static final long serialVersionUID = 3539330266187498701L;

    @Element(name = "SignatureValue")
    private String signatureValue;

    @Element(name = "RSAKeyValue")
    private CTePAARSAKeyValue rsaKeyValue;

    public String getSignatureValue() {
        return signatureValue;
    }

    public void setSignatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
    }

    public CTePAARSAKeyValue getRsaKeyValue() {
        return rsaKeyValue;
    }

    public void setRsaKeyValue(CTePAARSAKeyValue rsaKeyValue) {
        this.rsaKeyValue = rsaKeyValue;
    }
}
