package br.com.codex.v1.apinfe.cte200.transformers;

import br.com.codex.v1.apinfe.cte200.classes.CTTipoUnidadeCarga;
import org.simpleframework.xml.transform.Transform;

public class CTTipoUnidadeCargaTransformer implements Transform<CTTipoUnidadeCarga> {

    @Override
    public CTTipoUnidadeCarga read(final String codigo) {
        return CTTipoUnidadeCarga.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTTipoUnidadeCarga tipo) {
        return tipo.getCodigo();
    }
}