package br.com.codex.v1.apinfe.cte200.transformers;

import br.com.codex.v1.apinfe.cte200.classes.CTTipoEntregaData;
import org.simpleframework.xml.transform.Transform;

public class CTTipoEntregaDataTransformer implements Transform<CTTipoEntregaData> {

    @Override
    public CTTipoEntregaData read(final String codigo) {
        return CTTipoEntregaData.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTTipoEntregaData tipo) {
        return tipo.getCodigo();
    }
}