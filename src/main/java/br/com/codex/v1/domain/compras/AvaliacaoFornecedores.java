package br.com.codex.v1.domain.compras;

import br.com.codex.v1.domain.dto.AvaliacaoFornecedoresDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class AvaliacaoFornecedores implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String fornecedor;
    protected int pedido;
    protected float notaMinima;
    protected float notaFinal;
    protected Date dataAvaliacao;
    protected String avaliador;
    @JsonIgnore
    @OneToMany(mappedBy = "avaliacaoFornecedores", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<AvaliacaoFornecedoresDetalhes> avaliacaoFornecedoresItens = new ArrayList<>();

    public AvaliacaoFornecedores() {
        super();
    }

    public AvaliacaoFornecedores(Long id, String fornecedor, int pedido, float notaMinima,
                                 float notaFinal, Date dataAvaliacao, String avaliador) {
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
