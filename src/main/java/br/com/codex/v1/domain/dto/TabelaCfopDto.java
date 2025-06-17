package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class TabelaCfopDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer codigo;
    private String descricao;
    private String movimentacao; //determina de é interno, interestadual ou exterior
    private String fluxo; //determina se é entrada, saída

    public TabelaCfopDto() {
        super();
    }

    public TabelaCfopDto(TabelaCfop obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.movimentacao = obj.getMovimentacao();
        this.fluxo = obj.getFluxo();
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
    }
}
