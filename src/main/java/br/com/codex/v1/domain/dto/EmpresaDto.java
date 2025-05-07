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

    private Long id;
    private String cnpj;
    private String inscricaoEstadual;
    private String cnae;
    private String nome;
    private String endereco;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
    private String jdbcUrl;       // ex: jdbc:mysql://host:3306/empresa_012345
    private String username;
    private String password;

    public EmpresaDto() {
        super();
    }

    public EmpresaDto(Empresa obj) {
        this.id = obj.getId();
        this.cnpj = obj.getCnpj();
        this.inscricaoEstadual = obj.getInscricaoEstadual();
        this.cnae = obj.getCnae();
        this.nome = obj.getNome();
        this.endereco = obj.getEndereco();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.cep = obj.getCep();
        this.uf = obj.getUf();
        this.jdbcUrl = obj.getJdbcUrl();
        this.username = obj.getUsername();
        this.password = obj.getPassword();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = capitalizarPalavras(cnae);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = capitalizarPalavras(nome);
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = capitalizarPalavras(endereco);
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = capitalizarPalavras(bairro);
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = capitalizarPalavras(cidade);
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

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmpresaDto empresa = (EmpresaDto) o;
        return Objects.equals(id, empresa.id) && Objects.equals(cnpj, empresa.cnpj);
    }
}
