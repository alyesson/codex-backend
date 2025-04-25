package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.ControlePortariaDto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class ControlePortaria implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String nome;
    protected String cpf;
    protected String empresa;
    protected String visitado;
    protected String horaEntrada;
    protected String veiculo;
    protected String marca;
    protected String cor;
    protected String placa;
    protected LocalDate dataEntrada;
    protected String observacao;
    protected String autorEntrada;
    @Lob
    protected byte[] imagem;

    public ControlePortaria() {
        super();
    }

    public ControlePortaria(Integer id, String nome, String cpf, String empresa, String visitado, String horaEntrada, String veiculo, String marca, String cor, String placa, LocalDate dataEntrada, String observacao, String autorEntrada, byte[] imagem) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.empresa = empresa;
        this.visitado = visitado;
        this.horaEntrada = horaEntrada;
        this.veiculo = veiculo;
        this.marca = marca;
        this.cor = cor;
        this.placa = placa;
        this.dataEntrada = dataEntrada;
        this.observacao = observacao;
        this.autorEntrada = autorEntrada;
        this.imagem = imagem;
    }

    public ControlePortaria(ControlePortariaDto obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.cpf = obj.getCpf();
        this.empresa = obj.getEmpresa();
        this.visitado = obj.getVisitado();
        this.horaEntrada = obj.getHoraEntrada();
        this.veiculo = obj.getVeiculo();
        this.marca = obj.getMarca();
        this.cor = obj.getCor();
        this.placa = obj.getPlaca();
        this.dataEntrada = obj.getDataEntrada();
        this.observacao = obj.getObservacao();
        this.autorEntrada = obj.getAutorEntrada();
        this.imagem = obj.getImagem();
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getVisitado() {
        return visitado;
    }

    public void setVisitado(String visitado) {
        this.visitado = visitado;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getAutorEntrada() {
        return autorEntrada;
    }

    public void setAutorEntrada(String autorEntrada) {
        this.autorEntrada = autorEntrada;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ControlePortaria that = (ControlePortaria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
