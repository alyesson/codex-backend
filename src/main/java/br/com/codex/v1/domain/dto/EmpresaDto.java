package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.Empresa;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;
import static br.com.codex.v1.utilitario.RemoveCaracteresEspeciais.removerCaracteresEspeciais;

@Getter
@Setter
public class EmpresaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "Cnpj não pode estar em branco")
    private String cnpj;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String inscricaoEstadualSt;
    private String suframa;
    private String cnae;
    @NotBlank(message = "Nome fantasia não pode estar em branco")
    @Size(min = 3, max = 50, message = "Nome fantasia deve ter entre 3 e 50 caracteres")
    private String nomeFantasia;
    @NotBlank(message = "Razão social não pode estar em branco")
    private String razaoSocial;
    @NotBlank(message = "Endereço não pode estar em branco")
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    @NotBlank(message = "Cidade não pode estar em branco")
    private String cidade;
    private String cep;
    @NotBlank(message = "Uf não pode estar em branco")
    private String uf;
    @NotBlank(message = "Regime tributário não pode estar em branco")
    private String regimeTributario;
    private String telefone;
    @NotBlank(message = "Celular não pode estar em branco")
    private String celular;
    @NotBlank(message = "E-mail não pode estar em branco")
    private String emailContato;
    @NotBlank(message = "Situação não pode estar em branco")
    private String situacao;
    @NotBlank(message = "Classificação financeira não pode estar em branco")
    private String classificacaoFinanceira;
    private String jdbcUrl;// ex: jdbc:mysql://host:3306/empresa_012345
    @NotBlank(message = "Tipo de empresa não pode estar em branco")
    private String tipoEmpresa;
    private boolean possuiBase; //por padrão é inicializada como false;
    private String codigoCidade;

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
        this.numero = obj.getNumero();
        this.complemento = obj.getComplemento();
        this.bairro = obj.getBairro();
        this.cidade = obj.getCidade();
        this.cep = obj.getCep();
        this.uf = obj.getUf();
        this.regimeTributario = obj.getRegimeTributario();
        this.telefone = obj.getTelefone();
        this.celular = obj.getCelular();
        this.emailContato = obj.getEmailContato();
        this.situacao = obj.getSituacao();
        this.classificacaoFinanceira = obj.getClassificacaoFinanceira();
        this.jdbcUrl = obj.getJdbcUrl();
        this.tipoEmpresa = obj.getTipoEmpresa();
        this.possuiBase = obj.isPossuiBase();
        this.codigoCidade = obj.getCodigoCidade();
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = capitalizarPalavras(nomeFantasia);
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = capitalizarPalavras(razaoSocial);
    }

    public void setEndereco(String endereco) {
        this.endereco = capitalizarPalavras(endereco);
    }

    public void setComplemento(String complemento) {
        this.complemento = capitalizarPalavras(complemento);
    }

    public void setBairro(String bairro) {
        this.bairro = capitalizarPalavras(bairro);
    }

    public void setCidade(String cidade) {
        this.cidade = capitalizarPalavras(cidade);
    }

    public  void setCnpj(String cnpj) { this.cnpj = removerCaracteresEspeciais(cnpj); }

    public void setInscricaoEstadual(String inscricaoEstadual) { this.inscricaoEstadual = removerCaracteresEspeciais(inscricaoEstadual); }

    public void setInscricaoMunicipal(String inscricaoMunicipal) { this.inscricaoMunicipal = removerCaracteresEspeciais(inscricaoMunicipal); }

    public void setInscricaoEstadualSt(String inscricaoEstadualSt) { this.inscricaoEstadualSt = removerCaracteresEspeciais(inscricaoEstadualSt); }
}
