package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.NotaFiscalItem;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class NotaFiscalItemDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /* FK simples (id da nota) – facilita envio via JSON */
    private Long notaFiscalId;

    /* PROD */
    private String cProd;
    private String xProd;
    private String ncm;
    private String cfop;
    private String uCom;
    private BigDecimal qCom;
    private BigDecimal vUnCom;
    private BigDecimal vProd;
    private String uTrib;
    private BigDecimal qTrib;
    private BigDecimal vUnTrib;

    /* IMPOSTOS */
    private BigDecimal vICMS;
    private BigDecimal vIPI;
    private BigDecimal vPIS;
    private BigDecimal vCOFINS;

    public NotaFiscalItemDto() {
        super();
    }

    public NotaFiscalItemDto(NotaFiscalItem obj) {
        this.id = obj.getId();
        this.notaFiscalId = obj.getNotaFiscal() != null ? obj.getNotaFiscal().getId() : null;
        this.cProd = obj.getCProd();
        this.xProd = obj.getXProd();
        this.ncm = obj.getNcm();
        this.cfop = obj.getCfop();
        this.uCom = obj.getUCom();
        this.qCom = obj.getQCom();
        this.vUnCom = obj.getVUnCom();
        this.vProd = obj.getVProd();
        this.uTrib = obj.getUTrib();
        this.qTrib = obj.getQTrib();
        this.vUnTrib = obj.getVUnTrib();
        this.vICMS = obj.getVICMS();
        this.vIPI = obj.getVIPI();
        this.vPIS = obj.getVPIS();
        this.vCOFINS = obj.getVCOFINS();
    }
}

