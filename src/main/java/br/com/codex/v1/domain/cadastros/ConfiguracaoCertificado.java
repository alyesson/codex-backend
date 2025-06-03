package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.ConfiguracaoCertificadoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Objects;
@Getter
@Setter
@Entity
public class ConfiguracaoCertificado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Lob
    @Column(nullable = false)
    private byte[] arquivo; // .PFX ou .P12 criptografado

    @Column(nullable = false)
    private String tipo; // "A1", "A3"

    @Column(nullable = false)
    private Date dataValidade;

    private String razaoSocial;
    private String cnpj;

    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(nullable = false)
    private boolean ativo = true;

    public ConfiguracaoCertificado() {
        super();
    }

    public ConfiguracaoCertificado(Integer id, String nome, byte[] arquivo, String tipo, Date dataValidade, String razaoSocial, String cnpj, LocalDateTime dataCadastro, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.arquivo = arquivo;
        this.tipo = tipo;
        this.dataValidade = dataValidade;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
    }

    public ConfiguracaoCertificado(ConfiguracaoCertificadoDto obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.arquivo = obj.getArquivo();
        this.tipo = obj.getTipo();
        this.dataValidade = obj.getDataValidade();
        this.razaoSocial = obj.getRazaoSocial();
        this.cnpj = obj.getCnpj();
        this.dataCadastro = obj.getDataCadastro();
        this.ativo = obj.isAtivo();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConfiguracaoCertificado that = (ConfiguracaoCertificado) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
