package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.AtivoImobilizado;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class AtivoImobilizadoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigoBem;
    private String numeroSerie;
    private int notaFiscal;
    private String descricaoDoBem;
    private String grupoDeBens;
    private String filialPertencente;
    private String centroCusto;
    private String departamento;
    private LocalDate dataAquisicao;
    private LocalDate fimGarantia;
    private String conservacao;
    private BigDecimal valorDoBem;
    private BigDecimal valorDepreciado;
    private BigDecimal valorResidual;
    private BigDecimal valorDepreciacaoAcumulada;
    private BigDecimal valorDepreciacaoIr;
    private BigDecimal porcentagemResidual;
    private BigDecimal porcentagemDepreciaAno;
    private BigDecimal creditoIcms;
    private BigDecimal creditoIcmsUpf;
    private String observacao;
    private LocalDate dataBaixa;
    private String numeroPatrimonio;
    private String codigoConta;
    private int vidaUtilMeses;

    public AtivoImobilizadoDto() {
        super();
    }

    public AtivoImobilizadoDto(AtivoImobilizado obj) {
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
}
