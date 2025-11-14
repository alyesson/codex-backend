package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Getter
@Setter
public class TabelaCfopDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer codigo;
    @Column(columnDefinition = "TEXT")
    private String descricao;
    @NotBlank(message = "Movimentação do cfop não pode estar vazio")
    private String movimentacao; //determina de é interno, interestadual ou exterior
    @NotBlank(message = "O fluxo não pode estar vazio")
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
