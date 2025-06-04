package br.com.codex.v1.apinfe.cte200.transformers;

import br.com.codex.v1.apinfe.cte200.classes.CTTipoUnidadeTransporte;
import org.simpleframework.xml.transform.Transform;

public class CTTipoUnidadeTransporteTransformer implements Transform<CTTipoUnidadeTransporte> {

    @Override
    public CTTipoUnidadeTransporte read(final String codigo) {
        return CTTipoUnidadeTransporte.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTTipoUnidadeTransporte tipo) {
        return tipo.getCodigo();
    }
}