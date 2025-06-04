package br.com.codex.v1.apinfe.cte400.transformers;

import br.com.codex.v1.apinfe.cte400.classes.CTMotivoInsucesso;
import org.simpleframework.xml.transform.Transform;

public class CTMotivoInsucessoTransformer implements Transform<CTMotivoInsucesso> {

    @Override
    public CTMotivoInsucesso read(final String codigo) {
        return CTMotivoInsucesso.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTMotivoInsucesso tipo) {
        return tipo.getCodigo();
    }

}
