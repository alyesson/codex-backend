package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.domain.rh.FolhaRescisaoEventos;
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
    private String tipoDeDemissao;           // Sem justa causa, com justa causa, pedido demissão
    private String tipoContrato;           // Sem justa causa, com justa causa, pedido demissão
    private String tipoDeAvisoPrevio;        // Trabalhado, indenizado, dispensado
    private String codigoAfastamento;       // Código e-Social
    private LocalDate dataFimAvisoPrevio;
    private String categoriaTrabalhador;
    private String codigoSindical; //cnpj do sindicato
    private String entidadeSindical;
    private BigDecimal salarioHora;
    private Integer faltasNoMes;
    private Integer diasTrabalhadosNoMes;
    private List<FolhaRescisaoEventos> eventos;

    public FolhaRescisaoDto() {
        super();
    }

    public FolhaRescisaoDto(FolhaRescisao obj) {
        this.id = obj.getId();
        this.mesFolha = obj.getMesFolha();
        this.dataProcessamento = dataProcessamento;
        this.dataDoPagamento = dataDoPagamento;
        this.empresaNome = empresaNome;
        this.numeroCnpj = numeroCnpj;
        this.ederecoEmpresa = ederecoEmpresa;
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
        this.numDependenteIrrf = numDependenteIrrf;
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
        this.salarioHora = obj.getSalarioHora();
        this.faltasNoMes = obj.getFaltasNoMes();
        this.diasTrabalhadosNoMes = obj.getDiasTrabalhadosNoMes();
    }
}
