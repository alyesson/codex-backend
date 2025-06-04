package br.com.codex.v1.apinfe.cte400.classes.nota.assinatura;

import org.simpleframework.xml.Attribute;

public class CTeDigestMethod {

	@Attribute(name = "Algorithm", required = false)
    private String algorithm;

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
}
