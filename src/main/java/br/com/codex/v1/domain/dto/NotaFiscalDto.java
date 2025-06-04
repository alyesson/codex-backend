package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.NotaFiscal;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class NotaFiscalDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /* -------- básicos -------- */
    private Long id;
    private String chaveAcesso;
    private String cuf;
    private Integer modelo;
    private Integer serie;
    private Long numero;
    private OffsetDateTime dataHoraEmissao;
    private Integer tpEmis;
    private Integer tpNF;
    private String natOp;

    /* -------- emitente -------- */
    private String cnpjEmit;
    private String xNomeEmit;
    private String ieEmit;
    private String crt;

    /* -------- destinatário -------- */
    private String cnpjDest;
    private String xNomeDest;
    private String ieDest;

    /* endereço dest */
    private String xLgrDest;
    private String nroDest;
    private String xCplDest;
    private String xBairroDest;
    private String cMunDest;
    private String xMunDest;
    private String ufDest;
    private String cepDest;
    private String cPaisDest;
    private String xPaisDest;
    private String foneDest;

    /* -------- transportador -------- */
    private String modFrete;
    private String cnpjTransp;
    private String xNomeTransp;
    private String ieTransp;
    private String xEndTransp;
    private String xMunTransp;
    private String ufTransp;

    /* -------- fatura -------- */
    private String numeroFatura;
    private BigDecimal valorOriginalFatura;
    private BigDecimal valorDescontoFatura;
    private BigDecimal valorLiquidoFatura;

    /* -------- totais -------- */
    private BigDecimal vNF;
    private BigDecimal vICMS;
    private BigDecimal vST;

    /* -------- infos extras -------- */
    private String infRespTec;
    private String infIntermed;
    private String infAdic;
    private String pag;

    /* -------- listas -------- */
    private List<NotaFiscalItemDto> itens;
    private List<NotaFiscalDuplicatasDto> duplicatas;

    public NotaFiscalDto() {
        super();
    }

    public NotaFiscalDto(NotaFiscal obj) {
        this.id = obj.getId();
        this.chaveAcesso = obj.getChaveAcesso();
        this.cuf = obj.getCuf();
        this.modelo = obj.getModelo();
        this.serie = obj.getSerie();
        this.numero = obj.getNumero();
        this.dataHoraEmissao = obj.getDataHoraEmissao();
        this.tpEmis = obj.getTpEmis();
        this.tpNF = obj.getTpNF();
        this.natOp = obj.getNatOp();
        this.cnpjEmit = obj.getCnpjEmit();
        this.xNomeEmit = obj.getXNomeEmit();
        this.ieEmit = obj.getIeEmit();
        this.crt = obj.getCrt();
        this.cnpjDest = obj.getCnpjDest();
        this.xNomeDest = obj.getXNomeDest();
        this.ieDest = obj.getIeDest();
        this.xLgrDest = obj.getXLgrDest();
        this.nroDest = obj.getNroDest();
        this.xCplDest = obj.getXCplDest();
        this.xBairroDest = obj.getXBairroDest();
        this.cMunDest = obj.getCMunDest();
        this.xMunDest = obj.getXMunDest();
        this.ufDest = obj.getUfDest();
        this.cepDest = obj.getCepDest();
        this.cPaisDest = obj.getCPaisDest();
        this.xPaisDest = obj.getXPaisDest();
        this.foneDest = obj.getFoneDest();
        this.modFrete = obj.getModFrete();
        this.cnpjTransp = obj.getCnpjTransp();
        this.xNomeTransp = obj.getXNomeTransp();
        this.ieTransp = obj.getIeTransp();
        this.xEndTransp = obj.getXEndTransp();
        this.xMunTransp = obj.getXMunTransp();
        this.ufTransp = obj.getUfTransp();
        this.numeroFatura = obj.getNumeroFatura();
        this.valorOriginalFatura = obj.getValorOriginalFatura();
        this.valorDescontoFatura = obj.getValorDescontoFatura();
        this.valorLiquidoFatura = obj.getValorLiquidoFatura();
        this.vNF = obj.getVNF();
        this.vICMS = obj.getVICMS();
        this.vST = obj.getVST();
        this.infRespTec = obj.getInfRespTec();
        this.infIntermed = obj.getInfIntermed();
        this.infAdic = obj.getInfAdic();
        this.pag = obj.getPag();

        // Convertendo itens da nota fiscal para DTO
        this.itens = obj.getItens() != null
                ? obj.getItens().stream().map(NotaFiscalItemDto::new).toList()
                : null;

        // Convertendo duplicatas da nota fiscal para DTO
        this.duplicatas = obj.getDuplicatas() != null
                ? obj.getDuplicatas().stream().map(NotaFiscalDuplicatasDto::new).toList()
                : null;
    }
}