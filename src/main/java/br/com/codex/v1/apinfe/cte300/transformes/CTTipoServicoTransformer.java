package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTTipoServico;

public class CTTipoServicoTransformer implements Transform<CTTipoServico> {
    
    @Override
    public CTTipoServico read(String arg0) {
        return CTTipoServico.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTTipoServico arg0) {
        return arg0.getCodigo();
    }
}