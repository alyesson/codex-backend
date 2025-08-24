package br.com.codex.v1.domain.vendas;

import br.com.codex.v1.domain.dto.ServicosDto;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.enums.TipoCobranca;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.groovy.runtime.typehandling.BigDecimalMath;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Servicos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String codigo;
    @Column(nullable = false, length = 60)
    private String nome;
    private String descricao;

    @Column(nullable = false, length = 11)
    private LocalDate dataEmissao;

    @Column(nullable = false)
    private TipoCobranca tipoCobranca;

    @Column(length = 6)
    private String tempoMedioExecucao;

    @Column(nullable = false, length = 30)
    private String formaPagamento;

    @Column(nullable = false, length = 8)
    private BigDecimal baseCalculo = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal aliquotaPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal issPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal issValor = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal pisPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal pisValor = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal cofinsPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal cofinsValor = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal csllPercentual = BigDecimal.ZERO;

    @Column(length = 6)
    private BigDecimal cssValor = BigDecimal.ZERO;

    @Column(nullable = false, length = 8)
    private BigDecimal valorVista = BigDecimal.ZERO;

    @Column(nullable = false, length = 8)
    private BigDecimal valorPrazo = BigDecimal.ZERO;

    @Column(nullable = false, length = 8)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(length = 3)
    private String garantia;

    @Column(length = 500)
    private String observacoes;

    @Column(length = 15)
    private String nbsCodigo;

    @Column(length = 50)
    private String nbsNome;

    @Column(length = 15)
    private String cnaeCodigo;

    @Column(length = 50)
    private String cnaeNome;


    public Servicos() {
        super();
    }

    public Servicos(Long id, String codigo, String nome, String descricao, LocalDate dataEmissao, TipoCobranca tipoCobranca, String tempoMedioExecucao,
                    String formaPagamento, BigDecimal baseCalculo, BigDecimal aliquotaPercentual,
                    BigDecimal issPercentual, BigDecimal issValor, BigDecimal pisPercentual, BigDecimal pisValor,
                    BigDecimal cofinsPercentual, BigDecimal cofinsValor, BigDecimal csllPercentual, BigDecimal cssValor,
                    BigDecimal valorTotal, BigDecimal valorVista, BigDecimal valorPrazo, String garantia, String observacoes,
                    String nbsCodigo, String nbsNome, String cnaeCodigo, String cnaeNome) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.dataEmissao = dataEmissao;
        this.tipoCobranca = tipoCobranca;
        this.tempoMedioExecucao = tempoMedioExecucao;
        this.formaPagamento = formaPagamento;
        this.baseCalculo = baseCalculo;
        this.aliquotaPercentual = aliquotaPercentual;
        this.issPercentual = issPercentual;
        this.issValor = issValor;
        this.pisPercentual = pisPercentual;
        this.pisValor = pisValor;
        this.cofinsPercentual = cofinsPercentual;
        this.cofinsValor = cofinsValor;
        this.csllPercentual = csllPercentual;
        this.cssValor = cssValor;
        this.valorTotal = valorTotal;
        this.garantia = garantia;
        this.observacoes = observacoes;
        this.valorVista = valorVista;
        this.valorPrazo = valorPrazo;
        this.nbsCodigo = nbsCodigo;
        this.nbsNome = nbsNome;
        this.cnaeCodigo = cnaeCodigo;
        this.cnaeNome = cnaeNome;

    }

    public Servicos(ServicosDto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.nome = obj.getNome();
        this.descricao = obj.getDescricao();
        this.dataEmissao = obj.getDataEmissao();
        this.tipoCobranca = obj.getTipoCobranca();
        this.tempoMedioExecucao = obj.getTempoMedioExecucao();
        this.formaPagamento = obj.getFormaPagamento();
        this.baseCalculo = obj.getBaseCalculo();
        this.aliquotaPercentual = obj.getAliquotaPercentual();
        this.issPercentual = obj.getIssPercentual();
        this.issValor = obj.getIssValor();
        this.pisPercentual = obj.getPisPercentual();
        this.pisValor = obj.getPisValor();
        this.cofinsPercentual = obj.getCofinsPercentual();
        this.cofinsValor = obj.getCofinsValor();
        this.csllPercentual = obj.getCsllPercentual();
        this.cssValor = obj.getCssValor();
        this.valorTotal = obj.getValorTotal();
        this.garantia = obj.getGarantia();
        this.observacoes = obj.getObservacoes();
        this.valorVista = obj.getValorVista();
        this.valorPrazo = obj.getValorPrazo();
        this.nbsCodigo = obj.getNbsCodigo();
        this.nbsNome = obj.getNbsNome();
        this.cnaeCodigo = obj.getCnaeCodigo();
        this.cnaeNome = obj.getCnaeNome();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Servicos servicos = (Servicos) o;
        return Objects.equals(id, servicos.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
