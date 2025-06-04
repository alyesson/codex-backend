package br.com.codex.v1.apinfe.cte.transformers;

import org.simpleframework.xml.transform.Transform;

import br.com.codex.v1.apinfe.cte.CTTipoEmissao;

public class CTTipoEmissaoTransformer implements Transform<CTTipoEmissao> {
	
	@Override
    public CTTipoEmissao read(final String codigo) {
		return CTTipoEmissao.valueOfCodigo(codigo);
	}
	
	@Override
    public String write(final CTTipoEmissao tipo) {
		return tipo.getCodigo();
	}
}