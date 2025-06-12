package br.com.codex.v1.mapper;

import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.dto.NotaFiscalDuplicatasDto;
import br.com.codex.v1.domain.dto.NotaFiscalItemDto;
import br.com.swconsultoria.nfe.schema_4.enviNFe.*;
import br.com.swconsultoria.nfe.util.ConstantesUtil;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotaFiscalMapper {

    public static TNFe paraTNFe(NotaFiscalDto dto) {
        ObjectFactory factory = new ObjectFactory();
        TNFe tnFe = factory.createTNFe();

        TNFe.InfNFe infNFe = new TNFe.InfNFe();
        infNFe.setVersao(ConstantesUtil.VERSAO.NFE);
        infNFe.setId("NFe" + dto.getId());

        // Identificação
        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();
        ide.setNatOp(dto.getNaturezaOperacao());
        ide.setSerie(String.valueOf(dto.getSerie()));
        ide.setNNF(String.valueOf(dto.getNumero()));
        ide.setDhEmi(dto.getEmissao().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        ide.setDhSaiEnt(dto.getDhSaidaEntrada());
        ide.setTpNF(dto.getTipo());
        ide.setIdDest(dto.getIndicadorPresenca());
        ide.setCMunFG(dto.getCodigoMunicipioEmitente());
        ide.setFinNFe(dto.getCodigoNf());
        ide.setIndFinal(dto.get().toString());
        ide.setIndPres(dto.getPresencaComprador().toString());
        ide.setProcEmi("0");
        ide.setVerProc("1.0");
        infNFe.setIde(ide);

        // Emitente
        TNFe.InfNFe.Emit emit = new TNFe.InfNFe.Emit();
        emit.setCNPJ(dto.getCnpjEmitente());
        emit.setXNome(dto.getNomeEmitente());
        emit.setXFant(dto.getNomeFantasiaEmitente());
        emit.setIE(dto.getInscricaoEstadualEmitente());
        emit.setIEST(dto.getInscricaoEstadualStEmitente());
        emit.setIM(dto.getInscricaoMunicipalEmitente());
        emit.setCNAE(dto.getCnaeFiscalEmitente());
        emit.setCRT(dto.getRegimeTributarioEmitente());

        TEnderEmi enderEmit = new TEnderEmi();
        enderEmit.setXLgr(dto.getLogradouroEmitente());
        enderEmit.setNro(dto.getNumeroEmitente());
        enderEmit.setXCpl(dto.getComplementoEmitente());
        enderEmit.setXBairro(dto.getBairroEmitente());
        enderEmit.setCMun(dto.getCodigoMunicipioEmitente());
        enderEmit.setXMun(dto.getMunicipioEmitente());
        enderEmit.setUF(TUfEmi.fromValue(dto.getUfEmitente())); // Certifique-se de passar "SP", "RJ", etc.
        enderEmit.setCEP(dto.getCepEmitente());
        enderEmit.setFone(dto.getTelefoneEmitente());
        emit.setEnderEmit(enderEmit);
        infNFe.setEmit(emit);


        // Destinatário
        TNFe.InfNFe.Dest dest = new TNFe.InfNFe.Dest();
        dest.setCNPJ(dto.getCnpjDestinatario());
        dest.setCPF(dto.getCpfDestinatario());
        dest.setXNome(dto.getNomeDestinatario());
        dest.setIE(dto.getInscricaoEstadualDestinatario());
        dest.setIndIEDest(dto.getIndicadorInscricaoEstadualDestinatario().toString());
        dest.setEmail(dto.getEmailDestinatario());
        TEndereco enderDest = new TEndereco();
        enderDest.setXLgr(dto.getLogradouroDestinatario());
        enderDest.setNro(dto.getNumeroDestinatario());
        enderDest.setXCpl(dto.getComplementoDestinatario());
        enderDest.setXBairro(dto.getBairroDestinatario());
        enderDest.setCMun(dto.getCodigoMunicipioDestinatario());
        enderDest.setXMun(dto.getMunicipioDestinatario());
        enderDest.setUF(TUf.valueOf(dto.getUfDestinatario())); // "SP", "MG", etc.
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
                    obj.setVBC(item.getBcIcms().toString());
                    obj.setPICMS(item.getAliqIcms().toString());
                    obj.setVICMS(item.getValorIcms().toString());
                    obj.setPFCP(item.getAliqFcp().toString());
                    obj.setVFCP(item.getValorFcp().toString());
                    icms.setICMS00(obj);
                }

                case "02" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS02 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS02();

                }

                case "10" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS10 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS10();

                }

                case "15" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS15 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS15();
                }

                case "20" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS20 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS20();
                }

                case "30" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS30 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS30();

                }

                case "51" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS51 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS51();

                }

                case "53" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS53 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS53();

                }

                case "60" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS60 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS60();

                }

                case "61" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS61 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS61();

                }

                case "70" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS70 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS70();

                }

                case "90" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMS90 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS90();

                }

                case "part" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSPart();

                }

                case "st" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSST obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSST();

                }

                case "101" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN101();

                }

                case "102" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN102();


                }

                case "201" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN201();

                }

                case "202" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN202();

                }

                case "500" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN500();

                }

                case "900" ->{
                    TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900 obj = new TNFe.InfNFe.Det.Imposto.ICMS.ICMSSN900();

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