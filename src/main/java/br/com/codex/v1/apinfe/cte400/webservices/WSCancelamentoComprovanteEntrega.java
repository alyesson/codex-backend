package br.com.codex.v1.apinfe.cte400.webservices;

import br.com.codex.v1.apinfe.DFModelo;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.cte400.classes.evento.CTeEvento;
import br.com.codex.v1.apinfe.cte400.classes.evento.CTeEventoRetorno;
import br.com.codex.v1.apinfe.cte400.classes.evento.comprovanteentrega.CTeEnviaEventoCancelamentoComprovanteEntrega;
import br.com.codex.v1.apinfe.utils.DFAssinaturaDigital;
import br.com.codex.v1.apinfe.validadores.DFXMLValidador;
import org.apache.axiom.om.OMElement;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class WSCancelamentoComprovanteEntrega extends WSRecepcaoEvento {
    private static final String DESCRICAO_EVENTO = "Cancelamento do Comprovante de Entrega do CT-e";
    private static final BigDecimal VERSAO_LEIAUTE = new BigDecimal("4.00");
    private static final String EVENTO_COMPROVANTE_DE_ENTREGA = "110181";
    private static final List<DFModelo> modelosPermitidos = Arrays.asList(DFModelo.CTE);

    WSCancelamentoComprovanteEntrega(final CTeConfig config) {
        super(config, modelosPermitidos);
    }

    CTeEventoRetorno cancelaComprovanteEntregaAssinado(final String chaveAcesso, final String eventoAssinadoXml) throws Exception {
        final OMElement omElementResult = super.efetuaEvento(eventoAssinadoXml, chaveAcesso, VERSAO_LEIAUTE);
        return this.config.getPersister().read(CTeEventoRetorno.class, omElementResult.toString());
    }

    CTeEventoRetorno cancelaComprovanteEntrega(final String chaveAcesso, final String protocoloAutorizacao, final String protocoloComprovanteEntrega, final int sequencialEvento) throws Exception {
        final String xmlAssinado = this.getXmlAssinado(chaveAcesso, protocoloAutorizacao, protocoloComprovanteEntrega, sequencialEvento);
        return cancelaComprovanteEntregaAssinado(chaveAcesso, xmlAssinado);
    }

    String getXmlAssinado(final String chave, final String protocoloAutorizacao, final String protocoloComprovanteEntrega, final int sequencialEvento) throws Exception {
        final String xml = this.gerarDadosCancelamentoComprovanteEntrega(chave, protocoloAutorizacao, protocoloComprovanteEntrega, sequencialEvento).toString();
        return new DFAssinaturaDigital(this.config).assinarDocumento(xml);
    }

    private CTeEvento gerarDadosCancelamentoComprovanteEntrega(final String chaveAcesso, final String protocoloAutorizacao, final String protocoloComprovanteEntrega, final int sequencialEvento) throws Exception {
        final CTeEnviaEventoCancelamentoComprovanteEntrega cancComprovanteEntrega = new CTeEnviaEventoCancelamentoComprovanteEntrega();
        cancComprovanteEntrega.setDescricaoEvento(DESCRICAO_EVENTO);
        cancComprovanteEntrega.setProtocoloAutorizacao(protocoloAutorizacao);
        cancComprovanteEntrega.setProtocoloComprovanteEntrega(protocoloComprovanteEntrega);

        DFXMLValidador.validaEventoCancelamentoComprovanteEntregaCTe400(cancComprovanteEntrega.toString());

        return super.gerarEvento(chaveAcesso, VERSAO_LEIAUTE, cancComprovanteEntrega, EVENTO_COMPROVANTE_DE_ENTREGA, null, sequencialEvento);
    }
}
