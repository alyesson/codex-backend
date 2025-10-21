package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.CalculoFerias;
import br.com.codex.v1.domain.rh.CalculoFeriasEventos;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class CalculoFeriasEventosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer codigoEvento;
    private String descricaoEvento;
    private String referencia;
    private BigDecimal vencimentos;
    private BigDecimal descontos;
    private LocalDate dataProcessamento;
    private CalculoFeriasDto calculoFeriasDto;

    public CalculoFeriasEventosDto() {
        super();
    }

    public CalculoFeriasEventosDto(CalculoFeriasEventos obj) {
        this.id = obj.getId();
        this.codigoEvento = obj.getCodigoEvento();
        this.descricaoEvento = obj.getDescricaoEvento();
        this.referencia = obj.getReferencia();
        this.vencimentos = obj.getVencimentos();
        this.descontos = obj.getDescontos();
        this.dataProcessamento = obj.getDataProcessamento();
        this.calculoFeriasDto = new CalculoFeriasDto(obj.getCalculoFerias());
    }
}
