package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.ValeTransporte;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ValeTransporteDto implements Serializable {
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

    @NotNull(message = "Valor diário é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor diário deve ser maior que zero")
    @Digits(integer = 3, fraction = 2, message = "Valor diário deve ter no máximo 3 dígitos inteiros e 2 decimais")
    private BigDecimal valorDiario;

    @NotNull(message = "Dias utilizados é obrigatório")
    @Min(value = 1, message = "Dias utilizados deve ser no mínimo 1")
    @Max(value = 31, message = "Dias utilizados não pode ser maior que 31")
    private Integer diasUtilizados;

    @NotNull(message = "Valor total é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor total deve ser maior que zero")
    private BigDecimal valorTotal;

    @NotBlank(message = "Tipo do vale é obrigatório")
    @Pattern(regexp = "^(IDV|Passe)$", message = "Tipo do vale deve ser 'IDV' ou 'Passe'")
    private String tipoVale;

    private String numeroCartao;

    private String empresaTransporte;

    @Size(max = 500, message = "Observação não pode ter mais de 500 caracteres")
    private String observacao;

    @NotNull(message = "Status ativo é obrigatório")
    private Boolean ativo;

    public ValeTransporteDto() {
        super();
    }

    public ValeTransporteDto(ValeTransporte obj) {
        this.id = obj.getId();
        this.colaboradorId = obj.getColaborador().getId();
        this.colaboradorNome = obj.getColaborador().getNomeColaborador();
        this.colaboradorCpf = obj.getColaborador().getCpf();
        this.colaboradorDepartamento = obj.getColaborador().getNomeDepartamento();
        this.dataInicio = obj.getDataInicio();
        this.dataFim = obj.getDataFim();
        this.valorDiario = obj.getValorDiario();
        this.diasUtilizados = obj.getDiasUtilizados();
        this.valorTotal = obj.getValorTotal();
        this.tipoVale = obj.getTipoVale();
        this.numeroCartao = obj.getNumeroCartao();
        this.empresaTransporte = obj.getEmpresaTransporte();
        this.observacao = obj.getObservacao();
        this.ativo = obj.getAtivo();
    }
}