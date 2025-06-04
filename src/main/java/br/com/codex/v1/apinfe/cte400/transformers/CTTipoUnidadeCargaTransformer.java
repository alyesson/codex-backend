package br.com.codex.v1.apinfe.cte400.transformers;

import br.com.codex.v1.apinfe.cte400.classes.CTTipoUnidadeCarga;
import org.simpleframework.xml.transform.Transform;

public class CTTipoUnidadeCargaTransformer implements Transform<CTTipoUnidadeCarga> {
    
    @Override
    public CTTipoUnidadeCarga read(String arg0) {
        return CTTipoUnidadeCarga.valueOfCodigo(arg0);
    }
    
    @Override
    public String write(CTTipoUnidadeCarga arg0) {
        return arg0.getCodigo();
    }
}