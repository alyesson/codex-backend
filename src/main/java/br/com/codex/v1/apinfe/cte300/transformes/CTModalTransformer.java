package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTModal;

public class CTModalTransformer implements Transform<CTModal> {
    
    @Override
    public CTModal read(String arg0) {
        return CTModal.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTModal arg0) {
        return arg0.getCodigo();
    }
}