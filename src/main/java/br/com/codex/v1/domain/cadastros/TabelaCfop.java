package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.TabelaCfopDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static br.com.codex.v1.utilitario.CapitalizarPalavras.capitalizarPalavras;

@Entity
@Getter
@Setter
public class TabelaCfop implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Integer codigo;
    private String descricao;
    private String movimentacao; //determina de é interno, interestadual ou exterior
    private String fluxo; //determina se é entrada, saída

    public TabelaCfop() {
        super();
    }

    public TabelaCfop(Long id, Integer codigo, String descricao, String movimentacao, String fluxo) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.movimentacao = movimentacao;
        this.fluxo = fluxo;
    }

    public TabelaCfop(TabelaCfopDto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.descricao = obj.getDescricao();
        this.movimentacao = obj.getMovimentacao();
        this.fluxo = obj.getFluxo();

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TabelaCfop that = (TabelaCfop) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
