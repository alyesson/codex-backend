package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.ValeAlimentacaoRefeicao;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ValeAlimentacaoRefeicaoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "Colaborador é obrigatório")
    private Long colaboradorId;

    private String colaboradorNome;
    private String colaboradorCpf;
    private String colaboradorDepartamento;

    @NotNull(message = "Data de início é obrigatória")
    @FutureOrPresent(message = "Data de início deve ser hoje ou no futuro")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @NotBlank(message = "Tipo de benefício é obrigatório")
    private String tipoBeneficio;

    @NotBlank(message = "Forma de utilização é obrigatória")
    private String formaUtilizacao;

    @DecimalMin(value = "0.0", message = "Valor diário alimentação deve ser maior ou igual a zero")
    @Digits(integer = 4, fraction = 2, message = "Valor diário alimentação deve ter no máximo 4 dígitos inteiros e 2 decimais")
    private BigDecimal valorDiarioAlimentacao;

    @DecimalMin(value = "0.0", message = "Valor diário refeição deve ser maior ou igual a zero")
    @Digits(integer = 4, fraction = 2, message = "Valor diário refeição deve ter no máximo 4 dígitos inteiros e 2 decimais")
    private BigDecimal valorDiarioRefeicao;

    @NotNull(message = "Dias úteis é obrigatório")
    @Min(value = 1, message = "Dias úteis deve ser no mínimo 1")
    @Max(value = 31, message = "Dias úteis não pode ser maior que 31")
    private Integer diasUteis;

    private BigDecimal valorTotalAlimentacao;
    private BigDecimal valorTotalRefeicao;

    private String numeroCartao;
    private String empresaFornecedora;
    private String bandeiraCartao;

    private Boolean refeitorioInterno;
    private Boolean restauranteConveniado;

    @Size(max = 500, message = "Observações não pode ter mais de 500 caracteres")
    private String observacoes;

    @NotNull(message = "Status ativo é obrigatório")
    private Boolean ativo;

    private LocalDate dataVencimentoCartao;
    private LocalDate dataBloqueio;

    public ValeAlimentacaoRefeicaoDto() {
        super();
        this.refeitorioInterno = false;
        this.restauranteConveniado = true;
        this.diasUteis = 22;
        this.ativo = true;
    }

    public ValeAlimentacaoRefeicaoDto(ValeAlimentacaoRefeicao obj) {
        this.id = obj.getId();
        this.colaboradorId = obj.getColaborador().getId();
        this.colaboradorNome = obj.getColaborador().getNomeColaborador();
        this.colaboradorCpf = obj.getColaborador().getCpf();
        this.colaboradorDepartamento = obj.getColaborador().getNomeDepartamento();
        this.dataInicio = obj.getDataInicio();
        this.dataFim = obj.getDataFim();
        this.tipoBeneficio = obj.getTipoBeneficio();
        this.formaUtilizacao = obj.getFormaUtilizacao();
        this.valorDiarioAlimentacao = obj.getValorDiarioAlimentacao();
        this.valorDiarioRefeicao = obj.getValorDiarioRefeicao();
        this.diasUteis = obj.getDiasUteis();
        this.valorTotalAlimentacao = obj.getValorTotalAlimentacao();
        this.valorTotalRefeicao = obj.getValorTotalRefeicao();
        this.numeroCartao = obj.getNumeroCartao();
        this.empresaFornecedora = obj.getEmpresaFornecedora();
        this.bandeiraCartao = obj.getBandeiraCartao();
        this.refeitorioInterno = obj.getRefeitorioInterno();
        this.restauranteConveniado = obj.getRestauranteConveniado();
        this.observacoes = obj.getObservacoes();
        this.ativo = obj.getAtivo();
        this.dataVencimentoCartao = obj.getDataVencimentoCartao();
        this.dataBloqueio = obj.getDataBloqueio();
    }
}