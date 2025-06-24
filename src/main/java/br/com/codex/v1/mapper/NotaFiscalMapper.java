package br.com.codex.v1.mapper;

import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.dto.NotaFiscalDuplicatasDto;
import br.com.codex.v1.domain.dto.NotaFiscalItemDto;
import br.com.swconsultoria.nfe.schema_4.enviNFe.*;
import br.com.swconsultoria.nfe.util.ConstantesUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotaFiscalMapper {

    static TUf ufDestinatario = null;

    public static TNFe paraTNFe(NotaFiscalDto dto) {
        ObjectFactory factory = new ObjectFactory();
        TNFe tnFe = factory.createTNFe();

        TNFe.InfNFe infNFe = new TNFe.InfNFe();
        infNFe.setVersao(ConstantesUtil.VERSAO.NFE);
        infNFe.setId("NFe" + dto.getChave());

        // Identificação (IDE)
        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();
        ide.setCUF(dto.getCodigoUf());
        ide.setNatOp(dto.getNaturezaOperacao());
        ide.setMod(dto.getModelo());
        ide.setSerie(dto.getSerie());
        ide.setNNF(dto.getNumero());
        ide.setDhEmi(dto.getEmissao().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        // Gera a data/hora ATUAL no formato desejado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dataHoraAtualFormatada = LocalDateTime.now().format(formatter);
        ide.setDhSaiEnt(dataHoraAtualFormatada);

        ide.setTpNF(dto.getTipo());
        ide.setIdDest(dto.getLocalDestino());
        ide.setCMunFG(dto.getCodigoMunicipioEmitente());
        ide.setFinNFe(dto.getIndicadorFinal().toString());
        ide.setIndPres(dto.getIndicadorPresenca());
        ide.setIndIntermed(dto.getIndicadorIntermediario());
        ide.setProcEmi("0"); // Emissão própria
        ide.setVerProc("1.0");
        if ("65".equals(dto.getModelo())) {
            ide.setIndPres("1"); // Operação presencial para NFC-e
            ide.setIndFinal("1"); // Consumidor final
        }
        infNFe.setIde(ide);

        // Emitente
        TNFe.InfNFe.Emit emit = new TNFe.InfNFe.Emit();
        emit.setCNPJ(dto.getDocumentoEmitente());
        emit.setXNome(dto.getRazaoSocialEmitente());
        emit.setXFant(dto.getNomeFantasiaEmitente());
        emit.setIE(dto.getInscricaoEstadualEmitente());
        emit.setIEST(dto.getInscricaoEstadualStEmitente() != null ? dto.getInscricaoEstadualStEmitente().toString() : null);
        emit.setIM(dto.getInscricaoMunicipalEmitente() != null ? dto.getInscricaoMunicipalEmitente().toString() : null);
        emit.setCNAE(dto.getCnaeEmitente());
        emit.setCRT(dto.getRegimeTributarioEmitente().toString());
        TEnderEmi enderEmit = new TEnderEmi();
        enderEmit.setXLgr(dto.getLogradouroEmitente());
        enderEmit.setNro(dto.getNumeroEnderecoEmitente());
        enderEmit.setXBairro(dto.getBairroEmitente());
        enderEmit.setCMun(dto.getCodigoMunicipioEmitente());
        enderEmit.setXMun(dto.getNomeMunicipioEmitente());
        enderEmit.setUF(TUfEmi.fromValue(dto.getUfEmitente()));
        enderEmit.setCEP(dto.getCepEmitente());
        enderEmit.setFone(dto.getTelefoneEmitente());
        emit.setEnderEmit(enderEmit);
        infNFe.setEmit(emit);

        // Destinatário (opcional para NFC-e)
        if (!"65".equals(dto.getModelo()) || dto.getDocumentoDestinatario() != null) {
            TNFe.InfNFe.Dest dest = new TNFe.InfNFe.Dest();
            dest.setCNPJ(dto.getDocumentoDestinatario());
            dest.setXNome(dto.getRazaoSocialDestinatario());
            dest.setIE(dto.getInscricaoEstadualDestinatario());
            dest.setIndIEDest(dto.getIndicadorInscricaoEstadualDestinatario().toString());
            dest.setEmail(dto.getEmailDestinatario());
            TEndereco enderDest = new TEndereco();
            enderDest.setXLgr(dto.getLogradouroDestinatario());
            enderDest.setNro(dto.getNumeroEnderecoDestinatario());
            enderDest.setXBairro(dto.getBairroDestinatario());
            enderDest.setCMun(dto.getCodigoMunicipioDestinatario());
            enderDest.setXMun(dto.getNomeMunicipioDestinatario());
            enderDest.setUF(TUf.fromValue(dto.getUfDestinatario()));
            enderDest.setCEP(dto.getCepDestinatario());
            enderDest.setCPais(dto.getCodigoPaisDestinatario());
            enderDest.setXPais(dto.getPaisDestinatario());
            enderDest.setFone(dto.getTelefoneDestinatario());
            dest.setEnderDest(enderDest);
            infNFe.setDest(dest);
        }

        // Produtos/Itens
        List<NotaFiscalItemDto> itens = dto.getItens();
        for (int i = 0; i < itens.size(); i++) {
            NotaFiscalItemDto item = itens.get(i);
            TNFe.InfNFe.Det det = new TNFe.InfNFe.Det();
            det.setNItem(String.valueOf(i + 1));

            TNFe.InfNFe.Det.Prod prod = new TNFe.InfNFe.Det.Prod();
            prod.setCProd(item.getCodigoProduto());
            prod.setXProd(item.getNomeProduto());
            prod.setCEAN(item.getCEAN());
            prod.setNCM(item.getNcmSh());
            prod.setCEST(item.getCest());
            prod.setCFOP(item.getCfop());
            prod.setUCom(item.getUnidadeComercial());
            prod.setQCom(item.getQuantidadeComercial().toString());
            prod.setVUnCom(item.getValorUnitarioComercial().toString());
            prod.setVProd(item.getValorTotalProdutos().toString());
            prod.setUTrib(item.getUnidadeTributacao());
            prod.setQTrib(item.getQuantidadeTributacao().toString());
            prod.setVUnTrib(item.getValorUnitarioTributacao().toString());
            prod.setVFrete(item.getValorFrete() != null ? item.getValorFrete().toString() : "0.00");
            prod.setVSeg(item.getValorSeguro() != null ? item.getValorSeguro().toString() : "0.00");
            prod.setVDesc(item.getValorDesconto() != null ? item.getValorDesconto().toString() : "0.00");
            prod.setVOutro(item.getValorOutro() != null ? item.getValorOutro().toString() : "0.00");
            prod.setIndTot("1");
            prod.setXPed(item.getPedidoCompra());
            det.setProd(prod);

            // Impostos
            TNFe.InfNFe.Det.Imposto imposto = new TNFe.InfNFe.Det.Imposto();
            TNFe.InfNFe.Det.Imposto.ICMS icms = new TNFe.InfNFe.Det.Imposto.ICMS();
            String cst = item.getCstIcms();
            switch (cst) {
                case "00" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS00 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS00();
                    obj.setOrig(item.getOrigIcms() != null ? item.getValorOutro().toString() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc() != null ? item.getValorOutro().toString() : "0.00");
                    obj.setVBC(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setPICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setVICMS(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setPFCP(String.valueOf(item.getAliqFcp()) != null ? item.getAliqFcp().toString() : "0.00");
                    obj.setVFCP(String.valueOf(item.getValorFcp()) != null ? item.getValorFcp().toString() : "0.00");
                    icms.setICMS00(obj);
                }

                case "02" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS02 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS02();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setAdRemICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setQBCMono(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setVICMSMono(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    icms.setICMS02(obj);
                }

                case "10" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS10 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS10();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc() != null ? item.getModBc() : "0.00");
                    obj.setVBC(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setPICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()) != null ? item.getPercentMargemIcmsSt().toString() : "0.00");
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()) != null ? item.getPercentRedBcSt().toString() : "0.00");
                    obj.setVICMS(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setPFCP(String.valueOf(item.getAliqFcp()) != null ? item.getAliqFcp().toString() : "0.00");
                    obj.setPFCPST(null);
                    obj.setVFCP(String.valueOf(item.getValorFcp()) != null ? item.getValorFcp().toString() : "0.00");
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()) != null ? item.getBcFcp().toString() : "0.00");
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()) != null ? item.getBcFcpSt().toString() : "0.00");
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()) != null ? item.getBcIcmsSt().toString() : "0.00");
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()) != null ? item.getValorFcpSt().toString() : "0.00");
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()) != null ? item.getValorIcmsSt().toString() : "0.00");
                    obj.setVICMSSTDeson(String.valueOf(item.getValorIcmsDesonerado()) != null ? item.getValorIcmsDesonerado().toString() : "0.00");
                    obj.setModBCST(String.valueOf(item.getModBc()) != null ? item.getModBc() : "0.00");
                    obj.setMotDesICMSST(String.valueOf(item.getMotDesIcms()) != null ? item.getMotDesIcms() : "0.00");
                    icms.setICMS10(obj);
                }

                case "15" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS15 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS15();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms().toString() : "0.00");
                    obj.setCST(cst);
                    obj.setAdRemICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setQBCMono(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setVICMSMono(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setAdRemICMSReten(null);
                    obj.setQBCMonoReten(null);
                    obj.setVICMSMonoReten(null);
                    obj.setPRedAdRem(null);
                    obj.setQBCMonoReten(null);
                    icms.setICMS15(obj);
                }

                case "20" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS20 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS20();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms().toString() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc() != null ? item.getValorOutro().toString() : "0.00");
                    obj.setVBC(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setPICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setPFCP(String.valueOf(item.getAliqFcp()) != null ? item.getAliqFcp().toString() : "0.00");
                    obj.setIndDeduzDeson(String.valueOf(item.getValorIcmsDesonerado()) != null ? item.getValorIcmsDesonerado().toString() : "0.00");
                    obj.setMotDesICMS(String.valueOf(item.getMotDesIcms()) != null ? item.getMotDesIcms().toString() : "0.00");
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()) != null ? item.getPercentRedBc().toString() : "0.00");
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()) != null ? item.getBcFcp().toString() : "0.00");
                    obj.setVICMS(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setVICMSDeson(String.valueOf(item.getValorIcmsDesonerado()) != null ? item.getValorIcmsDesonerado().toString() : "0.00");
                    icms.setICMS20(obj);
                }

                case "30" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS30 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS30();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms().toString() : "0.00");
                    obj.setCST(cst);
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()) != null ? item.getBcFcpSt().toString() : "0.00");
                    obj.setModBCST(null);
                    obj.setIndDeduzDeson(String.valueOf(item.getValorIcmsDesonerado()) != null ? item.getValorIcmsDesonerado().toString() : "0.00");
                    obj.setMotDesICMS(String.valueOf(item.getMotDesIcms()) != null ? item.getMotDesIcms().toString() : "0.00");
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()) != null ? item.getPercentMargemIcmsSt().toString() : "0.00");
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()) != null ? item.getPercentRedBcSt().toString() : "0.00");
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()) != null ? item.getBcIcmsSt().toString() : "0.00");
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()) != null ? item.getValorFcpSt().toString() : "0.00");
                    obj.setVICMSDeson(String.valueOf(item.getValorIcmsDesonerado()) != null ? item.getValorIcmsDesonerado().toString() : "0.00");
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()) != null ? item.getValorIcmsSt().toString() : "0.00");
                    icms.setICMS30(obj);
                }

                case "51" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS51 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS51();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms().toString() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc() != null ? item.getModBc() : "0.00");
                    obj.setPICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setPFCP(String.valueOf(item.getAliqFcp()) != null ? item.getAliqFcp().toString() : "0.00");
                    obj.setCBenefRBC(null);
                    obj.setPDif(null);
                    obj.setPFCPDif(null);
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()) != null ? item.getPercentRedBc().toString() : "0.00");
                    obj.setVBC(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setVFCP(String.valueOf(item.getValorFcp()) != null ? item.getValorFcp().toString() : "0.00");
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()) != null ? item.getBcFcp().toString() : "0.00");
                    obj.setVFCPDif(null);
                    obj.setVFCPEfet(null);
                    obj.setVICMS(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setVICMSDif(String.valueOf(item.getValorIcmsDiferido()) != null ? item.getValorIcmsDiferido().toString() : "0.00");
                    obj.setVICMSOp(String.valueOf(item.getValorIcmsOperacao()) != null ? item.getValorIcmsOperacao().toString() : "0.00");
                    icms.setICMS51(obj);
                }

                case "53" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS53 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS53();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setAdRemICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setAdRemICMSDif(null);
                    obj.setQBCMono(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setQBCMonoDif(null);
                    obj.setPDif(null);
                    obj.setVICMSMono(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setVICMSMonoDif(null);
                    obj.setVICMSMonoOp(null);
                    icms.setICMS53(obj);
                }

                case "60" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS60 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS60();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setVBCSTRet(String.valueOf(item.getBcIcmsStRetido()) != null ? item.getBcIcmsStRetido().toString() : "0.00");
                    obj.setPST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSSubstituto(String.valueOf(item.getValorIcmsSubstituto()) != null ? item.getValorIcmsSubstituto().toString() : "0.00");
                    obj.setVICMSSTRet(String.valueOf(item.getValorIcmsStRetido()) != null ? item.getValorIcmsStRetido().toString() : "0.00");
                    obj.setVBCFCPSTRet(String.valueOf(item.getBcFcpStRetido()) != null ? item.getBcFcpStRetido().toString() : "0.00");
                    obj.setPFCPSTRet(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPSTRet(String.valueOf(item.getValorFcpStRetido()) != null ? item.getValorFcpStRetido().toString() : "0.00");
                    obj.setPRedBCEfet(String.valueOf(item.getPercentRedBcEfetivo()) != null ? item.getPercentRedBcEfetivo().toString() : "0.00");
                    obj.setVBCEfet(null);
                    obj.setPICMSEfet(String.valueOf(item.getAliqIcmsEfetivo()) != null ? item.getAliqIcmsEfetivo().toString() : "0.00");
                    obj.setVICMSEfet(String.valueOf(item.getValorIcmsEfetivo()) != null ? item.getValorIcmsEfetivo().toString() : "0.00");
                    icms.setICMS60(obj);
                }

                case "61" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS61 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS61();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setQBCMonoRet(null);
                    obj.setAdRemICMSRet(null);
                    obj.setVICMSMonoRet(null);
                    icms.setICMS61(obj);
                }

                case "70" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS70 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS70();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc() != null ? item.getModBc().toString() : "0.00");
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()) != null ? item.getPercentRedBc().toString() : "0.00");
                    obj.setVBC(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setPICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setVICMS(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()) != null ? item.getBcFcp().toString() : "0.00");
                    obj.setPFCP(String.valueOf(item.getAliqFcp()) != null ? item.getAliqFcp().toString() : "0.00");
                    obj.setVFCP(String.valueOf(item.getValorFcp()) != null ? item.getValorFcp().toString() : "0.00");
                    obj.setModBCST(item.getModBcSt() != null ? item.getModBcSt() : "0.00");
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()) != null ? item.getPercentMargemIcmsSt().toString() : "0.00");
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()) != null ? item.getPercentRedBcSt().toString() : "0.00");
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()) != null ? item.getBcIcmsSt().toString() : "0.00");
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()) != null ? item.getValorIcmsSt().toString() : "0.00");
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()) != null ? item.getBcFcpSt().toString() : "0.00");
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()) != null ? item.getValorFcpSt().toString() : "0.00");
                    obj.setVICMSDeson(String.valueOf(item.getValorIcmsDesonerado()) != null ? item.getValorIcmsDesonerado().toString() : "0.00");
                    obj.setMotDesICMS(item.getMotDesIcms() != null ? item.getMotDesIcms() : "0.00");
                    obj.setIndDeduzDeson(String.valueOf(item.getValorIcmsDesonerado()) != null ? item.getValorIcmsDesonerado().toString() : "0.00"); //Indica se o valor do ICMS desonerado (vICMSDeson) deduz do valor do item (vProd).0: Valor do ICMS desonerado (vICMSDeson) não deduz do valor do item (vProd) / total da NF-e 1: Valor do ICMS desonerado (vICMSDeson) deduz do valor do item (vProd) / total da NF-e.
                    obj.setVICMSSTDeson(null);
                    obj.setMotDesICMSST(String.valueOf(item.getMotDesIcms()) != null ? item.getMotDesIcms() : "0.00");
                    icms.setICMS70(obj);
                }

                case "90" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS90 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS90();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms().toString() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc() != null ? item.getModBc() : "0.00");
                    obj.setVBC(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()) != null ? item.getPercentRedBc().toString() : "0.00");
                    obj.setPICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setVICMS(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()) != null ? item.getBcFcp().toString() : "0.00");
                    obj.setPFCP(String.valueOf(item.getAliqFcp()) != null ? item.getAliqFcp().toString() : "0.00");
                    obj.setVFCP(String.valueOf(item.getValorFcp()) != null ? item.getValorFcp().toString() : "0.00");
                    obj.setModBCST(item.getModBcSt() != null ? item.getModBcSt() : "0.00");
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()) != null ? item.getPercentMargemIcmsSt().toString() : "0.00");
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()) != null ? item.getPercentRedBcSt().toString() : "0.00");
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()) != null ? item.getBcIcmsSt().toString() : "0.00");
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()) != null ? item.getValorIcmsSt().toString() : "0.00");
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()) != null ? item.getBcFcpSt().toString() : "0.00");
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()) != null ? item.getValorFcpSt().toString() : "0.00");
                    obj.setVICMSDeson(String.valueOf(item.getValorIcmsDesonerado()) != null ? item.getValorIcmsDesonerado().toString() : "0.00");
                    obj.setMotDesICMS(item.getMotDesIcms() != null ? item.getMotDesIcms() : "0.00");
                    obj.setIndDeduzDeson(String.valueOf(item.getValorIcmsDesonerado()) != null ? item.getValorIcmsDesonerado().toString() : "0.00");
                    obj.setVICMSSTDeson(null);
                    obj.setMotDesICMSST(String.valueOf(item.getMotDesIcms()) != null ? item.getMotDesIcms() : "0.00");
                    icms.setICMS90(obj);
                }

                case "part" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc() != null ? item.getModBc() : "0.00");
                    obj.setVBC(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()) != null ? item.getPercentRedBc().toString() : "0.00");
                    obj.setPICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setVICMS(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setModBCST(item.getModBcSt() != null ? item.getModBcSt() : "0.00");
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()) != null ? item.getPercentMargemIcmsSt().toString() : "0.00");
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()) != null ? item.getPercentRedBcSt().toString() : "0.00");
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()) != null ? item.getBcIcmsSt().toString() : "0.00");
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()) != null ? item.getValorIcmsSt().toString() : "0.00");
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()) != null ? item.getBcFcpSt().toString() : "0.00");
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()) != null ? item.getValorFcpSt().toString() : "0.00");
                    obj.setPBCOp(String.valueOf(item.getPercentBcOperacao()) != null ? item.getPercentBcOperacao().toString() : "0.00");
                    obj.setUFST(ufDestinatario);
                    icms.setICMSPart(obj);
                }

                case "st" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSST obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSST();
                    obj.setOrig(item.getOrigIcms()!= null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setVBCSTRet(String.valueOf(item.getBcIcmsStRetido()) != null ? item.getBcIcmsStRetido().toString() : "0.00");
                    obj.setPST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSSubstituto(String.valueOf(item.getValorIcmsSubstituto()) != null ? item.getValorIcmsSubstituto().toString() : "0.00");
                    obj.setVICMSSTRet(String.valueOf(item.getValorIcmsStRetido()) != null ? item.getValorIcmsStRetido().toString() : "0.00");
                    obj.setVBCFCPSTRet(String.valueOf(item.getBcFcpStRetido()) != null ? item.getBcFcpStRetido().toString() : "0.00");
                    obj.setPFCPSTRet(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPSTRet(String.valueOf(item.getValorFcpStRetido()) != null ? item.getValorFcpStRetido().toString() : "0.00");
                    obj.setVBCSTDest(String.valueOf(item.getBcIcmsStDestino()) != null ? item.getBcIcmsStDestino().toString() : "0.00");
                    obj.setVICMSSTDest(String.valueOf(item.getValorIcmsStDestino()) != null ? item.getValorIcmsStDestino().toString() : "0.00");
                    obj.setPRedBCEfet(String.valueOf(item.getPercentRedBcEfetivo()) != null ? item.getPercentRedBcEfetivo().toString() : "0.00");
                    obj.setVBCEfet(String.valueOf(item.getBcIcmsEfetivo()) != null ? item.getBcIcmsEfetivo().toString() : "0.00");
                    obj.setPICMSEfet(String.valueOf(item.getAliqIcmsEfetivo()) != null ? item.getAliqIcmsEfetivo().toString() : "0.00");
                    obj.setVICMSEfet(String.valueOf(item.getValorIcmsEfetivo()) != null ? item.getValorIcmsEfetivo().toString() : "0.00");
                    icms.setICMSST(obj);
                }

                case "101" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCSOSN(cst);
                    obj.setPCredSN(null);
                    obj.setVCredICMSSN(null);
                    icms.setICMSSN101(obj);
                }

                case "102" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCSOSN(cst);
                    icms.setICMSSN102(obj);
                }

                case "201" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCSOSN(cst);
                    obj.setModBCST(item.getModBcSt() != null ? item.getModBcSt() : "0.00");
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()) != null ? item.getPercentMargemIcmsSt().toString() : "0.00");
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()) != null ? item.getPercentRedBcSt().toString() : "0.00");
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()) != null ? item.getBcIcmsSt().toString() : "0.00");
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()) != null ? item.getValorIcmsSt().toString() : "0.00");
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()) != null ? item.getBcFcpSt().toString() : "0.00");
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()) != null ? item.getValorFcpSt().toString() : "0.00");
                    obj.setPCredSN(String.valueOf(item.getAliqCredSn()) != null ? item.getAliqCredSn().toString() : "0.00");
                    obj.setVCredICMSSN(String.valueOf(item.getValorCredIcmsSn()) != null ? item.getValorCredIcmsSn().toString() : "0.00");
                    icms.setICMSSN201(obj);
                }

                case "202" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCSOSN(cst);
                    obj.setModBCST(item.getModBcSt() != null ? item.getModBcSt() : "0.00");
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()) != null ? item.getPercentMargemIcmsSt().toString() : "0.00");
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()) != null ? item.getPercentRedBcSt().toString() : "0.00");
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()) != null ? item.getBcIcmsSt().toString() : "0.00");
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()) != null ? item.getValorIcmsSt().toString() : "0.00");
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()) != null ? item.getBcFcpSt().toString() : "0.00");
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()) != null ? item.getValorFcpSt().toString() : "0.00");
                    icms.setICMSSN202(obj);
                }

                case "500" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCSOSN(cst);
                    obj.setVBCSTRet(String.valueOf(item.getBcIcmsStRetido()) != null ? item.getBcIcmsStRetido().toString() : "0.00");
                    obj.setPST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSSubstituto(String.valueOf(item.getValorIcmsSubstituto()) != null ? item.getValorIcmsSubstituto().toString() : "0.00");
                    obj.setVICMSSTRet(String.valueOf(item.getValorIcmsStRetido()) != null ? item.getValorIcmsStRetido().toString() : "0.00");
                    obj.setVBCFCPSTRet(String.valueOf(item.getBcFcpStRetido()) != null ? item.getBcFcpStRetido().toString() : "0.00");
                    obj.setPFCPSTRet(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPSTRet(String.valueOf(item.getValorFcpStRetido()) != null ? item.getValorFcpStRetido().toString() : "0.00");
                    obj.setPRedBCEfet(String.valueOf(item.getPercentRedBcEfetivo()) != null ? item.getPercentRedBcEfetivo().toString() : "0.00");
                    obj.setVBCEfet(String.valueOf(item.getBcIcmsEfetivo()) != null ? item.getBcIcmsEfetivo().toString() : "0.00");
                    obj.setPICMSEfet(String.valueOf(item.getAliqIcmsEfetivo()) != null ? item.getAliqIcmsEfetivo().toString() : "0.00");
                    obj.setVICMSEfet(String.valueOf(item.getValorIcmsEfetivo()) != null ? item.getValorIcmsEfetivo().toString() : "0.00");
                    icms.setICMSSN500(obj);
                }

                case "900" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCSOSN(cst);
                    obj.setModBC(item.getModBc() != null ? item.getModBc().toString() : "0.00");
                    obj.setVBC(String.valueOf(item.getBcIcms()) != null ? item.getBcIcms().toString() : "0.00");
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()) != null ? item.getPercentRedBc().toString() : "0.00");
                    obj.setPICMS(String.valueOf(item.getAliqIcms()) != null ? item.getAliqIcms().toString() : "0.00");
                    obj.setVICMS(String.valueOf(item.getValorIcms()) != null ? item.getValorIcms().toString() : "0.00");
                    obj.setModBCST(item.getModBcSt() != null ? item.getModBcSt().toString() : "0.00");
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()) != null ? item.getPercentMargemIcmsSt().toString() : "0.00");
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()) != null ? item.getPercentRedBcSt().toString() : "0.00");
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()) != null ? item.getBcIcmsSt().toString() : "0.00");
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()) != null ? item.getValorIcmsSt().toString() : "0.00");
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()) != null ? item.getBcFcpSt().toString() : "0.00");
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()) != null ? item.getValorFcpSt().toString() : "0.00");
                    obj.setPCredSN(String.valueOf(item.getAliqCredSn()) != null ? item.getAliqCredSn().toString() : "0.00");
                    obj.setVCredICMSSN(String.valueOf(item.getValorCredIcmsSn()) != null ? item.getValorCredIcmsSn().toString() : "0.00");
                    icms.setICMSSN900(obj);
                }
            }
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoICMS(icms));


            // === PIS ===
            TNFe.InfNFe.Det.Imposto.PIS pis = new TNFe.InfNFe.Det.Imposto.PIS();
            TNFe.InfNFe.Det.Imposto.PIS.PISAliq pisAliq = new TNFe.InfNFe.Det.Imposto.PIS.PISAliq();
            pisAliq.setCST(item.getCstPis() != null ? item.getCstPis().toString() : "0.00");
            pisAliq.setVBC(item.getBcPis().toString() != null ? item.getBcPis().toString() : "0.00");
            pisAliq.setPPIS(item.getAliqPis().toString() != null ? item.getAliqPis().toString() : "0.00");
            pisAliq.setVPIS(item.getValorPis().toString() != null ? item.getValorPis().toString() : "0.00");
            pis.setPISAliq(pisAliq);
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoPIS(pis));

            // === Cofins ===
            TNFe.InfNFe.Det.Imposto.COFINS cofins = new TNFe.InfNFe.Det.Imposto.COFINS();
            TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq cofinsAliq = new TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq();
            cofinsAliq.setCST(item.getCstCofins() != null ? item.getCstCofins().toString() : "0.00");
            cofinsAliq.setVBC(item.getBcCofins().toString() != null ? item.getBcCofins().toString() : "0.00");
            cofinsAliq.setPCOFINS(item.getAliqCofins().toString() != null ? item.getAliqCofins().toString() : "0.00");
            cofinsAliq.setVCOFINS(item.getValorCofins().toString() != null ? item.getValorCofins().toString() : "0.00");
            cofins.setCOFINSAliq(cofinsAliq);
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoCOFINS(cofins));

            det.setImposto(imposto);
            infNFe.getDet().add(det);
        }

        // Totais
        TNFe.InfNFe.Total total = new TNFe.InfNFe.Total();
        TNFe.InfNFe.Total.ICMSTot icmsTot = new TNFe.InfNFe.Total.ICMSTot();
        icmsTot.setVBC(dto.getValorBaseCalculo().toString());
        icmsTot.setVICMS(dto.getValorIcms().toString());
        icmsTot.setVProd(dto.getValorProdutos().toString());
        icmsTot.setVNF(dto.getValorTotal().toString());
        icmsTot.setVDesc(dto.getValorDesconto() != null ? dto.getValorDesconto().toString() : "0.00");
        icmsTot.setVFrete(dto.getValorFrete() != null ? dto.getValorFrete().toString() : "0.00");
        icmsTot.setVSeg(dto.getValorSeguro() != null ? dto.getValorSeguro().toString() : "0.00");
        icmsTot.setVIPI(dto.getValorIpi() != null ? dto.getValorIpi().toString() : "0.00");
        icmsTot.setVIPIDevol(dto.getValorIpiDevolucao() != null ? dto.getValorCofins().toString() : "0.00");
        icmsTot.setVPIS(dto.getValorPis() != null ? dto.getValorPis().toString() : "0.00");
        icmsTot.setVCOFINS(dto.getValorCofins() != null ? dto.getValorCofins().toString() : "0.00");
        icmsTot.setVII(dto.getValorIi() != null ? dto.getValorCofins().toString() : "0.00");
        icmsTot.setVOutro(dto.getValorOutros() != null ? dto.getValorCofins().toString() : "0.00");
        total.setICMSTot(icmsTot);
        infNFe.setTotal(total);

        // Transporte
        TNFe.InfNFe.Transp transp = new TNFe.InfNFe.Transp();
        transp.setModFrete(dto.getModalidadeFrete());
        infNFe.setTransp(transp);

        // Fatura/Duplicatas
        TNFe.InfNFe.Cobr cobr = new TNFe.InfNFe.Cobr();
        for (NotaFiscalDuplicatasDto dup : dto.getDuplicatas()) {
            TNFe.InfNFe.Cobr.Dup dupXml = new TNFe.InfNFe.Cobr.Dup();
            dupXml.setNDup(dup.getNumeroDuplicata());
            dupXml.setDVenc(dup.getDataVencimento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dupXml.setVDup(dup.getValorDuplicata().toString());
            cobr.getDup().add(dupXml);
        }
        infNFe.setCobr(cobr);

        // Informações adicionais
        TNFe.InfNFe.InfAdic infAdic = new TNFe.InfNFe.InfAdic();
        infAdic.setInfAdFisco(dto.getInformacaoAdicionalFisco());
        infAdic.setInfCpl(dto.getInformacaoAdicionalContribuinte());
        infNFe.setInfAdic(infAdic);

        tnFe.setInfNFe(infNFe);
        return tnFe;
    }
}