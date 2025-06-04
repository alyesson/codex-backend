package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTTipoDirecao;

public class CTTipoDirecaoTransformer implements Transform<CTTipoDirecao> {
    
    @Override
    public CTTipoDirecao read(String arg0) {
        return CTTipoDirecao.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTTipoDirecao arg0) {
        return arg0.getCodigo();
    }
}