package br.com.codex.v1.mapper;

import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.dto.NotaFiscalDuplicatasDto;
import br.com.codex.v1.domain.dto.NotaFiscalItemDto;
import br.com.swconsultoria.nfe.schema_4.enviNFe.*;
import br.com.swconsultoria.nfe.util.ConstantesUtil;

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
        ide.setDhEmi(dto.getDataEmissao().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        ide.setDhSaiEnt(dto.getDataEntradaSaida().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        ide.setTpNF(dto.getTipoDocumento().toString());
        ide.setIdDest(dto.getLocalDestino().toString());
        ide.setCMunFG(dto.getMunicipio().toString());
        ide.setFinNFe(dto.getFinalidadeEmissao().toString());
        ide.setIndFinal(dto.getConsumidorFinal().toString());
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
            prod.setXProd(item.getDescricao());
            prod.setCEAN(item.getCean());
            prod.setCBarra("");
            prod.setNCM(item.getNcm());
            prod.setCEST(String.valueOf(item.getCest()));
            prod.setIndEscala("");
            prod.setCNPJFab(dto.getCnpjEmitente());
            prod.setCBenef("");
            prod.setEXTIPI(item.getExtipi());
            prod.setCFOP(item.getCfop());
            prod.setUCom(item.getUnidadeComercial());
            prod.setQCom(String.valueOf(item.getQuantidadeComercial()));
            prod.setVUnCom(String.valueOf(item.getValorUnitarioComercial()));
            prod.setVProd(String.valueOf(item.getValorBruto()));
            prod.setCEANTrib(item.getCodigoBarrasTributavel());
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
            TNFe.InfNFe.Det.Imposto.ICMS.ICMS00 icms00 = new TNFe.InfNFe.Det.Imposto.ICMS.ICMS00();
            icms00.setOrig(String.valueOf(item.getIcmsOrigem()));
            icms00.setCST(String.valueOf(item.getIcmsSituacaoTributaria()));
            icms00.setModBC("0");
            icms00.setVBC(item.getIcmsBaseCalculo().toString());
            icms00.setPICMS(item.getIcmsAliquota().toString());
            icms00.setVICMS(item.getIcmsValor().toString());
            icms.setICMS00(icms00);
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoICMS(icms));

            // === IPI ===
            TNFe.InfNFe.Det.IPI ipi = new TNFe.InfNFe.Det.Imposto.IPI();
            TNFe.InfNFe.Det.Imposto.IPI.IPITrib ipiTrib = new TNFe.InfNFe.Det.Imposto.IPI.IPITrib();
            ipiTrib.setCST(item.getIpiSituacaoTributaria());
            ipiTrib.setVBC(item.getIpiBaseCalculo().toString());
            ipiTrib.setPIPI(item.getIpiAliquota().toString());
            ipiTrib.setVIPI(item.getIpiValor().toString());
            ipi.setIPITrib(ipiTrib);
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoIPI(ipi));

            // === PIS ===
            TNFe.InfNFe.Det.Imposto.PIS pis = new TNFe.InfNFe.Det.Imposto.PIS();
            TNFe.InfNFe.Det.Imposto.PIS.PISAliq pisAliq = new TNFe.InfNFe.Det.Imposto.PIS.PISAliq();
            pisAliq.setCST(String.valueOf(item.getPisSituacaoTributaria()));
            pisAliq.setVBC(item.getPisBaseCalculo().toString());
            pisAliq.setPPIS(item.getPisAliquota().toString());
            pisAliq.setVPIS(item.getPisValor().toString());
            pis.setPISAliq(pisAliq);
            imposto.getContent().add(factory.createTNFeInfNFeDetImpostoPIS(pis));

            // === COFINS ===
            TNFe.InfNFe.Det.Imposto.COFINS cofins = new TNFe.InfNFe.Det.Imposto.COFINS();
            TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq cofinsAliq = new TNFe.InfNFe.Det.Imposto.COFINS.COFINSAliq();
            cofinsAliq.setCST(String.valueOf(item.getCofinsSituacaoTributaria()));
            cofinsAliq.setVBC(item.getCofinsBaseCalculo().toString());
            cofinsAliq.setPCOFINS(item.getCofinsAliquota().toString());
            cofinsAliq.setVCOFINS(item.getCofinsValor().toString());
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
        icmsTot.setVII(String.valueOf(dto.getValorTotalII()));
        icmsTot.setVIPI(String.valueOf(dto.getValorIPI()));
        icmsTot.setVPIS(String.valueOf(dto.getValorPIS()));
        icmsTot.setVCOFINS(String.valueOf(dto.getValorCOFINS()));
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
        infAdic.setInfAdFisco(dto.getInformacoesAdicionaisFisco());
        infAdic.setInfCpl(dto.getInformacoesAdicionaisContribuinte());
        infNFe.setInfAdic(infAdic);

        tnFe.setInfNFe(infNFe);
        return tnFe;
    }
}