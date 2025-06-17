package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.ControlePortaria;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class ControlePortariaDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotNull(message = "O nome é obrigatório")
    protected String nome;
    @NotNull(message = "O Cpf é obrigatória")
    protected String cpf;
    protected String empresa;
    @NotNull(message = "O departamento é obrigatória")
    protected String visitado;
    protected String horaEntrada;
    protected String veiculo;
    protected String marca;
    protected String cor;
    @NotNull(message = "A placa do veículo é obrigatória")
    protected String placa;
    protected LocalDate dataEntrada;
    protected String observacao;
    protected String autorEntrada;
    protected byte[] imagem;

    public ControlePortariaDto() {
        super();
    }

    public ControlePortariaDto(ControlePortaria obj) {
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
}
