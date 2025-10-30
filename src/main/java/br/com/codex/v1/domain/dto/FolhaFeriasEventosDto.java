package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.FolhaFeriasEventos;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class FolhaFeriasEventosDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer codigoEvento;
    private String descricaoEvento;
    private BigDecimal referencia;
    private BigDecimal vencimentos;
    private BigDecimal descontos;
    private LocalDate dataProcessamento;
    private FolhaFeriasDto folhaFeriasDto;

    public FolhaFeriasEventosDto() {
        super();
    }

    public FolhaFeriasEventosDto(FolhaFeriasEventos obj) {
        this.id = obj.getId();
        this.codigoEvento = obj.getCodigoEvento();
        this.descricaoEvento = obj.getDescricaoEvento();
        this.referencia = obj.getReferencia();
        this.vencimentos = obj.getVencimentos();
        this.descontos = obj.getDescontos();
        this.dataProcessamento = obj.getDataProcessamento();
        this.folhaFeriasDto = new FolhaFeriasDto(obj.getFolhaFerias());
    }
}
