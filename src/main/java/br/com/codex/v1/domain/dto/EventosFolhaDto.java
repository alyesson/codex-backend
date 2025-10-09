package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.EventosFolha;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class EventosFolhaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigo;
    private String descricao;

    public EventosFolhaDto() {
        super();
    }

    public EventosFolhaDto(EventosFolha obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }
}
