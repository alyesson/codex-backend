package br.com.codex.v1.apinfe.cte200.transformers;

import br.com.codex.v1.apinfe.cte200.classes.CTTipoDocumentoOutro;
import org.simpleframework.xml.transform.Transform;

public class CTTipoDocumentoOutroTransformer implements Transform<CTTipoDocumentoOutro> {

    @Override
    public CTTipoDocumentoOutro read(final String codigo) {
        return CTTipoDocumentoOutro.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTTipoDocumentoOutro tipo) {
        return tipo.getCodigo();
    }
}