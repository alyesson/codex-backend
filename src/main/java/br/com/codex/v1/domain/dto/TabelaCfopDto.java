package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class TabelaCfopDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer tipoCfop; //determina se é entrada, saída
    private String movimentacao; //determina de é interno, interestadual ou exterior
    private String codigo;
    private String descricao;

    public TabelaCfopDto() {
        super();
    }

    public TabelaCfopDto(TabelaCfop obj) {
        this.id = obj.getId();
        this.tipoCfop = obj.getTipoCfop();
        this.movimentacao = obj.getMovimentacao();
        this.codigo = obj.getCodigo().replace(",","").replace(".","");
        this.descricao = descricao;
    }
}
