package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.FolhaQuinzenalDto;
import br.com.codex.v1.domain.enums.Situacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class FolhaQuinzenal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String colaborador;
    private String departamento;
    private String centroCusto;
    private LocalDate admissao;
    private String codigoCbo;
    private String descricaoCbo;
    private String matriculaColaborador;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioBase;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioHora;

    private String jornada;
    private String nomeBanco;
    private String agencia;
    private String numeroConta;
    private String tipoSalario;
    private BigDecimal transporteDia;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalVencimentos;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalDescontos;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorLiquido;

    @Column(precision = 10, scale = 2)
    private BigDecimal baseCalculoIrrf;

    @Column(precision = 10, scale = 2)
    private BigDecimal fgtsDoMes;

    @Column(precision = 10, scale = 2)
    private BigDecimal baseCalculoFgts;

    private String empresa;
    private String cnpj;
    private Situacao situacao;

    @JsonIgnore
    @OneToMany(mappedBy = "folhaQuinzenal")
    private List<FolhaQuinzenalEventos> eventos = new ArrayList<>();

    public FolhaQuinzenal() {
        super();
    }

    public FolhaQuinzenal(Long id, String colaborador, String departamento, String centroCusto,
                          LocalDate admissao, String codigoCbo, String descricaoCbo,
                          String matriculaColaborador, BigDecimal salarioBase, BigDecimal salarioHora,
                          String jornada, String nomeBanco, String agencia, String numeroConta,
                          String tipoSalario, BigDecimal transporteDia, BigDecimal totalVencimentos,
                          BigDecimal totalDescontos, BigDecimal valorLiquido, BigDecimal baseCalculoIrrf,
                          BigDecimal fgtsDoMes, BigDecimal baseCalculoFgts, String empresa, String cnpj, Situacao situacao) {
        this.id = id;
        this.colaborador = colaborador;
        this.departamento = departamento;
        this.centroCusto = centroCusto;
        this.admissao = admissao;
        this.codigoCbo = codigoCbo;
        this.descricaoCbo = descricaoCbo;
        this.matriculaColaborador = matriculaColaborador;
        this.salarioBase = salarioBase;
        this.salarioHora = salarioHora;
        this.jornada = jornada;
        this.nomeBanco = nomeBanco;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.tipoSalario = tipoSalario;
        this.transporteDia = transporteDia;
        this.totalVencimentos = totalVencimentos;
        this.totalDescontos = totalDescontos;
        this.valorLiquido = valorLiquido;
        this.baseCalculoIrrf = baseCalculoIrrf;
        this.fgtsDoMes = fgtsDoMes;
        this.baseCalculoFgts = baseCalculoFgts;
        this.empresa = empresa;
        this.cnpj = cnpj;
        this.situacao = situacao;
    }

    public FolhaQuinzenal(FolhaQuinzenalDto obj) {
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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FolhaQuinzenal that = (FolhaQuinzenal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
