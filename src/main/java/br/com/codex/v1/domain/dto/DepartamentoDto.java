package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.Departamento;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class DepartamentoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected String codigo;
    protected String descricao;
    protected String centroCusto;

    public DepartamentoDto() {
        super();
    }

    public DepartamentoDto(Departamento obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.centroCusto = obj.getCentroCusto();
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }
}
