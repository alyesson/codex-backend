package br.com.codex.v1.domain.cadastros;

import br.com.codex.v1.domain.dto.AmbienteNotaFiscalDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
public class AmbienteNotaFiscal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 3, nullable = false)
    private Integer codigoAmbiente;

    public AmbienteNotaFiscal() {
        super();
    }

    public AmbienteNotaFiscal(Long id, Integer codigoAmbiente) {
        this.id = id;
        this.codigoAmbiente = codigoAmbiente;
    }

    public AmbienteNotaFiscal(AmbienteNotaFiscalDto obj) {
        this.id = obj.getId();
        this.codigoAmbiente = obj.getCodigoAmbiente();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AmbienteNotaFiscal that = (AmbienteNotaFiscal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
