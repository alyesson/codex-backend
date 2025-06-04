package br.com.codex.v1.apinfe.nfe310.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.nfe310.classes.NFNotaInfoSituacaoTributariaIPI;
import org.simpleframework.xml.Element;

public class NFNotaInfoItemImpostoIPINT extends DFBase {
    private static final long serialVersionUID = -3247957187868898218L;
    
    @Element(name = "IPINT")
    private NFNotaInfoSituacaoTributariaIPI situacaoTributariaIPI;

    public NFNotaInfoSituacaoTributariaIPI getSituacaoTributariaIPI() {
        return this.situacaoTributariaIPI;
    }

    public void setSituacaoTributariaIPI(final NFNotaInfoSituacaoTributariaIPI situacaoTributariaIPI) {
        this.situacaoTributariaIPI = situacaoTributariaIPI;
    }
}