package br.com.codex.v1.domain.rh;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
public class FolhaMensalEventosCalculada implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoEvento;
    private String descricaoEvento;
    private String referencia;

    @Column(precision = 10, scale = 2)
    private BigDecimal vencimentos;

    @Column(precision = 10, scale = 2)
    private BigDecimal descontos;

    @ManyToOne
    @JoinColumn(name = "folhaMensalCalculada_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FolhaMensalCalculada folhaMensalCalculada;

    public FolhaMensalEventosCalculada() {
        super();
    }

    public FolhaMensalEventosCalculada(Long id, String codigoEvento, String descricaoEvento, String referencia,
                                       BigDecimal vencimentos, BigDecimal descontos,
                                       FolhaMensalCalculada folhaMensalCalculada) {
        this.id = id;
        this.codigoEvento = codigoEvento;
        this.descricaoEvento = descricaoEvento;
        this.referencia = referencia;
        this.vencimentos = vencimentos;
        this.descontos = descontos;
        this.folhaMensalCalculada = folhaMensalCalculada;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FolhaMensalEventosCalculada that = (FolhaMensalEventosCalculada) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
