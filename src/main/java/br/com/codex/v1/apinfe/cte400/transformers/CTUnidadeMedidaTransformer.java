package br.com.codex.v1.apinfe.cte400.transformers;

import br.com.codex.v1.apinfe.cte400.classes.CTUnidadeMedida;
import org.simpleframework.xml.transform.Transform;

public class CTUnidadeMedidaTransformer implements Transform<CTUnidadeMedida> {

	@Override
    public CTUnidadeMedida read(String arg0) {
		return CTUnidadeMedida.valueOfCodigo(arg0);
	}

	@Override
    public String write(CTUnidadeMedida arg0) {
		return arg0.getCodigo();
	}
}