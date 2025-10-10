package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.FolhaQuinzenaEventos;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class FolhaQuinzenalEventosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String codigoEvento;
    private String descricaoEvento;
    private String referencia;
    private BigDecimal vencimentos;
    private BigDecimal descontos;
    private FolhaQuinzenalDto cadastroFolhaPagamentoQuinzenal;

    public FolhaQuinzenalEventosDto() {
        super();
    }

    public FolhaQuinzenalEventosDto(FolhaQuinzenaEventos obj) {
        this.id = obj.getId();
        this.codigoEvento = obj.getCodigoEvento();
        this.descricaoEvento = obj.getDescricaoEvento();
        this.referencia = obj.getReferencia();
        this.vencimentos = obj.getVencimentos();
        this.descontos = obj.getDescontos();
        this.cadastroFolhaPagamentoQuinzenal = new FolhaQuinzenalDto(obj.getFolhaQuinzenal());
    }
}
