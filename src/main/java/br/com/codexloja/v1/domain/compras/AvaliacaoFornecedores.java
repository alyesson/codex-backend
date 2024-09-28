package br.com.codexloja.v1.domain.compras;

import br.com.codexloja.v1.domain.dto.AvaliacaoFornecedoresDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class AvaliacaoFornecedores implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String fornecedor;
    protected int pedido;
    protected float notaMinima;
    protected float notaFinal;
    protected Date dataAvaliacao;
    protected String avaliador;
    @JsonIgnore
    @OneToMany(mappedBy = "avaliacaoFornecedores")
    protected List<AvaliacaoFornecedoresDetalhes> avaliacaoFornecedoresItens = new ArrayList<>();

    public AvaliacaoFornecedores() {
        super();
    }

    public AvaliacaoFornecedores(Integer id, String fornecedor, int pedido, float notaMinima, float notaFinal, Date dataAvaliacao, String avaliador) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.pedido = pedido;
        this.notaMinima = notaMinima;
        this.notaFinal = notaFinal;
        this.dataAvaliacao = dataAvaliacao;
        this.avaliador = avaliador;
    }

    public AvaliacaoFornecedores(AvaliacaoFornecedoresDto obj) {
        this.id = obj.getId();
        this.fornecedor = obj.getFornecedor();
        this.pedido = obj.getPedido();
        this.notaMinima = obj.getNotaMinima();
        this.notaFinal = obj.getNotaFinal();
        this.dataAvaliacao = obj.getDataAvaliacao();
        this.avaliador = obj.getAvaliador();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public int getPedido() {
        return pedido;
    }

    public void setPedido(int pedido) {
        this.pedido = pedido;
    }

    public float getNotaMinima() {
        return notaMinima;
    }

    public void setNotaMinima(float notaMinima) {
        this.notaMinima = notaMinima;
    }

    public float getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(float notaFinal) {
        this.notaFinal = notaFinal;
    }

    public Date getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(Date dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public String getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(String avaliador) {
        this.avaliador = avaliador;
    }

    public List<AvaliacaoFornecedoresDetalhes> getAvaliacaoFornecedoresItens() {
        return avaliacaoFornecedoresItens;
    }

    public void setAvaliacaoFornecedoresItens(List<AvaliacaoFornecedoresDetalhes> avaliacaoFornecedoresItens) {
        this.avaliacaoFornecedoresItens = avaliacaoFornecedoresItens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvaliacaoFornecedores that = (AvaliacaoFornecedores) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
