package br.com.codex.v1.apinfe.cte200.transformers;

import br.com.codex.v1.apinfe.cte200.classes.CTTomadorServico;
import org.simpleframework.xml.transform.Transform;

public class CTTomadorServicoTransformer implements Transform<CTTomadorServico> {

    @Override
    public CTTomadorServico read(final String codigo) {
        return CTTomadorServico.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTTomadorServico tipo) {
        return tipo.getCodigo();
    }
}