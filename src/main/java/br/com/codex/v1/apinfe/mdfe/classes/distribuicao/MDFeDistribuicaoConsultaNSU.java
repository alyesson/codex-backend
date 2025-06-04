package br.com.codex.v1.apinfe.mdfe.classes.distribuicao;

import org.simpleframework.xml.Element;

import br.com.codex.v1.apinfe.DFBase;

public class MDFeDistribuicaoConsultaNSU extends DFBase {

	private static final long serialVersionUID = -582191692175285331L;

	@Element(name = "NSU")
	private String nsu;

	public String getNsu() {
		return this.nsu;
	}

	public MDFeDistribuicaoConsultaNSU setNsu(final String nsu) {
		this.nsu = nsu;
		return this;
	}

}
