package br.com.codex.v1.domain.contabilidade;

import br.com.codex.v1.domain.dto.ControleNsuDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ControleNsu implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private Long ultimoNsu = 0L;

    @Column(nullable = false)
    private LocalDateTime dataUltimaConsulta;

    @Column(nullable = false)
    private String ambiente;

    public ControleNsu() {
        super();
    }

    public ControleNsu(String ambiente, LocalDateTime dataUltimaConsulta, Long ultimoNsu, String cnpj, Long id) {
        this.ambiente = ambiente;
        this.dataUltimaConsulta = dataUltimaConsulta;
        this.ultimoNsu = ultimoNsu;
        this.cnpj = cnpj;
        this.id = id;
    }

    public ControleNsu(ControleNsuDto obj) {
        this.ambiente = obj.getAmbiente();
        this.dataUltimaConsulta = obj.getDataUltimaConsulta();
        this.ultimoNsu = obj.getUltimoNsu();
        this.cnpj = obj.getCnpj();
        this.id = obj.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ControleNsu that = (ControleNsu) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
