package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.CadastroCurriculos;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CadastroCurriculosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected String nome;
    protected String sexo;
    protected String contato;
    protected String escolaridade;
    protected String areaFormacao;
    protected String cidade;
    protected String situacao;
    protected LocalDate dataCadastro;
    @Lob
    protected byte[] arquivo;

    public CadastroCurriculosDto() {
        super();
    }

    public CadastroCurriculosDto(CadastroCurriculos obj) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
