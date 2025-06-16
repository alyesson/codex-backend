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
    private Integer id;
    @NotBlank(message = "Tipo do cfop não pode estar vazio")
    private Integer tipoCfop; //determina se é entrada, saída
    @NotBlank(message = "Movimentação do cfop não pode estar vazio")
    private String movimentacao; //determina de é interno, interestadual ou exterior
    private String codigo;
    private String descricao;

    public TabelaCfop() {
        super();
    }

    public TabelaCfop(Integer id, Integer tipoCfop, String movimentacao, String codigo, String descricao) {
        this.id = id;
        this.tipoCfop = tipoCfop;
        this.movimentacao = movimentacao;
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public TabelaCfop(TabelaCfopDto obj) {
        this.id = obj.getId();
        this.tipoCfop = obj.getTipoCfop();
        this.movimentacao = obj.getMovimentacao();
        this.codigo = obj.getCodigo().replace(",","").replace(".","");
        this.descricao = descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = capitalizarPalavras(descricao);
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
