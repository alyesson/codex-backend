package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenal;
import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenalEventos;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class CadastroFolhaPagamentoQuinzenalEventosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String codigoEvento;
    private String descricaoEvento;
    private String referencia;
    private BigDecimal vencimentos;
    private BigDecimal descontos;
    private CadastroFolhaPagamentoQuinzenalDto cadastroFolhaPagamentoQuinzenal;

    public CadastroFolhaPagamentoQuinzenalEventosDto() {
        super();
    }

    public CadastroFolhaPagamentoQuinzenalEventosDto(CadastroFolhaPagamentoQuinzenalEventos obj) {
        this.id = obj.getId();
        this.codigoEvento = obj.getCodigoEvento();
        this.descricaoEvento = obj.getDescricaoEvento();
        this.referencia = obj.getReferencia();
        this.vencimentos = obj.getVencimentos();
        this.descontos = obj.getDescontos();
        this.cadastroFolhaPagamentoQuinzenal = new CadastroFolhaPagamentoQuinzenalDto(obj.getCadastroFolhaPagamentoQuinzenal());
    }
}
