package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.dto.NotaFiscalDuplicatasDto;
import br.com.codex.v1.domain.dto.NotaFiscalItemDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class NotaFiscal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ------------ GRUPO IDE (identificação) ------------ */
    private String chaveAcesso;         // 44 dígitos
    private String cuf;                 // Código da UF (B02)
    private Integer modelo;             // 55
    private Integer serie;              // (B07)
    private Long numero;                // nNF (B08)
    private OffsetDateTime dataHoraEmissao; // dhEmi (B09)
    private Integer tpEmis;             // Forma de emissão (B22)
    private Integer tpNF;               // 0=entrada,1=saída (P24)
    private String natOp;               // Natureza da operação (B11)

    /* ------------ GRUPO EMIT (emitente) ------------ */
    private String cnpjEmit;            // C02
    private String xNomeEmit;           // C03
    private String ieEmit;              // C17
    private String crt;                 // Regime tributário

    /* ------------ GRUPO DEST (destinatário) ------------ */
    private String cnpjDest;            // CNPJ/CPF destinatário
    private String xNomeDest;
    private String ieDest;
    private String xLgrDest;    // Logradouro
    private String nroDest;     // Número
    private String xCplDest;    // Complemento
    private String xBairroDest; // Bairro
    private String cMunDest;    // Código IBGE
    private String xMunDest;    // Nome município
    private String ufDest;      // UF
    private String cepDest;     // CEP
    private String cPaisDest;   // Código país
    private String xPaisDest;   // Nome país
    private String foneDest;    // Telefone

    /* ------------ TRANSPORTADOR (transporta) ------------ */
    private String modFrete;    // Modalidade do frete (0=emitente, 1=destinatário, etc.)
    private String cnpjTransp;
    private String xNomeTransp;
    private String ieTransp;
    private String xEndTransp;
    private String xMunTransp;
    private String ufTransp;

    /* ------------ FATURA (cobr) ------------ */
    private String numeroFatura;      // Número da fatura
    private BigDecimal valorOriginalFatura;
    private BigDecimal valorDescontoFatura;
    private BigDecimal valorLiquidoFatura;


    /* ------------ TOTAIS ------------ */
    private BigDecimal vNF;             // Valor total da nota (P32)
    private BigDecimal vICMS;           // Total ICMS (P33)
    private BigDecimal vST;             // Total ST (P34)

    /* ------------ INFORMAÇÕES ADICIONAIS ------------ */
    private String infRespTec;
    private String infIntermed;
    private String infAdic;
    private String pag;

    /* ------------ RELACIONAMENTO ------------ */
    @OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaFiscalItem> itens;

    @OneToMany(mappedBy = "notaFiscal", cascade = CascadeType.ALL)
    private List<NotaFiscalDuplicatas> duplicatas;

    public NotaFiscal() {
        super();
    }

    public NotaFiscal(Long id, String chaveAcesso, String cuf, Integer modelo, Integer serie, Long numero, OffsetDateTime dataHoraEmissao, Integer tpEmis, Integer tpNF, String natOp, String cnpjEmit, String xNomeEmit, String ieEmit, String crt, String cnpjDest, String xNomeDest, String ieDest, String xLgrDest, String nroDest, String xCplDest, String xBairroDest, String cMunDest, String xMunDest, String ufDest, String cepDest, String cPaisDest, String xPaisDest, String foneDest, String modFrete, String cnpjTransp, String xNomeTransp, String ieTransp, String xEndTransp, String xMunTransp, String ufTransp, String numeroFatura, BigDecimal valorOriginalFatura, BigDecimal valorDescontoFatura, BigDecimal valorLiquidoFatura, BigDecimal vNF, BigDecimal vICMS, BigDecimal vST, String infRespTec, String infIntermed, String infAdic, String pag, List<NotaFiscalItem> itens, List<NotaFiscalDuplicatas> duplicatas) {
        this.id = id;
        this.chaveAcesso = chaveAcesso;
        this.cuf = cuf;
        this.modelo = modelo;
        this.serie = serie;
        this.numero = numero;
        this.dataHoraEmissao = dataHoraEmissao;
        this.tpEmis = tpEmis;
        this.tpNF = tpNF;
        this.natOp = natOp;
        this.cnpjEmit = cnpjEmit;
        this.xNomeEmit = xNomeEmit;
        this.ieEmit = ieEmit;
        this.crt = crt;
        this.cnpjDest = cnpjDest;
        this.xNomeDest = xNomeDest;
        this.ieDest = ieDest;
        this.xLgrDest = xLgrDest;
        this.nroDest = nroDest;
        this.xCplDest = xCplDest;
        this.xBairroDest = xBairroDest;
        this.cMunDest = cMunDest;
        this.xMunDest = xMunDest;
        this.ufDest = ufDest;
        this.cepDest = cepDest;
        this.cPaisDest = cPaisDest;
        this.xPaisDest = xPaisDest;
        this.foneDest = foneDest;
        this.modFrete = modFrete;
        this.cnpjTransp = cnpjTransp;
        this.xNomeTransp = xNomeTransp;
        this.ieTransp = ieTransp;
        this.xEndTransp = xEndTransp;
        this.xMunTransp = xMunTransp;
        this.ufTransp = ufTransp;
        this.numeroFatura = numeroFatura;
        this.valorOriginalFatura = valorOriginalFatura;
        this.valorDescontoFatura = valorDescontoFatura;
        this.valorLiquidoFatura = valorLiquidoFatura;
        this.vNF = vNF;
        this.vICMS = vICMS;
        this.vST = vST;
        this.infRespTec = infRespTec;
        this.infIntermed = infIntermed;
        this.infAdic = infAdic;
        this.pag = pag;
        this.itens = itens;
        this.duplicatas = duplicatas;
    }

    public NotaFiscal(NotaFiscalDto obj) {
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
                ? obj.getItens().stream().map(NotaFiscalItem::new).toList()
                : null;

        // Convertendo duplicatas da nota fiscal para DTO
        this.duplicatas = obj.getDuplicatas() != null
                ? obj.getDuplicatas().stream().map(NotaFiscalDuplicatas::new).toList()
                : null;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NotaFiscal that = (NotaFiscal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}