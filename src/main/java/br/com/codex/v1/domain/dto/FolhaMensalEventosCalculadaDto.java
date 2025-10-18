package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.FolhaMensalEventos;
import br.com.codex.v1.domain.rh.FolhaMensalEventosCalculada;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class FolhaMensalEventosCalculadaDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer codigoEvento;
    private String descricaoEvento;
    private BigDecimal referencia;
    private BigDecimal vencimentos;
    private BigDecimal descontos;
    private FolhaMensalCalculadaDto folhaMensalCalculadaDto;

    public FolhaMensalEventosCalculadaDto() {
        super();
    }

    public FolhaMensalEventosCalculadaDto(FolhaMensalEventosCalculada obj) {
        this.id = obj.getId();
        this.codigoEvento = obj.getCodigoEvento();
        this.descricaoEvento = obj.getDescricaoEvento();
        this.referencia = obj.getReferencia();
        this.vencimentos = obj.getVencimentos();
        this.descontos = obj.getDescontos();
        this.folhaMensalCalculadaDto = new FolhaMensalCalculadaDto(obj.getFolhaMensalCalculada());
    }
}
