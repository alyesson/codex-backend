package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.contabilidade.ControleNsu;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class ControleNsuDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String cnpj;
    private Long ultimoNsu = 0L;
    private LocalDateTime dataUltimaConsulta;
    private String ambiente;

    public ControleNsuDto() {
        super();
    }

    public ControleNsuDto(ControleNsu obj) {
        this.ambiente = obj.getAmbiente();
        this.dataUltimaConsulta = obj.getDataUltimaConsulta();
        this.ultimoNsu = obj.getUltimoNsu();
        this.cnpj = obj.getCnpj();
        this.id = obj.getId();
    }
}
