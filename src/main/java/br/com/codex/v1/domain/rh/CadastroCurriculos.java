package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.CadastroCurriculosDto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class CadastroCurriculos implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String nome;
    protected String sexo;
    protected String contato;
    protected String escolaridade;
    protected String areaFormacao;
    protected String cidade;
    protected String situacao;
    protected LocalDate dataCadastro = LocalDate.now();;
    @Lob
    protected byte[] arquivo;

    public CadastroCurriculos() {
        super();
    }

    public CadastroCurriculos(Long id, String nome, String sexo, String contato, String escolaridade, String areaFormacao, String cidade, String situacao, LocalDate dataCadastro, byte[] arquivo) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.contato = contato;
        this.escolaridade = escolaridade;
        this.areaFormacao = areaFormacao;
        this.cidade = cidade;
        this.situacao = situacao;
        this.dataCadastro = dataCadastro;
        this.arquivo = arquivo;
    }

    public CadastroCurriculos(CadastroCurriculosDto obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.sexo = obj.getSexo();
        this.contato = obj.getContato();
        this.escolaridade = obj.getEscolaridade();
        this.areaFormacao = obj.getAreaFormacao();
        this.cidade = obj.getCidade();
        this.situacao = obj.getSituacao();
        this.dataCadastro = obj.getDataCadastro();
        this.arquivo = obj.getArquivo();
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getAreaFormacao() {
        return areaFormacao;
    }

    public void setAreaFormacao(String areaFormacao) {
        this.areaFormacao = areaFormacao;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CadastroCurriculos that = (CadastroCurriculos) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @PrePersist
    public void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = LocalDate.now();  // Define a data de cadastro antes de salvar no banco
        }
    }
}
