package br.com.codex.v1.apinfe.cte200.transformers;

import br.com.codex.v1.apinfe.cte200.classes.CTIdentificadorEmissor;
import org.simpleframework.xml.transform.Transform;

public class CTIdentificadorEmissorTransformer implements Transform<CTIdentificadorEmissor> {

    @Override
    public CTIdentificadorEmissor read(final String codigo) {
        return CTIdentificadorEmissor.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTIdentificadorEmissor tipo) {
        return tipo.getCodigo();
    }
}