package br.com.codex.v1.domain.fiscal.spedicms;

import br.com.codex.v1.domain.dto.NotaEntradaSpedDto;
import br.com.codex.v1.domain.dto.NotaSaidaSpedDto;
import br.com.codex.v1.domain.enums.IndPagamento;
import br.com.codex.v1.domain.enums.TipoFrete;
import br.com.codex.v1.domain.enums.TipoOperacao;
import br.com.codex.v1.domain.repository.ImportarXmlRepository;
import br.com.codex.v1.domain.repository.XmlNotaFiscalRepository;
import br.com.codex.v1.utilitario.Util;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNfeProc;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EfdNota {

    @Autowired
    private static ImportarXmlRepository importarXmlRepository; //obtém o xml das notas de entrada

    @Autowired
    private static XmlNotaFiscalRepository xmlNotaFiscalRepository; //obtém o xml das notas de saída

    private static String removeAcentos(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replace('&', 'E');
    }

    public static List<NotaEntradaSpedDto> getListaNotasEntrada(LocalDate dataInicial, LocalDate dataFinal) throws JAXBException, IOException {

        List<NotaEntradaSpedDto> notasEntrada = new ArrayList<>();
        List<String> xmlsEntrada = importarXmlRepository.findAllEntradaNotasPeriodo(dataInicial, dataFinal);

        for (String arqXml : xmlsEntrada) {
            notasEntrada.add(montaNotaEntrada(XmlNfeUtil.leXml(arqXml)));
        }

        return notasEntrada;
    }

    private static NotaEntradaSpedDto montaNotaEntrada(String xml) throws JAXBException {
        TNfeProc nfe = XmlNfeUtil.xmlToObject(removeAcentos(xml), TNfeProc.class);

        NotaEntradaSpedDto notaEntrada = new NotaEntradaSpedDto();

        notaEntrada.setCfop(nfe.getNFe().getInfNFe().getDet().get(0).getProd().getCFOP());
        notaEntrada.setIndPagamento(IndPagamento.A_VISTA);
        notaEntrada.setValor(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF()));
        notaEntrada.setTipoOperacao(TipoOperacao.SAIDA);
        notaEntrada.setTipoFrete(TipoFrete.getTipoFrete(nfe.getNFe().getInfNFe().getTransp().getModFrete()));
        notaEntrada.setSerie(nfe.getNFe().getInfNFe().getIde().getSerie());
        notaEntrada.setNumeroNota(Long.valueOf(nfe.getNFe().getInfNFe().getIde().getNNF()));
        notaEntrada.setModelo(nfe.getNFe().getInfNFe().getIde().getMod());
        notaEntrada.setDataNota(Util.dataNfeToLocalDateTime(nfe.getNFe().getInfNFe().getIde().getDhEmi()));
        notaEntrada.setChave(nfe.getNFe().getInfNFe().getId().substring(3));

        notaEntrada.setNomeDestinatario(nfe.getNFe().getInfNFe().getDest().getXNome());
        notaEntrada.setCpfCnpjDestinatario(Util.verifica(nfe.getNFe().getInfNFe().getDest().getCNPJ()).orElse(nfe.getNFe().getInfNFe().getDest().getCPF()));
        notaEntrada.setInscricaoEstadualDestinatario(nfe.getNFe().getInfNFe().getDest().getIE());
        notaEntrada.setNumeroEnderecoDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getNro());
        notaEntrada.setEnderecoDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXLgr());
        notaEntrada.setComplementoEnderecoDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXCpl());
        notaEntrada.setCMunicipioDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getCMun());
        notaEntrada.setSetorDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXBairro());

        return notaEntrada;
    }

    public static List<NotaSaidaSpedDto> getListaNotasSaida(LocalDateTime dataInicial, LocalDateTime dataFinal, String documentoEmissor) throws JAXBException, IOException {

        List<NotaSaidaSpedDto> notasEntrada = new ArrayList<>();
        List<String> xmlsEntrada = xmlNotaFiscalRepository.findAllNotasSaidasPeriodo(dataInicial, dataFinal);

        for (String arqXml : xmlsEntrada) {
            notasEntrada.add(montaNotaSaida(XmlNfeUtil.leXml(arqXml)));
        }

        return notasEntrada;
    }

    private static NotaSaidaSpedDto montaNotaSaida(String xml) throws JAXBException {
        TNfeProc nfe = XmlNfeUtil.xmlToObject(removeAcentos(xml), TNfeProc.class);

        NotaSaidaSpedDto notaSaida = new NotaSaidaSpedDto();

        notaSaida.setCfop(nfe.getNFe().getInfNFe().getDet().get(0).getProd().getCFOP());
        notaSaida.setIndPagamento(IndPagamento.A_VISTA);
        notaSaida.setValor(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF()));
        notaSaida.setTipoOperacao(TipoOperacao.SAIDA);
        notaSaida.setTipoFrete(TipoFrete.getTipoFrete(nfe.getNFe().getInfNFe().getTransp().getModFrete()));
        notaSaida.setSerie(nfe.getNFe().getInfNFe().getIde().getSerie());
        notaSaida.setNumeroNota(Long.valueOf(nfe.getNFe().getInfNFe().getIde().getNNF()));
        notaSaida.setModelo(nfe.getNFe().getInfNFe().getIde().getMod());
        notaSaida.setDataNota(Util.dataNfeToLocalDateTime(nfe.getNFe().getInfNFe().getIde().getDhEmi()));
        notaSaida.setChave(nfe.getNFe().getInfNFe().getId().substring(3));

        notaSaida.setNomeDestinatario(nfe.getNFe().getInfNFe().getDest().getXNome());
        notaSaida.setCpfCnpjDestinatario(Util.verifica(nfe.getNFe().getInfNFe().getDest().getCNPJ()).orElse(nfe.getNFe().getInfNFe().getDest().getCPF()));
        notaSaida.setInscricaoEstadualDestinatario(nfe.getNFe().getInfNFe().getDest().getIE());
        notaSaida.setNumeroEnderecoDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getNro());
        notaSaida.setEnderecoDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXLgr());
        notaSaida.setComplementoEnderecoDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXCpl());
        notaSaida.setCMunicipioDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getCMun());
        notaSaida.setSetorDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXBairro());

        return notaSaida;
    }
}
