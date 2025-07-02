package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.financeiro.CentroCusto;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

public class CentroCustoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    @NotNull(message = "O código não pode ficar em branco")
    protected String codigo;
    @NotNull(message = "A descrição não pode ficar em branco")
    protected String descricao;

    public CentroCustoDto() {
        super();
    }

    public CentroCustoDto(CentroCusto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
