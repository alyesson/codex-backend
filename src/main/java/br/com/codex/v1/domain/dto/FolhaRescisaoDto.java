package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.domain.rh.FolhaRescisaoEventos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FolhaRescisaoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String mesFolha;
    private LocalDate dataProcessamento;
    private LocalDate dataDoPagamento;
    private String empresaNome;
    private String numeroCnpj;
    private String ederecoEmpresa;
    private String bairroEmpresa;
    private String municipioEmpresa;
    private String ufEmpresa;
    private String cepEmpresa;
    private String cnaeEmpresa;

    @NotBlank(message = "O número da matrícula não pode ser nulo")
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
    private int numDependenteIrrf;
    private String tipoDeSalario;
    private int horasSemanais;
    private LocalDate dataDeAdmissao;
    private LocalDate dataDeDemissao;
    private LocalDate dataAVisoPrevio;

    @NotBlank(message = "O tipo de demissão não pode ser nulo")
    private String tipoDeDemissao;           // Sem justa causa, com justa causa, pedido demissão
    private String tipoContrato;           // Sem justa causa, com justa causa, pedido demissão
    private String tipoDeAvisoPrevio;        // Trabalhado, indenizado, dispensado

    @NotBlank(message = "O código de afastamento não pode ser nulo")
    private String codigoAfastamento;       // Código e-Social

    @NotBlank(message = "O fim do aviso prévio não pode ser nulo")
    private LocalDate dataFimAvisoPrevio;
    private String categoriaTrabalhador;
    private String codigoSindical; //cnpj do sindicato
    private String entidadeSindical;
    private BigDecimal salarioHora;
    private Integer faltasNoMes;
    private Integer diasTrabalhadosNoMes;
    private BigDecimal totalVencimentos;
    private BigDecimal totalDescontos;
    private BigDecimal valorLiquido;
    private List<FolhaRescisaoEventosDto> eventos;

    public FolhaRescisaoDto() {
        super();
    }

    public FolhaRescisaoDto(FolhaRescisao obj) {
        this.id = obj.getId();
        this.mesFolha = obj.getMesFolha();
        this.dataProcessamento = obj.getDataProcessamento();
        this.dataDoPagamento = obj.getDataDoPagamento();
        this.empresaNome = obj.getEmpresaNome();
        this.numeroCnpj = obj.getNumeroCnpj();
        this.ederecoEmpresa = obj.getEderecoEmpresa();
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
        this.numDependenteIrrf = obj.getNumDependenteIrrf();
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
}
