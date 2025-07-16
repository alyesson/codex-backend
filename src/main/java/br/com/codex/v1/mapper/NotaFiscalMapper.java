package br.com.codex.v1.mapper;

import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.dto.NotaFiscalDuplicatasDto;
import br.com.codex.v1.domain.dto.NotaFiscalItemDto;
import br.com.codex.v1.utilitario.FormatadorDecimal;
import br.com.swconsultoria.nfe.dom.enuns.EstadosEnum;
import br.com.swconsultoria.nfe.schema_4.enviNFe.*;
import br.com.swconsultoria.nfe.util.ChaveUtil;
import br.com.swconsultoria.nfe.util.ConstantesUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static br.com.codex.v1.utilitario.FormatadorDecimal.formatar;

public class NotaFiscalMapper {

    static TUf ufDestinatario = null;

    public static TNFe paraTNFe(NotaFiscalDto dto) {

        ZoneId zoneId = getZoneIdPorUF(dto.getCodigoUf());
        OffsetDateTime dataAtual = OffsetDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String dataFormatada = dataAtual.format(formatter);

        //Geração Número nota fiscal
        String numeroFormatado = String.format("%09d", Long.parseLong(dto.getNumero()));

        //Geração de código Cnf aleatório (recomendado pela Sefaz)
        String cnf = ChaveUtil.completarComZerosAEsquerda(String.valueOf(numeroFormatado), 8);

        ObjectFactory factory = new ObjectFactory();
        TNFe tnFe = factory.createTNFe();

        TNFe.InfNFe infNFe = new TNFe.InfNFe();
        infNFe.setVersao(ConstantesUtil.VERSAO.NFE);
        infNFe.setId(dto.getChave());

        // Identificação (IDE)
        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();

        ide.setCUF(EstadosEnum.valueOf(dto.getCodigoUf()).getCodigoUF());
        ide.setCNF(cnf);
        ide.setNatOp(dto.getNaturezaOperacao());
        ide.setMod(dto.getModelo());
        ide.setSerie(dto.getSerie());
        ide.setNNF(numeroFormatado);
        ide.setDhEmi(dataFormatada);
        ide.setDhSaiEnt(dataFormatada);
        ide.setTpNF(dto.getTipo());
        ide.setIdDest(dto.getLocalDestino());
        ide.setCMunFG(dto.getCodigoMunicipioEmitente());
        ide.setTpImp("2");
        ide.setTpEmis("1");
        ide.setTpAmb(dto.getTipoAmbiente());
        ide.setFinNFe(String.valueOf(dto.getFinalidadeEmissao()));
        ide.setIndFinal(String.valueOf(dto.getConsumidorFinal()));
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
            prod.setNCM(item.getNcmSh().replace(".",""));
            prod.setCEST(item.getCest().replace(".",""));
            prod.setCFOP(item.getCfop());
            prod.setUCom(item.getUnidadeComercial());
            prod.setQCom(item.getQuantidadeComercial().toString());
            prod.setVUnCom(formatar(item.getValorUnitarioComercial()));
            prod.setVProd(formatar(item.getValorTotalProdutos()));
            prod.setUTrib(item.getUnidadeTributacao());
            prod.setQTrib(item.getQuantidadeTributacao() != null ? item.getQuantidadeTributacao().toString() : "0");
            prod.setVUnTrib(formatar(item.getValorUnitarioTributacao()));
            prod.setVFrete(formatar(item.getValorFrete()));
            prod.setVSeg(formatar(item.getValorSeguro() ));
            prod.setVDesc(formatar(item.getValorDesconto()));
            prod.setVOutro(formatar(item.getValorOutro()));
            prod.setIndTot("1");
            det.setProd(prod);

            // Impostos
            TNFe.InfNFe.Det.Imposto imposto = new TNFe.InfNFe.Det.Imposto();
            TNFe.InfNFe.Det.Imposto.ICMS icms = new TNFe.InfNFe.Det.Imposto.ICMS();
            String cst = item.getCstIcms();

            // Cálculo do FCP (Fundo de Combate à Pobreza)
            BigDecimal valorFcp = BigDecimal.ZERO;
            if (item.getAliqFcp() != null && item.getAliqFcp().compareTo(BigDecimal.ZERO) > 0) {
                // Usa o valor total do produto (vProd) como base, ignorando item.getBcFcp()
                BigDecimal baseFcp = item.getValorTotalProdutos().setScale(2, RoundingMode.HALF_UP);
                valorFcp = baseFcp.multiply(item.getAliqFcp()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            }

            // Cálculo do FCP ST
            BigDecimal valorFcpSt = BigDecimal.ZERO;
            if (item.getAliqFcpSt() != null && item.getBcFcpSt() != null) {
                valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            }

            switch (cst) {
                case "00" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS00 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS00();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setModBC(formatar(item.getModBc()));

                    // Cálculo da BC do ICMS
                    BigDecimal bcIcms = (item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                    obj.setVBC(bcIcms.toString());

                    // Cálculo do valor do ICMS
                    BigDecimal valorIcms = BigDecimal.ZERO;
                    if (item.getAliqIcms() != null && bcIcms != null) {
                        valorIcms = bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                    }

                    obj.setPICMS(formatar(item.getAliqIcms()));
                    obj.setVICMS(formatar(valorIcms.toString()));
                    obj.setPFCP(formatar(item.getAliqFcp()));
                    obj.setVFCP(formatar(valorFcp.toString()));
                    icms.setICMS00(obj);
                }

                case "02" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS02 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS02();
                    obj.setOrig(item.getOrigIcms());
                    obj.setCST(cst);
                    obj.setAdRemICMS(formatar(item.getAliqIcms()));
                    obj.setQBCMono(formatar(item.getBcIcms()));
                    obj.setVICMSMono(formatar(item.getValorIcms()));
                    icms.setICMS02(obj);
                }

                case "10" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS10 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS10();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(formatar(item.getModBc() ));

                    // Cálculos para ICMS normal
                    BigDecimal bcIcms = (item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal valorIcms = BigDecimal.ZERO;
                    if (item.getAliqIcms() != null) {
                        valorIcms = (bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
                    }

                    // Cálculos para ICMS ST
                    BigDecimal bcIcmsSt = (item.getBcIcmsSt() != null ? item.getBcIcmsSt() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal valorIcmsSt = BigDecimal.ZERO;
                    if (item.getAliqIcmsSt() != null) {
                        valorIcmsSt = (bcIcmsSt.multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
                    }

                    obj.setVBC(bcIcms.toString());
                    obj.setPICMS(formatar(item.getAliqIcms()));
                    obj.setPICMSST(formatar(item.getAliqIcmsSt().toString()));
                    obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                    obj.setVICMS(formatar(valorIcms.toString()));
                    obj.setPFCP(formatar(item.getAliqFcp()));
                    obj.setPFCPST(formatar(item.getAliqFcpSt()));
                    obj.setVFCP(formatar(valorFcp.toString()));
                    obj.setVBCFCP(formatar(item.getBcFcp()));
                    obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                    obj.setVBCST(formatar(bcIcmsSt.toString()));
                    obj.setVFCPST(formatar(valorFcpSt.toString()));
                    obj.setVICMSST(formatar(valorIcmsSt.toString()));
                    obj.setVICMSSTDeson(formatar(item.getValorIcmsDesonerado()));
                    obj.setModBCST(formatar(item.getModBc()));
                    obj.setMotDesICMSST(formatar(item.getMotDesIcms()));
                    icms.setICMS10(obj);
                }

                case "15" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS15 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS15();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setAdRemICMS(formatar(item.getAliqIcms()));
                    obj.setQBCMono(formatar(item.getBcIcms()));
                    obj.setVICMSMono(formatar(item.getValorIcms()));
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
                    obj.setModBC(formatar(item.getModBc()));

                    // Cálculo com redução da BC
                    BigDecimal bcIcms = item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO;
                    BigDecimal valorIcms = BigDecimal.ZERO;
                    if (item.getAliqIcms() != null) {
                        valorIcms = (bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
                    }

                    obj.setVBC(bcIcms.toString());
                    obj.setPICMS(formatar(item.getAliqIcms()));
                    obj.setPFCP(formatar(item.getAliqFcp()));
                    obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado()));
                    obj.setMotDesICMS(formatar(item.getMotDesIcms()));
                    obj.setPRedBC(formatar(item.getPercentRedBc()));
                    obj.setVFCP(formatar(valorFcp.toString()));
                    obj.setVBCFCP(formatar(item.getBcFcp()));
                    obj.setVICMS(formatar(valorIcms.toString()));
                    obj.setVICMSDeson(formatar(item.getValorIcmsDesonerado()));
                    icms.setICMS20(obj);
                }

                case "30" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS30 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS30();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                    obj.setCST(cst);

                    // Cálculo do FCP ST (Fundo de Combate à Pobreza por ST)
                    if (item.getAliqFcpSt() != null && item.getBcFcpSt() != null) {
                        valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // Cálculo do ICMS ST
                    BigDecimal valorIcmsSt = BigDecimal.ZERO;
                    if (item.getAliqIcmsSt() != null && item.getBcIcmsSt() != null) {
                        valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    obj.setPFCPST(formatar(item.getAliqFcpSt()));
                    obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                    obj.setModBCST(null);
                    obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado()));
                    obj.setMotDesICMS(formatar(item.getMotDesIcms()));
                    obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                    obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                    obj.setVBCST(formatar(item.getBcIcmsSt()));
                    obj.setVFCPST(formatar(valorFcpSt));
                    obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado()).compareTo(String.valueOf(BigDecimal.ZERO)) > 0 ? "1" : "0");
                    obj.setVICMSST(formatar(valorIcmsSt));
                    icms.setICMS30(obj);
                }

