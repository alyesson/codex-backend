package br.com.codexloja.v1.domain.dto;

import br.com.codexloja.v1.domain.estoque.SubGrupo;

import java.io.Serial;
import java.io.Serializable;

import static br.com.codexloja.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class SubGrupoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    protected String codigoGrupo;
    protected String codigoSubGrupo;
    protected String descricao;

    public SubGrupoDto() {
        super();
    }

    public SubGrupoDto(SubGrupo obj) {
        this.id = obj.getId();
        this.codigoGrupo = obj.getCodigoGrupo();
        this.codigoSubGrupo = obj.getCodigoSubGrupo();
        this.descricao = obj.getDescricao();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public String getCodigoSubGrupo() {
        return codigoSubGrupo;
    }

    public void setCodigoSubGrupo(String codigoSubGrupo) {
        this.codigoSubGrupo = codigoSubGrupo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }
}
