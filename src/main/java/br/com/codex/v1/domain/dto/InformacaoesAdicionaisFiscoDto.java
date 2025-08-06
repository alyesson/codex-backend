package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.fiscal.InformacaoesAdicionaisFisco;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class InformacaoesAdicionaisFiscoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Column(length = 10)
    private String codigo;

    @Lob
    private String descricao;

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }

    public InformacaoesAdicionaisFiscoDto() {
        super();
    }

    public InformacaoesAdicionaisFiscoDto(InformacaoesAdicionaisFisco obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
    }
}