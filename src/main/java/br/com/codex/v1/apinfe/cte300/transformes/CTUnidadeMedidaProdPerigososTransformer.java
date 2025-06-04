package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTUnidadeMedidaProdPerigosos;

public class CTUnidadeMedidaProdPerigososTransformer implements Transform<CTUnidadeMedidaProdPerigosos> {

	@Override
    public CTUnidadeMedidaProdPerigosos read(String arg0) {
		return CTUnidadeMedidaProdPerigosos.valueOfCodigo(arg0);
	}

	@Override
    public String write(CTUnidadeMedidaProdPerigosos arg0) {
		return arg0.getCodigo();
	}
}