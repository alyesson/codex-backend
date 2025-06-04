package br.com.codex.v1.apinfe.cte400.transformers;

import br.com.codex.v1.apinfe.cte400.classes.CTTipoImpressao;
import org.simpleframework.xml.transform.Transform;

public class CTTipoImpressaoTransformes implements Transform<CTTipoImpressao> {
    
    @Override
    public CTTipoImpressao read(String arg0) {
        return CTTipoImpressao.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTTipoImpressao arg0) {
        return arg0.getCodigo();
    }
}