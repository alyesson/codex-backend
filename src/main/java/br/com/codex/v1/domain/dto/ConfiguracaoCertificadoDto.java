package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class ConfiguracaoCertificadoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "O nome do certificado não pode estar em branco")
    private String nome;

    @NotBlank(message = "O certificado não pode estar em branco")
    private byte[] arquivo; // .PFX ou .P12 criptografado

    @NotBlank(message = "O tipo do certificado não pode estar em branco")
    private String tipo; // "A1", "A3"

    @NotBlank(message = "A data de validade do certificado não pode estar em branco")
    private Date dataValidade;

    @NotBlank(message = "A razão social do certificado não pode estar em branco")
    private String razaoSocial;
    @NotBlank(message = "O cnpj não pode estar em branco")
    private String cnpj;

    private LocalDateTime dataCadastro = LocalDateTime.now();

    @NotBlank(message = "A situação do certificado não pode estar em branco")
    private boolean ativo = true;

    @NotBlank(message = "A senha do certificado não pode estar em branco")
    private String senha;

    private String uf;

    public ConfiguracaoCertificadoDto() {
        super();
    }

    public ConfiguracaoCertificadoDto(ConfiguracaoCertificado obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.arquivo = obj.getArquivo();
        this.tipo = obj.getTipo();
        this.dataValidade = obj.getDataValidade();
        this.razaoSocial = obj.getRazaoSocial();
        this.cnpj = obj.getCnpj();
        this.dataCadastro = obj.getDataCadastro();
        this.ativo = obj.isAtivo();
        this.senha = obj.getSenha();
        this.uf = obj.getUf();
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = capitalizarPalavras(razaoSocial);
    }
}
