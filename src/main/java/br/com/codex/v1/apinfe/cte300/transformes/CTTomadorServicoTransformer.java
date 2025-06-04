package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTTomadorServico;

public class CTTomadorServicoTransformer implements Transform<CTTomadorServico> {

	@Override
    public CTTomadorServico read(String arg0) {
		return CTTomadorServico.valueOfCodigo(arg0);
	}

	@Override
    public String write(CTTomadorServico arg0) {
		return arg0.getCodigo();
	}
}