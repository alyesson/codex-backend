package br.com.codex.v1.apinfe.cte300.transformes;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte300.classes.CTTipoUnidadeTransporte;

public class CTTipoUnidadeTransporteTransformer implements Transform<CTTipoUnidadeTransporte> {

	@Override
    public CTTipoUnidadeTransporte read(String arg0) {
		return CTTipoUnidadeTransporte.valueOfCodigo(arg0);
	}

	@Override
    public String write(CTTipoUnidadeTransporte arg0) {
		return arg0.getCodigo();
	}
}