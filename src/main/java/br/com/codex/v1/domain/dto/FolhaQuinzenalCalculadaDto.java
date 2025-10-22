package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.rh.FolhaQuinzenalCalculada;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class FolhaQuinzenalCalculadaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String colaborador;
    private String departamento;
    private String centroCusto;
    private LocalDate admissao;
    private String codigoCbo;
    private String descricaoCbo;

    @NotBlank(message = "O número da matrícula não pode ser nulo")
    private String matriculaColaborador;
    private BigDecimal salarioBase;
    private BigDecimal salarioHora;
    private String jornada;
    private String nomeBanco;
    private String agencia;
    private String numeroConta;
    private String tipoSalario;
    private BigDecimal transporteDia;
    private BigDecimal totalVencimentos;
    private BigDecimal totalDescontos;
    private BigDecimal valorLiquido;
    private BigDecimal baseCalculoIrrf;
    private BigDecimal fgtsDoMes;
    private BigDecimal baseCalculoFgts;
    private String empresa;
    private String cnpj;
    private Situacao situacao;
    private List<FolhaQuinzenalEventosCalculadaDto> eventos;

    public FolhaQuinzenalCalculadaDto() {
        super();
    }

    public FolhaQuinzenalCalculadaDto(FolhaQuinzenalCalculada obj) {
        this.id = obj.getId();
        this.colaborador = obj.getColaborador();
        this.departamento = obj.getDepartamento();
        this.centroCusto = obj.getCentroCusto();
        this.admissao = obj.getAdmissao();
        this.codigoCbo = obj.getCodigoCbo();
        this.descricaoCbo = obj.getDescricaoCbo();
        this.matriculaColaborador = obj.getMatriculaColaborador();
        this.salarioBase = obj.getSalarioBase();
        this.salarioHora = obj.getSalarioHora();
        this.jornada = obj.getJornada();
        this.nomeBanco = obj.getNomeBanco();
        this.agencia = obj.getAgencia();
        this.numeroConta = obj.getNumeroConta();
        this.tipoSalario = obj.getTipoSalario();
        this.transporteDia = obj.getTransporteDia();
        this.totalVencimentos = obj.getTotalVencimentos();
        this.totalDescontos = obj.getTotalDescontos();
        this.valorLiquido = obj.getValorLiquido();
        this.baseCalculoIrrf = obj.getBaseCalculoIrrf();
        this.fgtsDoMes = obj.getFgtsDoMes();
        this.baseCalculoFgts = obj.getBaseCalculoFgts();
        this.empresa = obj.getEmpresa();
        this.cnpj = obj.getCnpj();
        this.situacao = obj.getSituacao();
    }
}
