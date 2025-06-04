package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTModeloNF;

public class CTModeloNFTransformer implements Transform<CTModeloNF> {
    
    @Override
    public CTModeloNF read(String arg0) {
        return CTModeloNF.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTModeloNF arg0) {
        return arg0.getCodigo();
    }
}