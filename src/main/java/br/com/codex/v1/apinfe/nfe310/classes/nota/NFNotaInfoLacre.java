package br.com.codex.v1.apinfe.nfe310.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;

public class NFNotaInfoLacre extends DFBase {
    private static final long serialVersionUID = -5184684979849337146L;
    
    @Element(name = "nLacre")
    private String numeroLacres;

    public void setNumeroLacre(final String numeroLacres) {
        DFStringValidador.tamanho60(numeroLacres, "Numero Lacre");
        this.numeroLacres = numeroLacres;
    }

    public String getNumeroLacres() {
        return this.numeroLacres;
    }
}