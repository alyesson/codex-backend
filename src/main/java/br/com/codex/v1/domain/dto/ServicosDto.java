package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.enums.TipoCobranca;
import br.com.codex.v1.domain.vendas.Servicos;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ServicosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "O código do serviço não pode ficar em branco")
    private String codigo;

    @NotBlank(message = "O campo nome não pode ficar em branco")
    private String nome;

    @NotBlank(message = "O campo descrição do serviço não pode ficar em branco")
    private String descricao;
    private LocalDate dataEmissao;

    @NotNull(message = "O tipo de cobrança não pode ficar em branco")
    private TipoCobranca tipoCobranca;

    @NotNull(message = "O tempo médio de execução do serviço não pode ficar em branco")
    private String tempoMedioExecucao;

    @NotNull(message = "A forma de pagamento não pode ficar em branco")
    private String formaPagamento;
    private BigDecimal baseCalculo = BigDecimal.ZERO;
    private BigDecimal aliquotaPercentual = BigDecimal.ZERO;
    @NotNull(message = "O campo ISS não pode ficar em branco")
    private BigDecimal issPercentual = BigDecimal.ZERO;
    private BigDecimal issValor = BigDecimal.ZERO;
    private BigDecimal pisPercentual = BigDecimal.ZERO;
    private BigDecimal pisValor = BigDecimal.ZERO;
    private BigDecimal cofinsPercentual = BigDecimal.ZERO;
    private BigDecimal cofinsValor = BigDecimal.ZERO;
    private BigDecimal csllPercentual = BigDecimal.ZERO;
    private BigDecimal cssValor = BigDecimal.ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;
    private BigDecimal valorPrazo = BigDecimal.ZERO;
    private BigDecimal valorVista = BigDecimal.ZERO;

    @NotBlank(message = "A garantia não pode ficar em branco")
    private String garantia;
    private String observacoes;
    private String nbsCodigo;
    private String nbsNome;
    private String cnaeCodigo;
    private String cnaeNome;

    public ServicosDto() {
        super();
    }

    public ServicosDto(Servicos obj) {
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
}
