package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.Visitantes;
import br.com.codex.v1.utilitario.ValidCep;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;


public class VisitantesDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotNull(message = "O campo nome não pode ficar em branco")
    protected String nome;
    @NotNull(message = "O campo CPF não pode ficar em branco")
    @CPF
    protected String cpf;
    @NotNull(message = "O campo empresa não pode ficar em branco")
    protected String empresa;
    @NotNull(message = "O campo data nascimento não pode ficar em branco")
    protected Date nascimento;
    protected String sexo;
    @NotNull(message = "O campo celular não pode ficar em branco")
    protected String telefone;
    protected String endereco;
    protected String bairro;
    protected String cidade;
    protected String uf;
    @ValidCep
    protected String cep;
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected LocalDate dataCadastro = LocalDate.now();

    public VisitantesDto() {
        super();
    }

    public VisitantesDto(Visitantes obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.empresa = obj.getEmpresa();
        this.nascimento = obj.getNascimento();
        this.sexo = obj.getSexo();
        this.endereco = obj.getEndereco();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.uf = obj.getUf();
        this.cep = obj.getCep();
        this.dataCadastro = obj.getDataCadastro();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
