package br.com.codexloja.v1.domain.dto;

import br.com.codexloja.v1.domain.compras.AvaliacaoFornecedores;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;


public class AvaliacaoFornecedoresDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected String fornecedor;
    protected int pedido;
    protected float notaMinima;
    protected float notaFinal;
    protected Date dataAvaliacao;
    protected String avaliador;
    protected List<AvaliacaoFornecedoresDetalhesDto> itens;

    public AvaliacaoFornecedoresDto() {
        super();
    }

    public AvaliacaoFornecedoresDto(AvaliacaoFornecedores obj) {
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

    public List<AvaliacaoFornecedoresDetalhesDto> getItens() {
        return itens;
    }

    public void setItens(List<AvaliacaoFornecedoresDetalhesDto> itens) {
        this.itens = itens;
    }
}
