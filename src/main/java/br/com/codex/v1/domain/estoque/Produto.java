package br.com.codex.v1.domain.estoque;

import br.com.codex.v1.domain.dto.ProdutoDto;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Produto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    protected String codigo;
    protected String descricao;
    protected String grupo;
    protected String subGrupo;
    protected Integer minimo;
    protected Integer maximo;
    protected String unidadeComercial;
    protected String local;

    public Produto() {
        super();
    }

    public Produto(Integer id, String codigo, String descricao, String grupo, String subGrupo, Integer minimo, Integer maximo, String unidadeComercial, String local) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.grupo = grupo;
        this.subGrupo = subGrupo;
        this.minimo = minimo;
        this.maximo = maximo;
        this.unidadeComercial = unidadeComercial;
        this.local = local;
    }

    public Produto(ProdutoDto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.grupo = obj.getGrupo();
        this.subGrupo = obj.getSubGrupo();
        this.minimo = obj.getMinimo();
        this.maximo = obj.getMaximo();
        this.unidadeComercial = obj.getUnidadeComercial();
        this.local = obj.getLocal();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(String subGrupo) {
        this.subGrupo = subGrupo;
    }

    public Integer getMinimo() {
        return minimo;
    }

    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public String getUnidadeComercial() {
        return unidadeComercial;
    }

    public void setUnidadeComercial(String unidadeComercial) {
        this.unidadeComercial = unidadeComercial;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id) && Objects.equals(codigo, produto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }
}
