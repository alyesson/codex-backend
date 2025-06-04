package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTTipoPrazoDataEntrega;

public class CTTipoPrazoDataEntregaTransformer implements Transform<CTTipoPrazoDataEntrega> {
    
    @Override
    public CTTipoPrazoDataEntrega read(String arg0) {
        return CTTipoPrazoDataEntrega.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTTipoPrazoDataEntrega arg0) {
        return arg0.getCodigo();
    }
}
