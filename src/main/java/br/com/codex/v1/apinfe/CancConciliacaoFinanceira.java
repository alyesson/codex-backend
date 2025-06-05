package br.com.codex.v1.apinfe;

import br.com.codex.v1.apinfe.dom.ConfiguracoesNfe;
import br.com.codex.v1.apinfe.dom.enuns.DocumentoEnum;
import br.com.codex.v1.apinfe.dom.enuns.ServicosEnum;
import br.com.codex.v1.apinfe.exception.NfeException;
import br.com.codex.v1.apinfe.schema.envEventoCancEConf.TEnvEvento;
import br.com.codex.v1.apinfe.schema.retEventoCancEConf.TRetEnvEvento;
import br.com.codex.v1.apinfe.util.XmlNfeUtil;

import javax.xml.bind.JAXBException;

/**
 * @author Samuel Oliveira - samuel@swconsultoria.com.br
 */
class CancConciliacaoFinanceira {

    private CancConciliacaoFinanceira() {
    }

    static TRetEnvEvento eventoEConf(ConfiguracoesNfe config, TEnvEvento enviEvento, boolean valida)
            throws NfeException {

        try {

            String xml = XmlNfeUtil.objectToXml(enviEvento, config.getEncode());
            xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
            xml = xml.replaceAll("<evento v", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" v");

            xml = Eventos.enviarEvento(config, xml, ServicosEnum.CANC_ECONF, valida, true, DocumentoEnum.NFE);

            return XmlNfeUtil.xmlToObject(xml, TRetEnvEvento.class);

        } catch (JAXBException e) {
            throw new NfeException(e.getMessage(),e);
        }

    }

}
