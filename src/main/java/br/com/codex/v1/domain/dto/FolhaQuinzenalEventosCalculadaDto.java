package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.FolhaQuinzenalEventosCalculada;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class FolhaQuinzenalEventosCalculadaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer codigoEvento;
    private String descricaoEvento;
    private String referencia;
    private BigDecimal vencimentos;
    private BigDecimal descontos;
    private FolhaQuinzenalCalculadaDto folhaQuinzenalCalculadaDto;

    public FolhaQuinzenalEventosCalculadaDto() {
        super();
    }

    public FolhaQuinzenalEventosCalculadaDto(FolhaQuinzenalEventosCalculada obj) {
        this.id = obj.getId();
        this.codigoEvento = obj.getCodigoEvento();
        this.descricaoEvento = obj.getDescricaoEvento();
        this.referencia = obj.getReferencia();
        this.vencimentos = obj.getVencimentos();
        this.descontos = obj.getDescontos();
        this.folhaQuinzenalCalculadaDto = new FolhaQuinzenalCalculadaDto(obj.getFolhaQuinzenalCalculada());
    }
}
