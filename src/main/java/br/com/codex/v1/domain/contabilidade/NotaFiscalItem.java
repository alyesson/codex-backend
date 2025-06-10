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
    private Integer nItem;         //Obrigatório - Número do item
    private String cProd;          //Obrigatório - Código do produto
    private Integer cEAN;          // Código GTIN/EAN do produto
    private String xProd;          //Obrigatório - Descrição
    private Integer ncm;           //Obrigatório - NCM
    private String nve;           // Nomeclatura de valor Aduaneiro e Estatística
    private Integer cest;          // Código especificador da Substituição tributária
    private boolean indEscala;     // Indicador de produção em escala relevante - Convênio icms 52/2017
    private Integer cnpjFab;       // Obrigatório para produto em escala NÃO relevante;
    private Integer extipi;        // Código Ex tipi do produto
    private String cfop;           //Obrigatório - CFOP
    private String uCom;           //Obrigatório - Unidade comercial
    private BigDecimal qCom;       //Obrigatório - Quantidade
    private BigDecimal vUnCom;     //Obrigatório - Valor unitário Comercial
    private BigDecimal vProd;      // Valor bruto. Deve ser igual ao produto de Valor unitário comercial com quantidade comercial
    private String uTrib;          // Unidade tributável (opcional)
    private BigDecimal qTrib;      // Quantidade tributável (opcional)
    private BigDecimal vUnTrib;    // Valor unitário tributável (opcional)
    private BigDecimal vFrete;      // Valor do frete (opcional)
    private BigDecimal vSeg;        // Valor do seguro (opcional)
    private BigDecimal vDesc;       // Valor do desconto (opcional)
    private BigDecimal vOutro;      // Valor de outras despesas acessórias.
    private Integer indTot;         //Valor do item (valor_bruto) compõe valor total da NFe (valor_produtos)? 0: não 1: sim (valor padrão)
    private Integer xPed;           // Número do Item de Pedido de Compra.
    private Integer tpOp;           //Tipo da operação. 1: venda concessionária 2: faturamento direto 3: venda direta 0: outros

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