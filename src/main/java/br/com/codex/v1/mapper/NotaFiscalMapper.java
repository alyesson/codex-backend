package br.com.codex.v1.mapper;

import br.com.codex.v1.domain.contabilidade.NotaFiscal;
import br.com.codex.v1.domain.contabilidade.NotaFiscalDuplicatas;
import br.com.codex.v1.domain.contabilidade.NotaFiscalItem;
import br.com.swconsultoria.nfe.schema_4.enviNFe.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotaFiscalMapper {

    public static TNFe paraTNFe(NotaFiscal nf) {
        ObjectFactory factory = new ObjectFactory();
        TNFe tnfe = factory.createTNFe();

        TNFe.InfNFe infNFe = new TNFe.InfNFe();
        infNFe.setVersao("4.00");
        infNFe.setId("NFe" + nf.getChaveAcesso());

        // Identificação da NF
        TNFe.InfNFe.Ide ide = new TNFe.InfNFe.Ide();
        ide.setCUF(nf.getCuf());
        ide.setMod(nf.getModelo().toString());
        ide.setSerie(nf.getSerie().toString());
        ide.setNNF(nf.getNumero().toString());
        ide.setDhEmi(nf.getDataHoraEmissao().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        ide.setTpEmis(nf.getTpEmis().toString());
        ide.setTpNF(nf.getTpNF().toString());
        ide.setNatOp(nf.getNatOp());
        ide.setProcEmi("0");
        ide.setVerProc("1.0");
        infNFe.setIde(ide);

        // Emitente
        TNFe.InfNFe.Emit emit = new TNFe.InfNFe.Emit();
        emit.setCNPJ(nf.getCnpjEmit());
        emit.setXNome(nf.getXNomeEmit());
        emit.setIE(nf.getIeEmit());
        emit.setCRT(nf.getCrt());
        infNFe.setEmit(emit);

        // Destinatário
        TNFe.InfNFe.Dest dest = new TNFe.InfNFe.Dest();
        dest.setCNPJ(nf.getCnpjDest());
        dest.setXNome(nf.getXNomeDest());
        dest.setIE(nf.getIeDest());

        TEndereco enderDest =  new TEndereco();
        enderDest.setXLgr(nf.getXLgrDest());
        enderDest.setNro(nf.getNroDest());
        enderDest.setXCpl(nf.getXCplDest());
        enderDest.setXBairro(nf.getXBairroDest());
        enderDest.setCMun(nf.getCMunDest());
        enderDest.setXMun(nf.getXMunDest());
        enderDest.setUF(TUf.valueOf(nf.getUfDest()));
        enderDest.setCEP(nf.getCepDest());
        enderDest.setCPais(nf.getCPaisDest());
        enderDest.setXPais(nf.getXPaisDest());
        enderDest.setFone(nf.getFoneDest());
        dest.setEnderDest(enderDest);
        infNFe.setDest(dest);

        // Produtos / Itens
        List<NotaFiscalItem> itens = nf.getItens();
        for (int i = 0; i < itens.size(); i++) {
            NotaFiscalItem item = itens.get(i);
            TNFe.InfNFe.Det det = new TNFe.InfNFe.Det();
            det.setNItem(String.valueOf(i + 1));

            TNFe.InfNFe.Det.Prod prod = new TNFe.InfNFe.Det.Prod();
            prod.setCProd(item.getCProd());
            prod.setXProd(item.getXProd());
            prod.setNCM(item.getNcm());
            prod.setCFOP(item.getCfop());
            prod.setUCom(item.getUCom());
            prod.setQCom(String.valueOf(item.getQCom()));
            prod.setVUnCom(String.valueOf(item.getVUnCom()));
            prod.setVProd(String.valueOf(item.getVProd()));
            prod.setUTrib(item.getUTrib());
            prod.setQTrib(String.valueOf(item.getQTrib()));
            prod.setVUnTrib(String.valueOf(item.getVUnTrib()));
            det.setProd(prod);

            infNFe.getDet().add(det);
        }

        // Totais
        TNFe.InfNFe.Total total = new TNFe.InfNFe.Total();
        TNFe.InfNFe.Total.ICMSTot icmsTot = new TNFe.InfNFe.Total.ICMSTot();
        icmsTot.setVNF(String.valueOf(nf.getVNF()));
        icmsTot.setVICMS(String.valueOf(nf.getVICMS()));
        icmsTot.setVST(String.valueOf(nf.getVST()));
        total.setICMSTot(icmsTot);
        infNFe.setTotal(total);

        // Transporte
        TNFe.InfNFe.Transp transp = new TNFe.InfNFe.Transp();
        transp.setModFrete(nf.getModFrete());
        TNFe.InfNFe.Transp.Transporta transporta = new TNFe.InfNFe.Transp.Transporta();
        transporta.setCNPJ(nf.getCnpjTransp());
        transporta.setXNome(nf.getXNomeTransp());
        transporta.setIE(nf.getIeTransp());
        transporta.setXEnder(nf.getXEndTransp());
        transporta.setXMun(nf.getXMunTransp());
        transporta.setUF(TUf.valueOf(nf.getUfTransp()));
        transp.setTransporta(transporta);
        infNFe.setTransp(transp);

        // Fatura e Duplicatas
        TNFe.InfNFe.Cobr cobr = new TNFe.InfNFe.Cobr();
        TNFe.InfNFe.Cobr.Fat fat = new TNFe.InfNFe.Cobr.Fat();
        fat.setNFat(nf.getNumeroFatura());
        fat.setVOrig(String.valueOf(nf.getValorOriginalFatura()));
        fat.setVDesc(String.valueOf(nf.getValorDescontoFatura()));
        fat.setVLiq(String.valueOf(nf.getValorLiquidoFatura()));
        cobr.setFat(fat);

        List<NotaFiscalDuplicatas> duplicatas = nf.getDuplicatas();
        for (NotaFiscalDuplicatas dup : duplicatas) {
            TNFe.InfNFe.Cobr.Dup dupXml = new TNFe.InfNFe.Cobr.Dup();
            dupXml.setNDup(dup.getNumeroDuplicata());
            dupXml.setDVenc(dup.getDataVencimento().toLocalDate().toString());
            dupXml.setVDup(String.valueOf(dup.getValorDuplicata()));
            cobr.getDup().add(dupXml);
        }
        infNFe.setCobr(cobr);

        // Informações adicionais
        TNFe.InfNFe.InfAdic infAdic = new TNFe.InfNFe.InfAdic();
        infAdic.setInfAdFisco(nf.getInfIntermed());
        infAdic.setInfCpl(nf.getInfAdic());
        infNFe.setInfAdic(infAdic);

        // Pagamento (simplificado, manual)
        TNFe.InfNFe.Pag pag = new TNFe.InfNFe.Pag();
        TNFe.InfNFe.Pag.DetPag detPag = new TNFe.InfNFe.Pag.DetPag();
        detPag.setIndPag("0"); // à vista
        detPag.setTPag("01");  // dinheiro
        detPag.setVPag(String.valueOf(nf.getValorLiquidoFatura()));
        pag.getDetPag().add(detPag);
        infNFe.setPag(pag);

        tnfe.setInfNFe(infNFe);
        return tnfe;
    }
}
