package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.Usuario;
import br.com.codex.v1.domain.enums.Perfil;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UsuarioDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotNull(message = "O campo nome não pode ficar em branco")
    protected String nome;
    @CPF
    protected String cpf;
    @NotNull(message = "O data de nascimento nome não pode ficar em branco")
    protected Date nascimento;
    protected String sexo;
    @NotNull(message = "O campo telefone não pode ficar em branco")
    protected String telefone;
    @NotNull(message = "O campo endereço não pode ficar em branco")
    protected String endereco;
    protected String bairro;
    protected String cidade;
    protected String uf;
    protected String cep;
    protected String email;
    @NotNull(message = "O campo senha não pode ficar em branco")
    protected String senha;
    protected Set<Integer> perfis = new HashSet<>();
    @NotNull(message = "O campo departamento não pode ficar em branco")
    protected String departamento;
    protected String centroCusto;

    public UsuarioDto() {
        super();
    }

    public UsuarioDto(Usuario obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.nascimento = obj.getNascimento();
        this.sexo = obj.getSexo();
        this.telefone = obj.getTelefone();
        this.endereco = obj.getEndereco();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.uf = obj.getUf();
        this.cep = obj.getCep();
        this.email = obj.getEmail();
        this.senha = obj.getSenha();
        this.perfis = obj.getPerfis().stream().map(Perfil::getCodigo).collect(Collectors.toSet());
        this.departamento = obj.getDepartamento();
        this.centroCusto = obj.getCentroCusto();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Perfil> getPerfis() {
        return perfis.stream().map(Perfil ::toEnum).collect(Collectors.toSet());
    }

    public void addPerfil(Perfil perfil) {
        this.perfis.add(perfil.getCodigo());
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }
}
