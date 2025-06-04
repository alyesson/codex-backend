package br.com.codex.v1.apinfe.cte400.transformers;

import br.com.codex.v1.apinfe.cte400.classes.CTProcessoEmissao;
import org.simpleframework.xml.transform.Transform;

public class CTProcessoEmissaoTransformer implements Transform<CTProcessoEmissao> {
    
    @Override
    public CTProcessoEmissao read(String arg0) {
        return CTProcessoEmissao.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTProcessoEmissao arg0) {
        return arg0.getCodigo();
    }
}