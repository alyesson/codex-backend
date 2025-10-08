package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.Departamento;
import br.com.codex.v1.domain.financeiro.CentroCusto;
import br.com.codex.v1.domain.rh.CadastroColaboradores;
import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenal;
import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenalEventos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class CadastroFolhaPagamentoQuinzenalDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Funcionário não pode estar em branco")
    private CadastroColaboradores colaborador;
    private Departamento departamento;
    private LocalDate admissao;
    private String codigoCbo;
    private String descricaoCbo;
    private String matriculaColaborador;
    private BigDecimal salarioBase;
    private BigDecimal salarioHora;
    private String jornada;
    private String nomeBanco;
    private String agencia;
    private String numeroConta;
    private CentroCusto centroCusto;
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
    private List<CadastroFolhaPagamentoQuinzenalEventosDto> eventos;

    public CadastroFolhaPagamentoQuinzenalDto() {
        super();
    }

    public CadastroFolhaPagamentoQuinzenalDto(CadastroFolhaPagamentoQuinzenal obj) {
        this.id = obj.getId();
        this.colaborador = obj.getColaborador();
        this.departamento = obj.getDepartamento();
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
        this.centroCusto = obj.getCentroCusto();
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
    }
}
