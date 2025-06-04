package br.com.codex.v1.apinfe.cte400.transformers;

import br.com.codex.v1.apinfe.cte400.classes.CTTomadorServico;
import org.simpleframework.xml.transform.Transform;

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