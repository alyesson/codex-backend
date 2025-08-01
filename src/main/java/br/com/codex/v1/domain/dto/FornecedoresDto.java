package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.compras.Fornecedores;
import br.com.codex.v1.utilitario.CapitalizarPalavras;
import br.com.codex.v1.utilitario.MinimizarPalavras;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
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

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = CapitalizarPalavras.capitalizarPalavras(razaoSocial);
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = CapitalizarPalavras.capitalizarPalavras(nomeFantasia);
    }

    public void setEndereco(String endereco) {
        this.endereco = CapitalizarPalavras.capitalizarPalavras(endereco);
    }

    public void setBairro(String bairro) {
        this.bairro = CapitalizarPalavras.capitalizarPalavras(bairro);
    }

    public void setEmail(String email) {
        this.email = MinimizarPalavras.minimizarPalavras(email);
    }

    public void setVendedor(String vendedor) {
        this.vendedor = CapitalizarPalavras.capitalizarPalavras(vendedor);
    }
}
