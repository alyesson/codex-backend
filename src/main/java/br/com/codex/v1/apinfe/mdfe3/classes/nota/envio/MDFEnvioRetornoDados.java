package br.com.codex.v1.apinfe.mdfe3.classes.nota.envio;

import br.com.codex.v1.apinfe.DFBase;
import br.com.codex.v1.apinfe.mdfe3.classes.nota.MDFe;

/**
 * Created by Edivaldo Merlo Stens on 29/05/24.
 *
 * Retorno do envio de MDF-e.
 *
 */
public class MDFEnvioRetornoDados extends DFBase {

    private final MDFEnvioRetorno retorno;
    private final MDFe mdfeAssinado;

	public MDFEnvioRetornoDados(MDFEnvioRetorno retorno, MDFe mdfeAssinado) {
		this.retorno = retorno;
		this.mdfeAssinado = mdfeAssinado;
	}

	public MDFEnvioRetorno getRetorno() {
		return retorno;
	}

	public MDFe getMDFEAssinado() {
		return mdfeAssinado;
	}
}
