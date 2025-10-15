package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaMensalEventos;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class FolhaMensalEventosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigoEvento;
    private String descricaoEvento;
    private String referencia;
    private BigDecimal vencimentos;
    private BigDecimal descontos;
    private FolhaMensalDto folhaMensalDto;

    public FolhaMensalEventosDto() {
        super();
    }

    public FolhaMensalEventosDto(FolhaMensalEventos obj) {
        this.id = obj.getId();
        this.codigoEvento = obj.getCodigoEvento();
        this.descricaoEvento = obj.getDescricaoEvento();
        this.referencia = obj.getReferencia();
        this.vencimentos = obj.getVencimentos();
        this.descontos = obj.getDescontos();
        this.folhaMensalDto = new FolhaMensalDto(obj.getFolhaMensal());
    }
}
