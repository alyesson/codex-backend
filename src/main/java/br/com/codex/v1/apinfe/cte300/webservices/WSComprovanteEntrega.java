package br.com.codex.v1.apinfe.cte300.webservices;

import br.com.codex.v1.apinfe.DFModelo;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.cte300.classes.evento.CTeEvento;
import br.com.codex.v1.apinfe.cte300.classes.evento.CTeEventoRetorno;
import br.com.codex.v1.apinfe.cte300.classes.evento.comprovanteentrega.CTeEnviaEventoComprovanteEntrega;
import br.com.codex.v1.apinfe.utils.DFAssinaturaDigital;
import br.com.codex.v1.apinfe.validadores.DFXMLValidador;
import org.apache.axiom.om.OMElement;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class WSComprovanteEntrega extends WSRecepcaoEvento {
    private static final String DESCRICAO_EVENTO = "Comprovante de Entrega do CT-e";
    private static final BigDecimal VERSAO_LEIAUTE = new BigDecimal("3.00");
    private static final String EVENTO_COMPROVANTE_DE_ENTREGA = "110180";
    private static final List<DFModelo> modelosPermitidos = Arrays.asList(DFModelo.CTE);

    WSComprovanteEntrega(final CTeConfig config) {
        super(config, modelosPermitidos);
    }

    CTeEventoRetorno comprovanteEntregaAssinado(final String chaveAcesso, final String eventoAssinadoXml) throws Exception {
        final OMElement omElementResult = super.efetuaEvento(eventoAssinadoXml, chaveAcesso, WSComprovanteEntrega.VERSAO_LEIAUTE);
        return this.config.getPersister().read(CTeEventoRetorno.class, omElementResult.toString());
    }

    CTeEventoRetorno comprovanteEntrega(final String chaveAcesso, final CTeEnviaEventoComprovanteEntrega comprovanteEntrega, final int sequencialEvento) throws Exception {
        final String xmlAssinado = this.getXmlAssinado(chaveAcesso, comprovanteEntrega, sequencialEvento);
        return comprovanteEntregaAssinado(chaveAcesso, xmlAssinado);
    }

    String getXmlAssinado(final String chave, final CTeEnviaEventoComprovanteEntrega comprovanteEntrega, final int sequencialEvento) throws Exception {
        final String xml = this.gerarDadosComprovanteEntrega(chave, comprovanteEntrega, sequencialEvento).toString();
        return new DFAssinaturaDigital(this.config).assinarDocumento(xml);
    }

    private CTeEvento gerarDadosComprovanteEntrega(final String chaveAcesso, final CTeEnviaEventoComprovanteEntrega comprovanteEntrega, final int sequencialEvento) throws Exception {
        comprovanteEntrega.setDescricaoEvento(WSComprovanteEntrega.DESCRICAO_EVENTO);

        DFXMLValidador.validaEventoComprovanteEntregaCTe300(comprovanteEntrega.toString());

        return super.gerarEvento(chaveAcesso, WSComprovanteEntrega.VERSAO_LEIAUTE, comprovanteEntrega, WSComprovanteEntrega.EVENTO_COMPROVANTE_DE_ENTREGA, null, sequencialEvento);
    }
}