                case "51" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS51 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS51();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);

                    // Modalidade de determinação da BC (0-Margem Valor Agregado, 1-Pauta, 2-Valor Operação)
                    obj.setModBC(formatar(item.getModBc()));

                    // 2. CÁLCULO DO FCP (independente do diferimento)
                    if(item.getBcFcp() != null && item.getAliqFcp() != null) {
                        valorFcp = item.getBcFcp().multiply(item.getAliqFcp()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 3. CÁLCULOS DE ICMS (quando houver BC e alíquota)
                    BigDecimal valorIcmsOperacao = BigDecimal.ZERO;
                    BigDecimal valorIcmsDiferido = BigDecimal.ZERO;
                    BigDecimal valorIcms = BigDecimal.ZERO;

                    // Cálculo do ICMS (considerando que os valores já vêm calculados do DTO)
                    valorIcms = (item.getValorIcms() != null ? item.getValorIcms() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                    valorIcmsDiferido = (item.getValorIcmsDiferido() != null ? item.getValorIcmsDiferido() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                    valorIcmsOperacao = (item.getValorIcmsOperacao() != null ? item.getValorIcmsOperacao() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);

                    if(item.getBcIcms() != null && item.getAliqIcms() != null) {
                        // 3.1. Valor total do ICMS (antes do diferimento)
                        valorIcmsOperacao = item.getBcIcms().multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                        // 3.2. Calcula diferimento com base no valor parcial (se existir)
                        if(item.getValorIcmsDiferido() != null) {
                            // Se o DTO já trouxer o valor diferido, usa esse valor
                            valorIcmsDiferido = item.getValorIcmsDiferido().setScale(2, RoundingMode.HALF_UP);
                            valorIcms = (valorIcmsOperacao.subtract(valorIcmsDiferido)).setScale(2, RoundingMode.HALF_UP);
                        } else {
                            // Caso contrário, considera ICMS integral (sem diferimento)
                            valorIcms = valorIcmsOperacao.setScale(2, RoundingMode.HALF_UP);
                        }
                    }

                    obj.setPICMS(formatar(item.getAliqIcms()));
                    obj.setPFCP(formatar(item.getAliqFcp()));
                    obj.setCBenefRBC(null);
                    obj.setPDif(null);
                    obj.setPFCPDif(null);
                    obj.setPRedBC(formatar(item.getPercentRedBc()));
                    obj.setVBC(formatar(item.getBcIcms()));
                    obj.setVFCP(valorFcp.toString());
                    obj.setVBCFCP(formatar(item.getBcFcp()));
                    obj.setVFCPDif(null);
                    obj.setVFCPEfet(null);
                    obj.setVICMS(valorIcms.toString());
                    obj.setVICMSDif(valorIcmsDiferido.toString());
                    obj.setVICMSOp(valorIcmsOperacao.toString());
                    icms.setICMS51(obj);
                }

                case "53" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS53 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS53();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);

                    // Alíquota ad rem (por unidade de medida)
                    BigDecimal aliqAdRem = item.getAliqIcms() != null ? item.getAliqIcms() : BigDecimal.ZERO;
                    obj.setAdRemICMS(aliqAdRem.toString());

                    // Quantidade tributada
                    BigDecimal qtdTributada = item.getQuantidadeTributacao() != null ? item.getQuantidadeTributacao() : BigDecimal.ZERO;
                    obj.setQBCMono(qtdTributada.toString());

                    // Cálculo do valor do ICMS monofásico
                    BigDecimal valorIcmsMono = qtdTributada.multiply(aliqAdRem).setScale(2, RoundingMode.HALF_UP);

                    obj.setAdRemICMS(formatar(item.getAliqIcms()));
                    obj.setAdRemICMSDif(null);
                    obj.setQBCMono(formatar(item.getBcIcms()));
                    obj.setQBCMonoDif(null);
                    obj.setPDif(null);
                    obj.setVICMSMono(formatar(valorIcmsMono.toString()));
                    obj.setVICMSMonoDif(null);
                    obj.setVICMSMonoOp(null);
                    icms.setICMS53(obj);
                }

                case "60" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS60 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS60();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);

                    // Cálculo do ICMS ST retido
                    BigDecimal valorIcmsStRetido = BigDecimal.ZERO;
                    if(item.getBcIcmsStRetido() != null && item.getAliqIcmsSt() != null) {
                        valorIcmsStRetido = item.getBcIcmsStRetido().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // Cálculo do FCP ST retido
                    BigDecimal valorFcpStRetido = BigDecimal.ZERO;
                    if(item.getBcFcpStRetido() != null && item.getAliqFcpSt() != null) {
                        valorFcpStRetido = item.getBcFcpStRetido().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    obj.setVBCSTRet(formatar(item.getBcIcmsStRetido()));
                    obj.setPST(formatar(item.getAliqIcmsSt()));
                    obj.setVICMSSubstituto(formatar(item.getValorIcmsSubstituto()));
                    obj.setVICMSSTRet(formatar(valorIcmsStRetido));
                    obj.setVBCFCPSTRet(formatar(item.getBcFcpStRetido()));
                    obj.setPFCPSTRet(formatar(item.getAliqFcpSt()));
                    obj.setVFCPSTRet(formatar(valorFcpStRetido));
                    obj.setPRedBCEfet(formatar(item.getPercentRedBcEfetivo()));
                    obj.setVBCEfet(null);
                    obj.setPICMSEfet(formatar(item.getAliqIcmsEfetivo()));
                    obj.setVICMSEfet(formatar(item.getValorIcmsEfetivo()));
                    icms.setICMS60(obj);
                }

                case "61" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS61 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS61();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);

                    // 2. Cálculo alternativo (caso não tenha alíquota específica de retenção)
                    if (item.getValorIcms() != null) {
                        // Se existir valor de ICMS no item, usa como valor retido (ajuste conforme necessidade)
                        obj.setVICMSMonoRet(formatar(item.getValorIcms()));

                        // Campos de quantidade e alíquota retidos (opcionais) - deixamos null
                        obj.setQBCMonoRet(null);
                        obj.setAdRemICMSRet(null);
                    } else {
                        // Se não houver valor de ICMS, mantém tudo null (conforme NT 2023.001)
                        obj.setVICMSMonoRet(null);
                        obj.setQBCMonoRet(null);
                        obj.setAdRemICMSRet(null);
                    }
                    icms.setICMS61(obj);
                }

                case "70" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS70 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS70();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(formatar(item.getModBc()));

                    // Cálculo com redução da BC
                    BigDecimal bcIcms = (item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal valorIcms = BigDecimal.ZERO;
                    if (item.getAliqIcms() != null) {
                        valorIcms = bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                    }

                    // Cálculo do ICMS ST
                    BigDecimal bcIcmsSt = item.getBcIcmsSt() != null ? item.getBcIcmsSt() : BigDecimal.ZERO;
                    BigDecimal valorIcmsSt = BigDecimal.ZERO;
                    if (item.getAliqIcmsSt() != null) {
                        valorIcmsSt = bcIcmsSt.multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                    }

                    obj.setPRedBC(formatar(item.getPercentRedBc()));
                    obj.setVBC(formatar(bcIcms));
                    obj.setPICMS(formatar(item.getAliqIcms()));
                    obj.setVICMS(formatar(valorIcms));
                    obj.setVBCFCP(formatar(item.getBcFcp()));
                    obj.setPFCP(formatar(item.getAliqFcp()));
                    obj.setVFCP(valorFcp.toString());
                    obj.setModBCST(formatar(item.getModBcSt()));
                    obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                    obj.setVBCST(formatar(bcIcmsSt));
                    obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                    obj.setVBCST(formatar(item.getBcIcmsSt()));
                    obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                    obj.setVICMSST(formatar(item.getValorIcmsSt()));
                    obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                    obj.setPFCPST(formatar(item.getAliqFcpSt()));
                    obj.setVFCPST(formatar(valorFcpSt));
                    obj.setVICMSDeson(formatar(item.getValorIcmsDesonerado()));
                    obj.setMotDesICMS(formatar(item.getMotDesIcms()));
                    obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado())); //Indica se o valor do ICMS desonerado (vICMSDeson) deduz do valor do item (vProd).0: Valor do ICMS desonerado (vICMSDeson) não deduz do valor do item (vProd) / total da NF-e 1: Valor do ICMS desonerado (vICMSDeson) deduz do valor do item (vProd) / total da NF-e.
                    obj.setVICMSSTDeson(null);
                    obj.setMotDesICMSST(formatar(item.getMotDesIcms()));
                    icms.setICMS70(obj);
                }

                case "90" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS90 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS90();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);

                    // 2. CÁLCULO DO ICMS PRÓPRIO (quando aplicável)
                    BigDecimal valorIcms = BigDecimal.ZERO;
                    if (item.getBcIcms() != null && item.getAliqIcms() != null) {
                        valorIcms = item.getBcIcms().multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 3. CÁLCULO DO FCP (Fundo de Combate à Pobreza)
                    if (item.getBcFcp() != null && item.getAliqFcp() != null) {
                        valorFcp = item.getBcFcp().multiply(item.getAliqFcp()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 4. CÁLCULO DO ICMS ST (quando aplicável)
                    BigDecimal valorIcmsSt = BigDecimal.ZERO;
                    if (item.getBcIcmsSt() != null && item.getAliqIcmsSt() != null) {
                        valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 5. CÁLCULO DO FCP ST
                    if (item.getBcFcpSt() != null && item.getAliqFcpSt() != null) {
                        valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    obj.setModBC(formatar(item.getModBc()));
                    obj.setVBC(formatar(item.getBcIcms()));
                    obj.setPRedBC(formatar(item.getPercentRedBc()));
                    obj.setPICMS(formatar(item.getAliqIcms()));
                    obj.setVICMS(formatar(valorIcms.toString()));
                    obj.setVBCFCP(formatar(item.getBcFcp()));
                    obj.setPFCP(formatar(item.getAliqFcp()));
                    obj.setVFCP(formatar(valorFcp.toString()));
                    obj.setModBCST(formatar(item.getModBcSt()));
                    obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                    obj.setVBCST(formatar(item.getBcIcmsSt()));
                    obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                    obj.setVICMSST(formatar(valorIcmsSt.toString()));
                    obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                    obj.setPFCPST(formatar(item.getAliqFcpSt()));
                    obj.setVFCPST(formatar(valorFcpSt.toString()));
                    obj.setVICMSDeson(formatar(item.getValorIcmsDesonerado()));
                    obj.setMotDesICMS(formatar(item.getMotDesIcms()));
                    obj.setIndDeduzDeson(formatar(item.getValorIcmsDesonerado()));
                    obj.setVICMSSTDeson(null);
                    obj.setMotDesICMSST(formatar(item.getMotDesIcms()));
                    icms.setICMS90(obj);
                }

                case "part" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCST(cst);
                    obj.setModBC(formatar(item.getModBc()));

                    // 2. CÁLCULO DO ICMS PRÓPRIO
                    BigDecimal valorIcms = BigDecimal.ZERO;
                    if (item.getBcIcms() != null && item.getAliqIcms() != null) {
                        valorIcms = item.getBcIcms().multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 3. CÁLCULO DO ICMS ST (quando aplicável)
                    BigDecimal valorIcmsSt = BigDecimal.ZERO;
                    if (item.getBcIcmsSt() != null && item.getAliqIcmsSt() != null) {
                        valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 4. CÁLCULO DO FCP ST
                    if (item.getBcFcpSt() != null && item.getAliqFcpSt() != null) {
                        valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    obj.setVBC(formatar(item.getBcIcms()));
                    obj.setPRedBC(formatar(item.getPercentRedBc()));
                    obj.setPICMS(formatar(item.getAliqIcms()));
                    obj.setVICMS(valorIcms.toString());
                    obj.setModBCST(formatar(item.getModBcSt()));
                    obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                    obj.setVBCST(formatar(item.getBcIcmsSt()));
                    obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                    obj.setVICMSST(formatar(valorIcmsSt.toString()));
                    obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                    obj.setPFCPST(formatar(item.getAliqFcpSt()));
                    obj.setVFCPST(formatar(valorFcpSt.toString()));
                    obj.setPBCOp(formatar(item.getPercentBcOperacao()));
                    obj.setUFST(ufDestinatario); // UF de destino da mercadoria
                    icms.setICMSPart(obj);
                }

                case "st" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSST obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSST();
                    obj.setOrig(item.getOrigIcms()!= null ? item.getOrigIcms() : "0");
                    obj.setCST(cst);

                    // 2. CÁLCULO DO ICMS ST RETIDO
                    BigDecimal valorIcmsStRetido = BigDecimal.ZERO;
                    if (item.getBcIcmsStRetido() != null && item.getAliqIcmsSt() != null) {
                        valorIcmsStRetido = item.getBcIcmsStRetido().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 3. CÁLCULO DO FCP ST RETIDO
                    BigDecimal valorFcpStRetido = BigDecimal.ZERO;
                    if (item.getBcFcpStRetido() != null && item.getAliqFcpSt() != null) {
                        valorFcpStRetido = item.getBcFcpStRetido().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 4. CÁLCULO EFETIVO (quando aplicável)
                    BigDecimal valorIcmsEfetivo = BigDecimal.ZERO;
                    if (item.getBcIcmsEfetivo() != null && item.getAliqIcmsEfetivo() != null) {
                        valorIcmsEfetivo = item.getBcIcmsEfetivo().multiply(item.getAliqIcmsEfetivo())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    obj.setVBCSTRet(formatar(item.getBcIcmsStRetido()) != null ? item.getBcIcmsStRetido().toString() : "0.00");
                    obj.setPST(formatar(item.getAliqIcmsSt()) != null ? item.getAliqIcmsSt().toString() : "0.00");
                    obj.setVICMSSubstituto(formatar(item.getValorIcmsSubstituto()) != null ? item.getValorIcmsSubstituto().toString() : "0.00");
                    obj.setVICMSSTRet(formatar(valorIcmsStRetido));
                    obj.setVBCFCPSTRet(formatar(item.getBcFcpStRetido()) != null ? item.getBcFcpStRetido().toString() : "0.00");
                    obj.setPFCPSTRet(formatar(item.getAliqFcpSt()) != null ? item.getAliqFcpSt().toString() : "0.00");
                    obj.setVFCPSTRet(formatar(valorFcpStRetido));
                    obj.setVBCSTDest(formatar(item.getBcIcmsStDestino()) != null ? item.getBcIcmsStDestino().toString() : "0.00");
                    obj.setVICMSSTDest(formatar(item.getValorIcmsStDestino()) != null ? item.getValorIcmsStDestino().toString() : "0.00");
                    obj.setPRedBCEfet(formatar(item.getPercentRedBcEfetivo()) != null ? item.getPercentRedBcEfetivo().toString() : "0.00");
                    obj.setVBCEfet(formatar(item.getBcIcmsEfetivo()) != null ? item.getBcIcmsEfetivo().toString() : "0.00");
                    obj.setPICMSEfet(formatar(item.getAliqIcmsEfetivo()) != null ? item.getAliqIcmsEfetivo().toString() : "0.00");
                    obj.setVICMSEfet(formatar(valorIcmsEfetivo.compareTo(BigDecimal.ZERO) > 0 ? valorIcmsEfetivo.toString() : null));
                    icms.setICMSST(obj);
                }

                case "101" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                    obj.setCSOSN(cst);

                    if (item.getValorCredIcmsSn() != null) {
                        // Se existir valor de crédito diretamente no item
                        obj.setVCredICMSSN(formatar(item.getValorCredIcmsSn()));

                        // Se tiver alíquota de crédito (opcional)
                        obj.setPCredSN(formatar(item.getAliqCredSn()));
                    } else {
                        // Sem crédito (preenche null conforme manual do Simples Nacional)
                        obj.setPCredSN(null);
                        obj.setVCredICMSSN(null);
                    }
                    System.out.println("[DEBUG] Alíquota FCP: " + item.getAliqFcp() + "% | Base FCP: " + item.getBcFcp());
                    icms.setICMSSN101(obj);
                }

                case "102" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                    obj.setCSOSN(cst);
                    icms.setICMSSN102(obj);
                }

                case "201" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                    obj.setCSOSN(cst);

                    // 2. CÁLCULO DO ICMS ST
                    BigDecimal valorIcmsSt = BigDecimal.ZERO;
                    if (item.getBcIcmsSt() != null && item.getAliqIcmsSt() != null) {
                        valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 3. CÁLCULO DO FCP ST
                    if (item.getBcFcpSt() != null && item.getAliqFcpSt() != null) {
                        valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 4. CÁLCULO DO CRÉDITO PRESUMIDO (quando houver)
                    BigDecimal valorCredito = BigDecimal.ZERO;
                    if (item.getAliqCredSn() != null && item.getValorCredIcmsSn() != null) {
                        valorCredito = item.getValorCredIcmsSn(); // Usa valor direto se existir
                    }

                    obj.setModBCST(formatar(item.getModBcSt()));
                    obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                    obj.setVBCST(formatar(item.getBcIcmsSt()));
                    obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                    obj.setVICMSST(formatar(valorIcmsSt.toString()));
                    obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                    obj.setPFCPST(formatar(item.getAliqFcpSt()));
                    obj.setVFCPST(formatar(valorFcpSt.toString()));
                    obj.setPCredSN(formatar(item.getAliqCredSn()));
                    obj.setVCredICMSSN(formatar(valorCredito.compareTo(BigDecimal.ZERO) > 0 ? valorCredito.toString() : null));
                    icms.setICMSSN201(obj);
                }

                case "202" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                    obj.setCSOSN(cst);

                    // 2. CÁLCULO DO ICMS ST
                    BigDecimal valorIcmsSt = BigDecimal.ZERO;
                    if (item.getBcIcmsSt() != null && item.getAliqIcmsSt() != null) {
                        valorIcmsSt = item.getBcIcmsSt().multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 3. CÁLCULO DO FCP ST
                    if (item.getBcFcpSt() != null && item.getAliqFcpSt() != null) {
                        valorFcpSt = item.getBcFcpSt().multiply(item.getAliqFcpSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    obj.setModBCST(formatar(item.getModBcSt()));
                    obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                    obj.setVBCST(formatar(item.getBcIcmsSt()));
                    obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                    obj.setVICMSST(formatar(valorIcmsSt.toString()));
                    obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                    obj.setPFCPST(formatar(item.getAliqFcpSt()));
                    obj.setVFCPST(formatar(valorFcpSt.toString()));
                    icms.setICMSSN202(obj);
                }

                case "500" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0");
                    obj.setCSOSN(cst);

                    // 2. CÁLCULO DO ICMS ST RETIDO
                    BigDecimal valorIcmsStRetido = BigDecimal.ZERO;
                    if (item.getBcIcmsStRetido() != null && item.getAliqIcmsSt() != null) {
                        valorIcmsStRetido = item.getBcIcmsStRetido()
                                .multiply(item.getAliqIcmsSt())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 3. CÁLCULO DO FCP ST RETIDO
                    BigDecimal valorFcpStRetido = BigDecimal.ZERO;
                    if (item.getBcFcpStRetido() != null && item.getAliqFcpSt() != null) {
                        valorFcpStRetido = item.getBcFcpStRetido()
                                .multiply(item.getAliqFcpSt())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    // 4. CÁLCULO EFETIVO (quando aplicável)
                    BigDecimal valorIcmsEfetivo = BigDecimal.ZERO;
                    if (item.getBcIcmsEfetivo() != null && item.getAliqIcmsEfetivo() != null) {
                        valorIcmsEfetivo = item.getBcIcmsEfetivo()
                                .multiply(item.getAliqIcmsEfetivo())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    obj.setVBCSTRet(formatar(item.getBcIcmsStRetido()));
                    obj.setPST(formatar(item.getAliqIcmsSt()));
                    obj.setVICMSSubstituto(formatar(item.getValorIcmsSubstituto()));
                    obj.setVICMSSTRet(formatar(valorIcmsStRetido.toString()));
                    obj.setVBCFCPSTRet(formatar(item.getBcFcpStRetido()));
                    obj.setPFCPSTRet(formatar(valorFcpStRetido.toString()));
                    obj.setVFCPSTRet(formatar(item.getValorFcpStRetido()));
                    obj.setPRedBCEfet(formatar(item.getPercentRedBcEfetivo()));
                    obj.setVBCEfet(formatar(item.getBcIcmsEfetivo()));
                    obj.setPICMSEfet(formatar(item.getAliqIcmsEfetivo()));
                    obj.setVICMSEfet(formatar(valorIcmsEfetivo.compareTo(BigDecimal.ZERO) > 0 ? valorIcmsEfetivo.toString() : null));
                    icms.setICMSSN500(obj);
                }

                case "900" -> {
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900();
                    obj.setOrig(item.getOrigIcms() != null ? item.getOrigIcms() : "0.00");
                    obj.setCSOSN(cst);
                    obj.setModBC(formatar(item.getModBc()));

                    // Cálculos para ICMS normal
                    BigDecimal bcIcms = item.getBcIcms() != null ? item.getBcIcms() : BigDecimal.ZERO;
                    BigDecimal valorIcms = BigDecimal.ZERO;
                    if (item.getAliqIcms() != null) {
                        valorIcms = bcIcms.multiply(item.getAliqIcms()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
                    }

                    // Cálculos para ICMS ST
                    BigDecimal bcIcmsSt = item.getBcIcmsSt() != null ? item.getBcIcmsSt() : BigDecimal.ZERO;
                    BigDecimal valorIcmsSt = BigDecimal.ZERO;
                    if (item.getAliqIcmsSt() != null) {
                        valorIcmsSt = bcIcmsSt.multiply(item.getAliqIcmsSt()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    }

                    obj.setVBC(formatar(bcIcms.toString()));
                    obj.setPRedBC(formatar(item.getPercentRedBc()));
                    obj.setPICMS(formatar(item.getAliqIcms()));
                    obj.setVICMS(formatar(valorIcms.toString()));
                    obj.setModBCST(formatar(item.getModBcSt()));
                    obj.setPMVAST(formatar(item.getPercentMargemIcmsSt()));
                    obj.setPRedBCST(formatar(item.getPercentRedBcSt()));
                    obj.setVBCST(formatar(bcIcmsSt.toString()));
                    obj.setPICMSST(formatar(item.getAliqIcmsSt()));
                    obj.setVICMSST(formatar(valorIcmsSt.toString()));
                    obj.setVBCFCPST(formatar(item.getBcFcpSt()));
                    obj.setPFCPST(formatar(item.getAliqFcpSt()));
                    obj.setVFCPST(formatar(valorFcpSt.toString()));
                    obj.setPCredSN(formatar(item.getAliqCredSn()));
                    obj.setVCredICMSSN(formatar(item.getValorCredIcmsSn()));
                    icms.setICMSSN900(obj);
                }
            }
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoICMS(icms));


            // === PIS ===
            TNFe.InfNFe.Det.Imposto.PIS pis = new TNFe.InfNFe.Det.Imposto.PIS();
            TNFe.InfNFe.Det.Imposto.PIS.PISAliq pisAliq = new TNFe.InfNFe.Det.Imposto.PIS.PISAliq();
            pisAliq.setCST(item.getCstPis());

            // Cálculo do PIS
            BigDecimal valorPis = BigDecimal.ZERO;
            if (item.getBcPis() != null && item.getAliqPis() != null) {
                valorPis = item.getBcPis().multiply(item.getAliqPis()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
            }

            pisAliq.setVBC(formatar(item.getBcPis()));
            pisAliq.setPPIS(formatar(item.getAliqPis().toString()));
            pisAliq.setVPIS(formatar(valorPis.toString()));
            pis.setPISAliq(pisAliq);
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoPIS(pis));

            // === Cofins ===
            TNFe.InfNFe.Det.Imposto.COFINS cofins = new TNFe.InfNFe.Det.Imposto.COFINS();
            TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq cofinsAliq = new TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq();
            cofinsAliq.setCST(item.getCstCofins());

            // Cálculo do COFINS
            BigDecimal valorCofins = BigDecimal.ZERO;
            if (item.getBcCofins() != null && item.getAliqCofins() != null) {
                valorCofins = item.getBcCofins().multiply(item.getAliqCofins()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            }

            cofinsAliq.setVBC(formatar(item.getBcCofins()));
            cofinsAliq.setPCOFINS(formatar(item.getAliqCofins().toString()));
            cofinsAliq.setVCOFINS(formatar(valorCofins.toString()));
            cofins.setCOFINSAliq(cofinsAliq);
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoCOFINS(cofins));

            det.setImposto(imposto);
            infNFe.getDet().add(det);
        }

        // Totais
        TNFe.InfNFe.Total total = new TNFe.InfNFe.Total();
        TNFe.InfNFe.Total.ICMSTot icmsTot = new TNFe.InfNFe.Total.ICMSTot();

        icmsTot.setVBC(formatar(dto.getValorBaseCalculo()));
        icmsTot.setVICMS(formatar(dto.getValorIcms()));
        icmsTot.setVICMSDeson(formatar(dto.getValorIcmsDesonerado()));
        icmsTot.setVFCP(formatar(dto.getValorFcp()));
        icmsTot.setVBCST(formatar(dto.getValorBaseCalculoSt()));
        icmsTot.setVST(formatar(dto.getValorSt()));
        icmsTot.setVFCPST(formatar(dto.getValorFcpSt()));
        icmsTot.setVFCPSTRet(formatar(dto.getValorFcpStRetido()));
        icmsTot.setVProd(formatar(dto.getValorProdutos()));
        icmsTot.setVFrete(formatar(dto.getValorFrete()));
        icmsTot.setVSeg(formatar(dto.getValorSeguro()));
        icmsTot.setVDesc(formatar(dto.getValorDesconto()));
        icmsTot.setVII(formatar(dto.getValorIi()));
        icmsTot.setVIPI(formatar(dto.getValorIpi()));
        icmsTot.setVIPIDevol(formatar(dto.getValorIpiDevolucao()));
        icmsTot.setVPIS(formatar(dto.getValorPis()));
        icmsTot.setVCOFINS(formatar(dto.getValorCofins()));
        icmsTot.setVOutro(formatar(dto.getValorOutros()));
        icmsTot.setVNF(formatar(dto.getValorTotal()));

        total.setICMSTot(icmsTot);
        infNFe.setTotal(total);

        // Transporte
        TNFe.InfNFe.Transp transp = new TNFe.InfNFe.Transp();
        transp.setModFrete(dto.getModalidadeFrete());
        infNFe.setTransp(transp);

        //Forma de Pagamento
        TNFe.InfNFe.Pag.DetPag detPag = new TNFe.InfNFe.Pag.DetPag();
        detPag.setIndPag("0");
        detPag.setTPag(dto.getFormaPagamento());
        detPag.setVPag(formatar(dto.getValorPagamento()));
        TNFe.InfNFe.Pag pag = new TNFe.InfNFe.Pag();
        pag.getDetPag().add(detPag); // Adiciona o DetPag à lista
        infNFe.setPag(pag);

        // Informações adicionais
        TNFe.InfNFe.InfAdic infAdic = new TNFe.InfNFe.InfAdic();
        infAdic.setInfAdFisco(dto.getInformacaoAdicionalFisco());
        infAdic.setInfCpl(dto.getInformacaoAdicionalContribuinte());
        infNFe.setInfAdic(infAdic);

        tnFe.setInfNFe(infNFe);

        // Informações do Protocolo
        TProtNFe protNfe = new TProtNFe();
        protNfe.setVersao("4.00"); // Versão do protocolo

        // Cria e configura as informações do protocolo (InfProt)
        TProtNFe.InfProt infProt = new TProtNFe.InfProt();
        infProt.setId("ID" + dto.getNumeroProtocolo()); // ID do protocolo (ex.: "ID135251857425711")
        infProt.setTpAmb(dto.getTipoAmbiente()); // Tipo de ambiente (1 = Produção, 2 = Homologação)
        infProt.setVerAplic("SP_NFE_PL009_V4"); // Versão do aplicativo que processou a NF-e
        infProt.setChNFe(dto.getChave()); // Chave da NF-e
        infProt.setDhRecbto(OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))); // Data/hora de recebimento
        infProt.setNProt(dto.getNumeroProtocolo()); // Número do protocolo
        infProt.setCStat(dto.getCstat()); // Código do status (ex.: "100" = Autorizado)
        infProt.setXMotivo(dto.getMotivoProtocolo()); // Motivo da resposta (ex.: "Autorizado o uso da NF-e")
        protNfe.setInfProt(infProt);

        return tnFe;
    }

    //Obtém o Estado pela zona do fuso horário
    private static ZoneId getZoneIdPorUF(String codigoUF) {
        // Mapeamento de UF para ZoneId (fuso horário)
        return switch (codigoUF) { // Acre
            case "AC", "AM" -> // Alguns municípios do Amazonas
                    ZoneId.of("America/Rio_Branco"); // UTC-5
            default -> // Demais estados (SP, RJ, DF, etc.)
                    ZoneId.of("America/Sao_Paulo"); // UTC-3
        };
    }
}