package br.com.codex.v1.apinfe.nfe400.classes.nota;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.nfe400.classes.NFOrigemProcesso;
import br.com.codex.v1.apinfe.validadores.DFStringValidador;
import org.simpleframework.xml.Element;

public class NFNotaInfoProcessoReferenciado extends DFBase {
    private static final long serialVersionUID = -5213086059996742347L;

    @Element(name = "nProc")
    private String identificadorProcessoOuAtoConcessorio;

    @Element(name = "indProc")
    private NFOrigemProcesso indicadorOrigemProcesso;

    @Element(name = "tpAto", required = false)
    private NFTipoAtoConcessorio tipoAtoConcessorio;

    public void setIdentificadorProcessoOuAtoConcessorio(final String identificadorProcessoOuAtoConcessorio) {
        DFStringValidador.tamanho60(identificadorProcessoOuAtoConcessorio,
            "Identificador Processo Ou Ato Concessorio Processo Referenciado");
        this.identificadorProcessoOuAtoConcessorio = identificadorProcessoOuAtoConcessorio;
    }

    public void setIndicadorOrigemProcesso(final NFOrigemProcesso indicadorOrigemProcesso) {
        this.indicadorOrigemProcesso = indicadorOrigemProcesso;
    }

    public void setTipoAtoConcessorio(NFTipoAtoConcessorio tipoAtoConcessorio) {
        this.tipoAtoConcessorio = tipoAtoConcessorio;
    }

    public String getIdentificadorProcessoOuAtoConcessorio() {
        return this.identificadorProcessoOuAtoConcessorio;
    }

    public NFOrigemProcesso getIndicadorOrigemProcesso() {
        return this.indicadorOrigemProcesso;
    }

    public NFTipoAtoConcessorio getTipoAtoConcessorio() {
        return tipoAtoConcessorio;
    }
}