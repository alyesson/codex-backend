package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.ControleCarrosColaboradores;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class ControleCarrosColaboradoresDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotNull(message = "O campo nome do colaborador não pode ficar em branco")
    protected String nomeColaborador;
    @NotNull(message = "O campo departamento não pode ficar em branco")
    protected String departamento;
    @NotNull(message = "O campo veículo não pode ficar em branco")
    protected String veiculo;
    protected String corVeiculo;
    protected String anoVeiculo;
    @NotNull(message = "O campo marca do veículo não pode ficar em branco")
    protected String marcaVeiculo;
    @NotNull(message = "O campo placa do veículo não pode ficar em branco")
    protected String placaVeiculo;
    protected String contatoColaborador;
    protected String ultimaModificacao;

    public ControleCarrosColaboradoresDto() {
        super();
    }

    public ControleCarrosColaboradoresDto(ControleCarrosColaboradores obj) {
        this.id = obj.getId();
        this.nomeColaborador = obj.getNomeColaborador();
        this.departamento = obj.getDepartamento();
        this.veiculo = obj.getVeiculo();
        this.corVeiculo = obj.getCorVeiculo();
        this.anoVeiculo = obj.getAnoVeiculo();
        this.marcaVeiculo = obj.getMarcaVeiculo();
        this.placaVeiculo = obj.getPlacaVeiculo();
        this.contatoColaborador = obj.getContatoColaborador();
        this.ultimaModificacao = obj.getUltimaModificacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeColaborador() {
        return nomeColaborador;
    }

    public void setNomeColaborador(String nomeColaborador) {
        this.nomeColaborador = nomeColaborador;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public String getCorVeiculo() {
        return corVeiculo;
    }

    public void setCorVeiculo(String corVeiculo) {
        this.corVeiculo = corVeiculo;
    }

    public String getAnoVeiculo() {
        return anoVeiculo;
    }

    public void setAnoVeiculo(String anoVeiculo) {
        this.anoVeiculo = anoVeiculo;
    }

    public String getMarcaVeiculo() {
        return marcaVeiculo;
    }

    public void setMarcaVeiculo(String marcaVeiculo) {
        this.marcaVeiculo = marcaVeiculo;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getContatoColaborador() {
        return contatoColaborador;
    }

    public void setContatoColaborador(String contatoColaborador) {
        this.contatoColaborador = contatoColaborador;
    }

    public String getUltimaModificacao() {
        return ultimaModificacao;
    }

    public void setUltimaModificacao(String ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

}
