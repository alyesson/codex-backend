package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.fiscal.InformacaoesComplementares;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.io.Serial;
import java.io.Serializable;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class InformacaoesComplementaresDto implements Serializable {
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

    public InformacaoesComplementaresDto() {
        super();
    }

    public InformacaoesComplementaresDto(InformacaoesComplementares obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
    }
}