package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.contabilidade.*;
import br.com.codex.v1.domain.dto.ContaPagarDto;
import br.com.codex.v1.domain.dto.ContaReceberDto;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.dto.NotaFiscalDuplicatasDto;
import br.com.codex.v1.domain.financeiro.ContaPagar;
import br.com.codex.v1.domain.financeiro.ContaReceber;
import br.com.codex.v1.domain.repository.*;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import br.com.codex.v1.domain.contabilidade.ImportarXml;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TIpi;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNfeProc;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImportarXmlService {
    private static final Logger logger = LoggerFactory.getLogger(ImportarXmlService.class);

    @Autowired
    private ImportarXmlRepository importarXmlRepository;

    @Autowired
    private ImportarXmlItensRepository itensNotaFiscalRepository;

    @Autowired
    ContasService contasService;

    @Autowired
    HistoricoPadraoService historicoPadraoService;

    @Autowired
    LancamentoContabilRepository lancamentoContabilRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    ContaPagarRepository contaPagarRepository;

    String numeroDaNota;
    String dataEmissaoNota;

    public ImportarXml findByChave(String chave) {
        Optional<ImportarXml> nota = importarXmlRepository.findByChave(chave);

        if(!nota.isPresent()){
            throw new DataIntegrityViolationException("A chave " + chave + " não foi localizada");
        }else {
            System.out.println(nota.get());
            return nota.get();
        }
    }

    public void obterXmlCompletoAutomatico(String xml) {

        try {
            TNfeProc nfe = XmlNfeUtil.xmlToObject(xml, TNfeProc.class);

            // Verifica se a nota já existe para evitar duplicatas
            if (importarXmlRepository.existsByChave(nfe.getNFe().getInfNFe().getId())) {
                logger.warn("NF-e já importada: {}", nfe.getNFe().getInfNFe().getId());
                return;
            }

            ImportarXml nota = new ImportarXml();
            String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
            nota.setDataImportacao(java.sql.Date.valueOf(dataAtual));
            nota.setXml(xml);

            /*
             * Dados do cabeçalho nota
             */
            nota.setCodigoUf(nfe.getNFe().getInfNFe().getIde().getCUF());
            nota.setCodigoNf(nfe.getNFe().getInfNFe().getIde().getCNF());
            nota.setNaturezaOperacao(nfe.getNFe().getInfNFe().getIde().getNatOp());
            nota.setModelo(nfe.getNFe().getInfNFe().getIde().getMod());
            nota.setSerie(nfe.getNFe().getInfNFe().getIde().getSerie());
            nota.setNumero(nfe.getNFe().getInfNFe().getIde().getNNF());
            dataEmissaoNota = nfe.getNFe().getInfNFe().getIde().getDhEmi().substring(0, 10);
            LocalDate dataEmissao = LocalDate.parse(dataEmissaoNota);
            nota.setEmissao(dataEmissao);
            nota.setDhSaidaEntrada(nfe.getNFe().getInfNFe().getIde().getDhSaiEnt() != null ? nfe.getNFe().getInfNFe().getIde().getDhSaiEnt() : null);
            nota.setTipo(nfe.getNFe().getInfNFe().getIde().getTpNF());
            nota.setIndicadorPresenca(nfe.getNFe().getInfNFe().getIde().getIndPres() != null ? nfe.getNFe().getInfNFe().getIde().getIndPres() : "");

            numeroDaNota = nfe.getNFe().getInfNFe().getIde().getNNF();

            /*
             * Dados de emitente;
             */
            if (nfe.getNFe().getInfNFe().getEmit().getXNome() != null)
                nota.setRazaoSocialEmitente(nfe.getNFe().getInfNFe().getEmit().getXNome().toUpperCase());
            if (nfe.getNFe().getInfNFe().getEmit().getXFant() != null)
                nota.setNomeFantasiaEmitente(nfe.getNFe().getInfNFe().getEmit().getXFant().toUpperCase());
            if (nfe.getNFe().getInfNFe().getEmit().getCNPJ() != null) {
                nota.setDocumentoEmitente(nfe.getNFe().getInfNFe().getEmit().getCNPJ());
            } else {
                nota.setDocumentoEmitente(nfe.getNFe().getInfNFe().getEmit().getCPF());
            }
            if (nfe.getNFe().getInfNFe().getEmit().getIE() != null)
                nota.setInscricaoEstadualEmitente(nfe.getNFe().getInfNFe().getEmit().getIE());
            if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getCEP() != null)
                nota.setCepEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getCEP());
            if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getCMun() != null)
                nota.setCodigoMunicipioEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getCMun());
            if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXMun() != null)
                nota.setNomeMunicipioEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXMun());
            if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXBairro() != null)
                nota.setBairroEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXBairro());
            if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getFone() != null)
                nota.setTelefoneEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getFone());
            if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXLgr() != null)
                nota.setLogradouroEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXLgr());
            if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getNro() != null)
                nota.setNumeroEnderecoEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getNro());
            if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getUF() != null)
                nota.setUfEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getUF().value());

            /*
             * Dados destinatário
             */
            if (nfe.getNFe().getInfNFe().getDest().getXNome() != null)
                nota.setRazaoSocialDestinatario(nfe.getNFe().getInfNFe().getDest().getXNome().toUpperCase());
            if (nfe.getNFe().getInfNFe().getDest().getCNPJ() != null) {
                nota.setDocumentoDestinatario(nfe.getNFe().getInfNFe().getDest().getCNPJ());
            } else {
                nota.setDocumentoDestinatario(nfe.getNFe().getInfNFe().getDest().getCPF());
            }
            if (nfe.getNFe().getInfNFe().getDest().getIE() != null)
                nota.setInscricaoEstadualDestinatario(nfe.getNFe().getInfNFe().getDest().getIE());
            if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getCEP() != null)
                nota.setCepDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getCEP());
            if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getCMun() != null)
                nota.setCodigoMunicipioDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getCMun());
            if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getXMun() != null)
                nota.setNomeMunicipioDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXMun());
            if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getXBairro() != null)
                nota.setBairroDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXBairro());
            if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getFone() != null)
                nota.setTelefoneDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getFone());
            if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getXLgr() != null)
                nota.setLogradouroDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXLgr());
            if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getNro() != null)
                nota.setNumeroEnderecoDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getNro());
            if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getUF() != null)
                nota.setUfDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getUF().value());

            /*
             * Valores da nota
             */

            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot() != null) {
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVBC() != null)
                    nota.setValorBaseCalculo(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVBC()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVICMS() != null)
                    nota.setValorIcms(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVICMS()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVICMSDeson() != null)
                    nota.setValorIcmsDesonerado(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVICMSDeson()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCP() != null)
                    nota.setValorFcp(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCP()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVBCST() != null)
                    nota.setValorBaseCalculoSt(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVBCST()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVST() != null)
                    nota.setValorSt(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVST()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCPST() != null)
                    nota.setValorFcpSt(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCPST()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCPSTRet() != null)
                    nota.setValorFcpStRetido(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCPSTRet()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVProd() != null)
                    nota.setValorProdutos(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVProd()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFrete() != null)
                    nota.setValorFrete(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFrete()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVSeg() != null)
                    nota.setValorSeguro(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVSeg()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVDesc() != null)
                    nota.setValorDesconto(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVDesc()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVII() != null)
                    nota.setValorIi(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVII()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVIPI() != null)
                    nota.setValorIpi(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVIPI()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVIPIDevol() != null)
                    nota.setValorIpiDevolucao(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVIPIDevol()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVPIS() != null)
                    nota.setValorPis(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVPIS()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVCOFINS() != null)
                    nota.setValorCofins(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVCOFINS()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVOutro() != null)
                    nota.setValorOutros(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVOutro()));
                if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF() != null)
                    nota.setValorTotal(new BigDecimal((nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF())));
            }

            /*
             * Dados transporte
             */
            if (nfe.getNFe().getInfNFe().getTransp() != null) {
                if (nfe.getNFe().getInfNFe().getTransp().getModFrete() != null)
                    nota.setModalidadeFrete(nfe.getNFe().getInfNFe().getTransp().getModFrete());
                if (nfe.getNFe().getInfNFe().getTransp().getTransporta() != null) {
                    if (nfe.getNFe().getInfNFe().getTransp().getTransporta().getCNPJ() != null)
                        nota.setCnpjTransportador(nfe.getNFe().getInfNFe().getTransp().getTransporta().getCNPJ());
                    if (nfe.getNFe().getInfNFe().getTransp().getTransporta().getXNome() != null)
                        nota.setNomeTransportador(nfe.getNFe().getInfNFe().getTransp().getTransporta().getXNome());
                    if (nfe.getNFe().getInfNFe().getTransp().getTransporta().getXEnder() != null)
                        nota.setEnderecoTransportador(nfe.getNFe().getInfNFe().getTransp().getTransporta().getXEnder());
                    if (nfe.getNFe().getInfNFe().getTransp().getTransporta().getXMun() != null)
                        nota.setMunicipioTransportador(nfe.getNFe().getInfNFe().getTransp().getTransporta().getXMun());
                }
            }

            /*
             * Dados cobrança/fatura
             */
            if (nfe.getNFe().getInfNFe().getCobr() != null) {
                if (nfe.getNFe().getInfNFe().getCobr().getFat() != null) {
                    if (nfe.getNFe().getInfNFe().getCobr().getFat().getNFat() != null)
                        nota.setNumeroFatura(nfe.getNFe().getInfNFe().getCobr().getFat().getNFat());
                    if (nfe.getNFe().getInfNFe().getCobr().getFat().getVOrig() != null)
                        nota.setValorOriginalFatura(new BigDecimal(nfe.getNFe().getInfNFe().getCobr().getFat().getVOrig()));
                    if (nfe.getNFe().getInfNFe().getCobr().getFat().getVDesc() != null)
                        nota.setValorDescontoFatura(new BigDecimal(nfe.getNFe().getInfNFe().getCobr().getFat().getVDesc()));
                    if (nfe.getNFe().getInfNFe().getCobr().getFat().getVLiq() != null)
                        nota.setValorLiquidoFatura(new BigDecimal(nfe.getNFe().getInfNFe().getCobr().getFat().getVLiq()));
                }
            }

            /*
             * Informações adicionais....
             */
            if (nfe.getNFe().getInfNFe().getInfAdic() != null) {
                if (nfe.getNFe().getInfNFe().getInfAdic().getInfAdFisco() != null)
                    nota.setInformacaoAdicionalFisco(nfe.getNFe().getInfNFe().getInfAdic().getInfAdFisco());
                if (nfe.getNFe().getInfNFe().getInfAdic().getInfCpl() != null)
                    nota.setInformacaoAdicionalContribuinte(nfe.getNFe().getInfNFe().getInfAdic().getInfCpl());
            }
            /*
             * Dados protocolo;...
             */
            nota.setChave(nfe.getProtNFe().getInfProt().getChNFe());
            nota.setCstat(nfe.getProtNFe().getInfProt().getCStat());
            nota.setNumeroProtocolo(nfe.getProtNFe().getInfProt().getNProt());
            nota.setDataHoraProtocolo(nfe.getProtNFe().getInfProt().getDhRecbto());
            nota.setMotivoProtocolo(nfe.getProtNFe().getInfProt().getXMotivo());

            List<ImportarXmlItens> itens = obterItensXmlCompleto(nota);

            salvarNotaFiscal(nota);
            salvarNotaFiscalItens(itens);
            lancamentoContabil(nota);
            lancaContasPagar(nota);
            logger.info("NF-e {} importada com sucesso", nota.getChave());
        } catch (JAXBException e) {
            logger.error("Erro ao parsear XML", e);
        }
    }

    public ImportarXml obterXmlCompleto(MultipartFile file) {
        TNfeProc nfe = null;
        String xml=null;

        try {
            xml = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataIntegrityViolationException("Erro ao converter xml: "+e);
        }

        try {
            nfe = XmlNfeUtil.xmlToObject(xml, TNfeProc.class);
        } catch (JAXBException e) {
            throw new DataIntegrityViolationException("Erro nfe XmlNfeUtil.xmlToObject: " + e.getMessage());
        }

        ImportarXml nota = new ImportarXml();
        String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        nota.setDataImportacao(java.sql.Date.valueOf(dataAtual));
        nota.setXml(xml);

        /*
         * Dados do cabeçalho nota
         */
        nota.setCodigoUf(nfe.getNFe().getInfNFe().getIde().getCUF());
        nota.setCodigoNf(nfe.getNFe().getInfNFe().getIde().getCNF());
        nota.setNaturezaOperacao(nfe.getNFe().getInfNFe().getIde().getNatOp());
        nota.setModelo(nfe.getNFe().getInfNFe().getIde().getMod());
        nota.setSerie(nfe.getNFe().getInfNFe().getIde().getSerie());
        nota.setNumero(nfe.getNFe().getInfNFe().getIde().getNNF());
        dataEmissaoNota = nfe.getNFe().getInfNFe().getIde().getDhEmi().substring(0, 10);
        LocalDate dataEmissao = LocalDate.parse(dataEmissaoNota);
        nota.setEmissao(dataEmissao);
        nota.setDhSaidaEntrada(nfe.getNFe().getInfNFe().getIde().getDhSaiEnt() != null ? nfe.getNFe().getInfNFe().getIde().getDhSaiEnt() : null);
        nota.setTipo(nfe.getNFe().getInfNFe().getIde().getTpNF());
        nota.setIndicadorPresenca(nfe.getNFe().getInfNFe().getIde().getIndPres() != null ? nfe.getNFe().getInfNFe().getIde().getIndPres() : "");

        numeroDaNota = nfe.getNFe().getInfNFe().getIde().getNNF();

        /*
         * Dados de emitente;
         */
        if (nfe.getNFe().getInfNFe().getEmit().getXNome() != null)
            nota.setRazaoSocialEmitente(nfe.getNFe().getInfNFe().getEmit().getXNome().toUpperCase());
        if (nfe.getNFe().getInfNFe().getEmit().getXFant() != null)
            nota.setNomeFantasiaEmitente(nfe.getNFe().getInfNFe().getEmit().getXFant().toUpperCase());
        if (nfe.getNFe().getInfNFe().getEmit().getCNPJ() != null) {
            nota.setDocumentoEmitente(nfe.getNFe().getInfNFe().getEmit().getCNPJ());
        } else {
            nota.setDocumentoEmitente(nfe.getNFe().getInfNFe().getEmit().getCPF());
        }
        if (nfe.getNFe().getInfNFe().getEmit().getIE() != null)
            nota.setInscricaoEstadualEmitente(nfe.getNFe().getInfNFe().getEmit().getIE());
        if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getCEP() != null)
            nota.setCepEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getCEP());
        if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getCMun() != null)
            nota.setCodigoMunicipioEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getCMun());
        if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXMun() != null)
            nota.setNomeMunicipioEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXMun());
        if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXBairro() != null)
            nota.setBairroEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXBairro());
        if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getFone() != null)
            nota.setTelefoneEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getFone());
        if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXLgr() != null)
            nota.setLogradouroEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getXLgr());
        if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getNro() != null)
            nota.setNumeroEnderecoEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getNro());
        if (nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getUF() != null)
            nota.setUfEmitente(nfe.getNFe().getInfNFe().getEmit().getEnderEmit().getUF().value());

        /*
         * Dados destinatário
         */
        if (nfe.getNFe().getInfNFe().getDest().getXNome() != null)
            nota.setRazaoSocialDestinatario(nfe.getNFe().getInfNFe().getDest().getXNome().toUpperCase());
        if (nfe.getNFe().getInfNFe().getDest().getCNPJ() != null) {
            nota.setDocumentoDestinatario(nfe.getNFe().getInfNFe().getDest().getCNPJ());
        } else {
            nota.setDocumentoDestinatario(nfe.getNFe().getInfNFe().getDest().getCPF());
        }
        if (nfe.getNFe().getInfNFe().getDest().getIE() != null)
            nota.setInscricaoEstadualDestinatario(nfe.getNFe().getInfNFe().getDest().getIE());
        if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getCEP() != null)
            nota.setCepDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getCEP());
        if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getCMun() != null)
            nota.setCodigoMunicipioDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getCMun());
        if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getXMun() != null)
            nota.setNomeMunicipioDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXMun());
        if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getXBairro() != null)
            nota.setBairroDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXBairro());
        if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getFone() != null)
            nota.setTelefoneDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getFone());
        if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getXLgr() != null)
            nota.setLogradouroDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getXLgr());
        if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getNro() != null)
            nota.setNumeroEnderecoDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getNro());
        if (nfe.getNFe().getInfNFe().getDest().getEnderDest().getUF() != null)
            nota.setUfDestinatario(nfe.getNFe().getInfNFe().getDest().getEnderDest().getUF().value());

        /*
         * Valores da nota
         */

        if (nfe.getNFe().getInfNFe().getTotal().getICMSTot() != null) {
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVBC() != null)
                nota.setValorBaseCalculo(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVBC()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVICMS() != null)
                nota.setValorIcms(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVICMS()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVICMSDeson() != null)
                nota.setValorIcmsDesonerado(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVICMSDeson()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCP() != null)
                nota.setValorFcp(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCP()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVBCST() != null)
                nota.setValorBaseCalculoSt(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVBCST()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVST() != null)
                nota.setValorSt(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVST()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCPST() != null)
                nota.setValorFcpSt(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCPST()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCPSTRet() != null)
                nota.setValorFcpStRetido(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFCPSTRet()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVProd() != null)
                nota.setValorProdutos(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVProd()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFrete() != null)
                nota.setValorFrete(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVFrete()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVSeg() != null)
                nota.setValorSeguro(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVSeg()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVDesc() != null)
                nota.setValorDesconto(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVDesc()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVII() != null)
                nota.setValorIi(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVII()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVIPI() != null)
                nota.setValorIpi(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVIPI()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVIPIDevol() != null)
                nota.setValorIpiDevolucao(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVIPIDevol()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVPIS() != null)
                nota.setValorPis(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVPIS()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVCOFINS() != null)
                nota.setValorCofins(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVCOFINS()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVOutro() != null)
                nota.setValorOutros(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVOutro()));
            if (nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF() != null)
                nota.setValorTotal(new BigDecimal((nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF())));
        }

        /*
         * Dados transporte
         */
        if (nfe.getNFe().getInfNFe().getTransp() != null) {
            if (nfe.getNFe().getInfNFe().getTransp().getModFrete() != null)
                nota.setModalidadeFrete(nfe.getNFe().getInfNFe().getTransp().getModFrete());
            if (nfe.getNFe().getInfNFe().getTransp().getTransporta() != null) {
                if (nfe.getNFe().getInfNFe().getTransp().getTransporta().getCNPJ() != null)
                    nota.setCnpjTransportador(nfe.getNFe().getInfNFe().getTransp().getTransporta().getCNPJ());
                if (nfe.getNFe().getInfNFe().getTransp().getTransporta().getXNome() != null)
                    nota.setNomeTransportador(nfe.getNFe().getInfNFe().getTransp().getTransporta().getXNome());
                if (nfe.getNFe().getInfNFe().getTransp().getTransporta().getXEnder() != null)
                    nota.setEnderecoTransportador(nfe.getNFe().getInfNFe().getTransp().getTransporta().getXEnder());
                if (nfe.getNFe().getInfNFe().getTransp().getTransporta().getXMun() != null)
                    nota.setMunicipioTransportador(nfe.getNFe().getInfNFe().getTransp().getTransporta().getXMun());
            }
        }

        /*
         * Dados cobrança/fatura
         */
        if (nfe.getNFe().getInfNFe().getCobr() != null) {
            if (nfe.getNFe().getInfNFe().getCobr().getFat() != null) {
                if (nfe.getNFe().getInfNFe().getCobr().getFat().getNFat() != null)
                    nota.setNumeroFatura(nfe.getNFe().getInfNFe().getCobr().getFat().getNFat());
                if (nfe.getNFe().getInfNFe().getCobr().getFat().getVOrig() != null)
                    nota.setValorOriginalFatura(new BigDecimal(nfe.getNFe().getInfNFe().getCobr().getFat().getVOrig()));
                if (nfe.getNFe().getInfNFe().getCobr().getFat().getVDesc() != null)
                    nota.setValorDescontoFatura(new BigDecimal(nfe.getNFe().getInfNFe().getCobr().getFat().getVDesc()));
                if (nfe.getNFe().getInfNFe().getCobr().getFat().getVLiq() != null)
                    nota.setValorLiquidoFatura(new BigDecimal(nfe.getNFe().getInfNFe().getCobr().getFat().getVLiq()));
            }
        }

        /*
         * Informações adicionais....
         */
        if (nfe.getNFe().getInfNFe().getInfAdic() != null) {
            if (nfe.getNFe().getInfNFe().getInfAdic().getInfAdFisco() != null)
                nota.setInformacaoAdicionalFisco(nfe.getNFe().getInfNFe().getInfAdic().getInfAdFisco());
            if (nfe.getNFe().getInfNFe().getInfAdic().getInfCpl() != null)
                nota.setInformacaoAdicionalContribuinte(nfe.getNFe().getInfNFe().getInfAdic().getInfCpl());
        }
        /*
         * Dados protocolo;...
         */
        nota.setChave(nfe.getProtNFe().getInfProt().getChNFe());
        nota.setCstat(nfe.getProtNFe().getInfProt().getCStat());
        nota.setNumeroProtocolo(nfe.getProtNFe().getInfProt().getNProt());
        nota.setDataHoraProtocolo(nfe.getProtNFe().getInfProt().getDhRecbto());
        nota.setMotivoProtocolo(nfe.getProtNFe().getInfProt().getXMotivo());

        List<ImportarXmlItens> itens = obterItensXmlCompleto(nota);

        salvarNotaFiscal(nota);
        salvarNotaFiscalItens(itens);
        lancamentoContabil(nota);
        lancaContasPagar(nota);

        return nota;
    }

    public List<ImportarXmlItens> obterItensXmlCompleto(ImportarXml importarXml) {

        TNfeProc nfeIten = null;

        List<ImportarXmlItens> itens = new ArrayList<>();

        try {
            nfeIten = XmlNfeUtil.xmlToObject(importarXml.getXml(), TNfeProc.class);
        } catch (JAXBException e) {
            throw new DataIntegrityViolationException("Erro item XmlNfeUtil.xmlToObject: " + e.getMessage());
        }

        nfeIten.getNFe().getInfNFe().getDet().forEach(i -> {
            ImportarXmlItens item = new ImportarXmlItens();

            item.setNomeProduto(i.getProd().getXProd() != null ? i.getProd().getXProd() : "");
            item.setCodigoProduto(i.getProd().getCProd() != null ? i.getProd().getCProd() : "");
            item.setNcmSh(i.getProd().getNCM() != null ? i.getProd().getNCM() : "");
            item.setCfop(i.getProd().getCFOP() != null ? i.getProd().getCFOP() : "");
            item.setItem(i.getNItem() != null ? i.getNItem() : "0");
            item.setUnidadeComercial(i.getProd().getUCom() != null ? i.getProd().getUCom() : "");
            item.setQuantidadeComercial(i.getProd().getQCom() != null ? new BigDecimal(i.getProd().getQCom()) : new BigDecimal(0));
            item.setValorUnitarioComercial(i.getProd().getVUnCom() != null ? new BigDecimal(i.getProd().getVUnCom()) : new BigDecimal(0));
            item.setValorTotalProdutos(i.getProd().getVProd() != null ? new BigDecimal(i.getProd().getVProd()) : new BigDecimal(0));
            item.setUnidadeTributacao(i.getProd().getUTrib() != null ? i.getProd().getUTrib() : "");
            item.setQuantidadeTributacao(i.getProd().getQTrib() != null ? new BigDecimal(i.getProd().getQTrib()) : new BigDecimal(0));
            item.setValorUnitarioTributacao(i.getProd().getVUnTrib() != null ? new BigDecimal(i.getProd().getVUnTrib()) : new BigDecimal(0));

            item.setValorDesconto(i.getProd().getVDesc() != null ? new BigDecimal(i.getProd().getVDesc()) : new BigDecimal(0));
            item.setValorFrete(i.getProd().getVFrete() != null ? new BigDecimal(i.getProd().getVFrete()) : new BigDecimal(0));
            item.setValorSeguro(i.getProd().getVSeg() != null ? new BigDecimal(i.getProd().getVSeg()) : new BigDecimal(0));
            item.setValorOutro(i.getProd().getVOutro() != null ? new BigDecimal(i.getProd().getVOutro()) : new BigDecimal(0));

            item.setInformacaoAdicional(i.getInfAdProd() != null ? i.getInfAdProd() : "");

            //@@ Rastr0
            if (i.getProd().getRastro() != null && i.getProd().getRastro().size() > 0) {

                item.setFabricacaoLote(i.getProd().getRastro().get(0).getDFab() != null ? i.getProd().getRastro().get(0).getDFab() : null);
                item.setValidadeLote(i.getProd().getRastro().get(0).getDVal() != null ? i.getProd().getRastro().get(0).getDVal() : null);
                item.setNumeroLote(i.getProd().getRastro().get(0).getNLote() != null ? i.getProd().getRastro().get(0).getNLote() : "");
            }

            for (JAXBElement element : i.getImposto().getContent()) {
                TNFe.InfNFe.Det.Imposto.ICMS icms = null;
                TNFe.InfNFe.Det.Imposto.PIS pis = null;
                TNFe.InfNFe.Det.Imposto.PISST pisSt = null;
                TNFe.InfNFe.Det.Imposto.COFINS cofins = null;
                TNFe.InfNFe.Det.Imposto.COFINSST cofinsSt = null;
                TIpi ipi = null;

                switch (element.getName().getLocalPart()) {
                    case "ICMS":
                        icms = (TNFe.InfNFe.Det.Imposto.ICMS) element.getValue();
                        break;
                    case "PIS":
                        pis = (TNFe.InfNFe.Det.Imposto.PIS) element.getValue();
                        break;
                    case "PISST":
                        pisSt = (TNFe.InfNFe.Det.Imposto.PISST) element.getValue();
                        break;
                    case "COFINS":
                        cofins = (TNFe.InfNFe.Det.Imposto.COFINS) element.getValue();
                        break;
                    case "COFINSST":
                        cofinsSt = (TNFe.InfNFe.Det.Imposto.COFINSST) element.getValue();
                        break;
                    case "IPI":
                        ipi = (TIpi) element.getValue();
                        break;
                    default:
                        break;
                }

                if (icms != null) {
                    if (icms.getICMS00() != null) {
                        item.setCstIcms(icms.getICMS00().getCST() != null ? icms.getICMS00().getCST() : "");
                        item.setModBc(icms.getICMS00().getModBC() != null ? icms.getICMS00().getModBC() : "0");
                        item.setOrigIcms(icms.getICMS00().getOrig() != null ? icms.getICMS00().getOrig() : "0");
                        item.setAliqIcms(icms.getICMS00().getPICMS() != null ? new BigDecimal(icms.getICMS00().getPICMS()) : new BigDecimal(0));
                        item.setBcIcms(icms.getICMS00().getVBC() != null ? new BigDecimal(icms.getICMS00().getVBC()) : new BigDecimal(0));
                        item.setValorIcms(icms.getICMS00().getVICMS() != null ? new BigDecimal(icms.getICMS00().getVICMS()) : new BigDecimal(0));
                        item.setValorFcp(icms.getICMS00().getVFCP() != null ? new BigDecimal(icms.getICMS00().getVFCP()) : new BigDecimal(0));
                        item.setAliqFcp(icms.getICMS00().getPFCP() != null ? new BigDecimal(icms.getICMS00().getPFCP()) : new BigDecimal(0));

                    } else if (icms.getICMS10() != null) {
                        item.setCstIcms(icms.getICMS10().getCST() != null ? icms.getICMS10().getCST() : "");
                        item.setModBc(icms.getICMS10().getModBC() != null ? icms.getICMS10().getModBC() : "0");
                        item.setOrigIcms(icms.getICMS10().getOrig() != null ? icms.getICMS10().getOrig() : "0");
                        item.setBcIcms(icms.getICMS10().getVBC() != null ? new BigDecimal(icms.getICMS10().getVBC()) : new BigDecimal(0));
                        item.setModBcSt(icms.getICMS10().getModBCST() != null ? icms.getICMS10().getModBCST() : "0");
                        item.setAliqFcpSt(icms.getICMS10().getPFCPST() != null ? new BigDecimal(icms.getICMS10().getPFCPST()) : new BigDecimal(0));
                        item.setAliqFcp(icms.getICMS10().getPFCP() != null ? new BigDecimal(icms.getICMS10().getPFCP()) : new BigDecimal(0));
                        item.setAliqIcms(icms.getICMS10().getPICMS() != null ? new BigDecimal(icms.getICMS10().getPICMS()) : new BigDecimal(0));
                        item.setAliqIcmsSt(icms.getICMS10().getPICMSST() != null ? new BigDecimal(icms.getICMS10().getPICMSST()) : new BigDecimal(0));
                        item.setPercentMargemIcmsSt(icms.getICMS10().getPMVAST() != null ? new BigDecimal(icms.getICMS10().getPMVAST()) : new BigDecimal(0));
                        item.setPercentRedBcSt(icms.getICMS10().getPRedBCST() != null ? new BigDecimal(icms.getICMS10().getPRedBCST()) : new BigDecimal(0));
                        item.setBcFcpSt(icms.getICMS10().getVBCFCPST() != null ? new BigDecimal(icms.getICMS10().getVBCFCPST()) : new BigDecimal(0));
                        item.setValorFcp(icms.getICMS10().getVBCFCP() != null ? new BigDecimal(icms.getICMS10().getVBCFCP()) : new BigDecimal(0));
                        item.setBcIcmsSt(icms.getICMS10().getVBCST() != null ? new BigDecimal(icms.getICMS10().getVBCST()) : new BigDecimal(0));
                        item.setValorFcp(icms.getICMS10().getVFCP() != null ? new BigDecimal(icms.getICMS10().getVFCP()) : new BigDecimal(0));
                        item.setValorFcpSt(icms.getICMS10().getVFCPST() != null ? new BigDecimal(icms.getICMS10().getVFCPST()) : new BigDecimal(0));
                        item.setValorIcms(icms.getICMS10().getVICMS() != null ? new BigDecimal(icms.getICMS10().getVICMS()) : new BigDecimal(0));
                        item.setValorIcmsSt(icms.getICMS10().getVICMSST() != null ? new BigDecimal(icms.getICMS10().getVICMSST()) : new BigDecimal(0));

                    } else if (icms.getICMS20() != null) {
                        item.setCstIcms(icms.getICMS20().getCST() != null ? icms.getICMS20().getCST() : "");
                        item.setModBc(icms.getICMS20().getModBC() != null ? icms.getICMS20().getModBC() : "0");
                        item.setOrigIcms(icms.getICMS20().getOrig() != null ? icms.getICMS20().getOrig() : "0");
                        item.setAliqIcms(icms.getICMS20().getPICMS() != null ? new BigDecimal(icms.getICMS20().getPICMS()) : new BigDecimal(0));
                        item.setValorIcms(icms.getICMS20().getVICMS() != null ? new BigDecimal(icms.getICMS20().getVICMS()) : new BigDecimal(0));
                        item.setPercentRedBc(icms.getICMS20().getPRedBC() != null ? new BigDecimal(icms.getICMS20().getPRedBC()) : new BigDecimal(0));
                        item.setBcIcms(icms.getICMS20().getVBC() != null ? new BigDecimal(icms.getICMS20().getVBC()) : new BigDecimal(0));
                        item.setValorFcp(icms.getICMS20().getVFCP() != null ? new BigDecimal(icms.getICMS20().getVFCP()) : new BigDecimal(0));
                        item.setValorIcmsDesonerado(icms.getICMS20().getVICMSDeson() != null ? new BigDecimal(icms.getICMS20().getVICMSDeson()) : new BigDecimal(0));
                        item.setMotDesIcms(icms.getICMS20().getMotDesICMS() != null ? icms.getICMS20().getMotDesICMS() : "0");
                        item.setAliqFcp(icms.getICMS20().getPFCP() != null ? new BigDecimal(icms.getICMS20().getPFCP()) : new BigDecimal(0));
                        item.setBcFcp(icms.getICMS20().getVBCFCP() != null ? new BigDecimal(icms.getICMS20().getVBCFCP()) : new BigDecimal(0));

                    } else if (icms.getICMS30() != null) {
                        item.setCstIcms(icms.getICMS30().getCST() != null ? icms.getICMS30().getCST() : "");
                        item.setModBcSt(icms.getICMS30().getModBCST() != null ? icms.getICMS30().getModBCST() : "0");
                        item.setMotDesIcms(icms.getICMS30().getMotDesICMS() != null ? icms.getICMS30().getMotDesICMS() : "0");
                        item.setOrigIcms(icms.getICMS30().getOrig() != null ? icms.getICMS30().getOrig() : "0");
                        item.setAliqFcpSt(icms.getICMS30().getPFCPST() != null ? new BigDecimal(icms.getICMS30().getPFCPST()) : new BigDecimal(0));
                        item.setAliqIcmsSt(icms.getICMS30().getPICMSST() != null ? new BigDecimal(icms.getICMS30().getPICMSST()) : new BigDecimal(0));
                        item.setPercentMargemIcmsSt(icms.getICMS30().getPMVAST() != null ? new BigDecimal(icms.getICMS30().getPMVAST()) : new BigDecimal(0));
                        item.setPercentRedBcSt(icms.getICMS30().getPRedBCST() != null ? new BigDecimal(icms.getICMS30().getPRedBCST()) : new BigDecimal(0));
                        item.setBcFcpSt(icms.getICMS30().getVBCFCPST() != null ? new BigDecimal(icms.getICMS30().getVBCFCPST()) : new BigDecimal(0));
                        item.setBcIcmsSt(icms.getICMS30().getVBCST() != null ? new BigDecimal(icms.getICMS30().getVBCST()) : new BigDecimal(0));
                        item.setValorFcpSt(icms.getICMS30().getVFCPST() != null ? new BigDecimal(icms.getICMS30().getVFCPST()) : new BigDecimal(0));
                        item.setValorIcmsDesonerado(icms.getICMS30().getVICMSDeson() != null ? new BigDecimal(icms.getICMS30().getVICMSDeson()) : new BigDecimal(0));
                        item.setValorIcmsSt(icms.getICMS30().getVICMSST() != null ? new BigDecimal(icms.getICMS30().getVICMSST()) : new BigDecimal(0));

                    } else if (icms.getICMS40() != null) {
                        item.setCstIcms(icms.getICMS40().getCST() != null ? icms.getICMS40().getCST() : "");
                        item.setMotDesIcms(icms.getICMS40().getMotDesICMS() != null ? icms.getICMS40().getMotDesICMS() : "0");
                        item.setOrigIcms(icms.getICMS40().getOrig() != null ? icms.getICMS40().getOrig() : "0");
                        item.setValorIcmsDesonerado(icms.getICMS40().getVICMSDeson() != null ? new BigDecimal(icms.getICMS40().getVICMSDeson()) : new BigDecimal(0));

                    } else if (icms.getICMS51() != null) {
                        item.setCstIcms(icms.getICMS51().getCST() != null ? icms.getICMS51().getCST() : "");
                        item.setModBc(icms.getICMS51().getModBC() != null ? icms.getICMS51().getModBC() : "0");
                        item.setOrigIcms(icms.getICMS51().getOrig() != null ? icms.getICMS51().getOrig() : "0");
                        item.setAliqIcmsDiferido(icms.getICMS51().getPDif() != null ? new BigDecimal(icms.getICMS51().getPDif()) : new BigDecimal(0));
                        item.setAliqFcp(icms.getICMS51().getPFCP() != null ? new BigDecimal(icms.getICMS51().getPFCP()) : new BigDecimal(0));
                        item.setAliqIcms(icms.getICMS51().getPICMS() != null ? new BigDecimal(icms.getICMS51().getPICMS()) : new BigDecimal(0));
                        item.setPercentRedBc(icms.getICMS51().getPRedBC() != null ? new BigDecimal(icms.getICMS51().getPRedBC()) : new BigDecimal(0));
                        item.setBcIcms(icms.getICMS51().getVBC() != null ? new BigDecimal(icms.getICMS51().getVBC()) : new BigDecimal(0));
                        item.setBcFcp(icms.getICMS51().getVBCFCP() != null ? new BigDecimal(icms.getICMS51().getVBCFCP()) : new BigDecimal(0));
                        item.setValorFcp(icms.getICMS51().getVFCP() != null ? new BigDecimal(icms.getICMS51().getVFCP()) : new BigDecimal(0));
                        item.setValorIcms(icms.getICMS51().getVICMS() != null ? new BigDecimal(icms.getICMS51().getVICMS()) : new BigDecimal(0));
                        item.setValorIcmsDiferido(icms.getICMS51().getVICMSDif() != null ? new BigDecimal(icms.getICMS51().getVICMSDif()) : new BigDecimal(0));
                        item.setValorIcmsOperacao(icms.getICMS51().getVICMSOp() != null ? new BigDecimal(icms.getICMS51().getVICMSOp()) : new BigDecimal(0));

                    } else if (icms.getICMS60() != null) {
                        item.setCstIcms(icms.getICMS60().getCST() != null ? icms.getICMS60().getCST() : "");
                        item.setOrigIcms(icms.getICMS60().getOrig() != null ? icms.getICMS60().getOrig() : "0");
                        item.setAliqFcpStRetido(icms.getICMS60().getPFCPSTRet() != null ? new BigDecimal(icms.getICMS60().getPFCPSTRet()) : new BigDecimal(0));
                        item.setAliqIcmsEfetivo(icms.getICMS60().getPICMSEfet() != null ? new BigDecimal(icms.getICMS60().getPICMSEfet()) : new BigDecimal(0));
                        item.setPercentRedBcEfetivo(icms.getICMS60().getPRedBCEfet() != null ? new BigDecimal(icms.getICMS60().getPRedBCEfet()) : new BigDecimal(0));
                        item.setPstIcms(icms.getICMS60().getPST() != null ? new BigDecimal(icms.getICMS60().getPST()) : new BigDecimal(0));
                        item.setBcIcmsEfetivo(icms.getICMS60().getVBCEfet() != null ? new BigDecimal(icms.getICMS60().getVBCEfet()) : new BigDecimal(0));
                        item.setBcFcpStRetido(icms.getICMS60().getVBCFCPSTRet() != null ? new BigDecimal(icms.getICMS60().getVBCFCPSTRet()) : new BigDecimal(0));
                        item.setBcIcmsStRetido(icms.getICMS60().getVBCSTRet() != null ? new BigDecimal(icms.getICMS60().getVBCSTRet()) : new BigDecimal(0));
                        item.setValorFcpStRetido(icms.getICMS60().getVFCPSTRet() != null ? new BigDecimal(icms.getICMS60().getVFCPSTRet()) : new BigDecimal(0));
                        item.setValorIcmsEfetivo(icms.getICMS60().getVICMSEfet() != null ? new BigDecimal(icms.getICMS60().getVICMSEfet()) : new BigDecimal(0));
                        item.setValorIcmsSubstituto(icms.getICMS60().getVICMSSubstituto() != null ? new BigDecimal(icms.getICMS60().getVICMSSubstituto()) : new BigDecimal(0));

                    } else if (icms.getICMS70() != null) {
                        item.setCstIcms(icms.getICMS70().getCST() != null ? icms.getICMS70().getCST() : "");
                        item.setModBc(icms.getICMS70().getModBC() != null ? icms.getICMS70().getModBC() : "0");
                        item.setModBcSt(icms.getICMS70().getModBCST() != null ? icms.getICMS70().getModBCST() : "0");
                        item.setMotDesIcms(icms.getICMS70().getMotDesICMS() != null ? icms.getICMS70().getMotDesICMS() : "0");
                        item.setOrigIcms(icms.getICMS70().getOrig() != null ? icms.getICMS70().getOrig() : "0");
                        item.setAliqFcp(icms.getICMS70().getPFCP() != null ? new BigDecimal(icms.getICMS70().getPFCP()) : new BigDecimal(0));
                        item.setAliqFcpSt(icms.getICMS70().getPFCPST() != null ? new BigDecimal(icms.getICMS70().getPFCPST()) : new BigDecimal(0));
                        item.setAliqIcms(icms.getICMS70().getPICMS() != null ? new BigDecimal(icms.getICMS70().getPICMS()) : new BigDecimal(0));
                        item.setAliqIcmsSt(icms.getICMS70().getPICMSST() != null ? new BigDecimal(icms.getICMS70().getPICMSST()) : new BigDecimal(0));
                        item.setPercentMargemIcmsSt(icms.getICMS70().getPMVAST() != null ? new BigDecimal(icms.getICMS70().getPMVAST()) : new BigDecimal(0));
                        item.setPercentRedBc(icms.getICMS70().getPRedBC() != null ? new BigDecimal(icms.getICMS70().getPRedBC()) : new BigDecimal(0));
                        item.setPercentRedBcSt(icms.getICMS70().getPRedBCST() != null ? new BigDecimal(icms.getICMS70().getPRedBCST()) : new BigDecimal(0));
                        item.setBcIcms(icms.getICMS70().getVBC() != null ? new BigDecimal(icms.getICMS70().getVBC()) : new BigDecimal(0));
                        item.setBcFcp(icms.getICMS70().getVBCFCP() != null ? new BigDecimal(icms.getICMS70().getVBCFCP()) : new BigDecimal(0));
                        item.setBcFcpSt(icms.getICMS70().getVBCFCPST() != null ? new BigDecimal(icms.getICMS70().getVBCFCPST()) : new BigDecimal(0));
                        item.setBcIcmsSt(icms.getICMS70().getVBCST() != null ? new BigDecimal(icms.getICMS70().getVBCST()) : new BigDecimal(0));
                        item.setValorFcp(icms.getICMS70().getVFCP() != null ? new BigDecimal(icms.getICMS70().getVFCP()) : new BigDecimal(0));
                        item.setValorFcpSt(icms.getICMS70().getVFCPST() != null ? new BigDecimal(icms.getICMS70().getVFCPST()) : new BigDecimal(0));
                        item.setValorIcms(icms.getICMS70().getVICMS() != null ? new BigDecimal(icms.getICMS70().getVICMS()) : new BigDecimal(0));
                        item.setValorIcmsDesonerado(icms.getICMS70().getVICMSDeson() != null ? new BigDecimal(icms.getICMS70().getVICMSDeson()) : new BigDecimal(0));
                        item.setValorIcmsSt(icms.getICMS70().getVICMSST() != null ? new BigDecimal(icms.getICMS70().getVICMSST()) : new BigDecimal(0));

                    } else if (icms.getICMS90() != null) {
                        item.setCstIcms(icms.getICMS90().getCST() != null ? icms.getICMS90().getCST() : "");
                        item.setModBc(icms.getICMS90().getModBC() != null ? icms.getICMS90().getModBC() : "0");
                        item.setModBcSt(icms.getICMS90().getModBCST() != null ? icms.getICMS90().getModBCST() : "0");
                        item.setMotDesIcms(icms.getICMS90().getMotDesICMS() != null ? icms.getICMS90().getMotDesICMS() : "0");
                        item.setOrigIcms(icms.getICMS90().getOrig() != null ? icms.getICMS90().getOrig() : "0");
                        item.setAliqFcp(icms.getICMS90().getPFCP() != null ? new BigDecimal(icms.getICMS90().getPFCP()) : new BigDecimal(0));
                        item.setAliqFcpSt(icms.getICMS90().getPFCPST() != null ? new BigDecimal(icms.getICMS90().getPFCPST()) : new BigDecimal(0));
                        item.setAliqIcms(icms.getICMS90().getPICMS() != null ? new BigDecimal(icms.getICMS90().getPICMS()) : new BigDecimal(0));
                        item.setAliqIcmsSt(icms.getICMS90().getPICMSST() != null ? new BigDecimal(icms.getICMS90().getPICMSST()) : new BigDecimal(0));
                        item.setPercentMargemIcmsSt(icms.getICMS90().getPMVAST() != null ? new BigDecimal(icms.getICMS90().getPMVAST()) : new BigDecimal(0));
                        item.setPercentRedBc(icms.getICMS90().getPRedBC() != null ? new BigDecimal(icms.getICMS90().getPRedBC()) : new BigDecimal(0));
                        item.setPercentRedBcSt(icms.getICMS90().getPRedBCST() != null ? new BigDecimal(icms.getICMS90().getPRedBCST()) : new BigDecimal(0));
                        item.setBcIcms(icms.getICMS90().getVBC() != null ? new BigDecimal(icms.getICMS90().getVBC()) : new BigDecimal(0));
                        item.setBcFcp(icms.getICMS90().getVBCFCP() != null ? new BigDecimal(icms.getICMS90().getVBCFCP()) : new BigDecimal(0));
                        item.setBcFcpSt(icms.getICMS90().getVBCFCPST() != null ? new BigDecimal(icms.getICMS90().getVBCFCPST()) : new BigDecimal(0));
                        item.setBcIcmsSt(icms.getICMS90().getVBCST() != null ? new BigDecimal(icms.getICMS90().getVBCST()) : new BigDecimal(0));
                        item.setValorFcp(icms.getICMS90().getVFCP() != null ? new BigDecimal(icms.getICMS90().getVFCP()) : new BigDecimal(0));
                        item.setValorFcpSt(icms.getICMS90().getVFCPST() != null ? new BigDecimal(icms.getICMS90().getVFCPST()) : new BigDecimal(0));
                        item.setValorIcms(icms.getICMS90().getVICMS() != null ? new BigDecimal(icms.getICMS90().getVICMS()) : new BigDecimal(0));
                        item.setValorIcmsDesonerado(icms.getICMS90().getVICMSDeson() != null ? new BigDecimal(icms.getICMS90().getVICMSDeson()) : new BigDecimal(0));
                        item.setValorIcmsSt(icms.getICMS90().getVICMSST() != null ? new BigDecimal(icms.getICMS90().getVICMSST()) : new BigDecimal(0));

                    } else if (icms.getICMSSN101() != null) {
                        item.setCsoSn(icms.getICMSSN101().getCSOSN() != null ? icms.getICMSSN101().getCSOSN() : "0");
                        item.setOrigIcms(icms.getICMSSN101().getOrig() != null ? icms.getICMSSN101().getOrig() : "0");
                        item.setAliqCredSn(icms.getICMSSN101().getPCredSN() != null ? new BigDecimal(icms.getICMSSN101().getPCredSN()) : new BigDecimal(0));
                        item.setValorCredIcmsSn(icms.getICMSSN101().getVCredICMSSN() != null ? new BigDecimal(icms.getICMSSN101().getVCredICMSSN()) : new BigDecimal(0));

                    } else if (icms.getICMSSN102() != null) {
                        item.setCsoSn(icms.getICMSSN102().getCSOSN() != null ? icms.getICMSSN102().getCSOSN() : "0");
                        item.setOrigIcms(icms.getICMSSN102().getOrig() != null ? icms.getICMSSN102().getOrig() : "0");

                    } else if (icms.getICMSSN201() != null) {
                        item.setCsoSn(icms.getICMSSN201().getCSOSN() != null ? icms.getICMSSN201().getCSOSN() : "0");
                        item.setModBcSt(icms.getICMSSN201().getModBCST() != null ? icms.getICMSSN201().getModBCST() : "0");
                        item.setOrigIcms(icms.getICMSSN201().getOrig() != null ? icms.getICMSSN201().getOrig() : "0");
                        item.setAliqCredSn(icms.getICMSSN201().getPCredSN() != null ? new BigDecimal(icms.getICMSSN201().getPCredSN()) : new BigDecimal(0));
                        item.setAliqFcpSt(icms.getICMSSN201().getPFCPST() != null ? new BigDecimal(icms.getICMSSN201().getPFCPST()) : new BigDecimal(0));
                        item.setAliqIcmsSt(icms.getICMSSN201().getPICMSST() != null ? new BigDecimal(icms.getICMSSN201().getPICMSST()) : new BigDecimal(0));
                        item.setPercentMargemIcmsSt(icms.getICMSSN201().getPMVAST() != null ? new BigDecimal(icms.getICMSSN201().getPMVAST()) : new BigDecimal(0));
                        item.setPercentRedBcSt(icms.getICMSSN201().getPRedBCST() != null ? new BigDecimal(icms.getICMSSN201().getPRedBCST()) : new BigDecimal(0));
                        item.setBcFcpSt(icms.getICMSSN201().getVBCFCPST() != null ? new BigDecimal(icms.getICMSSN201().getVBCFCPST()) : new BigDecimal(0));
                        item.setBcIcmsSt(icms.getICMSSN201().getVBCST() != null ? new BigDecimal(icms.getICMSSN201().getVBCST()) : new BigDecimal(0));
                        item.setValorCredIcmsSn(icms.getICMSSN201().getVCredICMSSN() != null ? new BigDecimal(icms.getICMSSN201().getVCredICMSSN()) : new BigDecimal(0));
                        item.setValorFcpSt(icms.getICMSSN201().getVFCPST() != null ? new BigDecimal(icms.getICMSSN201().getVFCPST()) : new BigDecimal(0));
                        item.setValorIcmsSt(icms.getICMSSN201().getVICMSST() != null ? new BigDecimal(icms.getICMSSN201().getVICMSST()) : new BigDecimal(0));

                    } else if (icms.getICMSSN202() != null) {
                        item.setCsoSn(icms.getICMSSN202().getCSOSN() != null ? icms.getICMSSN202().getCSOSN() : "0");
                        item.setModBcSt(icms.getICMSSN202().getModBCST() != null ? icms.getICMSSN202().getModBCST() : "0");
                        item.setOrigIcms(icms.getICMSSN202().getOrig() != null ? icms.getICMSSN202().getOrig() : "0");
                        item.setAliqFcpSt(icms.getICMSSN202().getPFCPST() != null ? new BigDecimal(icms.getICMSSN202().getPFCPST()) : new BigDecimal(0));
                        item.setAliqIcmsSt(icms.getICMSSN202().getPICMSST() != null ? new BigDecimal(icms.getICMSSN202().getPICMSST()) : new BigDecimal(0));
                        item.setPercentMargemIcmsSt(icms.getICMSSN202().getPMVAST() != null ? new BigDecimal(icms.getICMSSN202().getPMVAST()) : new BigDecimal(0));
                        item.setPercentRedBcSt(icms.getICMSSN202().getPRedBCST() != null ? new BigDecimal(icms.getICMSSN202().getPRedBCST()) : new BigDecimal(0));
                        item.setBcFcpSt(icms.getICMSSN202().getVBCFCPST() != null ? new BigDecimal(icms.getICMSSN202().getVBCFCPST()) : new BigDecimal(0));
                        item.setBcIcmsSt(icms.getICMSSN202().getVBCST() != null ? new BigDecimal(icms.getICMSSN202().getVBCST()) : new BigDecimal(0));
                        item.setValorFcpSt(icms.getICMSSN202().getVFCPST() != null ? new BigDecimal(icms.getICMSSN202().getVFCPST()) : new BigDecimal(0));
                        item.setValorIcmsSt(icms.getICMSSN202().getVICMSST() != null ? new BigDecimal(icms.getICMSSN202().getVICMSST()) : new BigDecimal(0));

                    } else if (icms.getICMSSN500() != null) {
                        item.setCsoSn(icms.getICMSSN500().getCSOSN() != null ? icms.getICMSSN500().getCSOSN() : "0");
                        item.setOrigIcms(icms.getICMSSN500().getOrig() != null ? icms.getICMSSN500().getOrig() : "0");
                        item.setAliqFcpStRetido(icms.getICMSSN500().getPFCPSTRet() != null ? new BigDecimal(icms.getICMSSN500().getPFCPSTRet()) : new BigDecimal(0));
                        item.setAliqIcmsEfetivo(icms.getICMSSN500().getPICMSEfet() != null ? new BigDecimal(icms.getICMSSN500().getPICMSEfet()) : new BigDecimal(0));
                        item.setPercentRedBcEfetivo(icms.getICMSSN500().getPRedBCEfet() != null ? new BigDecimal(icms.getICMSSN500().getPRedBCEfet()) : new BigDecimal(0));
                        item.setPstIcms(icms.getICMSSN500().getPST() != null ? new BigDecimal(icms.getICMSSN500().getPST()) : new BigDecimal(0));
                        item.setBcIcmsEfetivo(icms.getICMSSN500().getVBCEfet() != null ? new BigDecimal(icms.getICMSSN500().getVBCEfet()) : new BigDecimal(0));
                        item.setBcFcpStRetido(icms.getICMSSN500().getVBCFCPSTRet() != null ? new BigDecimal(icms.getICMSSN500().getVBCFCPSTRet()) : new BigDecimal(0));
                        item.setBcIcmsStRetido(icms.getICMSSN500().getVBCSTRet() != null ? new BigDecimal(icms.getICMSSN500().getVBCSTRet()) : new BigDecimal(0));
                        item.setValorFcpStRetido(icms.getICMSSN500().getVFCPSTRet() != null ? new BigDecimal(icms.getICMSSN500().getVFCPSTRet()) : new BigDecimal(0));
                        item.setValorIcmsEfetivo(icms.getICMSSN500().getVICMSEfet() != null ? new BigDecimal(icms.getICMSSN500().getVICMSEfet()) : new BigDecimal(0));
                        item.setValorIcmsStRetido(icms.getICMSSN500().getVICMSSTRet() != null ? new BigDecimal(icms.getICMSSN500().getVICMSSTRet()) : new BigDecimal(0));
                        item.setValorIcmsSubstituto(icms.getICMSSN500().getVICMSSubstituto() != null ? new BigDecimal(icms.getICMSSN500().getVICMSSubstituto()) : new BigDecimal(0));

                    } else if (icms.getICMSSN900() != null) {
                        item.setCsoSn(icms.getICMSSN900().getCSOSN() != null ? icms.getICMSSN900().getCSOSN() : "0");
                        item.setModBc(icms.getICMSSN900().getModBC() != null ? icms.getICMSSN900().getModBC() : "0");
                        item.setModBcSt(icms.getICMSSN900().getModBCST() != null ? icms.getICMSSN900().getModBCST() : "0");
                        item.setOrigIcms(icms.getICMSSN900().getOrig() != null ? icms.getICMSSN900().getOrig() : "0");
                        item.setAliqCredSn(icms.getICMSSN900().getPCredSN() != null ? new BigDecimal(icms.getICMSSN900().getPCredSN()) : new BigDecimal(0));
                        item.setAliqFcpSt(icms.getICMSSN900().getPFCPST() != null ? new BigDecimal(icms.getICMSSN900().getPFCPST()) : new BigDecimal(0));
                        item.setAliqIcms(icms.getICMSSN900().getPICMS() != null ? new BigDecimal(icms.getICMSSN900().getPICMS()) : new BigDecimal(0));
                        item.setAliqIcmsSt(icms.getICMSSN900().getPICMSST() != null ? new BigDecimal(icms.getICMSSN900().getPICMSST()) : new BigDecimal(0));
                        item.setPercentMargemIcmsSt(icms.getICMSSN900().getPMVAST() != null ? new BigDecimal(icms.getICMSSN900().getPMVAST()) : new BigDecimal(0));
                        item.setPercentRedBc(icms.getICMSSN900().getPRedBC() != null ? new BigDecimal(icms.getICMSSN900().getPRedBC()) : new BigDecimal(0));
                        item.setPercentRedBcSt(icms.getICMSSN900().getPRedBCST() != null ? new BigDecimal(icms.getICMSSN900().getPRedBCST()) : new BigDecimal(0));
                        item.setBcIcms(icms.getICMSSN900().getVBC() != null ? new BigDecimal(icms.getICMSSN900().getVBC()) : new BigDecimal(0));
                        item.setValorCredIcmsSn(icms.getICMSSN900().getVCredICMSSN() != null ? new BigDecimal(icms.getICMSSN900().getVCredICMSSN()) : new BigDecimal(0));
                        item.setValorFcpSt(icms.getICMSSN900().getVFCPST() != null ? new BigDecimal(icms.getICMSSN900().getVFCPST()) : new BigDecimal(0));
                        item.setValorIcms(icms.getICMSSN900().getVICMS() != null ? new BigDecimal(icms.getICMSSN900().getVICMS()) : new BigDecimal(0));
                        item.setValorIcmsSt(icms.getICMSSN900().getVICMSST() != null ? new BigDecimal(icms.getICMSSN900().getVICMSST()) : new BigDecimal(0));

                    } else if (icms.getICMSST() != null) {
                        item.setCstIcms(icms.getICMSST().getCST() != null ? icms.getICMSST().getCST() : "");
                        item.setOrigIcms(icms.getICMSST().getOrig() != null ? icms.getICMSST().getOrig() : "0");
                        item.setAliqFcpStRetido(icms.getICMSST().getPFCPSTRet() != null ? new BigDecimal(icms.getICMSST().getPFCPSTRet()) : new BigDecimal(0));
                        item.setAliqIcmsEfetivo(icms.getICMSST().getPICMSEfet() != null ? new BigDecimal(icms.getICMSST().getPICMSEfet()) : new BigDecimal(0));
                        item.setPercentRedBcEfetivo(icms.getICMSST().getPRedBCEfet() != null ? new BigDecimal(icms.getICMSST().getPRedBCEfet()) : new BigDecimal(0));
                        item.setPstIcms(icms.getICMSST().getPST() != null ? new BigDecimal(icms.getICMSST().getPST()) : new BigDecimal(0));
                        item.setBcIcmsEfetivo(icms.getICMSST().getVBCEfet() != null ? new BigDecimal(icms.getICMSST().getVBCEfet()) : new BigDecimal(0));
                        item.setBcFcpStRetido(icms.getICMSST().getVBCFCPSTRet() != null ? new BigDecimal(icms.getICMSST().getVBCFCPSTRet()) : new BigDecimal(0));
                        item.setBcIcmsStDestino(icms.getICMSST().getVBCSTDest() != null ? new BigDecimal(icms.getICMSST().getVBCSTDest()) : new BigDecimal(0));
                        item.setBcIcmsStRetido(icms.getICMSST().getVBCSTRet() != null ? new BigDecimal(icms.getICMSST().getVBCSTRet()) : new BigDecimal(0));
                        item.setValorIcmsEfetivo(icms.getICMSST().getVICMSEfet() != null ? new BigDecimal(icms.getICMSST().getVICMSEfet()) : new BigDecimal(0));
                        item.setValorIcmsStDestino(icms.getICMSST().getVICMSSTDest() != null ? new BigDecimal(icms.getICMSST().getVICMSSTDest()) : new BigDecimal(0));
                        item.setValorIcmsStRetido(icms.getICMSST().getVICMSSTRet() != null ? new BigDecimal(icms.getICMSST().getVICMSSTRet()) : new BigDecimal(0));
                        item.setValorIcmsSubstituto(icms.getICMSST().getVICMSSubstituto() != null ? new BigDecimal(icms.getICMSST().getVICMSSubstituto()) : new BigDecimal(0));

                    } else if (icms.getICMSPart() != null) {
                        item.setCstIcms(icms.getICMSPart().getCST() != null ? icms.getICMSPart().getCST() : "");
                        item.setModBc(icms.getICMSPart().getModBC() != null ? icms.getICMSPart().getModBC() : "0");
                        item.setModBcSt(icms.getICMSPart().getModBCST() != null ? icms.getICMSPart().getModBCST() : "0");
                        item.setOrigIcms(icms.getICMSPart().getOrig() != null ? icms.getICMSPart().getOrig() : "0");
                        item.setPercentBcOperacao(icms.getICMSPart().getPBCOp() != null ? new BigDecimal(icms.getICMSPart().getPBCOp()) : new BigDecimal(0));
                        item.setAliqIcms(icms.getICMSPart().getPICMS() != null ? new BigDecimal(icms.getICMSPart().getPICMS()) : new BigDecimal(0));
                        item.setAliqIcmsSt(icms.getICMSPart().getPICMSST() != null ? new BigDecimal(icms.getICMSPart().getPICMSST()) : new BigDecimal(0));
                        item.setPercentMargemIcmsSt(icms.getICMSPart().getPMVAST() != null ? new BigDecimal(icms.getICMSPart().getPMVAST()) : new BigDecimal(0));
                        item.setPercentRedBc(icms.getICMSPart().getPRedBC() != null ? new BigDecimal(icms.getICMSPart().getPRedBC()) : new BigDecimal(0));
                        item.setPercentRedBcSt(icms.getICMSPart().getPRedBCST() != null ? new BigDecimal(icms.getICMSPart().getPRedBCST()) : new BigDecimal(0));
                        item.setUfIcmsSt(icms.getICMSPart().getUFST() != null ? icms.getICMSPart().getUFST().value() : "");
                        item.setBcIcms(icms.getICMSPart().getVBC() != null ? new BigDecimal(icms.getICMSPart().getVBC()) : new BigDecimal(0));
                        item.setBcIcmsSt(icms.getICMSPart().getVBCST() != null ? new BigDecimal(icms.getICMSPart().getVBCST()) : new BigDecimal(0));
                        item.setValorIcms(icms.getICMSPart().getVICMS() != null ? new BigDecimal(icms.getICMSPart().getVICMS()) : new BigDecimal(0));
                        item.setValorIcmsSt(icms.getICMSPart().getVICMSST() != null ? new BigDecimal(icms.getICMSPart().getVICMSST()) : new BigDecimal(0));
                    }
                }

                if (pis != null) {
                    if (pis.getPISAliq() != null) {
                        item.setCstPis(pis.getPISAliq().getCST() != null ? pis.getPISAliq().getCST() : "");
                        item.setAliqPis(pis.getPISAliq().getPPIS() != null ? new BigDecimal(pis.getPISAliq().getPPIS()) : new BigDecimal(0));
                        item.setBcPis(pis.getPISAliq().getVBC() != null ? new BigDecimal(pis.getPISAliq().getVBC()) : new BigDecimal(0));
                        item.setValorPis(pis.getPISAliq().getVPIS() != null ? new BigDecimal(pis.getPISAliq().getVPIS()) : new BigDecimal(0));

                    } else if (pis.getPISNT() != null) {
                        item.setCstPis(pis.getPISNT().getCST() != null ? pis.getPISNT().getCST() : "");

                    } else if (pis.getPISOutr() != null) {
                        item.setCstPis(pis.getPISOutr().getCST() != null ? pis.getPISOutr().getCST() : "");

                    } else if (pis.getPISQtde() != null) {
                        item.setCstPis(pis.getPISQtde().getCST() != null ? pis.getPISQtde().getCST() : "");
                    }
                }

                if (pisSt != null) {
                    pisSt.getPPIS();
                    pisSt.getQBCProd();
                    pisSt.getVAliqProd();
                    pisSt.getVBC();
                    pisSt.getVPIS();
                }

                if (cofins != null) {
                    if (cofins.getCOFINSOutr() != null) {
                        item.setCstCofins(cofins.getCOFINSOutr().getCST() != null ? cofins.getCOFINSOutr().getCST() : "");
                        item.setAliqCofins(cofins.getCOFINSOutr().getPCOFINS() != null ? new BigDecimal(cofins.getCOFINSOutr().getPCOFINS()) : new BigDecimal(0));
                        item.setQuantVendidaCofins(cofins.getCOFINSOutr().getQBCProd() != null ? new BigDecimal(cofins.getCOFINSOutr().getQBCProd()) : new BigDecimal(0));
                        item.setValorAliqCofinsRs(cofins.getCOFINSOutr().getVAliqProd() != null ? new BigDecimal(cofins.getCOFINSOutr().getVAliqProd()) : new BigDecimal(0));
                        item.setBcCofins(cofins.getCOFINSOutr().getVBC() != null ? new BigDecimal(cofins.getCOFINSOutr().getVBC()) : new BigDecimal(0));
                        item.setValorCofins(cofins.getCOFINSOutr().getVCOFINS() != null ? new BigDecimal(cofins.getCOFINSOutr().getVCOFINS()) : new BigDecimal(0));

                    } else if (cofins.getCOFINSNT() != null) {
                        item.setCstCofins(cofins.getCOFINSNT().getCST() != null ? cofins.getCOFINSNT().getCST() : "");

                    } else if (cofins.getCOFINSAliq() != null) {
                        item.setCstCofins(cofins.getCOFINSAliq().getCST() != null ? cofins.getCOFINSAliq().getCST() : "");

                    } else if (cofins.getCOFINSQtde() != null) {
                        item.setCstCofins(cofins.getCOFINSQtde().getCST() != null ? cofins.getCOFINSQtde().getCST() : "");
                    }
                }

                if (cofinsSt != null) {
                    cofinsSt.getPCOFINS();
                    cofinsSt.getQBCProd();
                    cofinsSt.getVAliqProd();
                    cofinsSt.getVBC();
                    cofinsSt.getVCOFINS();
                }

                if (ipi != null) {
                    item.setEnquadramentoIpi(ipi.getCEnq() != null ? ipi.getCEnq() : "");
                    item.setCnpjProdIpi(ipi.getCNPJProd() != null ? ipi.getCNPJProd() : "");
                    item.setCodigoSeloIpi(ipi.getCSelo() != null ? ipi.getCSelo() : "");
                    item.setQuantidadeSeloIpi(ipi.getQSelo() != null ? new BigDecimal(ipi.getQSelo()) : new BigDecimal(0));

                    if (ipi.getIPINT() != null) {
                        item.setCstIpi(ipi.getIPINT().getCST() != null ? ipi.getIPINT().getCST() : "");

                    } else if (ipi.getIPITrib() != null) {
                        item.setCstIpi(ipi.getIPITrib().getCST() != null ? ipi.getIPITrib().getCST() : "");
                        item.setAliqIpi(ipi.getIPITrib().getPIPI() != null ? new BigDecimal(ipi.getIPITrib().getPIPI()) : new BigDecimal(0));
                        item.setQuantUniIpi(ipi.getIPITrib().getQUnid() != null ? new BigDecimal(ipi.getIPITrib().getQUnid()) : new BigDecimal(0));
                        item.setBcIpi(ipi.getIPITrib().getVBC() != null ? new BigDecimal(ipi.getIPITrib().getVBC()) : new BigDecimal(0));
                        item.setValorIpi(ipi.getIPITrib().getVIPI() != null ? new BigDecimal(ipi.getIPITrib().getVIPI()) : new BigDecimal(0));
                        item.setValorUniIpi(ipi.getIPITrib().getVUnid() != null ? new BigDecimal(ipi.getIPITrib().getVUnid()) : new BigDecimal(0));
                    }
                }
            }

            item.setNumeroNotaFiscal(numeroDaNota);
            itens.add(item);
        });

        return itens;
    }

    private ImportarXml salvarNotaFiscal(ImportarXml importarXml){

        boolean verificaChave = importarXmlRepository.existsByChave(importarXml.getChave());
           if(verificaChave){
                throw new DataIntegrityViolationException("A nota fiscal " + importarXml.getNumero() + " já foi importada");
           }
        return importarXmlRepository.save(importarXml);
    }

    public List<ImportarXmlItens> salvarNotaFiscalItens(List<ImportarXmlItens> importarXmlItensList) {
        return itensNotaFiscalRepository.saveAll(importarXmlItensList);
    }

    public void delete(Long id) {
        itensNotaFiscalRepository.deleteById(id);
    }

    public List<ImportarXml> findAllByYear(Integer anoAtual) {
        return importarXmlRepository.findAllByYear(anoAtual);
    }

    public List<ImportarXml> findAllEntradaPeriodo(Date dataInicial, Date dataFinal) {
        return importarXmlRepository.findAllEntradaPeriodo(dataInicial, dataFinal);
    }

    /**
     * Aqui verifica se a nota é emitida por mim ou por outra empresa
     */
    private boolean isNotaEntrada(ImportarXml importarXml) {
        // Verifica se a nota foi emitida por você (comparando CNPJ)
        String cnpjEmitente = importarXml.getDocumentoEmitente();
        Optional<Empresa> minhaEmpresa  = empresaRepository.findByCnpj(cnpjEmitente);

        boolean foiEmitidaPorVoce = minhaEmpresa .isPresent();

        // - Se foi emitida por você: tpNF=1 é SAÍDA (venda) => retorna false (não é entrada)
        // - Se foi emitida por outro: tpNF=1 é ENTRADA (compra) => retorna true
        return foiEmitidaPorVoce ?
                "0".equals(importarXml.getTipo()) :  // Se sua nota é tpNF=0 (entrada para você)
                "1".equals(importarXml.getTipo());   // Se nota de terceiro é tpNF=1 (entrada para você)
    }

    private LancamentoContabil lancamentoContabil(ImportarXml importarXml) {

        Contas contaDebito;
        Contas contaCredito;
        HistoricoPadrao historico;

        LancamentoContabil lancamento = new LancamentoContabil();
        lancamento.setDataLancamento(importarXml.getDataImportacao());
        lancamento.setValor(importarXml.getValorTotal());

        // Verifica se a nota é de entrada (compra): tpNF = 1 significa saída do fornecedor, entrada para a empresa
        boolean isEntrada = isNotaEntrada(importarXml);

        if (isEntrada) {
            // Nota de ENTRADA (compra feita pela empresa)
            contaDebito = contasService.findByNome("Estoque");
            contaCredito = contasService.findByNome(importarXml.getRazaoSocialEmitente());
            historico = historicoPadraoService.findByDescricao("Compra de Mercadorias");

            String complemento = "NF " + importarXml.getNumero()
                    + " - Fornecedor: " + (importarXml.getRazaoSocialEmitente() != null ? importarXml.getRazaoSocialEmitente() : "Desconhecido")
                    + " - Chave: " + importarXml.getChave();
            lancamento.setComplementoHistorico(complemento);

        } else {
            // Nota de SAÍDA (venda feita pela empresa)
            contaDebito = contasService.findByNome(importarXml.getRazaoSocialDestinatario());
            contaCredito = contasService.findByNome("Receita Bruta De Vendas E Mercadorias");
            historico = historicoPadraoService.findByDescricao("Venda de Mercadorias");

            String complemento = "NF " + importarXml.getNumero()
                    + " - Cliente: " + (importarXml.getRazaoSocialDestinatario() != null ? importarXml.getRazaoSocialDestinatario() : "Desconhecido")
                    + " - Chave: " + importarXml.getChave();
            lancamento.setComplementoHistorico(complemento);
        }

        lancamento.setContaDebito(contaDebito);
        lancamento.setContaCredito(contaCredito);
        lancamento.setHistoricoPadrao(historico);
        lancamento.setNotaFiscalOrigem(importarXml);

        return lancamentoContabilRepository.save(lancamento);
    }

    public Long findByNumeroAndRazaoSocialEmitente(String numero, String razaoSocialEmitente) {
        Optional<Long> objNotaId = importarXmlRepository.findIdByNumeroAndRazaoSocialEmitente(numero, razaoSocialEmitente);
        return objNotaId.orElseThrow(() -> new ObjectNotFoundException("Nota fiscal não encontrada para o emissor "+razaoSocialEmitente));
    }

    /**
     * Realiza o Lançamento Do Título da Nota Fiscal nos Lançamentos de Conta a Receber
     */
    private void lancaContasPagar(ImportarXml importarXml) {

        try {
            // Primeiro verifica se é uma nota de entrada (compra)
            if (!isNotaEntrada(importarXml)) {
                logger.info("Nota fiscal {} é de saída (venda), não será lançada como conta a pagar", importarXml.getNumero());
                return;
            }

            // Obtém as duplicatas do XML original
            TNfeProc nfe = XmlNfeUtil.xmlToObject(importarXml.getXml(), TNfeProc.class);

            if (nfe.getNFe().getInfNFe().getCobr() == null ||
                    nfe.getNFe().getInfNFe().getCobr().getDup() == null ||
                    nfe.getNFe().getInfNFe().getCobr().getDup().isEmpty()) {
                logger.info("Nota fiscal {} não possui duplicatas para lançar como contas a pagar", importarXml.getNumero());
                return;
            }

            // Obtém a lista de duplicatas
            List<TNFe.InfNFe.Cobr.Dup> duplicatas = nfe.getNFe().getInfNFe().getCobr().getDup();
            int quantidadeParcelas = duplicatas.size();

            for (TNFe.InfNFe.Cobr.Dup duplicata : duplicatas) {
                LocalDate dataAtual = LocalDate.now();
                ContaPagarDto contaPagarDto = new ContaPagarDto();

                contaPagarDto.setId(null);
                contaPagarDto.setDescricao("Compra Realizada: Parcela " + duplicata.getNDup() + " - Nota Fiscal #" + importarXml.getNumero());
                contaPagarDto.setCategoria("Despesa Variável");
                contaPagarDto.setDataEmissao(Date.valueOf(dataAtual));
                contaPagarDto.setPagoA(importarXml.getRazaoSocialDestinatario());
                contaPagarDto.setNumeroDocumento(importarXml.getNumero() + "/" + duplicata.getNDup());
                contaPagarDto.setRepete("Não");
                contaPagarDto.setDataVencimento(Date.valueOf(LocalDate.parse(duplicata.getDVenc().substring(0, 10))));
                contaPagarDto.setDataCompetencia(Date.valueOf(importarXml.getEmissao()));
                contaPagarDto.setQuantidadeParcelas(quantidadeParcelas);
                contaPagarDto.setValor(new BigDecimal(duplicata.getVDup()));
                contaPagarDto.setSituacao("A Pagar");
                contaPagarDto.setMetodoPagamento(null);

                ContaPagar contaPagar = new ContaPagar(contaPagarDto);
                contaPagarRepository.save(contaPagar);

                logger.info("Lançada conta a pagar {}/{} da nota {} no valor de {}",
                        duplicata.getNDup(), quantidadeParcelas, importarXml.getNumero(), duplicata.getVDup());
            }
        } catch (JAXBException e) {
            logger.error("Erro ao parsear XML para obter duplicatas da nota {}: {}",
                    importarXml.getNumero(), e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado ao lançar contas a pagar para nota {}: {}",
                    importarXml.getNumero(), e.getMessage());
        }
    }
}
