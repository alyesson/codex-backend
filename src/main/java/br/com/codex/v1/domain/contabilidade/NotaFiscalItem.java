package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.NotaFiscalItemDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
public class NotaFiscalItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* FK — chave estrangeira para NotaFiscal.id */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_fiscal_id", nullable = false)
    private NotaFiscal notaFiscal;

    /* ------------ GRUPO PROD (det) ------------ */
    private Integer nItem;         // Número do item
    private Integer cEAN;          // Código GTIN/EAN do produto
    private String cProd;          // Código do produto
    private String xProd;          // Descrição
    private String ncm;            // NCM
    private String cfop;           // CFOP
    private String uCom;           // Unidade comercial
    private BigDecimal qCom;       // Quantidade
    private BigDecimal vUnCom;     // Valor unitário
    private BigDecimal vProd;      // Valor total do item
    private String uTrib;          // Unidade tributável (opcional)
    private BigDecimal qTrib;      // Quantidade tributável (opcional)
    private BigDecimal vUnTrib;    // Valor unitário tributável (opcional)

    /* ------------ IMPOSTOS (simplificado) ------------ */
    private BigDecimal vICMS;
    private BigDecimal vIPI;
    private BigDecimal vPIS;
    private BigDecimal vCOFINS;

    public NotaFiscalItem() {
        super();
    }

    public NotaFiscalItem(Long id, NotaFiscal notaFiscal, Integer nItem, Integer cEan, String cProd, String xProd, String ncm, String cfop, String uCom, BigDecimal qCom, BigDecimal vUnCom, BigDecimal vProd, String uTrib, BigDecimal qTrib, BigDecimal vUnTrib, BigDecimal vICMS, BigDecimal vIPI, BigDecimal vPIS, BigDecimal vCOFINS) {
        this.id = id;
        this.notaFiscal = notaFiscal;
        this.nItem = nItem;
        this.cEAN = cEAN;
        this.cProd = cProd;
        this.xProd = xProd;
        this.ncm = ncm;
        this.cfop = cfop;
        this.uCom = uCom;
        this.qCom = qCom;
        this.vUnCom = vUnCom;
        this.vProd = vProd;
        this.uTrib = uTrib;
        this.qTrib = qTrib;
        this.vUnTrib = vUnTrib;
        this.vICMS = vICMS;
        this.vIPI = vIPI;
        this.vPIS = vPIS;
        this.vCOFINS = vCOFINS;
    }

    public NotaFiscalItem(NotaFiscalItemDto obj) {
        this.id = obj.getId();
        this.nItem = obj.getNItem();
        this.cEAN = obj.getCEAN();
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NotaFiscalItem that = (NotaFiscalItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}