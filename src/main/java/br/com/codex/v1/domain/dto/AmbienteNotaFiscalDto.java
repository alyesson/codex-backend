package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class AmbienteNotaFiscalDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    @NotBlank(message = "Ambiente não pode estar vazio")
    private Integer codigoAmbiente;

    public AmbienteNotaFiscalDto() {
        super();
    }

    public AmbienteNotaFiscalDto(AmbienteNotaFiscal obj) {
        this.id = obj.getId();
        this.codigoAmbiente = obj.getCodigoAmbiente();
    }
}
