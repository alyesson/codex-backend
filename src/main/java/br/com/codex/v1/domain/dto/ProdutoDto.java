package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.estoque.Produto;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class ProdutoDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    @NotNull(message = "Código do produto não pode ficar em branco")
    protected String codigo;
    @NotNull(message = "O nome do produto não pode ficar em branco")
    protected String descricao;
    protected String grupo;
    protected String subGrupo;
    @NotNull(message = "Quantidade mínima não pode ficar em branco")
    protected Integer minimo;
    @NotNull(message = "Quantidade máxima não pode ficar em branco")
    protected Integer maximo;
    protected String unidadeComercial;
    protected String local;

    public ProdutoDto() {
        super();
    }

    public ProdutoDto(Produto obj) {
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
        this.descricao = capitalizarPalavras(descricao);
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
}
