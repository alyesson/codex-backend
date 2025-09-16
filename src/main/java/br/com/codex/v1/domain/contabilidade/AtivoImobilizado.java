package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.AtivoImobilizadoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Entity
@Getter
@Setter
public class AtivoImobilizado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10)
    private String codigoBem;
    @Column(length = 15)
    private String numeroSerie;
    @Column(length = 9)
    private int notaFiscal;
    @Column(length = 75)
    private String descricaoDoBem;
    @Column(length = 50)
    private String grupoDeBens;
    @Column(length = 50)
    private String filialPertencente;
    @Column(length = 30)
    private String centroCusto;
    @Column(length = 30)
    private String departamento;
    @Column(length = 13)
    private LocalDate dataAquisicao;
    @Column(length = 15)
    private LocalDate fimGarantia;
    @Column(length = 15)
    private String conservacao;
    @Column(length = 12)
    private BigDecimal valorDoBem;
    @Column(length = 12)
    private BigDecimal valorDepreciado;
    @Column(length = 12)
    private BigDecimal valorResidual;
    @Column(length = 12)
    private BigDecimal valorDepreciacaoAcumulada;
    @Column(length = 12)
    private BigDecimal valorDepreciacaoIr;
    @Column(length = 6)
    private BigDecimal porcentagemResidual;
    @Column(length = 6)
    private BigDecimal porcentagemDepreciaAno;
    @Column(length = 12)
    private BigDecimal creditoIcms;
    @Column(length = 12)
    private BigDecimal creditoIcmsUpf;
    @Lob
    private String observacao;
    @Column(length = 15)
    private LocalDate dataBaixa;
    @Column(length = 12)
    private String numeroPatrimonio;
    @Column(length = 12)
    private String codigoConta;
    @Column(length = 12)
    private int vidaUtilMeses;

    public AtivoImobilizado() {
        super();
    }

    public AtivoImobilizado(Long id, String codigoBem, String numeroSerie, int notaFiscal, String descricaoDoBem,
                            String grupoDeBens, String filialPertencente, String centroCusto, String departamento,
                            LocalDate dataAquisicao, LocalDate fimGarantia, String conservacao, BigDecimal valorDoBem,
                            BigDecimal valorDepreciado, BigDecimal valorResidual, BigDecimal valorDepreciacaoAcumulada,
                            BigDecimal valorDepreciacaoIr, BigDecimal porcentagemResidual, BigDecimal porcentagemDepreciaAno,
                            BigDecimal creditoIcms, BigDecimal creditoIcmsUpf, String observacao, LocalDate dataBaixa,
                            String numeroPatrimonio, String codigoConta, int vidaUtilMeses) {
        this.id = id;
        this.codigoBem = codigoBem;
        this.numeroSerie = numeroSerie;
        this.notaFiscal = notaFiscal;
        this.descricaoDoBem = descricaoDoBem;
        this.grupoDeBens = grupoDeBens;
        this.filialPertencente = filialPertencente;
        this.centroCusto = centroCusto;
        this.departamento = departamento;
        this.dataAquisicao = dataAquisicao;
        this.fimGarantia = fimGarantia;
        this.conservacao = conservacao;
        this.valorDoBem = valorDoBem;
        this.valorDepreciado = valorDepreciado;
        this.valorResidual = valorResidual;
        this.valorDepreciacaoAcumulada = valorDepreciacaoAcumulada;
        this.valorDepreciacaoIr = valorDepreciacaoIr;
        this.porcentagemResidual = porcentagemResidual;
        this.porcentagemDepreciaAno = porcentagemDepreciaAno;
        this.creditoIcms = creditoIcms;
        this.creditoIcmsUpf = creditoIcmsUpf;
        this.observacao = observacao;
        this.dataBaixa = dataBaixa;
        this.numeroPatrimonio = numeroPatrimonio;
        this.codigoConta = codigoConta;
        this.vidaUtilMeses = vidaUtilMeses;
    }

    public AtivoImobilizado(AtivoImobilizadoDto obj) {
        this.id = obj.getId();
        this.codigoBem = obj.getCodigoBem();
        this.numeroSerie = obj.getNumeroSerie();
        this.notaFiscal = obj.getNotaFiscal();
        this.descricaoDoBem = obj.getDescricaoDoBem();
        this.grupoDeBens = obj.getGrupoDeBens();
        this.filialPertencente = obj.getFilialPertencente();
        this.centroCusto = obj.getCentroCusto();
        this.departamento = obj.getDepartamento();
        this.dataAquisicao = obj.getDataAquisicao();
        this.fimGarantia = obj.getFimGarantia();
        this.conservacao = obj.getConservacao();
        this.valorDoBem = obj.getValorDoBem();
        this.valorDepreciado = obj.getValorDepreciado();
        this.valorResidual = obj.getValorResidual();
        this.valorDepreciacaoAcumulada = obj.getValorDepreciacaoAcumulada();
        this.valorDepreciacaoIr = obj.getValorDepreciacaoIr();
        this.porcentagemResidual = obj.getPorcentagemResidual();
        this.porcentagemDepreciaAno = obj.getPorcentagemDepreciaAno();
        this.creditoIcms = obj.getCreditoIcms();
        this.creditoIcmsUpf = obj.getCreditoIcmsUpf();
        this.observacao = obj.getObservacao();
        this.dataBaixa = obj.getDataBaixa();
        this.numeroPatrimonio = obj.getNumeroPatrimonio();
        this.codigoConta = obj.getCodigoConta();
        this.vidaUtilMeses = obj.getVidaUtilMeses();
    }


    public void setDescricaoDoBem(String descricaoDoBem) {
        this.descricaoDoBem = capitalizarPalavras(descricaoDoBem);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AtivoImobilizado that = (AtivoImobilizado) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
