package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.FolhaRescisaoDto;
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
public class FolhaRescisao implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mesFolha;
    private LocalDate dataProcessamento;
    private LocalDate dataDoPagamento;
    private String empresaNome;
    private String numeroCnpj;
    private String enderecoEmpresa;
    private String bairroEmpresa;
    private String municipioEmpresa;
    private String ufEmpresa;
    private String cepEmpresa;
    private String cnaeEmpresa;
    private String numeroMatricula;
    private String numeroPis;
    private String nomDoFuncionario;
    private String ederecoFuncionario;
    private String bairroFuncionario;
    private String municipioFuncionario;
    private String ufFuncionario;
    private String cepFuncionario;
    private String ctps;
    private String cpf;
    private String dataNascimento;
    private String nomeMae;
    private int numeroDependenteIrrf;
    private String tipoDeSalario;
    private int horasSemanais;
    private LocalDate dataDeAdmissao;
    private LocalDate dataDeDemissao;
    private LocalDate dataAVisoPrevio;
    private String tipoDeDemissao;           // Sem justa causa, com justa causa, pedido demissão
    private String tipoContrato;           // Sem justa causa, com justa causa, pedido demissão
    private String tipoDeAvisoPrevio;        // Trabalhado, indenizado, dispensado
    private String codigoAfastamento;       // Código e-Social
    private LocalDate dataFimAvisoPrevio;
    private String categoriaTrabalhador;
    private String codigoSindical; //cnpj do sindicato
    private String entidadeSindical;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioBase;

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioHora;
    private Integer faltasNoMes;
    private Integer diasTrabalhadosNoMes;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalVencimentos;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalDescontos;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorLiquido;



    @JsonIgnore
    @OneToMany(mappedBy = "folhaRescisao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FolhaRescisaoEventos> eventos = new ArrayList<>();

    public FolhaRescisao() {
        super();
    }

    public FolhaRescisao(Long id, String mesFolha, LocalDate dataProcessamento, LocalDate dataDoPagamento,
                         String empresaNome, String numeroCnpj, String enderecoEmpresa, String bairroEmpresa,
                         String municipioEmpresa, String ufEmpresa, String cepEmpresa, String cnaeEmpresa,
                         String numeroMatricula, String numeroPis, String nomDoFuncionario, String ederecoFuncionario,
                         String bairroFuncionario, String municipioFuncionario, String ufFuncionario,
                         String cepFuncionario, String ctps, String cpf, String dataNascimento, String nomeMae,
                         int numeroDependenteIrrf, String tipoDeSalario, int horasSemanais, LocalDate dataDeAdmissao,
                         LocalDate dataDeDemissao, LocalDate dataAVisoPrevio, String tipoDeDemissao, String tipoContrato,
                         String tipoDeAvisoPrevio, String codigoAfastamento, LocalDate dataFimAvisoPrevio,
                         String categoriaTrabalhador, String codigoSindical, String entidadeSindical, BigDecimal salarioHora,
                         Integer faltasNoMes, Integer diasTrabalhadosNoMes, BigDecimal totalVencimentos,
                         BigDecimal totalDescontos, BigDecimal valorLiquido) {
        this.id = id;
        this.mesFolha = mesFolha;
        this.dataProcessamento = dataProcessamento;
        this.dataDoPagamento = dataDoPagamento;
        this.empresaNome = empresaNome;
        this.numeroCnpj = numeroCnpj;
        this.enderecoEmpresa = enderecoEmpresa;
        this.bairroEmpresa = bairroEmpresa;
        this.municipioEmpresa = municipioEmpresa;
        this.ufEmpresa = ufEmpresa;
        this.cepEmpresa = cepEmpresa;
        this.cnaeEmpresa = cnaeEmpresa;
        this.numeroMatricula = numeroMatricula;
        this.numeroPis = numeroPis;
        this.nomDoFuncionario = nomDoFuncionario;
        this.ederecoFuncionario = ederecoFuncionario;
        this.bairroFuncionario = bairroFuncionario;
        this.municipioFuncionario = municipioFuncionario;
        this.ufFuncionario = ufFuncionario;
        this.cepFuncionario = cepFuncionario;
        this.ctps = ctps;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.nomeMae = nomeMae;
        this.numeroDependenteIrrf = numeroDependenteIrrf;
        this.tipoDeSalario = tipoDeSalario;
        this.horasSemanais = horasSemanais;
        this.dataDeAdmissao = dataDeAdmissao;
        this.dataDeDemissao = dataDeDemissao;
        this.dataAVisoPrevio = dataAVisoPrevio;
        this.tipoDeDemissao = tipoDeDemissao;
        this.tipoContrato = tipoContrato;
        this.tipoDeAvisoPrevio = tipoDeAvisoPrevio;
        this.codigoAfastamento = codigoAfastamento;
        this.dataFimAvisoPrevio = dataFimAvisoPrevio;
        this.categoriaTrabalhador = categoriaTrabalhador;
        this.codigoSindical = codigoSindical;
        this.entidadeSindical = entidadeSindical;
        this.salarioHora = salarioHora;
        this.faltasNoMes = faltasNoMes;
        this.diasTrabalhadosNoMes = diasTrabalhadosNoMes;
        this.totalVencimentos = totalVencimentos;
        this.totalDescontos = totalDescontos;
        this.valorLiquido = valorLiquido;
    }

    public FolhaRescisao(FolhaRescisaoDto obj) {
        this.id = obj.getId();
        this.mesFolha = obj.getMesFolha();
        this.dataProcessamento = obj.getDataProcessamento();
        this.dataDoPagamento = obj.getDataDoPagamento();
        this.empresaNome = obj.getEmpresaNome();
        this.numeroCnpj = obj.getNumeroCnpj();
        this.enderecoEmpresa = obj.getEnderecoEmpresa();
        this.bairroEmpresa = obj.getBairroEmpresa();
        this.municipioEmpresa = obj.getMunicipioEmpresa();
        this.ufEmpresa = obj.getUfEmpresa();
        this.cepEmpresa = obj.getCepEmpresa();
        this.cnaeEmpresa = obj.getCnaeEmpresa();
        this.numeroMatricula = obj.getNumeroMatricula();
        this.numeroPis = obj.getNumeroPis();
        this.nomDoFuncionario = obj.getNomDoFuncionario();
        this.ederecoFuncionario = obj.getEderecoFuncionario();
        this.bairroFuncionario = obj.getBairroFuncionario();
        this.municipioFuncionario = obj.getMunicipioFuncionario();
        this.ufFuncionario = obj.getUfFuncionario();
        this.cepFuncionario = obj.getCepFuncionario();
        this.ctps = obj.getCtps();
        this.cpf = obj.getCpf();
        this.dataNascimento = obj.getDataNascimento();
        this.nomeMae = obj.getNomeMae();
        this.numeroDependenteIrrf = obj.getNumeroDependenteIrrf();
        this.tipoDeSalario = obj.getTipoDeSalario();
        this.horasSemanais = obj.getHorasSemanais();
        this.dataDeAdmissao = obj.getDataDeAdmissao();
        this.dataDeDemissao = obj.getDataDeDemissao();
        this.dataAVisoPrevio = obj.getDataAVisoPrevio();
        this.tipoDeDemissao = obj.getTipoDeDemissao();
        this.tipoContrato = obj.getTipoContrato();
        this.tipoDeAvisoPrevio = obj.getTipoDeAvisoPrevio();
        this.codigoAfastamento = obj.getCodigoAfastamento();
        this.dataFimAvisoPrevio = obj.getDataFimAvisoPrevio();
        this.categoriaTrabalhador = obj.getCategoriaTrabalhador();
        this.codigoSindical = obj.getCodigoSindical();
        this.entidadeSindical = obj.getEntidadeSindical();
        this.salarioHora = obj.getSalarioHora();
        this.faltasNoMes = obj.getFaltasNoMes();
        this.diasTrabalhadosNoMes = obj.getDiasTrabalhadosNoMes();
        this.totalVencimentos = obj.getTotalVencimentos();
        this.totalDescontos = obj.getTotalDescontos();
        this.valorLiquido = obj.getValorLiquido();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FolhaRescisao that = (FolhaRescisao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
