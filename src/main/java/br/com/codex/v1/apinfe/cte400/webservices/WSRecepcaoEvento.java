package br.com.codex.v1.apinfe.cte400.webservices;

import br.com.codex.v1.apinfe.DFLog;
import br.com.codex.v1.apinfe.DFModelo;
import br.com.codex.v1.apinfe.cte.CTeConfig;
import br.com.codex.v1.apinfe.cte400.classes.CTAutorizador400;
import br.com.codex.v1.apinfe.cte400.classes.evento.CTeDetalhamentoEvento;
import br.com.codex.v1.apinfe.cte400.classes.evento.CTeEvento;
import br.com.codex.v1.apinfe.cte400.classes.evento.CTeInfoEvento;
import br.com.codex.v1.apinfe.cte400.classes.evento.CTeTipoEvento;
import br.com.codex.v1.apinfe.cte400.parsers.CTChaveParser;
import br.com.codex.v1.apinfe.cte400.webservices.gerado.CTeRecepcaoEventoV4Stub;
import br.com.codex.v1.apinfe.validadores.DFXMLValidador;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

abstract class WSRecepcaoEvento implements DFLog {

    protected final CTeConfig config;
    private final List<DFModelo> modelosPermitidos;

    WSRecepcaoEvento(CTeConfig config, List<DFModelo> modelosPermitidos) {
        this.config = config;
        this.modelosPermitidos = modelosPermitidos;
    }

    protected OMElement efetuaEvento(final String xmlAssinado, final String chaveAcesso, final BigDecimal versao) throws Exception {
        return efetuaEvento(xmlAssinado, chaveAcesso, versao, false);
    }

    protected OMElement efetuaEventoSVC(final String xmlAssinado, final String chaveAcesso, final BigDecimal versao) throws Exception {
        return efetuaEvento(xmlAssinado, chaveAcesso, versao, true);
    }

    protected OMElement efetuaEvento(final String xmlAssinado, final String chaveAcesso, final BigDecimal versao, final boolean contingencia) throws Exception {
        final CTChaveParser ctChaveParser = new CTChaveParser(chaveAcesso);
        if (!modelosPermitidos.contains(ctChaveParser.getModelo())) {
            throw new IllegalArgumentException("CT-e do modelo \"" + ctChaveParser.getModelo().toString() + "\" não é permitido nesse evento.");
        }

        DFXMLValidador.validaEventoCTe400(xmlAssinado);

        final CTeRecepcaoEventoV4Stub.CteDadosMsg dados = new CTeRecepcaoEventoV4Stub.CteDadosMsg();

        final OMElement omElementXML = AXIOMUtil.stringToOM(xmlAssinado);
        this.getLogger().debug(omElementXML.toString());
        dados.setExtraElement(omElementXML);

        final CTAutorizador400 autorizador;
        if (contingencia) {
            autorizador = CTAutorizador400.valueOfTipoEmissao(this.config.getTipoEmissao(), this.config.getCUF());
        } else {
            autorizador = CTAutorizador400.valueOfChaveAcesso(chaveAcesso);
        }
        final String urlWebService = autorizador.getRecepcaoEvento(this.config.getAmbiente());
        if (urlWebService == null) {
            throw new IllegalArgumentException("Nao foi possivel encontrar URL para RecepcaoEvento " + ctChaveParser.getModelo().name() + ", autorizador " + autorizador.name());
        }

        CTeRecepcaoEventoV4Stub.CteRecepcaoEventoResult cteRecepcaoEventoResult = new CTeRecepcaoEventoV4Stub(urlWebService, config).cteRecepcaoEvento(dados);
        final OMElement omElementResult = cteRecepcaoEventoResult.getExtraElement();
        this.getLogger().debug(omElementResult.toString());
        return omElementResult;
    }

    protected CTeEvento gerarEvento(String chaveAcesso, BigDecimal versao, CTeTipoEvento evento, String codigoEvento, String cpfOuCnpj, int sequencialEvento) throws Exception {
        final CTChaveParser chaveParser = new CTChaveParser(chaveAcesso);

        CTeDetalhamentoEvento cteDetalhamentoEventoCancelamento = new CTeDetalhamentoEvento();
        cteDetalhamentoEventoCancelamento.setVersaoEvento(versao);
        cteDetalhamentoEventoCancelamento.setEvento(evento);

        final CTeInfoEvento infoEvento = new CTeInfoEvento();
        infoEvento.setAmbiente(this.config.getAmbiente());
        infoEvento.setChave(chaveAcesso);

        if (cpfOuCnpj != null) {
            if (cpfOuCnpj.length() == 11) {
                infoEvento.setCpf(cpfOuCnpj);
            } else {
                infoEvento.setCnpj(cpfOuCnpj);
            }
        } else {
            infoEvento.setCnpj(chaveParser.getCnpjEmitente());
        }

        infoEvento.setDataHoraEvento(ZonedDateTime.now(this.config.getTimeZone().toZoneId()));
        infoEvento.setNumeroSequencialEvento(sequencialEvento);
        infoEvento.setId(String.format("ID%s%s%03d", codigoEvento, chaveAcesso, sequencialEvento));
        infoEvento.setOrgao(chaveParser.getNFUnidadeFederativa());
        infoEvento.setCodigoEvento(codigoEvento);
        infoEvento.setDetalheEvento(cteDetalhamentoEventoCancelamento);

        CTeEvento cteEvento = new CTeEvento();
        cteEvento.setInfoEvento(infoEvento);
        cteEvento.setVersao(versao);

        return cteEvento;
    }


}
