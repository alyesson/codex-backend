package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.Empresa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class EmpresaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String cnpj;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String inscricaoEstadualSt;
    private String suframa;
    private String cnae;
    private String nomeFantasia;
    private String razaoSocial;
    private String endereco;
    private String complemento;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
    private String regimeTributario;
    private String telelefone;
    private String celular;
    private String emailContato;
    private String situacao;
    private String classificacaoFinanceira;
    private String jdbcUrl;       // ex: jdbc:mysql://host:3306/empresa_012345

    public EmpresaDto() {
        super();
    }

    public EmpresaDto(Empresa obj) {
        this.id = obj.getId();
        this.cnpj = obj.getCnpj();
        this.inscricaoEstadual = obj.getInscricaoEstadual();
        this.inscricaoMunicipal = obj.getInscricaoMunicipal();
        this.inscricaoEstadualSt = obj.getInscricaoEstadualSt();
        this.suframa = obj.getSuframa();
        this.cnae = obj.getCnae();
        this.nomeFantasia = obj.getNomeFantasia();
        this.razaoSocial = obj.getRazaoSocial();
        this.endereco = obj.getEndereco();
        this.complemento = obj.getComplemento();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.cep = obj.getCep();
        this.uf = obj.getUf();
        this.regimeTributario = obj.getRegimeTributario();
        this.telelefone = obj.getTelelefone();
        this.celular = obj.getCelular();
        this.emailContato = obj.getEmailContato();
        this.situacao = obj.getSituacao();
        this.classificacaoFinanceira = obj.getClassificacaoFinanceira();
        this.jdbcUrl = obj.getJdbcUrl();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getInscricaoEstadualSt() {
        return inscricaoEstadualSt;
    }

    public void setInscricaoEstadualSt(String inscricaoEstadualSt) {
        this.inscricaoEstadualSt = inscricaoEstadualSt;
    }

    public String getSuframa() {
        return suframa;
    }

    public void setSuframa(String suframa) {
        this.suframa = suframa;
    }

    public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getRegimeTributario() {
        return regimeTributario;
    }

    public void setRegimeTributario(String regimeTributario) {
        this.regimeTributario = regimeTributario;
    }

    public String getTelelefone() {
        return telelefone;
    }

    public void setTelelefone(String telelefone) {
        this.telelefone = telelefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getClassificacaoFinanceira() {
        return classificacaoFinanceira;
    }

    public void setClassificacaoFinanceira(String classificacaoFinanceira) {
        this.classificacaoFinanceira = classificacaoFinanceira;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
