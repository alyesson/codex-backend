package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.ContasDto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Contas implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String conta;
    protected String nome;

    @Column(unique = true)
    protected String reduzido;
    protected String utilidade;
    protected String saldo;
    protected String tipo;
    protected String natureza;
    protected Date inclusao;
    protected String situacao;
    protected String observacao;

    public Contas() {
        super();
    }

    public Contas(Long id, String conta, String nome, String reduzido, String utilidade, String saldo, String tipo, String natureza, Date inclusao, String situacao, String observacao) {
        this.id = id;
        this.conta = conta;
        this.nome = nome;
        this.reduzido = reduzido;
        this.utilidade = utilidade;
        this.saldo = saldo;
        this.tipo = tipo;
        this.natureza = natureza;
        this.inclusao = inclusao;
        this.situacao = situacao;
        this.observacao = observacao;
    }

    public Contas(ContasDto obj) {
        this.id = obj.getId();
        this.conta = obj.getConta();
        this.nome = obj.getNome();
        this.reduzido = obj.getReduzido();
        this.utilidade = obj.getUtilidade();
        this.saldo = obj.getSaldo();
        this.tipo = obj.getTipo();
        this.natureza = obj.getNatureza();
        this.inclusao = obj.getInclusao();
        this.situacao = obj.getSituacao();
        this.observacao = obj.getObservacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getReduzido() {
        return reduzido;
    }

    public void setReduzido(String reduzido) {
        this.reduzido = reduzido;
    }

    public String getUtilidade() {
        return utilidade;
    }

    public void setUtilidade(String utilidade) {
        this.utilidade = utilidade;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public Date getInclusao() {
        return inclusao;
    }

    public void setInclusao(Date inclusao) {
        this.inclusao = inclusao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contas contas = (Contas) o;
        return Objects.equals(id, contas.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
