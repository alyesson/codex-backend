package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTRetirada;

public class CTRetiradaTransformer implements Transform<CTRetirada> {
    
    @Override
    public CTRetirada read(String arg0) {
        return CTRetirada.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTRetirada arg0) {
        return arg0.getCodigo();
    }
}