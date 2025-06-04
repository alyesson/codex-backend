package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTIndicadorTomador;

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
