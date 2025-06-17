package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.Fornecedores;
import br.com.codex.v1.utilitario.CapitalizarPalavras;
import br.com.codex.v1.utilitario.MinimizarPalavras;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class FornecedoresDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotNull(message = "A razão social não pode estar em branco")
    protected String razaoSocial;
    protected String nomeFantasia;
    @CNPJ
    @NotNull(message = "O CNPJ não pode estar em branco")
    protected String cnpj;
    protected String ie;
    protected String endereco;
    protected String bairro;
    @NotNull(message = "O campo cidade não pode estar em branco")
    protected String cidade;
    protected String cep;
    @NotNull(message = "O campo estado não pode estar em branco")
    protected String uf;
    protected String email;
    @NotNull(message = "O telefone de contato não pode estar em branco")
    protected String telefone;
    protected String vendedor;

    public FornecedoresDto() {
        super();
    }

    public FornecedoresDto(Fornecedores obj) {
        this.id = obj.getId();
        this.razaoSocial = obj.getRazaoSocial();
        this.nomeFantasia = obj.getNomeFantasia();
        this.cnpj = obj.getCnpj();
        this.ie = obj.getIe();
        this.endereco = obj.getEndereco();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.cep = obj.getCep();
        this.uf = obj.getUf();
        this.email = obj.getEmail();
        this.telefone = obj.getTelefone();
        this.vendedor = obj.getVendedor();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = CapitalizarPalavras.capitalizarPalavras(razaoSocial);
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = CapitalizarPalavras.capitalizarPalavras(nomeFantasia);
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getIe() {
        return ie;
    }

    public void setIe(String ie) {
        this.ie = ie;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = CapitalizarPalavras.capitalizarPalavras(endereco);
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = CapitalizarPalavras.capitalizarPalavras(bairro);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = MinimizarPalavras.minimizarPalavras(email);
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = CapitalizarPalavras.capitalizarPalavras(vendedor);
    }
}
