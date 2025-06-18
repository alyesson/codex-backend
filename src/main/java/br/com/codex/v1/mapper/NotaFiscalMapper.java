package br.com.codex.v1.mapper;

import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.dto.NotaFiscalDuplicatasDto;
import br.com.codex.v1.domain.dto.NotaFiscalItemDto;
import br.com.swconsultoria.nfe.schema_4.enviNFe.*;
import br.com.swconsultoria.nfe.util.ConstantesUtil;

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

        // Identificação
        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();
        ide.setCUF(dto.getCodigoUf());
        ide.setNatOp(dto.getNaturezaOperacao());
        ide.setSerie(String.valueOf(dto.getSerie()));
        ide.setNNF(String.valueOf(dto.getNumero()));
        ide.setDhEmi(dto.getEmissao().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        ide.setDhSaiEnt(dto.getDhSaidaEntrada());
        ide.setTpNF(dto.getTipo());
        ide.setIdDest(dto.getIndicadorPresenca());
        ide.setCMunFG(dto.getCodigoMunicipioEmitente());
        ide.setFinNFe(dto.getCodigoNf());
        ide.setIndFinal(dto.getIndicadorFinal().toString());
        ide.setIndPres(dto.getIndicadorPresenca());
        ide.setProcEmi("0");
        ide.setVerProc("1.0");
        infNFe.setIde(ide);

        // Emitente
        TNFe.InfNFe.Emit emit = new TNFe.InfNFe.Emit();
        emit.setCNPJ(dto.getDocumentoEmitente());
        emit.setXNome(dto.getRazaoSocialEmitente());
        emit.setXFant(dto.getNomeFantasiaEmitente());
        emit.setIE(dto.getInscricaoEstadualEmitente());
        emit.setIEST(String.valueOf(dto.getInscricaoEstadualStEmitente()));
        emit.setIM(String.valueOf(dto.getInscricaoMunicipalEmitente()));
        emit.setCNAE(dto.getCnaeEmitente());
        emit.setCRT(String.valueOf(dto.getRegimeTributarioEmitente()));
        TEnderEmi enderEmit = new TEnderEmi();
        enderEmit.setXLgr(dto.getLogradouroEmitente());
        enderEmit.setNro(dto.getNumeroEnderecoEmitente());
        enderEmit.setXCpl("");
        enderEmit.setXBairro(dto.getBairroEmitente());
        enderEmit.setCMun(dto.getCodigoMunicipioEmitente());
        enderEmit.setXMun(dto.getCodigoMunicipioEmitente());
        enderEmit.setUF(TUfEmi.fromValue(dto.getUfEmitente())); // Certifique-se de passar "SP", "RJ", etc.
        enderEmit.setCEP(dto.getCepEmitente());
        enderEmit.setFone(dto.getTelefoneEmitente());
        emit.setEnderEmit(enderEmit);
        infNFe.setEmit(emit);


        // Destinatário
        TNFe.InfNFe.Dest dest = new TNFe.InfNFe.Dest();
        dest.setCNPJ(dto.getDocumentoDestinatario());
        dest.setCPF(dto.getDocumentoDestinatario());
        dest.setXNome(dto.getRazaoSocialDestinatario());
        dest.setIE(dto.getInscricaoEstadualDestinatario());
        dest.setIndIEDest(String.valueOf(dto.getIndicadorInscricaoEstadualDestinatario()));
        dest.setEmail(dto.getEmailDestinatario());
        TEndereco enderDest = new TEndereco();
        enderDest.setXLgr(dto.getLogradouroDestinatario());
        enderDest.setNro(dto.getNumeroEnderecoDestinatario());
        enderDest.setXCpl("");
        enderDest.setXBairro(dto.getBairroDestinatario());
        enderDest.setCMun(dto.getCodigoMunicipioDestinatario());
        enderDest.setXMun(dto.getNomeMunicipioDestinatario());
        enderDest.setUF(TUf.valueOf(dto.getUfDestinatario())); // "SP", "MG", etc.
        ufDestinatario = TUf.valueOf(dto.getUfDestinatario());
        enderDest.setCEP(dto.getCepDestinatario());
        enderDest.setCPais(dto.getCodigoPaisDestinatario());
        enderDest.setXPais(dto.getPaisDestinatario());
        enderDest.setFone(dto.getTelefoneDestinatario());

        dest.setEnderDest(enderDest);

        // Produtos / Itens
        List<NotaFiscalItemDto> itens = dto.getItens();
        for (int i = 0; i < itens.size(); i++) {
            NotaFiscalItemDto item = itens.get(i);

            TNFe.InfNFe.Det det = new TNFe.InfNFe.Det();
            det.setNItem(String.valueOf(i + 1));

            TNFe.InfNFe.Det.Prod prod = new TNFe.InfNFe.Det.Prod();
            prod.setCProd(item.getCodigoProduto());
            prod.setXProd(item.getNomeProduto());
            prod.setCEAN(item.getCEAN());
            prod.setCBarra("");
            prod.setNCM(item.getNcmSh());
            prod.setCEST(String.valueOf(item.getCest()));
            prod.setIndEscala("");
            prod.setCNPJFab("");
            prod.setCBenef("");
            prod.setEXTIPI(item.getExtipi());
            prod.setCFOP(item.getCfop());
            prod.setUCom(item.getUnidadeComercial());
            prod.setQCom(String.valueOf(item.getQuantidadeComercial()));
            prod.setVUnCom(String.valueOf(item.getValorUnitarioComercial()));
            prod.setVProd(String.valueOf(item.getValorUnitarioComercial()));
            prod.setCEANTrib(item.getCEAN());
            prod.setCBarraTrib("");
            prod.setUTrib(item.getUnidadeComercial());
            prod.setQTrib(String.valueOf(item.getQuantidadeComercial()));
            prod.setVUnTrib(String.valueOf(item.getValorUnitarioComercial()));
            prod.setVFrete(String.valueOf(item.getValorFrete()));
            prod.setVSeg(String.valueOf(item.getValorSeguro()));
            prod.setVDesc(String.valueOf(item.getValorDesconto()));
            prod.setVOutro("0.00");
            prod.setIndTot("1");
            prod.setXPed(item.getPedidoCompra());
            prod.setNItemPed("");
            prod.setNFCI("");
            det.setProd(prod);

            // Impostos
            TNFe.InfNFe.Det.Imposto imposto = new TNFe.InfNFe.Det.Imposto();

            // === ICMS ===
            TNFe.InfNFe.Det.Imposto.ICMS icms = new TNFe.InfNFe.Det.Imposto.ICMS();
            String cst = item.getCstIcms();
            switch (cst) {
                case "00" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS00 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS00();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc());
                    obj.setVBC(String.valueOf(item.getBcIcms()));
                    obj.setPICMS(String.valueOf(item.getAliqIcms()));
                    obj.setVICMS(String.valueOf(item.getValorIcms()));
                    obj.setPFCP(String.valueOf(item.getAliqFcp()));
                    obj.setVFCP(String.valueOf(item.getValorFcp()));
                    icms.setICMS00(obj);
                }

                case "02" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS02 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS02();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setAdRemICMS(String.valueOf(item.getAliqIcms()));
                    obj.setQBCMono(String.valueOf(item.getBcIcms()));
                    obj.setVICMSMono(String.valueOf(item.getValorIcms()));
                    icms.setICMS02(obj);
                }

                case "10" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS10 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS10();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc());
                    obj.setVBC(String.valueOf(item.getBcIcms()));
                    obj.setPICMS(String.valueOf(item.getAliqIcms()));
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()));
                    obj.setVICMS(String.valueOf(item.getValorIcms()));
                    obj.setPFCP(String.valueOf(item.getAliqFcp()));
                    obj.setPFCPST(null);
                    obj.setVFCP(String.valueOf(item.getValorFcp()));
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()));
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()));
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()));
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()));
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()));
                    obj.setVICMSSTDeson(String.valueOf(item.getValorIcmsDesonerado()));
                    obj.setModBCST(String.valueOf(item.getModBc()));
                    obj.setMotDesICMSST(String.valueOf(item.getMotDesIcms()));
                    icms.setICMS10(obj);
                }

                case "15" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS15 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS15();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setAdRemICMS(String.valueOf(item.getAliqIcms()));
                    obj.setQBCMono(String.valueOf(item.getBcIcms()));
                    obj.setVICMSMono(String.valueOf(item.getValorIcms()));
                    obj.setAdRemICMSReten(null);
                    obj.setQBCMonoReten(null);
                    obj.setVICMSMonoReten(null);
                    obj.setPRedAdRem(null);
                    obj.setQBCMonoReten(null);
                    icms.setICMS15(obj);
                }

                case "20" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS20 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS20();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc());
                    obj.setVBC(String.valueOf(item.getBcIcms()));
                    obj.setPICMS(String.valueOf(item.getAliqIcms()));
                    obj.setPFCP(String.valueOf(item.getAliqFcp()));
                    obj.setIndDeduzDeson(String.valueOf(item.getValorIcmsDesonerado()));
                    obj.setMotDesICMS(String.valueOf(item.getMotDesIcms()));
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()));
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()));
                    obj.setVICMS(String.valueOf(item.getValorIcms()));
                    obj.setVICMSDeson(String.valueOf(item.getValorIcmsDesonerado()));
                    icms.setICMS20(obj);
                }

                case "30" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS30 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS30();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()));
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()));
                    obj.setModBCST(null);
                    obj.setIndDeduzDeson(String.valueOf(item.getValorIcmsDesonerado()));
                    obj.setMotDesICMS(String.valueOf(item.getMotDesIcms()));
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()));
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()));
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()));
                    obj.setVICMSDeson(String.valueOf(item.getValorIcmsDesonerado()));
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()));
                    icms.setICMS30(obj);
                }

                case "51" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS51 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS51();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc());
                    obj.setPICMS(String.valueOf(item.getAliqIcms()));
                    obj.setPFCP(String.valueOf(item.getAliqFcp()));
                    obj.setCBenefRBC(null);
                    obj.setPDif(null);
                    obj.setPFCPDif(null);
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()));
                    obj.setVBC(String.valueOf(item.getBcIcms()));
                    obj.setVFCP(String.valueOf(item.getValorFcp()));
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()));
                    obj.setVFCPDif(null);
                    obj.setVFCPEfet(null);
                    obj.setVICMS(String.valueOf(item.getValorIcms()));
                    obj.setVICMSDif(String.valueOf(item.getValorIcmsDiferido()));
                    obj.setVICMSOp(String.valueOf(item.getValorIcmsOperacao()));
                    icms.setICMS51(obj);
                }

                case "53" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS53 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS53();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setAdRemICMS(String.valueOf(item.getAliqIcms()));
                    obj.setAdRemICMSDif(null);
                    obj.setQBCMono(String.valueOf(item.getBcIcms()));
                    obj.setQBCMonoDif(null);
                    obj.setPDif(null);
                    obj.setVICMSMono(String.valueOf(item.getValorIcms()));
                    obj.setVICMSMonoDif(null);
                    obj.setVICMSMonoOp(null);
                    icms.setICMS53(obj);
                }

                case "60" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS60 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS60();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setVBCSTRet(String.valueOf(item.getBcIcmsStRetido()));
                    obj.setPST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setVICMSSubstituto(String.valueOf(item.getValorIcmsSubstituto()));
                    obj.setVICMSSTRet(String.valueOf(item.getValorIcmsStRetido()));
                    obj.setVBCFCPSTRet(String.valueOf(item.getBcFcpStRetido()));
                    obj.setPFCPSTRet(String.valueOf(item.getAliqFcpSt()));
                    obj.setVFCPSTRet(String.valueOf(item.getValorFcpStRetido()));
                    obj.setPRedBCEfet(String.valueOf(item.getPercentRedBcEfetivo()));
                    obj.setVBCEfet(null);
                    obj.setPICMSEfet(String.valueOf(item.getAliqIcmsEfetivo()));
                    obj.setVICMSEfet(String.valueOf(item.getValorIcmsEfetivo()));
                    icms.setICMS60(obj);
                }

                case "61" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS61 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS61();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setQBCMonoRet(null);
                    obj.setAdRemICMSRet(null);
                    obj.setVICMSMonoRet(null);
                    icms.setICMS61(obj);
                }

                case "70" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS70 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS70();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc());
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()));
                    obj.setVBC(String.valueOf(item.getBcIcms()));
                    obj.setPICMS(String.valueOf(item.getAliqIcms()));
                    obj.setVICMS(String.valueOf(item.getValorIcms()));
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()));
                    obj.setPFCP(String.valueOf(item.getAliqFcp()));
                    obj.setVFCP(String.valueOf(item.getValorFcp()));
                    obj.setModBCST(item.getModBcSt());
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()));
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()));
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()));
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()));
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()));
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()));
                    obj.setVICMSDeson(String.valueOf(item.getValorIcmsDesonerado()));
                    obj.setMotDesICMS(item.getMotDesIcms());
                    obj.setIndDeduzDeson(String.valueOf(item.getValorIcmsDesonerado())); //Indica se o valor do ICMS desonerado (vICMSDeson) deduz do valor do item (vProd).0: Valor do ICMS desonerado (vICMSDeson) não deduz do valor do item (vProd) / total da NF-e 1: Valor do ICMS desonerado (vICMSDeson) deduz do valor do item (vProd) / total da NF-e.
                    obj.setVICMSSTDeson(null);
                    obj.setMotDesICMSST(String.valueOf(item.getMotDesIcms()));
                    icms.setICMS70(obj);
                }

                case "90" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS90 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS90();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc());
                    obj.setVBC(String.valueOf(item.getBcIcms()));
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()));
                    obj.setPICMS(String.valueOf(item.getAliqIcms()));
                    obj.setVICMS(String.valueOf(item.getValorIcms()));
                    obj.setVBCFCP(String.valueOf(item.getBcFcp()));
                    obj.setPFCP(String.valueOf(item.getAliqFcp()));
                    obj.setVFCP(String.valueOf(item.getValorFcp()));
                    obj.setModBCST(item.getModBcSt());
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()));
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()));
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()));
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()));
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()));
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()));
                    obj.setVICMSDeson(String.valueOf(item.getValorIcmsDesonerado()));
                    obj.setMotDesICMS(item.getMotDesIcms());
                    obj.setIndDeduzDeson(String.valueOf(item.getValorIcmsDesonerado()));
                    obj.setVICMSSTDeson(null);
                    obj.setMotDesICMSST(String.valueOf(item.getMotDesIcms()));
                    icms.setICMS90(obj);
                }

                case "part" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setModBC(item.getModBc());
                    obj.setVBC(String.valueOf(item.getBcIcms()));
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()));
                    obj.setPICMS(String.valueOf(item.getAliqIcms()));
                    obj.setVICMS(String.valueOf(item.getValorIcms()));
                    obj.setModBCST(item.getModBcSt());
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()));
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()));
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()));
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()));
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()));
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()));
                    obj.setPBCOp(String.valueOf(item.getPercentBcOperacao()));
                    obj.setUFST(ufDestinatario);
                    icms.setICMSPart(obj);
                }

                case "st" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSST obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSST();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setVBCSTRet(String.valueOf(item.getBcIcmsStRetido()));
                    obj.setPST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setVICMSSubstituto(String.valueOf(item.getValorIcmsSubstituto()));
                    obj.setVICMSSTRet(String.valueOf(item.getValorIcmsStRetido()));
                    obj.setVBCFCPSTRet(String.valueOf(item.getBcFcpStRetido()));
                    obj.setPFCPSTRet(String.valueOf(item.getAliqFcpSt()));
                    obj.setVFCPSTRet(String.valueOf(item.getValorFcpStRetido()));
                    obj.setVBCSTDest(String.valueOf(item.getBcIcmsStDestino()));
                    obj.setVICMSSTDest(String.valueOf(item.getValorIcmsStDestino()));
                    obj.setPRedBCEfet(String.valueOf(item.getPercentRedBcEfetivo()));
                    obj.setVBCEfet(String.valueOf(item.getBcIcmsEfetivo()));
                    obj.setPICMSEfet(String.valueOf(item.getAliqIcmsEfetivo()));
                    obj.setVICMSEfet(String.valueOf(item.getValorIcmsEfetivo()));
                    icms.setICMSST(obj);
                }

                case "101" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCSOSN(cst);
                    obj.setPCredSN(null);
                    obj.setVCredICMSSN(null);
                    icms.setICMSSN101(obj);
                }

                case "102" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCSOSN(cst);
                    icms.setICMSSN102(obj);
                }

                case "201" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCSOSN(cst);
                    obj.setModBCST(item.getModBcSt());
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()));
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()));
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()));
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()));
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()));
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()));
                    obj.setPCredSN(String.valueOf(item.getAliqCredSn()));
                    obj.setVCredICMSSN(String.valueOf(item.getValorCredIcmsSn()));
                    icms.setICMSSN201(obj);
                }

                case "202" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCSOSN(cst);
                    obj.setModBCST(item.getModBcSt());
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()));
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()));
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()));
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()));
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()));
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()));
                    icms.setICMSSN202(obj);
                }

                case "500" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCSOSN(cst);
                    obj.setVBCSTRet(String.valueOf(item.getBcIcmsStRetido()));
                    obj.setPST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setVICMSSubstituto(String.valueOf(item.getValorIcmsSubstituto()));
                    obj.setVICMSSTRet(String.valueOf(item.getValorIcmsStRetido()));
                    obj.setVBCFCPSTRet(String.valueOf(item.getBcFcpStRetido()));
                    obj.setPFCPSTRet(String.valueOf(item.getAliqFcpSt()));
                    obj.setVFCPSTRet(String.valueOf(item.getValorFcpStRetido()));
                    obj.setPRedBCEfet(String.valueOf(item.getPercentRedBcEfetivo()));
                    obj.setVBCEfet(String.valueOf(item.getBcIcmsEfetivo()));
                    obj.setPICMSEfet(String.valueOf(item.getAliqIcmsEfetivo()));
                    obj.setVICMSEfet(String.valueOf(item.getValorIcmsEfetivo()));
                    icms.setICMSSN500(obj);
                }

                case "900" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCSOSN(cst);
                    obj.setModBC(item.getModBc());
                    obj.setVBC(String.valueOf(item.getBcIcms()));
                    obj.setPRedBC(String.valueOf(item.getPercentRedBc()));
                    obj.setPICMS(String.valueOf(item.getAliqIcms()));
                    obj.setVICMS(String.valueOf(item.getValorIcms()));
                    obj.setModBCST(item.getModBcSt());
                    obj.setPMVAST(String.valueOf(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(String.valueOf(item.getPercentRedBcSt()));
                    obj.setVBCST(String.valueOf(item.getBcIcmsSt()));
                    obj.setPICMSST(String.valueOf(item.getAliqIcmsSt()));
                    obj.setVICMSST(String.valueOf(item.getValorIcmsSt()));
                    obj.setVBCFCPST(String.valueOf(item.getBcFcpSt()));
                    obj.setPFCPST(String.valueOf(item.getAliqFcpSt()));
                    obj.setVFCPST(String.valueOf(item.getValorFcpSt()));
                    obj.setPCredSN(String.valueOf(item.getAliqCredSn()));
                    obj.setVCredICMSSN(String.valueOf(item.getValorCredIcmsSn()));
                    icms.setICMSSN900(obj);
                }
            }
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoICMS(icms));


            // === PIS ===
            TNFe.InfNFe.Det.Imposto.PIS pis = new TNFe.InfNFe.Det.Imposto.PIS();
            TNFe.InfNFe.Det.Imposto.PIS.PISAliq pisAliq = new TNFe.InfNFe.Det.Imposto.PIS.PISAliq();
            pisAliq.setCST(String.valueOf(item.getCstPis()));
            pisAliq.setVBC(item.getBcPis().toString());
            pisAliq.setPPIS(item.getAliqPis().toString());
            pisAliq.setVPIS(item.getValorPis().toString());
            pis.setPISAliq(pisAliq);
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoPIS(pis));

            // === COFINS ===
            TNFe.InfNFe.Det.Imposto.COFINS cofins = new TNFe.InfNFe.Det.Imposto.COFINS();
            TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq cofinsAliq = new TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq();
            cofinsAliq.setCST(String.valueOf(item.getCstCofins()));
            cofinsAliq.setVBC(item.getBcCofins().toString());
            cofinsAliq.setPCOFINS(item.getAliqCofins().toString());
            cofinsAliq.setVCOFINS(item.getValorCofins().toString());
            cofins.setCOFINSAliq(cofinsAliq);
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoCOFINS(cofins));

            // Atribui o bloco imposto ao item
            det.setImposto(imposto);
            infNFe.getDet().add(det);
        }

        // Totais
        TNFe.InfNFe.Total total = new TNFe.InfNFe.Total();
        TNFe.InfNFe.Total.ICMSTot icmsTot = new TNFe.InfNFe.Total.ICMSTot();
        icmsTot.setVProd(String.valueOf(dto.getValorProdutos()));
        icmsTot.setVNF(String.valueOf(dto.getValorTotal()));
        icmsTot.setVDesc(String.valueOf(dto.getValorDesconto()));
        icmsTot.setVFrete(String.valueOf(dto.getValorFrete()));
        icmsTot.setVSeg(String.valueOf(dto.getValorSeguro()));
        icmsTot.setVII(String.valueOf(dto.getValorIi()));
        icmsTot.setVIPI(String.valueOf(dto.getValorIpi()));
        icmsTot.setVPIS(String.valueOf(dto.getValorPis()));
        icmsTot.setVCOFINS(String.valueOf(dto.getValorCofins()));
        total.setICMSTot(icmsTot);
        infNFe.setTotal(total);

        // Transporte
        TNFe.InfNFe.Transp transp = new TNFe.InfNFe.Transp();
        transp.setModFrete(dto.getModalidadeFrete().toString());
        infNFe.setTransp(transp);

        // Fatura / Duplicatas
        TNFe.InfNFe.Cobr cobr = new TNFe.InfNFe.Cobr();
        for (NotaFiscalDuplicatasDto dup : dto.getDuplicatas()) {
            TNFe.InfNFe.Cobr.Dup dupXml = new TNFe.InfNFe.Cobr.Dup();
            dupXml.setNDup(dup.getNumeroDuplicata());
            dupXml.setDVenc(dup.getDataVencimento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dupXml.setVDup(String.valueOf(dup.getValorDuplicata()));
            cobr.getDup().add(dupXml);
        }
        infNFe.setCobr(cobr);

        // Info adicional
        TNFe.InfNFe.InfAdic infAdic = new TNFe.InfNFe.InfAdic();
        infAdic.setInfAdFisco(dto.getInformacaoAdicionalFisco().toString());
        infAdic.setInfCpl(dto.getInformacaoAdicionalContribuinte().toString());
        infNFe.setInfAdic(infAdic);

        tnFe.setInfNFe(infNFe);
        return tnFe;
    }
}