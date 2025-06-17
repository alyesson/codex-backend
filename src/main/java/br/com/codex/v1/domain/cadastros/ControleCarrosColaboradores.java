package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.ControleCarrosColaboradoresDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class ControleCarrosColaboradores implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String nomeColaborador;
    protected String departamento;
    protected String veiculo;
    protected String corVeiculo;
    protected String anoVeiculo;
    protected String marcaVeiculo;
    protected String placaVeiculo;
    protected String contatoColaborador;
    protected String ultimaModificacao;

    public ControleCarrosColaboradores() {
        super();
    }

    public ControleCarrosColaboradores(Long id, String nomeColaborador, String departamento, String veiculo, String corVeiculo, String anoVeiculo, String marcaVeiculo, String placaVeiculo, String contatoColaborador, String ultimaModificacao) {
        this.id = id;
        this.nomeColaborador = nomeColaborador;
        this.departamento = departamento;
        this.veiculo = veiculo;
        this.corVeiculo = corVeiculo;
        this.anoVeiculo = anoVeiculo;
        this.marcaVeiculo = marcaVeiculo;
        this.placaVeiculo = placaVeiculo;
        this.contatoColaborador = contatoColaborador;
        this.ultimaModificacao = ultimaModificacao;
    }

    public ControleCarrosColaboradores(ControleCarrosColaboradoresDto obj) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ControleCarrosColaboradores that = (ControleCarrosColaboradores) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
