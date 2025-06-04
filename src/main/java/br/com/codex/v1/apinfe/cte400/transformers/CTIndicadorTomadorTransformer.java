package br.com.codex.v1.apinfe.cte400.transformers;

import br.com.codex.v1.apinfe.cte400.classes.CTIndicadorTomador;
import org.simpleframework.xml.transform.Transform;

public class CTIndicadorTomadorTransformer implements Transform<CTIndicadorTomador> {
    
    @Override
    public CTIndicadorTomador read(String arg0) {
        return CTIndicadorTomador.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTIndicadorTomador arg0) {
        return arg0.getCodigo();
    }
}
