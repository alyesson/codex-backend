package br.com.codex.v1.apinfe.cte300.transformes;

import br.com.codex.v1.apinfe.cte300.classes.CTTipoEspecieGtv;
import org.simpleframework.xml.transform.Transform;

public class CTTipoEspecieGtvTransformer implements Transform<CTTipoEspecieGtv> {

	@Override
    public CTTipoEspecieGtv read(String arg0) {
		return CTTipoEspecieGtv.valueOfCodigo(arg0);
	}

	@Override
    public String write(CTTipoEspecieGtv arg0) {
		return arg0.getCodigo();
	}
}