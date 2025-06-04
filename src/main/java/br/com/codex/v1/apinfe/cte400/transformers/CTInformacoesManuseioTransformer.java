package br.com.codex.v1.apinfe.cte400.transformers;

import br.com.codex.v1.apinfe.cte400.classes.CTInformacoesManuseio;
import org.simpleframework.xml.transform.Transform;

public class CTInformacoesManuseioTransformer implements Transform<CTInformacoesManuseio> {
    
    @Override
    public CTInformacoesManuseio read(String arg0) {
        return CTInformacoesManuseio.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTInformacoesManuseio arg0) {
        return arg0.getCodigo();
    }
}