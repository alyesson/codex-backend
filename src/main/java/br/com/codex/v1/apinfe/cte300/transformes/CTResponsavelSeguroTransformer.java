package br.com.codex.v1.apinfe.cte300.transformes;

import br.com.codex.v1.apinfe.cte300.classes.CTResponsavelSeguro;
import org.simpleframework.xml.transform.Transform;

public class CTResponsavelSeguroTransformer implements Transform<CTResponsavelSeguro> {

    @Override
    public CTResponsavelSeguro read(final String codigo) {
        return CTResponsavelSeguro.valueOfCodigo(codigo);
    }

    @Override
    public String write(final CTResponsavelSeguro tipo) {
        return tipo.getCodigo();
    }

}
