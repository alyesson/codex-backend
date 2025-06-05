package br.com.codex.v1.apinfe;

import br.com.codex.v1.apinfe.dom.ConfiguracoesNfe;
import br.com.codex.v1.apinfe.dom.enuns.DocumentoEnum;
import br.com.codex.v1.apinfe.dom.enuns.ServicosEnum;
import br.com.codex.v1.apinfe.exception.NfeException;
import br.com.codex.v1.apinfe.schema.envEventoCancNFe.TEnvEvento;
import br.com.codex.v1.apinfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.com.codex.v1.apinfe.util.XmlNfeUtil;

import javax.xml.bind.JAXBException;

/**
 * @author Samuel Oliveira - samuel@swconsultoria.com.br Data: 28/09/2017 - 11:11
 */
class Cancelar {

	static TRetEnvEvento eventoCancelamento(ConfiguracoesNfe config, TEnvEvento enviEvento, boolean valida, DocumentoEnum tipoDocumento)
			throws NfeException {

		try {

			String xml = XmlNfeUtil.objectToXml(enviEvento, config.getEncode());
			xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
			xml = xml.replaceAll("<evento v", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" v");

			xml = Eventos.enviarEvento(config, xml, ServicosEnum.CANCELAMENTO, valida, true, tipoDocumento);

			return XmlNfeUtil.xmlToObject(xml, TRetEnvEvento.class);

		} catch (JAXBException e) {
			throw new NfeException(e.getMessage(),e);
		}

	}

	static br.com.codex.v1.apinfe.schema.envEventoCancSubst.TRetEnvEvento eventoCancelamentoSubstituicao(ConfiguracoesNfe config, br.com.codex.v1.apinfe.schema.envEventoCancSubst.TEnvEvento enviEvento, boolean valida)
			throws NfeException {

		try {

			String xml = XmlNfeUtil.objectToXml(enviEvento, config.getEncode());
			xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
			xml = xml.replaceAll("<evento v", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" v");

			xml = Eventos.enviarEvento(config, xml, ServicosEnum.CANCELAMENTO_SUBSTITUICAO, valida,true, DocumentoEnum.NFCE);

			return XmlNfeUtil.xmlToObject(xml, br.com.codex.v1.apinfe.schema.envEventoCancSubst.TRetEnvEvento.class);

		} catch (JAXBException e) {
			throw new NfeException(e.getMessage(),e);
		}

	}

}
